package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private Set<Object> mockCandidates;

    @Before
    public void setUp() {
        injectionStrategy = new PropertyAndSetterInjection();
        mockCandidates = newMockSafeHashSet();
    }

    @Test
    public void testProcessInjection_NoMockCandidates() {
        Field injectMocksField = getTestClassField("injectMocksField");
        Object injectMocksFieldOwner = new Object();
        assertFalse(injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_NoMockCandidatesWithInitialization() {
        Field injectMocksField = getTestClassField("injectMocksField");
        Object injectMocksFieldOwner = new Object();
        FieldInitializationReport report = injectStrategy.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
        assertFalse(report.isSuccess());
    }

    @Test
    public void testProcessInjection_SingleMockCandidate() {
        Object mock = new Object();
        mockCandidates.add(mock);
        Field injectMocksField = getTestClassField("injectMocksField");
        Object injectMocksFieldOwner = new Object();
        assertTrue(injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
        assertNotNull(injectMocksFieldOwner.getClass().getDeclaredField("injectedField"));
        Object injectedField = injectMocksFieldOwner.getClass().getDeclaredField("injectedField").get(injectMocksFieldOwner);
        assertEquals(mock, injectedField);

        // verify mock was removed from candidates
        assertFalse(mockCandidates.contains(mock));
    }

    @Test(expected = InvocationTargetException.class)
    public void testProcessInjection_MockCandidateInitializationException() {
        Object mock = new Object() {
            @Override
            public Object init() throws Exception {
                throw new Exception();
            }
        };
        mockCandidates.add(mock);
        Field injectMocksField = getTestClassField("injectMocksField");
        Object injectMocksFieldOwner = new Object();
        injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_MockCandidateNotInjectableByProperty() {
        Object mock = new Object();
        mockCandidates.add(mock);
        Field injectMocksField = getTestClassField("injectMocksField");
        Object injectMocksFieldOwner = new Object();
        injectMocksField.setAccessible(true);
        setPrivateField(injectMocksFieldOwner, "injectedField", mock);
        assertFalse(injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_SingleMockCandidateByName() {
        Object mock1 = new Object();
        mockCandidates.add(mock1);
        Object mock2 = new Object();
        mockCandidates.add(mock2);

        Field injectMocksField = getTestClassField("injectMocksField");
        Object injectMocksFieldOwner = new Object();

        assertTrue(injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
        assertNotNull(injectMocksFieldOwner.getClass().getDeclaredField("injectedField"));
        Object injectedField = injectMocksFieldOwner.getClass().getDeclaredField("injectedField").get(injectMocksFieldOwner);
        assertEquals(mock1, injectedField);

        // verify mock was removed from candidates
        assertFalse(mockCandidates.contains(mock1));
        assertTrue(mockCandidates.contains(mock2));
    }

    @Test
    public void testProcessInjection_MultipleMockCandidates() {
        Object mock1 = new Object();
        mockCandidates.add(mock1);
        Object mock2 = new Object();
        mockCandidates.add(mock2);

        Field injectMocksField = getTestClassField("injectMocksField");
        Object injectMocksFieldOwner = new Object();

        assertFalse(injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_MultipleMockCandidatesByName() {
        Object mock1 = new Object();
        mockCandidates.add(mock1);
        Object mock2 = new Object();
        mockCandidates.add(mock2);

        Field injectMocksField = getTestClassField("injectMocksField");
        Object injectMocksFieldOwner = new Object();

        assertTrue(injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
        assertNotNull(injectMocksFieldOwner.getClass().getDeclaredField("injectedField"));
        Object injectedField = injectMocksFieldOwner.getClass().getDeclaredField("injectedField").get(injectMocksFieldOwner);
        assertEquals(mock1, injectedField);

        // verify mock was removed from candidates
        assertFalse(mockCandidates.contains(mock1));
        assertTrue(mockCandidates.contains(mock2));
    }

    @Test
    public void testProcessInjection_NoMockCandidatesInHierarchy() {
        Field injectMocksField = getTestClassField("injectMocksField");
        Object injectMocksFieldOwner = new Object();
        Class<?> classUnderTest = injectMocksFieldOwner.getClass();
        while (classUnderTest != Object.class) {
            injectionStrategy.injectMockCandidates(classUnderTest, mockCandidates, injectMocksFieldOwner);
            classUnderTest = classUnderTest.getSuperclass();
        }
        assertFalse(injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_MockCandidatesInHierarchy() {
        Object mock1 = new Object();
        mockCandidates.add(mock1);
        Object mock2 = new Object();
        mockCandidates.add(mock2);

        Field injectMocksField = getTestClassField("injectMocksField");
        Object injectMocksFieldOwner = new Object();
        Class<?> classUnderTest = injectMocksFieldOwner.getClass();

        assertTrue(injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
        assertNotNull(injectMocksFieldOwner.getClass().getDeclaredField("injectedField"));
        Object injectedField = injectMocksFieldOwner.getClass().getDeclaredField("injectedField").get(injectMocksFieldOwner);
        assertEquals(mock1, injectedField);

        // verify mock was removed from candidates
        assertFalse(mockCandidates.contains(mock1));
        assertTrue(mockCandidates.contains(mock2));
    }

    private void setPrivateField(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Field getTestClassField(String fieldName) {
        try {
            return ReflectionUtils.getField(this.getClass(), fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    static class ReflectionUtils {
        @SuppressWarnings("unchecked")
        public static <T> Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
            Field field = null;
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                Class<?> superclass = clazz.getSuperclass();
                if (superclass != null) {
                    field = getField(superclass, fieldName);
                }
            }
            if (field == null) {
                throw new NoSuchFieldException("No field named '" + fieldName + "' in class '" + clazz.getName() + "'");
            }
            return field;
        }
    }
}