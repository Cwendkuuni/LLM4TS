package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.exceptions.base.InstantiationException;

import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NullOuterClassInstance_NoArgConstructor() {
        Class<String> stringClass = String.class;
        assertNotNull(ConstructorInstantiator.noArgConstructor(stringClass));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_NullOuterClassInstance_ThrowExceptionForNoArgConstructor() {
        Class<String> stringClass = String.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(stringClass);
    }

    @Test
    public void testNewInstance_NotNullOuterClassInstance_NoArgConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        String instance = instantiator.newInstance(stringClass);
        assertNotNull(instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_NotNullOuterClassInstance_ThrowExceptionForNoSuchConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.newInstance(stringBuilderClass);
    }

    @Test
    public void testNewInstance_NotNullOuterClassInstance_WithOuterClassConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<PrintStream> printStreamClass = PrintStream.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        PrintStream instance = instantiator.newInstance(printStreamClass);
        assertNotNull(instance);
    }
}