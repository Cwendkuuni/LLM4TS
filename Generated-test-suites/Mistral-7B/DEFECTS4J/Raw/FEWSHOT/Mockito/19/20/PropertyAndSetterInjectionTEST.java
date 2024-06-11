package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.runners.MockitoJUnitRunner.InjectMocks;

/**
 * Test class for PropertyAndSetterInjection.
 */
public class PropertyAndSetterInjectionTest {

    private static final Class<?> CLASS_TO_TEST = TestClassToInject.class;

    @InjectMocks
    private TestClassToInject testClassToInject;

    private MockCandidateFilter mockCandidateFilter;
    private ListUtil.Filter<Field> notFinalOrStatic;
    private List<Field> orderedInstanceFields;
    private Field testField;
    private Object testFieldOwner;
    private Set<Object> mockCandidates;

    @Before
    public void setUp() {
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        notFinalOrStatic = new ListUtil.Filter<Field>() {
            public boolean isOut(Field object) {
                return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
            }
        };

        testField = CLASS_TO_TEST.getDeclaredField("fieldToInject");
        testFieldOwner = new Object() {};
        testField.setAccessible(true);
        mockCandidates = newMockSafeHashSet(Arrays.asList(new Object() {}, new Object() {}));
    }

    @Test
    public void testProcessInjection() {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        assertTrue(propertyAndSetterInjection.processInjection(testField, testFieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_NoMockFound() {
        mockCandidates.clear();
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        assertFalse(propertyAndSetterInjection.processInjection(testField, testFieldOwner, mockCandidates));
    }

    @Test
    public void testInitializeInjectMocksField() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(testField, testFieldOwner);
        assertTrue(report != null);
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksField_InitializationThrowsException() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        FieldInitializer fieldInitializer = new FieldInitializer(testFieldOwner, testField);
        fieldInitializer.initialize(); // this will throw an exception
        propertyAndSetterInjection.initializeInjectMocksField(testField, testFieldOwner);
    }

    @Test
    public void testInjectMockCandidates() {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        orderedInstanceFields = orderedInstanceFieldsFrom(CLASS_TO_TEST);
        assertTrue(propertyAndSetterInjection.injectMockCandidates(CLASS_TO_TEST, mockCandidates, testFieldOwner));
    }

    @Test
    public void testInjectMockCandidates_NoMockFound() {
        mockCandidates.clear();
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        assertFalse(propertyAndSetterInjection.injectMockCandidates(CLASS_TO_TEST, mockCandidates, testFieldOwner));
    }

    @Test
    public void testInjectMockCandidatesOnFields() {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        List<Field> orderedInstanceFieldsCopy = new ArrayList<>(orderedInstanceFields);
        assertTrue(propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, testFieldOwner, false, orderedInstanceFieldsCopy));
    }

    @Test
    public void testInjectMockCandidatesOnFields_NoMockFound() {
        mockCandidates.clear();
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        List<Field> orderedInstanceFieldsCopy = new ArrayList<>(orderedInstanceFields);
        assertFalse(propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, testFieldOwner, false, orderedInstanceFieldsCopy));
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        List<Field> orderedInstanceFieldsFromClassToTest = orderedInstanceFieldsFrom(CLASS_TO_TEST);
        assertEquals(orderedInstanceFieldsFromClassToTest, Arrays.asList(testField));
    }

    @Test
    public void testOrderedInstanceFieldsFrom_EmptyClass() {
        List<Field> orderedInstanceFieldsFromEmptyClass = orderedInstanceFieldsFrom(Object.class);
        assertTrue(orderedInstanceFieldsFromEmptyClass.isEmpty());
    }

    private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        return propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    }
}

class TestClassToInject {
    @InjectMocks
    private Field fieldToInject;
}