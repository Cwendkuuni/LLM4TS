package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.configuration.DefaultInjectionEngine.injectMocksOnFields;
import static org.mockito.internal.configuration.DefaultInjectionEngine.injectMockCandidate;
import static org.mockito.internal.configuration.DefaultInjectionEngine.orderedInstanceFieldsFrom;
import static org.mockito.internal.util.reflection.FieldUtils.getPrivateFieldValue;
import static org.mockito.internal.util.reflection.FieldUtils.setPrivateFieldValue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.MockCandidate;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.internal.util.reflection.Whitebox;

public class DefaultInjectionEngineTest {

    private static final String TEST_CLASS_NAME = "TestClass";
    private static final String MOCK_CLASS_NAME = "MockClass";
    private static final String MOCK_FIELD_NAME = "mockField";
    private static final String INJECT_MOCKS_FIELD_NAME = "injectMocksField";
    private static final String SUB_MOCK_FIELD_NAME = "subMockField";
    private static final String FINAL_MOCK_FIELD_NAME = "finalMockField";
    private static final String NAMED_MOCK_FIELD_NAME = "namedMockField";

    private DefaultInjectionEngine injectionEngine;
    private Set<Object> mocks;
    private Object testClassInstance;
    private Field injectMocksField;
    private Field mockField;
    private Field subMockField;
    private Field finalMockField;
    private Field namedMockField;

    @Before
    public void setUp() throws Exception {
        injectionEngine = new DefaultInjectionEngine();
        mocks = new HashSet<>();
        testClassInstance = Whitebox.newInstance(TEST_CLASS_NAME);
        injectMocksField = FieldUtils.getPrivateField(testClassInstance, INJECT_MOCKS_FIELD_NAME, false);
        mockField = FieldUtils.getPrivateField(TestClass.class, MOCK_FIELD_NAME, true);
        subMockField = FieldUtils.getPrivateField(SubMockClass.class, SUB_MOCK_FIELD_NAME, true);
        finalMockField = FieldUtils.getPrivateField(FinalMockClass.class, FINAL_MOCK_FIELD_NAME, true);
        namedMockField = FieldUtils.getPrivateField(NamedMockClass.class, NAMED_MOCK_FIELD_NAME, true);

        MockCandidateFilter nameBasedFilter = new NameBasedCandidateFilter(new FinalMockCandidateFilter());
        MockCandidateFilter typeBasedFilter = new TypeBasedCandidateFilter(nameBasedFilter);
        injectionEngine.setMockCandidateFilter(typeBasedFilter);
    }

    @Test
    public void testInjectMocksOnFields_noAnnotatedFields() {
        Set<Field> injectMocksFields = Arrays.asList(new Field[0]).toString().equals(injectMocksField.getType().getDeclaredFields().toString()) ? Arrays.asList(new Field[0]) : Arrays.asList(injectMocksField);
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        assertFalse("No fields annotated with @InjectMocks", injectionMocksFields.isEmpty());
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFields_mockInitializationFailure() throws Exception {
        Set<Field> injectMocksFields = Arrays.asList(injectMocksField);
        MockitoException exception = new MockitoException("Cannot initialize mock for field " + injectMocksField.getName());
        FieldInitializer initializer = new FieldInitializer(testClassInstance, injectMocksField);
        initializer.setExceptionThrown(true);
        injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testInjectMocksOnFields_injectMock() throws Exception {
        MockCandidate mockCandidate = new MockCandidate(Mockito.mock(MOCK_CLASS_NAME));
        mocks.add(mockCandidate.getMock());

        Set<Field> injectMocksFields = Arrays.asList(injectMocksField);
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Object injectedMock = getPrivateFieldValue(injectMocksField, testClassInstance);
        assertNotNull("Injected mock is null", injectedMock);
        assertEquals(mockCandidate.getMock(), injectedMock);
    }

    @Test
    public void testInjectMocksOnFields_injectSubTypeMock() throws Exception {
        MockCandidate mockCandidate = new MockCandidate(Mockito.mock(MOCK_CLASS_NAME));
        MockCandidate subMockCandidate = new MockCandidate(Mockito.mock(SubMockClass.class));
        mocks.add(mockCandidate.getMock());
        mocks.add(subMockCandidate.getMock());

        Set<Field> injectMocksFields = Arrays.asList(injectMocksField, subMockField);
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Object injectedSubMock = getPrivateFieldValue(subMockField, testClassInstance);
        assertNotNull("Injected sub mock is null", injectedSubMock);
        assertEquals(subMockCandidate.getMock(), injectedSubMock);
    }

    @Test
    public void testInjectMocksOnFields_injectSuperTypeMock() throws Exception {
        Mockito.when(mockField.get(testClassInstance)).thenReturn(Mockito.mock(MOCK_CLASS_NAME));

        MockCandidate mockCandidate = new MockCandidate(Mockito.mock(MOCK_CLASS_NAME));
        MockCandidate superMockCandidate = new MockCandidate(Mockito.mock(Object.class));
        mocks.add(mockCandidate.getMock());
        mocks.add(superMockCandidate.getMock());

        Set<Field> injectMocksFields = Arrays.asList(injectMocksField, mockField, finalMockField);
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Object injectedMock = getPrivateFieldValue(injectMocksField, testClassInstance);
        assertNotNull("Injected mock is null", injectedMock);
        assertEquals(mockCandidate.getMock(), injectedMock);

        Object injectedSuperMock = getPrivateFieldValue(finalMockField, testClassInstance);
        assertNotNull("Injected super mock is null", injectedSuperMock);
        assertEquals(superMockCandidate.getMock(), injectedSuperMock);
    }

    @Test
    public void testInjectMocksOnFields_injectNamedMock() throws Exception {
        MockCandidate namedMockCandidate = new MockCandidate(Mockito.mock(NAMED_MOCK_FIELD_NAME));
        mocks.add(namedMockCandidate.getMock());

        Set<Field> injectMocksFields = Arrays.asList(injectMocksField, namedMockField);
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Object injectedMock = getPrivateFieldValue(injectMocksField, testClassInstance);
        assertNotNull("Injected mock is null", injectedMock);
        assertEquals(mockField.getName(), namedMockCandidate.getName());
        assertEquals(namedMockCandidate.getMock(), injectedMock);
    }

    @Test
    public void testInjectMocksOnFields_injectFinalMock() throws Exception {
        MockCandidate finalMockCandidate = new MockCandidate(Mockito.mock(FINAL_MOCK_FIELD_NAME));
        mocks.add(finalMockCandidate.getMock());

        Set<Field> injectMocksFields = Arrays.asList(injectMocksField, finalMockField);
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Object injectedMock = getPrivateFieldValue(injectMocksField, testClassInstance);
        assertNotNull("Injected mock is null", injectedMock);
        assertNull("Injected final mock should be null", getPrivateFieldValue(finalMockField, testClassInstance));
        assertEquals(mockField.getName(), injectedMock.getClass().getCanonicalName());
    }

    @Test
    public void testInjectMockCandidate_noCandidates() throws Exception {
        Set<Object> mocksToBeInjected = new HashSet<>(mocks);
        injectionEngine.injectMockCandidate(MOCK_CLASS_NAME, mocksToBeInjected, testClassInstance);
        assertFalse("No candidates found for class " + MOCK_CLASS_NAME, mocksToBeInjected.isEmpty());
    }

    @Test
    public void testInjectMockCandidate_singleCandidate() throws Exception {
        MockCandidate mockCandidate = new MockCandidate(Mockito.mock(MOCK_CLASS_NAME));
        mocks.add(mockCandidate.getMock());
        mocksToBeInjected.remove(mockCandidate.getMock());

        injectionEngine.injectMockCandidate(MOCK_CLASS_NAME, mocksToBeInjected, testClassInstance);

        Object injectedMock = getPrivateFieldValue(mockField, testClassInstance);
        assertNotNull("Injected mock is null", injectedMock);
        assertEquals(mockCandidate.getMock(), injectedMock);

        assertFalse("No mocks left in mocksToBeInjected", mocksToBeInjected.isEmpty());
    }

    @Test
    public void testInjectMockCandidate_multipleCandidatesWithNameMatch() throws Exception {
        MockCandidate namedMockCandidate1 = new MockCandidate(Mockito.mock(NAMED_MOCK_FIELD_NAME));
        MockCandidate namedMockCandidate2 = new MockCandidate(Mockito.mock(NAMED_MOCK_FIELD_NAME));
        mocks.addAll(Arrays.asList(namedMockCandidate1, namedMockCandidate2));
        mocksToBeInjected.remove(namedMockCandidate1.getMock());

        injectionEngine.injectMockCandidate(NAMED_MOCK_FIELD_NAME, mocksToBeInjected, testClassInstance);

        Object injectedMock = getPrivateFieldValue(namedMockField, testClassInstance);
        assertNotNull("Injected mock is null", injectedMock);
        assertEquals(namedMockCandidate2.getMock(), injectedMock);

        assertFalse("No mocks left in mocksToBeInjected", mocksToBeInjected.isEmpty());
    }

    @Test
    public void testInjectMockCandidate_multipleCandidatesWithNoMatch() throws Exception {
        MockCandidate mockCandidate1 = new MockCandidate(Mockito.mock(MOCK_CLASS_NAME));
        MockCandidate mockCandidate2 = new MockCandidate(Mockito.mock(SubMockClass.class));
        mocks.addAll(Arrays.asList(mockCandidate1, mockCandidate2));
        mocksToBeInjected.remove(mockCandidate1.getMock());

        injectionEngine.injectMockCandidate(MOCK_CLASS_NAME, mocksToBeInjected, testClassInstance);

        assertFalse("Candidates remaining in mocksToBeInjected", mocksToBeInjected.isEmpty());
    }
}