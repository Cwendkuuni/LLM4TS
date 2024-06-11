import static org.junit.Assert.*;
import org.junit.Test;

public class ConstructorInstantiatorTest {

    private ConstructorInstantiator instantiator;

    @Test
    public void testNoArgConstructor() {
        instantiator = new ConstructorInstantiator(null);
        MyClass obj = instantiator.newInstance(MyClass.class);
        assertNotNull(obj);
    }

    @Test
    public void testWithOuterClass() {
        instantiator = new ConstructorInstantiator(new OuterClass());
        MyClass obj = instantiator.newInstance(MyClass.class);
        assertNotNull(obj);
    }
    
    @Test
    public void testParamsException() {
        instantiator = new ConstructorInstantiator(new OuterClass());
        try {
            instantiator.newInstance(InvalidClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'InvalidClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }
    
    @Test
    public void testNoArgConstructorException() {
        instantiator = new ConstructorInstantiator(new OuterClass());
        try {
            instantiator.newInstance(NoConstructorClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'NoConstructorClass'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    private static class MyClass {
        // Test class
    }

    private static class OuterClass {
        // Sample outer class
    }

    private static class InvalidClass {
        // Class with invalid constructor
    }

    private static class NoConstructorClass {
        // Class with no constructor
    }

}