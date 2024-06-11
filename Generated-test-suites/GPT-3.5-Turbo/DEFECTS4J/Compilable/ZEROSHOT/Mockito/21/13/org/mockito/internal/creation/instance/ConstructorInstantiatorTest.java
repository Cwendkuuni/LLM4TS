package org.mockito.internal.creation.instance;

import org.junit.Assert;
import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_noArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Object obj = constructorInstantiator.newInstance(Object.class);
        Assert.assertNotNull(obj);
    }

    @Test
    public void testNewInstance_withOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Object obj = constructorInstantiator.newInstance(Object.class);
        Assert.assertNotNull(obj);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_withOuterClass_exception() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(String.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_noArgConstructor_exception() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(String.class);
    }
}