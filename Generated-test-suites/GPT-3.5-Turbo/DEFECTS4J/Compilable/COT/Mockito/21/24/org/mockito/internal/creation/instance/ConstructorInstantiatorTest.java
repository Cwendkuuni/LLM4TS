package org.mockito.internal.creation.instance;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiatorWithNullOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Assert.assertNotNull(instantiator);
    }

    @Test
    public void testConstructorInstantiatorWithNonNullOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        Assert.assertNotNull(instantiator);
    }

    @Test
    public void testNewInstanceWithNullOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object result = instantiator.newInstance(Object.class);
        Assert.assertNotNull(result);
    }

    @Test
    public void testNewInstanceWithNonNullOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        Object result = instantiator.newInstance(Object.class);
        Assert.assertNotNull(result);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstanceWithNullOuterClassAndNonParameterlessConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(ClassWithNonParameterlessConstructor.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstanceWithNonNullOuterClassAndNonParameterlessConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        instantiator.newInstance(ClassWithNonParameterlessConstructor.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstanceWithNullOuterClassAndMissingConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(ClassWithMissingConstructor.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstanceWithNonNullOuterClassAndMissingConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        instantiator.newInstance(ClassWithMissingConstructor.class);
    }

    private static class ClassWithNonParameterlessConstructor {
        
        public ClassWithNonParameterlessConstructor(Object argument) {
        
        }
    }

    private static class ClassWithMissingConstructor {
        
    }

}

