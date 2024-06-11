package org.mockito.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Field field;
    private Set<Object> mocks;
    private Object testClassInstance;

    @Before
    public void setup() throws NoSuchFieldException {
        injectionEngine = new DefaultInjectionEngine();
        field = MyClass.class.getDeclaredField("myField");
        mocks = new HashSet<>();
        testClassInstance = new MyClass();
    }

    @Test
    public void testInjectMocksOnFields_noMocks() {
        injectionEngine.injectMocksOnFields(new HashSet<>(), mocks, testClassInstance);

        Object injectedField = ((MyClass) testClassInstance).getMyField();
        assertNull(injectedField);
    }

    @Test
    public void testInjectMocksOnFields_noInjectMocksFields() {
        mocks.add(mock(MyDependency.class));
        injectionEngine.injectMocksOnFields(new HashSet<>(), mocks, testClassInstance);

        Object injectedField = ((MyClass) testClassInstance).getMyField();
        assertNull(injectedField);
    }

    @Test
    public void testInjectMocksOnFields_singleMockCandidate() {
        MyDependency mockDependency = mock(MyDependency.class);
        mocks.add(mockDependency);
        injectionEngine.injectMocksOnFields(new HashSet<Field>() {{ add(field); }}, mocks, testClassInstance);

        Object injectedField = ((MyClass) testClassInstance).getMyField();
        assertEquals(mockDependency, injectedField);
    }

    @Test
    public void testInjectMocksOnFields_multipleMockCandidates() {
        MyDependency mockDependency1 = mock(MyDependency.class);
        MyDependency mockDependency2 = mock(MyDependency.class);
        mocks.add(mockDependency1);
        mocks.add(mockDependency2);
        injectionEngine.injectMocksOnFields(new HashSet<Field>() {{ add(field); }}, mocks, testClassInstance);

        Object injectedField = ((MyClass) testClassInstance).getMyField();
        assertEquals(mockDependency1, injectedField);
    }

    @Test
    public void testInjectMocksOnFields_noCandidateFound() {
        MyDependency dependency = new MyDependency();
        ((MyClass) testClassInstance).setMyField(dependency);
        injectionEngine.injectMocksOnFields(new HashSet<Field>() {{ add(field); }}, mocks, testClassInstance);

        Object injectedField = ((MyClass) testClassInstance).getMyField();
        assertEquals(dependency, injectedField);
    }

    private class MyClass {
        private MyDependency myField;

        public MyDependency getMyField() {
            return myField;
        }

        public void setMyField(MyDependency myField) {
            this.myField = myField;
        }
    }

    private class MyDependency {
        
    }
}