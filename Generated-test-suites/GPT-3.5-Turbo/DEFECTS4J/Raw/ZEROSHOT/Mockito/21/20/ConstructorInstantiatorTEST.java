package org.mockito.internal.creation.instance;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {

    // Test data
    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void setup() {
        constructorInstantiator = new ConstructorInstantiator(null);
    }

    @Test
    public void testNewInstance_withNullOuterClassInstance_returnsNoArgConstructor() {
        Class<Object> cls = Object.class;
        Object result = constructorInstantiator.newInstance(cls);
        assertNull(result);
    }

    @Test
    public void testNewInstance_withNonNullOuterClassInstance_returnsWithOuterClass() {
        Class<Object> cls = Object.class;
        Object outerClassInstance = new Object();
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Object result = constructorInstantiator.newInstance(cls);
        assertNotNull(result);
    }

    @Test
    public void testWithOuterClass_withValidParameters_returnsObject() {
        Class<Object> cls = Object.class;
        Object outerClassInstance = new Object();
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Object result = constructorInstantiator.withOuterClass(cls);
        assertNotNull(result);
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClass_withInvalidParameters_throwsException() {
        Class<Object> cls = Object.class;
        Object outerClassInstance = new Integer(10);
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.withOuterClass(cls);
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClass_withExceptionInConstructor_throwsException() {
        Class<TestClass> cls = TestClass.class;
        Object outerClassInstance = new TestClass();
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.withOuterClass(cls);
    }

    @Test
    public void testParamsException_withValidParameters_returnsInstantationException() {
        Class<Object> cls = Object.class;
        Exception e = new Exception("Test exception");
        InstantationException result = constructorInstantiator.paramsException(cls, e);
        assertNotNull(result);
        assertEquals("Unable to create mock instance of 'Object'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", result.getMessage());
        assertEquals(e, result.getCause());
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructor_withExceptionInConstructor_throwsException() {
        Class<TestClass> cls = TestClass.class;
        constructorInstantiator.noArgConstructor(cls);
    }

    class TestClass {
        public TestClass() throws Exception {
            throw new Exception("Test exception");
        }
    }

}