import static org.junit.Assert.*;
import org.junit.Test;

public class ConstructorInstantiatorTest {
    // 
    
    // Test case for noArgConstructor when cls has a parameter-less constructor
    @Test
    public void testNoArgConstructor_ValidClass_ReturnsInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        ClassWithNoArgConstructor instance = instantiator.newInstance(ClassWithNoArgConstructor.class);
        assertNotNull(instance);
    }
    
    // Test case for noArgConstructor when cls does not have a parameter-less constructor
    @Test(expected = InstantationException.class)
    public void testNoArgConstructor_InvalidClass_ThrowsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(ClassWithoutNoArgConstructor.class);
    }
    
    // Test case for withOuterClass when cls has a constructor with outerClassInstance as parameter
    @Test
    public void testWithOuterClass_ValidClass_ReturnsInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        ClassWithOuterClassConstructor instance = instantiator.newInstance(ClassWithOuterClassConstructor.class);
        assertNotNull(instance);
    }
    
    // Test case for withOuterClass when cls does not have a constructor with outerClassInstance as parameter
    @Test(expected = InstantationException.class)
    public void testWithOuterClass_InvalidClass_ThrowsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        instantiator.newInstance(ClassWithoutOuterClassConstructor.class);
    }
    
    // 
    
    // Helper classes for testing
    
    public static class ClassWithNoArgConstructor {
        public ClassWithNoArgConstructor() {
            // empty constructor
        }
    }
    
    public static class ClassWithoutNoArgConstructor {
        public ClassWithoutNoArgConstructor(String parameter) {
            // constructor with parameter
        }
    }
    
    public static class ClassWithOuterClassConstructor {
        public ClassWithOuterClassConstructor(OuterClass outer) {
            // constructor with OuterClass as parameter
        }
    }
    
    public static class ClassWithoutOuterClassConstructor {
        public ClassWithoutOuterClassConstructor() {
            // empty constructor
        }
    }
    
    public static class OuterClass {
        // empty class
    }
}