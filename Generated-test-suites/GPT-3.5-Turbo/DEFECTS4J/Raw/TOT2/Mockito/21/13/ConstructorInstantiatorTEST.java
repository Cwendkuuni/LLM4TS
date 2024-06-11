import static org.junit.Assert.*;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceNoArg() {
        // Typical use case - creating instance with no outer class
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object obj = instantiator.newInstance(Object.class);
        assertNotNull(obj);
    }

    @Test
    public void testNewInstanceWithOuterClass() {
        // Typical use case - creating instance with outer class
        Object outerClass = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClass);
        Object obj = instantiator.newInstance(Object.class);
        assertNotNull(obj);
    }

    @Test
    public void testWithOuterClassConstructorException() {
        // Edge case - constructor exception with outer class
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        try {
            instantiator.newInstance(String.class);
            fail("Expected InstantiationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'String'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNoArgConstructorException() {
        // Error scenario - exception in parameter-less constructor
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(AbstractClass.class);
            fail("Expected InstantiationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'AbstractClass'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }
}