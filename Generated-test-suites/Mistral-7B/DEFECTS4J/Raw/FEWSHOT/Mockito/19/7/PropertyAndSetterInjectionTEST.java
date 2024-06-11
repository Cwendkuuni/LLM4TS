package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjection() throws Exception {
        MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        Class<?> mockClass = MockitoMockitoTest.MockClassWithInjectMocks.class;
        Field injectMocksField = mockClass.getDeclaredField("injectMocksField");
        Object injectMocksFieldOwner = new MockitoMockitoTest.MockClassWithInjectMocks();

        Set<Object> mockCandidates = newMockSafeHashSet(Collections.singletonList(new MockitoMockitoTest.MockClassToInject()));

        assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_NoMockFound() throws Exception {
        MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        Class<?> mockClass = MockitoMockitoTest.MockClassWithInjectMocks.class;
        Field injectMocksField = mockClass.getDeclaredField("injectMocksField");
        Object injectMocksFieldOwner = new MockitoMockitoTest.MockClassWithInjectMocks();

        Set<Object> mockCandidates = newMockSafeHashSet(Collections.emptyList());

        assertFalse(propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }

    @Test
    public void testInitializeInjectMocksField() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        Class<?> mockClass = MockitoMockitoTest.MockClassWithInjectMocksAndField.class;
        Field injectMocksField = mockClass.getDeclaredField("injectMocksField");
        Object injectMocksFieldOwner = new MockitoMockitoTest.MockClassWithInjectMocksAndField();

        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

        assertNotNull(report.getMockInstance());
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksField_Failure() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        Class<?> mockClass = MockitoMockitoTest.MockClassWithInjectMocksThrowingException.class;
        Field injectMocksField = mockClass.getDeclaredField("injectMocksField");
        Object injectMocksFieldOwner = new MockitoMockitoTest.MockClassWithInjectMocksThrowingException();

        propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
    }

    @Test
    public void testInjectMockCandidates() throws Exception {
        MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        Class<?> awaitingInjectionClazz = MockitoMockitoTest.MockClassWithInjectMocksAndFields.class;
        Set<Object> mocks = newMockSafeHashSet(Arrays.asList(new MockitoMockitoTest.MockClassToInject(), new MockitoMockitoTest.MockClassToInject()));
        Object instance = new MockitoMockitoTest.MockClassWithInjectMocksAndFields();

        assertTrue(propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
    }

    @Test
    public void testInjectMockCandidates_NoMockFound() throws Exception {
        MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        Class<?> awaitingInjectionClazz = MockitoMockitoTest.MockClassWithInjectMocksAndFields.class;
        Set<Object> mocks = newMockSafeHashSet(Collections.emptyList());
        Object instance = new MockitoMockitoTest.MockClassWithInjectMocksAndFields();

        assertFalse(propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
    }

    @Test
    public void testInjectMockCandidatesOnFields() throws Exception {
        MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        Class<?> awaitingInjectionClazz = MockitoMockitoTest.MockClassWithInjectMocksAndFields.class;
        Set<Object> mocks = newMockSafeHashSet(Arrays.asList(new MockitoMockitoTest.MockClassToInject(), new MockitoMockitoTest.MockClassToInject()));
        Object instance = new MockitoMockitoTest.MockClassWithInjectMocksAndFields();

        List<Field> orderedInstanceFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());

        assertTrue(propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
    }

    @Test
    public void testInjectMockCandidatesOnFields_NoMockFound() throws Exception {
        MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        Class<?> awaitingInjectionClazz = MockitoMockitoTest.MockClassWithInjectMocksAndFields.class;
        Set<Object> mocks = newMockSafeHashSet(Collections.emptyList());
        Object instance = new MockitoMockitoTest.MockClassWithInjectMocksAndFields();

        List<Field> orderedInstanceFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());

        assertFalse(propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        Class<?> awaitingInjectionClazz = MockitoMockitoTest.MockClassWithInjectMocksAndFields.class;

        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertEquals(2, orderedInstanceFields.size());
    }
}