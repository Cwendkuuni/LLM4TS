import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor_withValidClass_returnsInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object instance = instantiator.newInstance(Object.class);
        assertNotNull(instance);
    }
    
    @Test
    public void testNoArgConstructor_withInvalidClass_throwsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(ConstructorInstantiator.class);
            fail("Expected InstantationException");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'ConstructorInstantiator'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }
    
    @Test
    public void testWithOuterClass_withValidClass_returnsInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        Object instance = instantiator.newInstance(Object.class);
        assertNotNull(instance);
    }
    
    @Test
    public void testWithOuterClass_withInvalidClass_throwsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        try {
            instantiator.newInstance(ConstructorInstantiator.class);
            fail("Expected InstantationException");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'ConstructorInstantiator'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }
    
    @Test
    public void testWithOuterClass_withInvalidOuterInstanceType_throwsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator("invalid");
        try {
            instantiator.newInstance(ConstructorInstantiator.class);
            fail("Expected InstantationException");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'ConstructorInstantiator'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }
    
    private class OuterClass {}
    
}