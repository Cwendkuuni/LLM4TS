package org.mockito.internal.configuration.injection;

import org.junit.Before;
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
import org.mockito.internal.util.reflection.TestFields;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.reflection.ReflectionUtils.*;
import static org.mockito.internal.util.reflection.TestFields.*;

public class PropertyAndSetterInjectionTest {

    private Object testClassInstance;
    private static final Class<?> TEST_CLASS = MyTestClass.class;
    private static final Field INJECT_MOCKS_FIELD = getField(TEST_CLASS, INJECT_MOCKS_FIELD_NAME);
    private static final Class<?> MOCKED_CLASS = MockedClass.class;
    private static final Object MOCKED_INSTANCE = createMock(MOCKED_CLASS);
    private static final Set<Object> mocks = Sets.newMockSafeHashSet(MOCKED_INSTANCE);

    @Before
    public void setUp() throws Exception {
        testClassInstance = createTestClassInstance();
    }

    @Test
    public void testProcessInjection_noInjectMocksField_noInjection() throws Exception {
        assertFalse(processInjection(null, testClassInstance, mocks));
    }

    @Test
    public void testProcessInjection_noInjectMocksFieldOwner_noInjection() throws Exception {
        assertFalse(processInjection(INJECT_MOCKS_FIELD, null, mocks));
    }

    @Test
    public void testProcessInjection_emptyMockCandidates_noInjection() throws Exception {
        assertFalse(processInjection(INJECT_MOCKS_FIELD, testClassInstance, Collections.emptySet()));
    }

    @Test
    public void testInitializeInjectMocksField_noMockCandidate_nullReport() throws Exception {
        FieldInitializationReport report = initializeInjectMocksField(null, testClassInstance);
        assertNull(report);
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksField_mockCandidateInitializationException_reported() throws Exception {
        FieldInitializationReport report = initializeInjectMocksField(testFieldWithConstructorException(TEST_CLASS), testClassInstance);
        assertNotNull(report);
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksField_noInjectMocksAnnotation_reported() throws Exception {
        FieldInitializationReport report = initializeInjectMocksField(testFieldWithoutInjectMocksAnnotation(TEST_CLASS), testClassInstance);
        assertNotNull(report);
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksField_noConstructor_reported() throws Exception {
        FieldInitializationReport report = initializeInjectMocksField(testFieldWithoutConstructor(TEST_CLASS), testClassInstance);
        assertNotNull(report);
    }

    @Test
    public void testInjectMockCandidates_noMockCandidates_noInjection() throws Exception {
        assertFalse(injectMockCandidates(TEST_CLASS, mocks, testClassInstance));
    }

    @Test
    public void testInjectMockCandidates_oneMockCandidate_oneInjection() throws Exception {
        assertFalse(injectMockCandidates(TEST_CLASS, mocks, testClassInstance));
        assertFalse(injectMockCandidates(TEST_CLASS, mocks, testClassInstance));
        assertTrue(testFieldGetValue(testClassInstance, INJECT_MOCKS_FIELD).equals(MOCKED_INSTANCE));
    }

    @Test
    public void testInjectMockCandidates_multipleMockCandidates_oneInjection() throws Exception {
        assertFalse(injectMockCandidates(TEST_CLASS, mocks, testClassInstance));
        assertFalse(injectMockCandidates(TEST_CLASS, mocks, testClassInstance));
        assertTrue(testFieldGetValue(testClassInstance, INJECT_MOCKS_FIELD).equals(MOCKED_INSTANCE));
    }

    @Test
    public void testInjectMockCandidates_noMockCandidate_noInjection() throws Exception {
        falseFalse(injectMockCandidates(MOCKED_CLASS, mocks, testClassInstance));
        falseFalse(injectMockCandidates(MOCKED_CLASS, mocks, testClassInstance));
    }

    @Test
    public void testInjectMockCandidatesOnFields_noMockCandidates_noInjection() throws Exception {
        assertFalse(injectMockCandidatesOnFields(mocks, testClassInstance, false, orderedInstanceFieldsFrom(TEST_CLASS)));
        assertFalse(injectMockCandidatesOnFields(mocks, testClassInstance, false, orderedInstanceFieldsFrom(TEST_CLASS)));
    }

    @Test
    public void testInjectMockCandidatesOnFields_oneMockCandidate_oneInjection() throws Exception {
        assertFalse(injectMockCandidatesOnFields(mocks, testClassInstance, false, orderedInstanceFieldsFrom(TEST_CLASS)));
        assertFalse(injectMockCandidatesOnFields(mocks, testClassInstance, false, orderedInstanceFieldsFrom(TEST_CLASS)));
        assertTrue(testFieldGetValue(testClassInstance, testFieldWithMockCandidate(TEST_CLASS).getName()).equals(MOCKED_INSTANCE));
    }

    @Test
    public void testInjectMockCandidatesOnFields_multipleMockCandidates_oneInjection() throws Exception {
        assertFalse(injectMockCandidatesOnFields(mocks, testClassInstance, false, orderedInstanceFieldsFrom(TEST_CLASS)));
        assertFalse(injectMockCandidatesOnFields(mocks, testClassInstance, false, orderedInstanceFieldsFrom(TEST_CLASS)));
        assertTrue(testFieldGetValue(testClassInstance, testFieldWithMockCandidate(TEST_CLASS).getName()).equals(MOCKED_INSTANCE));
    }

    @Test
    public void testInjectMockCandidatesOnFields_noMockCandidate_noInjection() throws Exception {
        assertFalse(injectMockCandidatesOnFields(mocks, testClassInstance, false, orderedInstanceFieldsFrom(MOCKED_CLASS)));
        assertFalse(injectMockCandidatesOnFields(mocks, testClassInstance, false, orderedInstanceFieldsFrom(MOCKED_CLASS)));
    }

    @Test
    public void testInjectMockCandidatesOnFields_propertySetterAvailable_oneInjection() throws Exception {
        assertFalse(injectMockCandidatesOnFields(mocks, testClassInstance, false, orderedInstanceFieldsFrom(TEST_CLASS)));
        assertFalse(injectMockCandidatesOnFields(mocks, testClassInstance, false, orderedInstanceFieldsFrom(TEST_CLASS)));
        assertTrue(testFieldGetValue(testClassInstance, testFieldWithMockCandidateAndSetter(TEST_CLASS).getName()).equals(MOCKED_INSTANCE));
    }

    @Test
    public void testInjectMockCandidatesOnFields_propertySetterNotAvailable_fieldInjection() throws Exception {
        assertFalse(injectMockCandidatesOnFields(mocks, testClassInstance, false, orderedInstanceFieldsFrom(TEST_CLASS)));
        assertFalse(injectMockCandidatesOnFields(mocks, testClassInstance, false, orderedInstanceFieldsFrom(TEST_CLASS)));
        assertTrue(testFieldGetValue(testClassInstance, testFieldWithoutSetter(TEST_CLASS).getName()).equals(MOCKED_INSTANCE));
    }

    private Object createTestClassInstance() throws Exception {
        return newInstance(TEST_CLASS);
    }

    private boolean processInjection(Field injectMocksField, Object injectMocksFieldOwner, Set<Object> mockCandidates) throws Exception {
        return new PropertyAndSetterInjection().processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
    }

    private FieldInitializationReport initializeInjectMocksField(Field field, Object fieldOwner) throws Exception {
        return new PropertyAndSetterInjection().initializeInjectMocksField(field, fieldOwner);
    }

    private boolean injectMockCandidates(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object instance) throws Exception {
        return new PropertyAndSetterInjection().injectMockCandidates(awaitingInjectionClazz, mocks, instance);
    }

    private boolean injectMockCandidatesOnFields(Set<Object> mocks, Object instance, boolean injectionOccurred, List<Field> orderedInstanceFields) throws Exception {
        return new PropertyAndSetterInjection().injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
    }

    private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        return new PropertyAndSetterInjection().orderedInstanceFieldsFrom(awaitingInjectionClazz);
    }
}

class MyTestClass {
    @InjectMocks
    private MockedClass mockedField;
}

class MockedClass {
}

class TestFieldWithConstructorException extends Field {
    {
        setAccessible(true);
    }

    public TestFieldWithConstructorException() {
        throw new RuntimeException("Mock initialization error");
    }
}

class TestFieldWithoutInjectMocksAnnotation extends Field {
    {
        setAccessible(true);
        setAnnotation(InjectMocks.class);
    }
}

class TestFieldWithoutConstructor extends Field {
    {
        setAccessible(true);
        setAnnotation(InjectMocks.class);
    }
}

class TestFieldWithMockCandidate extends Field {
    {
        setAccessible(true);
        setAnnotation(InjectMocks.class);
    }
}

class TestFieldWithMockCandidateAndSetter extends Field {
    {
        setAccessible(true);
        setAnnotation(InjectMocks.class);
    }
}

class TestFieldWithoutSetter extends Field {
    {
        setAccessible(true);
        setAnnotation(InjectMocks.class);
    }
}