package org.mockito.internal.creation.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.InstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

public class ConstructorInstantiatorTest {

    private static final Class<ConstructorInstantiator> CONSTRUCTOR_INSTANTIATOR_CLASS = ConstructorInstantiator.class;
    private static final Class<Object> OBJECT_CLASS = Object.class;

    private ConstructorInstantiator constructorInstantiator;
    private Object outerClassInstance;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        outerClassInstance = new Object();
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }

    @Test
    public void testNullOuterClassInstance() throws Exception {
        Constructor<ConstructorInstantiator> constructor = CONSTRUCTOR_INSTANTIATOR_CLASS.getDeclaredConstructor();
        constructor.setAccessible(true);
        ConstructorInstantiator instantiator = (ConstructorInstantiator) constructor.newInstance();

        try {
            instantiator.newInstance(Object.class);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test
    public void testNoArgConstructor() throws Exception {
        Object instance = constructorInstantiator.newInstance(String.class);
        assertNotNull(instance);
    }

    @Test
    public void testConstructorWithOuterClass() throws Exception {
        Class<TestClassWithConstructor> testClassWithConstructor = TestClassWithConstructor.class;

        Object instance = constructorInstantiator.newInstance(testClassWithConstructor);
        assertNotNull(instance);

        TestClassWithConstructor testClassWithConstructorInstance = (TestClassWithConstructor) instance;
        assertNotNull(getInternalState(testClassWithConstructorInstance, "outerClassInstance"));
        assertEquals(outerClassInstance, getInternalState(testClassWithConstructorInstance, "outerClassInstance"));
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithNoArgConstructorException() throws Exception {
        constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(TestClassWithNoArgConstructor.class);
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithParamsException() throws Exception {
        constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(TestClassWithParams.class);
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithWrongOuterClassType() throws Exception {
        Class<TestClassWithWrongOuterClassType> testClassWithWrongOuterClassType = TestClassWithWrongOuterClassType.class;

        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        try {
            constructorInstantiator.newInstance(testClassWithWrongOuterClassType);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    private static class TestClassWithConstructor {
        private Object outerClassInstance;

        public TestClassWithConstructor(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
    }

    private static class TestClassWithNoArgConstructor {
    }

    private static class TestClassWithParams {
        TestClassWithParams(int param) {
        }
    }

    private static class TestClassWithWrongOuterClassType {
        private TestClassWithWrongOuterClassType(String s) {
        }
    }
}