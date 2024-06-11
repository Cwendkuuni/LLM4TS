package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        MyClass obj = instantiator.newInstance(MyClass.class);
        assertNotNull(obj);
    }

    @Test
    public void testWithOuterClass() {
        OuterClass outerInstance = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        MyClass obj = instantiator.newInstance(MyClass.class);
        assertNotNull(obj);
        assertEquals(outerInstance, obj.getOuterClassInstance());
    }

    @Test
    public void testExceptionWithOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        try {
            instantiator.newInstance(MyClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'MyClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testExceptionNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());

        try {
            instantiator.newInstance(MyClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'MyClass'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    private static class MyClass {
        private final OuterClass outerClassInstance;

        public MyClass(OuterClass outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }

        public OuterClass getOuterClassInstance() {
            return outerClassInstance;
        }
    }

    private static class OuterClass {
    }
}