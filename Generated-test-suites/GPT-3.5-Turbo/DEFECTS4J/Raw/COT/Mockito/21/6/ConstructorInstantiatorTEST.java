
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object instance = instantiator.newInstance(Object.class);
        assertNotNull(instance);
        assertEquals(Object.class, instance.getClass());
    }

    @Test
    public void testNewInstanceWithOuterClass() {
        OuterClass outerClass = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClass);
        InnerClass instance = instantiator.newInstance(InnerClass.class);
        assertNotNull(instance);
        assertEquals(InnerClass.class, instance.getClass());
        assertSame(outerClass, instance.getOuterClass());
    }

    @Test
    public void testNewInstanceWithInvalidOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new InvalidOuterClass());
        try {
            instantiator.newInstance(InnerClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'InnerClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNewInstanceWithNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        NoArgConstructorClass instance = instantiator.newInstance(NoArgConstructorClass.class);
        assertNotNull(instance);
        assertEquals(NoArgConstructorClass.class, instance.getClass());
    }

    @Test
    public void testNewInstanceWithNoArgConstructorFails() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(NoParameterConstructorClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'NoParameterConstructorClass'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here
    
}
