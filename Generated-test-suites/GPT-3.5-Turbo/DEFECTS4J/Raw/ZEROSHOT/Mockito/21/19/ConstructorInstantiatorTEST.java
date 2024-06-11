package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConstructorInstantiatorTest {

    @Mock
    private InstantationException mockException;

    @Test
    public void testConstructorInstantiator() {
        Object outerClassInstance = Mockito.mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        assertNotNull(constructorInstantiator);
        assertEquals(outerClassInstance, constructorInstantiator.getOuterClassInstance());
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        Object outerClassInstance = null;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        Class<TestClass> cls = TestClass.class;
        TestClass result = constructorInstantiator.newInstance(cls);

        assertNotNull(result);
        assertEquals(cls, result.getClass());
    }

    @Test
    public void testNewInstanceWithOuterClass() {
        Object outerClassInstance = Mockito.mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        Class<TestClass> cls = TestClass.class;
        TestClass result = constructorInstantiator.newInstance(cls);

        assertNotNull(result);
        assertEquals(cls, result.getClass());
        assertEquals(outerClassInstance, result.getOuterClassInstance());
    }

    @Test
    public void testWithOuterClass() throws Exception {
        Object outerClassInstance = Mockito.mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        Class<TestClass> cls = TestClass.class;
        Constructor<TestClass> constructor = cls.getDeclaredConstructor(outerClassInstance.getClass());
        TestClass result = constructorInstantiator.withOuterClass(cls);

        assertNotNull(result);
        assertEquals(cls, result.getClass());
        assertEquals(outerClassInstance, result.getOuterClassInstance());
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClassException() throws Exception {
        Object outerClassInstance = Mockito.mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        Class<TestClass> cls = TestClass.class;
        when(cls.getDeclaredConstructor(outerClassInstance.getClass())).thenThrow(Exception.class);
        constructorInstantiator.withOuterClass(cls);
    }

    @Test
    public void testParamsException() {
        Class<TestClass> cls = TestClass.class;
        Exception exception = Mockito.mock(Exception.class);
        when(exception.getMessage()).thenReturn("Exception message");
        InstantationException result = ConstructorInstantiator.paramsException(cls, exception);

        assertNotNull(result);
        assertEquals("Unable to create mock instance of '"
                + cls.getSimpleName() + "'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", result.getMessage());
        assertEquals(exception, result.getCause());
    }

    @Test
    public void testNoArgConstructor() {
        Class<TestClass> cls = TestClass.class;
        TestClass result = ConstructorInstantiator.noArgConstructor(cls);

        assertNotNull(result);
        assertEquals(cls, result.getClass());
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructorException() {
        Class<TestClass> cls = TestClass.class;
        when(cls.newInstance()).thenThrow(Exception.class);
        ConstructorInstantiator.noArgConstructor(cls);
    }

    private class TestClass {
        private Object outerClassInstance;

        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }
}