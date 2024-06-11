import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        // Typical use case with a class that has a parameter-less constructor
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        SomeClass instance = instantiator.newInstance(SomeClass.class);
        assertNotNull(instance);
    }

    @Test
    public void testNoArgConstructor_Exception() {
        // Error scenario where the class does not have a parameter-less constructor
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(SomeClassWithArgs.class);
            fail("Expected an InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'SomeClassWithArgs'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testWithOuterClass() {
        // Typical use case with a class that has an outer class instance
        OuterClass outerInstance = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        SomeClass instance = instantiator.newInstance(SomeClass.class);
        assertNotNull(instance);
        assertEquals(outerInstance, instance.getOuterInstance());
    }

    @Test
    public void testWithOuterClass_Args() {
        // Error scenario where the class does not have a constructor that accepts the outer class instance as a parameter
        OuterClass outerInstance = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        try {
            instantiator.newInstance(SomeClassWithArgs.class);
            fail("Expected an InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'SomeClassWithArgs'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testWithOuterClass_Args_Exception() {
        // Error scenario where an exception occurs while instantiating the class with outer class instance
        OuterClass outerInstance = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        try {
            instantiator.newInstance(SomeClassWithException.class);
            fail("Expected an InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'SomeClassWithException'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
            assertNotNull(e.getCause());
        }
    }

    private static class SomeClass {
        // Empty constructor for typical use case
        public SomeClass() {}
        
        public OuterClass getOuterInstance() {
            return null;
        }
    }
    
    private static class SomeClassWithArgs {
        // Constructor with arguments for error scenario
        public SomeClassWithArgs(int arg) {}
    }
    
    private static class SomeClassWithException {
        // Constructor that throws an exception for error scenario
        public SomeClassWithException() {
            throw new RuntimeException();
        }
    }
    
    private static class OuterClass {}
}