package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.mockitostub.MockitoTest;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.internal.configuration.DefaultInjectionEngine.injectMocksOnFields;
import static org.mockito.internal.configuration.DefaultInjectionEngine.injectMockCandidate;
import static org.mockito.internal.configuration.DefaultInjectionEngine.orderedInstanceFieldsFrom;

public class DefaultInjectionEngineTest {

    private static final String TEST_CLASS_NAME = "TestClassWithInjectMocks";
    private static final String MOCK_CLASS_NAME_1 = "MockClass1";
    private static final String MOCK_CLASS_NAME_2 = "MockClass2";
    private static final String FIELD_NAME_1 = "field1";
    private static final String FIELD_NAME_2 = "field2";

    private DefaultInjectionEngine injectionEngine;
    private Reporter reporter;
    private Set<Field> injectMocksFields;
    private Set<Object> mocks;
    private Object testClassInstance;

    @Before
    public void setup() {
        injectionEngine = new DefaultInjectionEngine();
        reporter = new Reporter();
        testClassInstance = new MockitoTest();

        injectMocksFields = new HashSet<>();
        injectMocksFields.add(TestClassWithInjectMocks.class.getDeclaredField(FIELD_NAME_1));
        injectMocksFields.add(TestClassWithInjectMocks.class.getDeclaredField(FIELD_NAME_2));

        mocks = new HashSet<>();
        mocks.add(MockitoTest.class.getDeclaredField(MOCK_CLASS_NAME_1).get(null));
        mocks.add(MockitoTest.class.getDeclaredField(MOCK_CLASS_NAME_2).get(null));
    }

    @Test
    public void testInjectMocksOnFields() {
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        for (Field field : injectMocksFields) {
            Object fieldInstance = null;
            try {
                fieldInstance = new FieldInitializer(testClassInstance, field).initialize();
            } catch (MockitoException e) {
                Assert.fail("Could not initialize field instance for " + field.getName() + ": " + e.getMessage());
            }

            Class<?> fieldClass = fieldInstance.getClass();
            while (fieldClass != Object.class) {
                Assert.assertEquals(mocks.size(), mocks.size() + 1);
                injectMockCandidate(fieldClass, mocks, fieldInstance);
                fieldClass = fieldClass.getSuperclass();
            }
        }

        Assert.assertEquals(mocks.size(), 4); // Initial mocks + 2 mocks injected
    }

    @Test(expected = AssertionError.class)
    public void testMultipleMockCandidates() {
        // Set up a test case where there are multiple mock candidates for a single field
        injectMocksFields.clear();
        injectMocksFields.add(TestClassWithMultipleCandidates.class.getDeclaredField("field"));

        mocks.clear();
        mocks.addAll(Arrays.asList(
                MockitoTest.class.getDeclaredField(MOCK_CLASS_NAME_1).get(null),
                MockitoTest.class.getDeclaredField(MOCK_CLASS_NAME_2).get(null),
                MockitoTest.class.getDeclaredField(MOCK_CLASS_NAME_1).get(null)
        ));

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }
}

class TestClassWithInjectMocks {
    @InjectMocks
    private static class InnerClass1 {
        private MockClass1 mock1;
        private MockClass2 mock2;
    }

    @InjectMocks
    private static class InnerClass2 {
        private MockClass1 mock1;
        private MockClass2 mock2;
    }

    private InnerClass1 innerClass1;
    private InnerClass2 innerClass2;
}

class TestClassWithMultipleCandidates {
    @InjectMocks
    private static class InnerClass {
        private MockClass1 mock1;
        private MockClass1 mock2;
        private MockClass2 mock3;

        public InnerClass(MockClass1 mock1, MockClass1 mock2, MockClass2 mock3) {
            this.mock1 = mock1;
            this.mock2 = mock2;
            this.mock3 = mock3;
        }
    }

    private InnerClass innerClass;
}