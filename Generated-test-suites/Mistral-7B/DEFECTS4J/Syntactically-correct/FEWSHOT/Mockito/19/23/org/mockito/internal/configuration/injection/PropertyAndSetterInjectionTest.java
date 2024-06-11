package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

/**
 * Test class for PropertyAndSetterInjection.
 */
public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injection;
    private Field field1;
    private Field field2;
    private Field field3;
    private Class mockType;

    @Before
    public void setup() {
        injection = new PropertyAndSetterInjection();
        mockType = Mockito.mock(String.class);

        Class testClass = Mockito.mock(TestClass.class);
        try {
            field1 = testClass.getDeclaredField("field1");
            field1.setAccessible(true);
            field1.setAnnotation(MockitoInject.class);
            field1.set(testClass, null);

            field2 = testClass.getDeclaredField("field2");
            field2.setAccessible(true);
            field2.setAnnotation(MockitoInject.class);
            field2.set(testClass, null);

            field3 = testClass.getDeclaredField("field3");
            field3.setAccessible(true);
            field3.setAnnotation(MockitoInject.class);
            field3.set(testClass, null);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testProcessInjection() {
        Set<Object> mockCandidates = newHashSet(mockType, mockType, mockType);

        Object injectMocksFieldOwner = Mockito.mock(TestClass.class);
        Field injectMocksField = Mockito.mock(Field.class);
        try {
            injectMocksField.set(injectMocksFieldOwner, field1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        assertTrue(injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));

        assertEquals(mockType, field1.get(injectMocksFieldOwner));
        assertEquals(mockType, field2.get(injectMocksFieldOwner));
        assertNull(field3.get(injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_NoMockCandidates() {
        Set<Object> mockCandidates = newHashSet();

        Object injectMocksFieldOwner = Mockito.mock(TestClass.class);
        Field injectMocksField = Mockito.mock(Field.class);
        try {
            injectMocksField.set(injectMocksFieldOwner, field1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        assertFalse(injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));

        assertNull(field1.get(injectMocksFieldOwner));
        assertNull(field2.get(injectMocksFieldOwner));
        assertNull(field3.get(injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_MockCandidatesByType() {
        Set<Object> mockCandidates = newHashSet(mockType, mockType, mockType);

        Object injectMocksFieldOwner = Mockito.mock(TestClass.class);
        Field injectMocksField = Mockito.mock(Field.class);
        try {
            injectMocksField.set(injectMocksFieldOwner, field1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        assertEquals(mockType, injection.injectMockCandidates(TestClass.class, mockCandidates, injectMocksFieldOwner).get(0));

        assertEquals(mockType, field1.get(injectMocksFieldOwner));
        assertEquals(mockType, field2.get(injectMocksFieldOwner));
        assertNull(field3.get(injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_MockCandidatesByName() {
        Set<Object> mockCandidates = newHashSet(mockType, new Object() {}, mockType);

        Object injectMocksFieldOwner = Mockito.mock(TestClass.class);
        Field injectMocksField = Mockito.mock(Field.class);
        try {
            injectMocksField.set(injectMocksFieldOwner, field1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        assertEquals(mockType, injection.injectMockCandidates(TestClass.class, mockCandidates, injectMocksFieldOwner).get(1));

        assertEquals(mockType, field1.get(injectMocksFieldOwner));
        assertEquals(mockType, field2.get(injectMocksFieldOwner));
        assertNull(field3.get(injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_MultipleMockCandidates() {
        Set<Object> mockCandidates = newHashSet(mockType, mockType, mockType);

        Object injectMocksFieldOwner = Mockito.mock(TestClass.class);
        Field injectMocksField = Mockito.mock(Field.class);
        try {
            injectMocksField.set(injectMocksFieldOwner, field1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        List<Object> result = injection.injectMockCandidates(TestClass.class, mockCandidates, injectMocksFieldOwner);

        assertEquals(2, result.size());
        assertEquals(mockType, result.get(0));
        assertEquals(mockType, result.get(1));

        assertEquals(mockType, field1.get(injectMocksFieldOwner));
        assertEquals(mockType, field2.get(injectMocksFieldOwner));
        assertNull(field3.get(injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_NoMockCandidatesAndNoUserProvidedDependencies() {
        Set<Object> mockCandidates = newHashSet();

        Object injectMocksFieldOwner = Mockito.mock(TestClass.class);
        Field injectMocksField = Mockito.mock(Field.class);
        try {
            injectMocksField.set(injectMocksFieldOwner, field1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        assertFalse(injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));

        assertNull(field1.get(injectMocksFieldOwner));
        assertNull(field2.get(injectMocksFieldOwner));
        assertNull(field3.get(injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_UserProvidedDependency() {
        Object dependency = new Object();

        Set<Object> mockCandidates = newHashSet(mockType, mockType, mockType);

        Object injectMocksFieldOwner = Mockito.mock(TestClass.class);
        Field injectMocksField = Mockito.mock(Field.class);
        try {
            injectMocksField.set(injectMocksFieldOwner, field1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        injection.injectMockCandidates(TestClass.class, mockCandidates, injectMocksFieldOwner);

        assertNull(field1.get(injectMocksFieldOwner));
        assertNull(field2.get(injectMocksFieldOwner));
        assertEquals(dependency, field3.get(injectMocksFieldOwner));
    }

    private Set<Object> newHashSet(Object... objects) {
        return new MockitoSafeHashSet(Arrays.asList(objects));
    }

    @SuppressWarnings("rawtypes")
    static class TestClass {
        @MockitoInject
        private Field field1;

        @MockitoInject
        private Field field2;

        @MockitoInject
        private Field field3;
    }
}