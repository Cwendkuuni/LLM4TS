import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields_singleInjectMocksField() throws Exception {
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        Field mockField = TestClass.class.getDeclaredField("mockField");
        injectMocksFields.add(mockField);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that the mockField is initialized and injected
        assertNotNull(mockField.get(testClassInstance));
        assertTrue(mocks.contains(mockField.get(testClassInstance)));
    }

    @Test
    public void testInjectMocksOnFields_multipleInjectMocksFields() throws Exception {
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        Field mockField1 = TestClass.class.getDeclaredField("mockField1");
        Field mockField2 = TestClass.class.getDeclaredField("mockField2");
        Field mockField3 = TestClass.class.getDeclaredField("mockField3");
        injectMocksFields.add(mockField1);
        injectMocksFields.add(mockField2);
        injectMocksFields.add(mockField3);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that all mock fields are initialized and injected
        assertNotNull(mockField1.get(testClassInstance));
        assertTrue(mocks.contains(mockField1.get(testClassInstance)));
        assertNotNull(mockField2.get(testClassInstance));
        assertTrue(mocks.contains(mockField2.get(testClassInstance)));
        assertNotNull(mockField3.get(testClassInstance));
        assertTrue(mocks.contains(mockField3.get(testClassInstance)));
    }

    @Test
    public void testInjectMocksOnFields_noMocksToBeInjected() throws Exception {
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        Field mockField = TestClass.class.getDeclaredField("mockField");
        injectMocksFields.add(mockField);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that the mockField is not initialized or injected
        assertNull(mockField.get(testClassInstance));
        assertFalse(mocks.contains(mockField.get(testClassInstance)));
    }

    @Test
    public void testInjectMocksOnFields_mockCandidateFilterCalled() throws Exception {
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        Field mockField = TestClass.class.getDeclaredField("mockField");
        injectMocksFields.add(mockField);

        DefaultInjectionEngine.MockCandidateFilter mockCandidateFilter = Mockito.mock(DefaultInjectionEngine.MockCandidateFilter.class);
        injectionEngine.setMockCandidateFilter(mockCandidateFilter);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that the mockCandidateFilter's thenInject() method is called
        Mockito.verify(mockCandidateFilter).thenInject();
    }

    @Test
    public void testInjectMocksOnFields_orderedInstanceFieldsFromCalled() throws Exception {
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        Field mockField = TestClass.class.getDeclaredField("mockField");
        injectMocksFields.add(mockField);

        DefaultInjectionEngine.MockCandidateFilter mockCandidateFilter = Mockito.mock(DefaultInjectionEngine.MockCandidateFilter.class);
        injectionEngine.setMockCandidateFilter(mockCandidateFilter);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that the orderedInstanceFieldsFrom() method is called
        Mockito.verify(mockCandidateFilter).orderedInstanceFieldsFrom(TestClass.class);
    }
}

class TestClass {
    private Object mockField;

    private Object mockField1;
    private Object mockField2;
    private Object mockField3;
}