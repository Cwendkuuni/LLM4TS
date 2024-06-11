import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine defaultInjectionEngine;

    @Before
    public void setUp() {
        defaultInjectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields_AllMocksInjected() {
        Field mockField1 = createMockField();
        Field mockField2 = createMockField();
        Field injectMocksField = createInjectMocksField();
        Set<Field> injectMocksFields = new HashSet<Field>();
        injectMocksFields.add(injectMocksField);
        Set<Object> mocks = new HashSet<Object>();
        mocks.add(mockField1);
        mocks.add(mockField2);
        Object testClassInstance = new Object();

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        assertNotNull(mockField1.get(testClassInstance));
        assertNotNull(mockField2.get(testClassInstance));
        assertNotNull(injectMocksField.get(testClassInstance));
    }

    @Test
    public void testInjectMocksOnFields_NoInjectMocksFields() {
        Set<Field> injectMocksFields = new HashSet<Field>();
        Set<Object> mocks = new HashSet<Object>();
        Object testClassInstance = new Object();

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // No exception should be thrown
    }

    @Test(expected = Reporter.class)
    public void testInjectMocksOnFields_CannotInitializeForInjectMocksAnnotation() {
        Field mockField = createMockField();
        Field injectMocksField = createInjectMocksField();
        Set<Field> injectMocksFields = new HashSet<Field>();
        injectMocksFields.add(injectMocksField);
        Set<Object> mocks = new HashSet<Object>();
        mocks.add(mockField);
        Object testClassInstance = new Object();

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testInjectMocksOnFields_OnlyOneMockCandidate() {
        Field mockField = createMockField();
        Field injectMocksField = createInjectMocksField();
        Set<Field> injectMocksFields = new HashSet<Field>();
        injectMocksFields.add(injectMocksField);
        Set<Object> mocks = new HashSet<Object>();
        mocks.add(mockField);
        Object testClassInstance = new Object();

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        assertNotNull(mockField.get(testClassInstance));
        assertNotNull(injectMocksField.get(testClassInstance));
    }

    @Test
    public void testInjectMocksOnFields_MultipleMockCandidates() {
        Field mockField1 = createMockField();
        Field mockField2 = createMockField();
        Field injectMocksField = createInjectMocksField();
        Set<Field> injectMocksFields = new HashSet<Field>();
        injectMocksFields.add(injectMocksField);
        Set<Object> mocks = new HashSet<Object>();
        mocks.add(mockField1);
        mocks.add(mockField2);
        Object testClassInstance = new Object();

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        assertNull(mockField1.get(testClassInstance));
        assertNull(mockField2.get(testClassInstance));
        assertNotNull(injectMocksField.get(testClassInstance));
    }

    private Field createMockField() {
        Field field = mock(Field.class);
        when(field.getType()).thenReturn(Object.class);
        return field;
    }

    private Field createInjectMocksField() {
        Field field = mock(Field.class);
        when(field.getType()).thenReturn(Object.class);
        return field;
    }

}