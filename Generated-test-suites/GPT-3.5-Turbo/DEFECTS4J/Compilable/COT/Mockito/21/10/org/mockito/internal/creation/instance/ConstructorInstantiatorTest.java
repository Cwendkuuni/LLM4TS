package org.mockito.internal.creation.instance;

import org.junit.Assert;
import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        ClassToInstantiate instance = instantiator.newInstance(ClassToInstantiate.class);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testWithOuterClass() {
        ClassToInstantiateOuter outerInstance = new ClassToInstantiateOuter();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        ClassToInstantiate instance = instantiator.newInstance(ClassToInstantiate.class);
        Assert.assertNotNull(instance);
        Assert.assertEquals(outerInstance, instance.getOuterClassInstance());
    }

    @Test(expected = InstantationException.class)
    public void testInvalidOuterClassInstance() {
        Object invalidOuterInstance = "Invalid outer instance";
        ConstructorInstantiator instantiator = new ConstructorInstantiator(invalidOuterInstance);
        ClassToInstantiate instance = instantiator.newInstance(ClassToInstantiate.class);
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructorFailure() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        ClassWithNoArgConstructorFailure instance = instantiator.newInstance(ClassWithNoArgConstructorFailure.class);
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClassFailure() {
        ClassToInstantiateOuter outerInstance = new ClassToInstantiateOuter();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        ClassWithOuterClassFailure instance = instantiator.newInstance(ClassWithOuterClassFailure.class);
    }

    private static class ClassToInstantiate {
        private Object outerClassInstance;

        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }

    private static class ClassToInstantiateOuter {

    }

    private static class ClassWithNoArgConstructorFailure {

        public ClassWithNoArgConstructorFailure(String arg) {
        }
    }

    private static class ClassWithOuterClassFailure {

        public ClassWithOuterClassFailure() {
        }
    }
}

class InstantationException extends RuntimeException {

    public InstantationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstantationException(String message) {
        super(message);
    }
}
