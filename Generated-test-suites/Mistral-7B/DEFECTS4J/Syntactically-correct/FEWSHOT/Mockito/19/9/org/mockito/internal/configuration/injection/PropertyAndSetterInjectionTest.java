package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.reflection.FieldTestUtil;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.reflection.FieldTestUtil.getField;
import static org.mockito.internal.util.reflection.FieldTestUtil.setField;
import static org.mockito.internal.util.reflection.FieldTestUtil.verifyAccessible;

/**
 * Test class for PropertyAndSetterInjection class.
 */
public class PropertyAndSetterInjectionTest {

    @Rule
    public MockitoJUnit mockitoJUnit = new MockitoJUnit();

    @Rule
    public MockitoJUnitRunner mockitoJUnitRunner = new MockitoJUnitRunner();

    private PropertyAndSetterInjection propertyAndSetterInjection;

    private static final String CLASS_NAME = "com.test.TestClass";

    @Before
    public void setup() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjectionNullField() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        assertFalse(propertyAndSetterInjection.processInjection(null, null, mockCandidates));
    }

    @Test
    public void testProcessInjectionEmptyFieldCandidates() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        Field injectMocksField = getField(Object.class, "testField");
        Object injectMocksFieldOwner = new Object();
        assertFalse(propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjectionNoMockCandidates() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        Field injectMocksField = getField(Object.class, "testField");
        Object injectMocksFieldOwner = new Object();
        FieldInitializationReport report = new FieldInitializer(injectMocksFieldOwner, injectMocksField).initialize();
        Class<?> fieldClass = report.fieldClass();
        Object fieldInstanceNeedingInjection = report.fieldInstance();
        Set<Object> mocksToBeInjected = new HashSet<Object>();
        mocksToBeInjected.addAll(mockCandidates);
        assertFalse(propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjectionMockCandidatesFound() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        mockCandidates.add(new Object());
        mockCandidates.add(new Object());
        Field injectMocksField = getField(Object.class, "testField");
        Object injectMocksFieldOwner = new Object();
        FieldInitializationReport report = new FieldInitializer(injectMocksFieldOwner, injectMocksField).initialize();
        Class<?> fieldClass = report.fieldClass();
        Object fieldInstanceNeedingInjection = report.fieldInstance();
        Set<Object> mocksToBeInjected = new HashSet<Object>();
        mocksToBeInjected.addAll(mockCandidates);
        assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }

    @Test
    public void testInitializeInjectMocksFieldNullField() {
        Field injectMocksField = null;
        Object fieldOwner = new Object();
        assertNull(propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, fieldOwner));
    }

    @Test
    public void testInitializeInjectMocksFieldNoMockCandidate() throws Exception {
        Field injectMocksField = getField(Object.class, "testField");
        Object fieldOwner = new Object();
        FieldInitializationReport report = new FieldInitializer(fieldOwner, injectMocksField).initialize();
        assertNull(report);
    }

    @Test
    public void testInitializeInjectMocksFieldMockCandidateFound() throws Exception {
        Field injectMocksField = getField(Object.class, "testField");
        Object fieldOwner = new Object();
        FieldInitializer fieldInitializer = new FieldInitializer(fieldOwner, injectMocksField);
        Object mockCandidate = new Object();
        Whitebox.setInternalState(fieldInitializer, "mockCandidate", mockCandidate);
        FieldInitializationReport report = new FieldInitializer(fieldOwner, injectMocksField).initialize();
        assertEquals(mockCandidate, report.mockCandidate());
    }

    @Test
    public void testInjectMockCandidatesNullClass() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        Object instance = new Object();
        assertFalse(propertyAndSetterInjection.injectMockCandidates(null, mockCandidates, instance));
    }

    @Test
    public void testInjectMockCandidatesEmptyList() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        Object instance = new Object();
        assertFalse(propertyAndSetterInjection.injectMockCandidates(Object.class, mockCandidates, instance));
    }

    @Test
    public void testInjectMockCandidatesNoMockCandidates() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        Object instance = new Object();
        List<Field> orderedInstanceFields = Arrays.asList(getDeclaredFields(Object.class));
        assertFalse(propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields));
    }

    @Test
    public void testInjectMockCandidatesMockCandidateFound() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        mockCandidates.add(new Object());
        Object instance = new Object();
        List<Field> orderedInstanceFields = Arrays.asList(getDeclaredFields(Object.class));
        assertTrue(propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields));
    }

    @Test
    public void testInjectMockCandidatesMockCandidateFoundByPropertySetter() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        mockCandidates.add(new Object());
        Object instance = new Object();
        List<Field> orderedInstanceFields = Arrays.asList(getDeclaredFields(Object.class));
        Field fieldToBeInjected = getDeclaredField(Object.class, "testField");
        Whitebox.setInternalState(propertyAndSetterInjection, "mockCandidateFilter", new MockCandidateFilter() {
            @Override
            public Object filterCandidate(Set<Object> candidates, Field field, Object instance) {
                return candidates.iterator().next();
            }

            @Override
            public Object thenInject() {
                setField(instance, field, new Object());
                return null;
            }
        });
        assertTrue(propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields));
    }

    @Test
    public void testInjectMockCandidatesMockCandidateFoundByFieldInjection() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        mockCandidates.add(new Object());
        Object instance = new Object();
        List<Field> orderedInstanceFields = Arrays.asList(getDeclaredFields(Object.class));
        Field fieldToBeInjected = getDeclaredField(Object.class, "testField");
        Whitebox.setInternalState(propertyAndSetterInjection, "mockCandidateFilter", new MockCandidateFilter() {
            @Override
            public Object filterCandidate(Set<Object> candidates, Field field, Object instance) {
                return null;
            }

            @Override
            public Object thenInject() {
                setField(instance, field, new Object());
                return null;
            }
        });
        assertTrue(propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields));
    }

    @Test
    public void testInjectMockCandidatesNoFieldsToInject() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        Object instance = new Object();
        List<Field> orderedInstanceFields = Arrays.asList(getDeclaredFields(Object.class));
        assertTrue(propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields));
    }

    @Test(expected = AssertionError.class)
    public void testInjectMockCandidatesMockCandidateNotFound() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        Object instance = new Object();
        List<Field> orderedInstanceFields = Arrays.asList(getDeclaredFields(Object.class));
        Whitebox.setInternalState(propertyAndSetterInjection, "mockCandidateFilter", new MockCandidateFilter() {
            @Override
            public Object filterCandidate(Set<Object> candidates, Field field, Object instance) {
                return null;
            }

            @Override
            public Object thenInject() {
                throw new AssertionError();
            }
        });
        propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields);
    }

    private Field getDeclaredField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            verifyAccessible(field);
            return field;
        } catch (NoSuchFieldException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Field> getDeclaredFields(Class<?> clazz) {
        try {
            return Arrays.asList(clazz.getDeclaredFields());
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }

}