package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object result = instantiator.newInstance(String.class);

        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testNewInstanceWithNonNullOuterClass() {
        OuterClass outerClass = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClass);
        Object result = instantiator.newInstance(InnerClass.class);

        assertEquals(InnerClass.class, result.getClass());
        assertEquals(outerClass, ((InnerClass) result).getOuterInstance());
    }

    @Test(expected = InstantationException.class)
    public void testNewInstanceWithInvalidOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator("invalid");
        instantiator.newInstance(InnerClass.class);
    }

    private static class OuterClass {
    }

    private static class InnerClass {
        private final OuterClass outerInstance;

        InnerClass(OuterClass outerInstance) {
            this.outerInstance = outerInstance;
        }

        OuterClass getOuterInstance() {
            return outerInstance;
        }
    }
}