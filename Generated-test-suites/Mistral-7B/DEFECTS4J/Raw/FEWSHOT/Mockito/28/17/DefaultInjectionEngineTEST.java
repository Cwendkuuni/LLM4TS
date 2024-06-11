package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.mockito3.annotation.InjectMocks;
import org.mockito.mockito3.annotation.Mock;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Set<Field> injectMocksFields;
    private Set<Object> mocks;
    private Object testClassInstance;

    @Mock
    private Object mock1;

    @Mock
    private Object mock2;

    @Mock
    private Object mock3;

    @InjectMocks
    private TestClass testClass;

    @Test
    public void testInjectMocksOnFields() {
        injectionEngine = new DefaultInjectionEngine();
        injectMocksFields = new HashSet<>();
        mocks = new HashSet<>();
        testClassInstance = testClass;
        initMocks(testClass);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Assert.assertEquals(1, mocks.size());
        Assert.assertTrue(mocks.contains(mock1));

        verify(mock1).someMethod();
    }

    @Test
    public void testInjectMocksOnFieldsWithDuplicates() {
        injectionEngine = new DefaultInjectionEngine();
        injectMocksFields = new HashSet<>();
        mocks = new HashSet<>();
        testClassInstance = testClass;
        initMocks(testClass);

        Field field1 = testClass.getClass().getDeclaredField("field1");
        Field field2 = testClass.getClass().getDeclaredField("field1");
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Assert.assertEquals(1, mocks.size());
        Assert.assertTrue(mocks.contains(mock1)); // Any mock can be used

        verify(mock1).someMethod();
    }

    @Test
    public void testInjectMocksOnFieldsWithNoCandidate() {
        injectionEngine = new DefaultInjectionEngine();
        injectMocksFields = new HashSet<>();
        mocks = new HashSet<>();
        testClassInstance = testClass;
        initMocks(testClass);

        Field field1 = testClass.getClass().getDeclaredField("field1");
        injectMocksFields.add(field1);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Assert.assertEquals(1, mocks.size());
        Assert.assertTrue(mocks.contains(mock1));

        verifyZeroInteractions(mock1); // No interactions expected
    }

    @Test
    public void testInjectMocksOnFieldsWithCandidateOnName() {
        injectionEngine = new DefaultInjectionEngine();
        injectMocksFields = new HashSet<>();
        mocks = new HashSet<>();
        testClassInstance = testClass;
        initMocks(testClass);

        Field field1 = testClass.getClass().getDeclaredField("field1");
        Field field2 = testClass.getClass().getDeclaredField("field2");
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);
        mocks.add(mock2);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Assert.assertEquals(1, mocks.size());
        Assert.assertTrue(mocks.contains(mock2));

        verify(mock2).someMethod();
    }

    @Test
    public void testInjectMocksOnFieldsWithCandidateOnType() {
        injectionEngine = new DefaultInjectionEngine();
        injectMocksFields = new HashSet<>();
        mocks = new HashSet<>();
        testClassInstance = testClass;
        initMocks(testClass);

        Class<?> testClassType = testClass.getClass();
        Field field1 = testClassType.getDeclaredField("field1");
        Field field3 = testClassType.getDeclaredField("field3");
        injectMocksFields.add(field1);
        injectMocksFields.add(field3);
        mocks.add(mock3);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Assert.assertEquals(1, mocks.size());
        Assert.assertTrue(mocks.contains(mock3));

        verify(mock3).someMethod();
    }

    @Test
    public void testInjectMocksOnFieldsWithCandidateOnTypeAndName() {
        injectionEngine = new DefaultInjectionEngine();
        injectMocksFields = new HashSet<>();
        mocks = new HashSet<>();
        testClassInstance = testClass;
        initMocks(testClass);

        Field field1 = testClass.getClass().getDeclaredField("field1");
        Field field3 = testClass.getClass().getDeclaredField("field3");
        injectMocksFields.add(field1);
        injectMocksFields.add(field3);
        mocks.add(mock3);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Assert.assertEquals(1, mocks.size());
        Assert.assertTrue(mocks.contains(mock3));

        verify(mock3).someMethod();
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFieldsWithCandidateFailure() {
        injectionEngine = new DefaultInjectionEngine();
        injectMocksFields = new HashSet<>();
        mocks = new HashSet<>();
        testClassInstance = testClass;
        initMocks(testClass);

        Class<?> testClassType = testClass.getClass();
        Field field1 = testClassType.getDeclaredField("field1");
        Field field4 = testClassType.getDeclaredField("field4");
        injectMocksFields.add(field1);
        injectMocksFields.add(field4);
        mocks.add(mock3);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Assert.assertEquals(1, mocks.size()); // Should fail

        // No interactions expected
    }

    static class TestClass {
        @InjectMocks
        private Object field1;

        @InjectMocks
        private Object field2;

        @InjectMocks
        private Object field3;

        private void someMethod() {
        }
    }
}