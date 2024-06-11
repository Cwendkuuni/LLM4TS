package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.getInnerStaticField;
import static org.mockito.internal.util.reflection.Whitebox.invokeMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        Class<String> cls = String.class;
        assertNull(new ConstructorInstantiator(null).newInstance(cls));
    }

    @Test
    public void testNewInstanceWithNullClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        assertNull(instantiator.newInstance(null));
    }

    @Test
    public void testNewInstanceWithParameterlessConstructor() {
        Class<String> cls = String.class;
        Object outerClassInstance = new Object();
        String instance = new ConstructorInstantiator(outerClassInstance).newInstance(cls);
        assertInstanceOf(String.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNonParameterlessConstructorThrowException() {
        Class<MyTestClass> cls = MyTestClass.class;
        Object outerClassInstance = new Object();
        new ConstructorInstantiator(outerClassInstance).newInstance(cls);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassWithNullClass() {
        Class<MyTestClass> cls = null;
        Object outerClassInstance = new Object();
        new ConstructorInstantiator(outerClassInstance).withOuterClass(cls);
    }

    @Test
    public void testWithOuterClassWithParameterlessConstructor() {
        Class<MyTestClass> cls = MyTestClass.class;
        Object outerClassInstance = new Object();
        MyTestClass instance = new ConstructorInstantiator(outerClassInstance).withOuterClass(cls);
        assertInstanceOf(MyTestClass.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassWithNonParameterlessConstructor() {
        Class<MyTestClassWithTwoParams> cls = MyTestClassWithTwoParams.class;
        Object outerClassInstance = new Object();
        new ConstructorInstantiator(outerClassInstance).withOuterClass(cls);
    }

    private static class MyTestClass {
    }

    private static class MyTestClassWithTwoParams {
        MyTestClassWithTwoParams(Object outerClassInstance) {
        }
    }
}