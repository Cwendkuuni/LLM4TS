package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.reflection.Whitebox;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

public class PropertyAndSetterInjectionTest {

    private static final Class<?> CLASS_UNDER_TEST = PropertyAndSetterInjection.class;
    private static final Class<?> MOCK_CANDIDATE_CLASS = Mockito.class;

    @Rule
    public MockitoJUnit MockitoJUnitRule = new MockitoJUnit();

    @Before
    public void setup() {
        Reporter.setDefaultReporter(new Reporter() {
            @Override
            public void reportFailure(Throwable cause) {
                // Do nothing
            }
        });
    }

    @Test
    public void testProcessInjection() throws Exception {
        Object mockOwner = MockitoJUnitRunner.createTempMock(Object.class);
        Set<Object> mockCandidates = newMockSafeHashSet(MOCK_CANDIDATE_CLASS);
        Field injectMocksField = Whitebox.getInternalState(MockitoJUnitRunner.getTestClass(CLASS_UNDER_TEST), "injectMocksField");

        // Given
        Mockito.doCallRealMethod().when(injectMocksField).set(mockOwner, Mockito.mock(MOCK_CANDIDATE_CLASS));

        // When
        boolean injectionOccurred = new PropertyAndSetterInjection().processInjection(injectMocksField, mockOwner, mockCandidates);

        // Then
        assertTrue(injectionOccurred);

        // Verify that the mock has been injected
        Object injectedMock = Whitebox.getInternalState(mockOwner, "injectedMock");
        Mockito.verify(injectedMock).answer(Mockito.any());
    }

    @Test
    public void testProcessInjection_NoMockFound() throws Exception {
        Object mockOwner = MockitoJUnitRunner.createTempMock(Object.class);
        Set<Object> mockCandidates = newMockSafeHashSet(MOCK_CANDIDATE_CLASS);
        Field injectMocksField = Whitebox.getInternalState(MockitoJUnitRunner.getTestClass(CLASS_UNDER_TEST), "injectMocksField");

        // Given
        Mockito.doCallRealMethod().when(injectMocksField).set(mockOwner, null);

        // When
        boolean injectionOccurred = new PropertyAndSetterInjection().processInjection(injectMocksField, mockOwner, mockCandidates);

        // Then
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInitializeInjectMocksField() throws Exception {
        Object fieldOwner = MockitoJUnitRunner.createTempMock(Object.class);
        Field injectMocksField = Mockito.mock(Field.class);
        Mockito.when(injectMocksField.getName()).thenReturn("injectMocksField");

        // Given
        Mockito.doCallRealMethod().when(injectMocksField).getType();

        // When
        FieldInitializationReport report = new PropertyAndSetterInjection().initializeInjectMocksField(injectMocksField, fieldOwner);

        // Then
        assertTrue("report should not be null", report != null);
        assertTrue("report.fieldClass() should not be null", report.fieldClass() != null);
        assertTrue("report.fieldInstance() should not be null", report.fieldInstance() != null);
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksField_InitializationFailure() throws Exception {
        Object fieldOwner = MockitoJUnitRunner.createTempMock(Object.class);
        Field injectMocksField = Mockito.mock(Field.class);
        Mockito.when(injectMocksField.getName()).thenReturn("injectMocksField");
        Mockito.doThrow(new RuntimeException("Initialization failed")).when(injectMocksField).initialize();

        // When
        new PropertyAndSetterInjection().initializeInjectMocksField(injectMocksField, fieldOwner);

        // Then (exception should be thrown)
    }

    @Test
    public void testInjectMockCandidates() throws Exception {
        Class<?> awaitingInjectionClazz = Mockito.class;
        Object instance = MockitoJUnitRunner.createTempMock(Object.class);
        Set<Object> mocks = newMockSafeHashSet(MOCK_CANDIDATE_CLASS);

        // Given
        Mockito.doCallRealMethod().when(awaitingInjectionClazz).getDeclaredFields();
        List<Field> orderedInstanceFields = Mockito.mock(List.class);
        Mockito.when(orderedInstanceFields.iterator()).thenReturn(Mockito.mock(Iterator.class));

        // When
        boolean injectionOccurred = new PropertyAndSetterInjection().injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_NoMockFound() throws Exception {
        Class<?> awaitingInjectionClazz = Mockito.class;
        Object instance = MockitoJUnitRunner.createTempMock(Object.class);
        Set<Object> mocks = newMockSafeHashSet(MOCK_CANDIDATE_CLASS);

        // Given
        Mockito.doReturn(Collections.emptyList()).when(awaitingInjectionClazz).getDeclaredFields();

        // When
        boolean injectionOccurred = new PropertyAndSetterInjection().injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidatesOnFields() throws Exception {
        Class<?> awaitingInjectionClazz = Mockito.class;
        Object instance = MockitoJUnitRunner.createTempMock(Object.class);
        Set<Object> mocks = newMockSafeHashSet(MOCK_CANDIDATE_CLASS);
        List<Field> orderedInstanceFields = Arrays.asList(Mockito.mock(Field.class), Mockito.mock(Field.class));

        // Given
        Mockito.doReturn(orderedInstanceFields).when(awaitingInjectionClazz).getDeclaredFields();

        // When
        boolean injectionOccurred = new PropertyAndSetterInjection().injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Then
        assertFalse(injectionOccurred);

        // When
        injectionOccurred = new PropertyAndSetterInjection().injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Then
        assertFalse(injectionOccurred);

        // Given
        Mockito.doAnswer(i -> {
            Object mockCandidate = i.getArguments()[2];
            Mockito.when(mockCandidate.getName()).thenReturn("mockCandidate");
            return mockCandidate;
        }).when(orderedInstanceFields).get(0);

        // When
        boolean injectionOccurred2 = new PropertyAndSetterInjection().injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Then
        assertTrue(injectionOccurred2);

        // Given
        Mockito.doAnswer(i -> {
            Object mockCandidate = i.getArguments()[2];
            Mockito.when(mockCandidate.getName()).thenReturn("mockCandidate2");
            return mockCandidate;
        }).when(orderedInstanceFields).get(1);

        // When
        boolean injectionOccurred3 = new PropertyAndSetterInjection().injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Then
        assertTrue(injectionOccurred3);
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        Class<?> awaitingInjectionClazz = Mockito.class;

        // Given
        List<Field> declaredFields = Arrays.asList(Mockito.mock(Field.class), Mockito.mock(Field.class), Mockito.mock(Field.class));

        // When
        List<Field> orderedInstanceFields = new PropertyAndSetterInjection().orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Then
        List<String> names = new ArrayList<>();
        for (Field field : orderedInstanceFields) {
            names.add(field.getName());
        }

        // Verify that the fields are ordered from sub-type to super-type
        List<String> expectedOrder = Arrays.asList("field1", "field2", "field3");
        assertEquals(expectedOrder, names);
    }
}