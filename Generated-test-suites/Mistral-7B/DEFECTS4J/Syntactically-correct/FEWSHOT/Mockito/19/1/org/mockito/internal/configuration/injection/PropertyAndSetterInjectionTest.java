package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.configuration.injection.filter.MockCandidateFilter.filterCandidates;
import static org.mockito.internal.util.collections.ListUtil.filter;
import static org.mockito.internal.util.collections.ListUtil.newArrayList;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;
import static org.mockito.internal.util.reflection.FieldInitializationReport.mockInitializationFailed;
import static org.mockito.internal.util.reflection.FieldInitializationReport.mockInitializationSuccess;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

/**
 * Test class for PropertyAndSetterInjection
 */
public class PropertyAndSetterInjectionTest {

    private static final String CLASS_NAME = PropertyAndSetterInjection.class.getName();

    /**
     * Test initialize injectMocks field with mock instance
     */
    @Test
    public void testInitializeInjectMocksFieldWithMockInstance() {
        Object mock = new Object() {};
        Field injectMocksField = new Object().getClass().getDeclaredField("injectMocks");
        Object injectMocksFieldOwner = new Object();
        FieldInitializationReport report = new PropertyAndSetterInjection().initializeInjectMocksField(injectMocksField, injectMocksFieldOwner, mock);
        assertTrue(mockInitializationSuccess(report));
        assertEquals(mock, report.getInjectedObject());
    }

    /**
     * Test initialize injectMocks field with null
     */
    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksFieldWithNull() {
        Field injectMocksField = new Object().getClass().getDeclaredField("injectMocks");
        Object injectMocksFieldOwner = new Object();
        new PropertyAndSetterInjection().initializeInjectMocksField(injectMocksField, injectMocksFieldOwner, null);
    }

    /**
     * Test initialize injectMocks field with non-mock instance
     */
    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksFieldWithNonMockInstance() {
        Object nonMockInstance = new Object() {};
        Field injectMocksField = new Object().getClass().getDeclaredField("injectMocks");
        Object injectMocksFieldOwner = new Object();
        new PropertyAndSetterInjection().initializeInjectMocksField(injectMocksField, injectMocksFieldOwner, nonMockInstance);
    }

    /**
     * Test injectMockCandidates method with empty mocks
     */
    @Test
    public void testInjectMockCandidatesWithEmptyMocks() {
        Object instance = new Object();
        Set<Object> mocks = newMockSafeHashSet();
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(Object.class, mocks, instance);
        assertFalse(injectionOccurred);
    }

    /**
     * Test injectMockCandidates method with non-mock instance and empty mocks
     */
    @Test
    public void testInjectMockCandidatesWithNonMockInstanceAndEmptyMocks() {
        Object instance = new Object();
        Set<Object> mocks = newMockSafeHashSet();
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        propertyAndSetterInjection.injectMockCandidates(Object.class, mocks, instance);
    }

    /**
     * Test injectMockCandidates method with mocks and no injection occurred
     */
    @Test
    public void testInjectMockCandidatesWithMocksAndNoInjectionOccurred() {
        Object instance = new Object();
        Set<Object> mocks = newMockSafeHashSet(new Object[] {new Object() {}, new Object() {}});
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(Object.class, mocks, instance);
        assertFalse(injectionOccurred);
    }

    /**
     * Test injectMockCandidates method with mocks and injection occurred
     */
    @Test
    public void testInjectMockCandidatesWithMocksAndInjectionOccurred() {
        Object instance = new Object();
        Set<Object> mocks = newMockSafeHashSet(new Object[] {new Object() {}, new Object() {}});
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(Object.class);
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(Object.class, mocks, instance);
        assertTrue(injectionOccurred);
    }

    /**
     * Test injectMockCandidatesOnFields method with empty mocks
     */
    @Test
    public void testInjectMockCandidatesOnFieldsWithEmptyMocks() {
        Object instance = new Object();
        Set<Object> mocks = newMockSafeHashSet();
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, newArrayList());
        assertFalse(injectionOccurred);
    }

    /**
     * Test injectMockCandidatesOnFields method with empty list of fields
     */
    @Test
    public void testInjectMockCandidatesOnFieldsWithEmptyListOfFields() {
        Object instance = new Object();
        Set<Object> mocks = newMockSafeHashSet();
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, newArrayList());
        assertFalse(injectionOccurred);
    }

    /**
     * Test injectMockCandidatesOnFields method with mocks and no injection occurred
     */
    @Test
    public void testInjectMockCandidatesOnFieldsWithMocksAndNoInjectionOccurred() {
        Object instance = new Object();
        Set<Object> mocks = newMockSafeHashSet(new Object[] {new Object() {}, new Object() {}});
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(Object.class);
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);
        assertFalse(injectionOccurred);
    }

    /**
     * Test injectMockCandidatesOnFields method with mocks and injection occurred
     */
    @Test
    public void testInjectMockCandidatesOnFieldsWithMocksAndInjectionOccurred() {
        Object instance = new Object();
        Set<Object> mocks = newMockSafeHashSet(new Object[] {new Object() {}, new Object() {}});
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(Object.class);
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);
        assertTrue(injectionOccurred);
    }

    /**
     * Test orderedInstanceFieldsFrom method with empty class
     */
    @Test
    public void testOrderedInstanceFieldsFromWithEmptyClass() {
        List<Field> orderedInstanceFields = new PropertyAndSetterInjection().orderedInstanceFieldsFrom(Object.class);
        assertTrue(orderedInstanceFields.isEmpty());
    }

    /**
     * Test orderedInstanceFieldsFrom method with non-empty class
     */
    @Test
    public void testOrderedInstanceFieldsFromWithNonEmptyClass() {
        List<Field> orderedInstanceFields = new PropertyAndSetterInjection().orderedInstanceFieldsFrom(Object.class);
        assertTrue(!orderedInstanceFields.isEmpty());
    }

    /**
     * Test filterCandidate method with empty mocks and non-matching field
     */
    @Test
    public void testFilterCandidateWithEmptyMocksAndNonMatchingField() {
        Set<Object> mocks = newMockSafeHashSet();
        Field field = new Object().getClass().getDeclaredField("nonMatchingField");
        MockCandidateFilter filter = new PropertyAndSetterInjection().mockCandidateFilter;
        Object candidate = filter.filterCandidate(mocks, field, new Object());
        assertNull(candidate);
    }

    /**
     * Test filterCandidate method with empty mocks and matching field
     */
    @Test(expected = IllegalAccessException.class)
    public void testFilterCandidateWithEmptyMocksAndMatchingField() {
        Set<Object> mocks = newMockSafeHashSet();
        Field field = new Object().getClass().getDeclaredField("field");
        MockCandidateFilter filter = new PropertyAndSetterInjection().mockCandidateFilter;
        Object candidate = filter.filterCandidate(mocks, field, new Object()).thenInject();
    }

    /**
     * Test filterCandidate method with mocks and non-matching field
     */
    @Test
    public void testFilterCandidateWithMocksAndNonMatchingField() {
        Set<Object> mocks = newMockSafeHashSet(new Object[] {new Object() {}, new Object() {}});
        Field field = new Object().getClass().getDeclaredField("nonMatchingField");
        MockCandidateFilter filter = new PropertyAndSetterInjection().mockCandidateFilter;
        Object candidate = filter.filterCandidate(mocks, field, new Object()).thenInject();
        assertNull(candidate);
    }

    /**
     * Test filterCandidate method with mocks and matching field
     */
    @Test
    public void testFilterCandidateWithMocksAndMatchingField() {
        Set<Object> mocks = newMockSafeHashSet(new Object[] {new Object() {}, new Object() {}});
        Field field = new Object().getClass().getDeclaredField("field");
        MockCandidateFilter filter = new PropertyAndSetterInjection().mockCandidateFilter;
        Object candidate = filter.filterCandidate(mocks, field, new Object()).thenInject();
        assertNotNull(candidate);
    }

    /**
     * Test thenInject method with null
     */
    @Test(expected = NullPointerException.class)
    public void testThenInjectWithNull() {
        MockCandidateFilter filter = new PropertyAndSetterInjection().mockCandidateFilter;
        filter.thenInject(null);
    }

    /**
     * Test thenInject method with mock instance
     */
    @Test
    public void testThenInjectWithMockInstance() {
        MockCandidateFilter filter = new PropertyAndSetterInjection().mockCandidateFilter;
        Object mock = new Object() {};
        Object result = filter.thenInject(mock);
        assertNotNull(result);
        assertEquals(mock, result);
    }

    /**
     * Test thenInject method with non-mock instance
     */
    @Test(expected = MockitoException.class)
    public void testThenInjectWithNonMockInstance() {
        MockCandidateFilter filter = new PropertyAndSetterInjection().mockCandidateFilter;
        Object nonMockInstance = new Object() {};
        filter.thenInject(nonMockInstance);
    }

    /**
     * Test initializeField method with field annotation and no exception
     */
    @Test
    public void testInitializeFieldWithFieldAnnotationAndNoException() {
        Field field = new Object().getClass().getDeclaredField("injectMocks");
        Object instance = new Object();
        FieldInitializationReport report = new FieldInitializer(instance, field).initialize();
        assertTrue(mockInitializationSuccess(report));
        assertNull(report.getCause());
    }

    /**
     * Test initializeField method with field annotation and exception
     */
    @Test(expected = InvocationTargetException.class)
    public void testInitializeFieldWithFieldAnnotationAndException() {
        Field field = new Object().getClass().getDeclaredField("injectMocks");
        Object instance = new Object();
        new FieldInitializer(instance, field).initialize();
    }

    /**
     * Test initializeField method with non-annotated field and no exception
     */
    @Test
    public void testInitializeFieldWithNonAnnotatedFieldAndNoException() {
        Field field = new Object().getClass().getDeclaredField("nonAnnotatedField");
        Object instance = new Object();
        FieldInitializationReport report = new FieldInitializer(instance, field).initialize();
        assertTrue(mockInitializationSuccess(report));
        assertNull(report.getCause());
    }

    /**
     * Test initializeField method with non-annotated field and exception
     */
    @Test(expected = IllegalAccessException.class)
    public void testInitializeFieldWithNonAnnotatedFieldAndException() {
        Field field = new Object().getClass().getDeclaredField("nonAnnotatedField");
        Object instance = new Object();
        new FieldInitializer(instance, field).initialize();
    }

    /**
     * Test isOut method with final field
     */
    @Test
    public void testIsOutWithFinalField() {
        Field field = new Object().getClass().getDeclaredField("finalField");
        assertTrue(new PropertyAndSetterInjection().notFinalOrStatic.isOut(field));
    }

    /**
     * Test isOut method with static field
     */
    @Test
    public void testIsOutWithStaticField() {
        Field field = new Object().getClass().getDeclaredField("staticField");
        assertTrue(new PropertyAndSetterInjection().notFinalOrStatic.isOut(field));
    }

    /**
     * Test isOut method with non-final and non-static field
     */
    @Test
    public void testIsOutWithNonFinalNonStaticField() {
        Field field = new Object().getClass().getDeclaredField("field");
        assertFalse(new PropertyAndSetterInjection().notFinalOrStatic.isOut(field));
    }

    /**
     * Test sort method with empty list
     */
    @Test
    public void testSortWithEmptyList() {
        List<Field> fields = new ArrayList<>();
        new SuperTypesLastSorter().sort(fields);
        assertTrue(fields.isEmpty());
    }

    /**
     * Test sort method with non-empty list
     */
    @Test
    public void testSortWithNonEmptyList() {
        List<Field> fields = Arrays.asList(new Field[] {new Field() {}, new Field() {}});
        new SuperTypesLastSorter().sort(fields);
        assertTrue(!fields.isEmpty());
        Field first = fields.get(0);
        Field second = fields.get(1);
        // The order depends on the class structure
        if (first.getDeclaringClass().isAssignableFrom(second.getDeclaringClass())) {
            assertTrue(first.getDeclaringClass().isAssignableFrom(second.getDeclaringClass()));
            assertTrue(first.getName().compareTo(second.getName()) < 0);
        } else {
            assertTrue(second.getDeclaringClass().isAssignableFrom(first.getDeclaringClass()));
            assertTrue(first.getName().compareTo(second.getName()) > 0);
        }
    }
}