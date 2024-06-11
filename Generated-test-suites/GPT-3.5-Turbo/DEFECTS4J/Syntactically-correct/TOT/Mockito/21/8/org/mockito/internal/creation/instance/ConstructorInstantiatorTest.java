import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {
    
    // Test for the no-arg constructor
    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator ci = new ConstructorInstantiator(null);
        Object result = ci.newInstance(Object.class);
        // Assert that the result is not null
        assertNotNull(result);
        // Assert that the result is an instance of the specified class
        assertTrue(result instanceof Object);
    }
    
    // Test for the constructor with outer class instance
    @Test
    public void testWithOuterClassConstructor() {
        // Create an outer class instance
        OuterClass outerClass = new OuterClass();
        ConstructorInstantiator ci = new ConstructorInstantiator(outerClass);
        InnerClass result = ci.newInstance(InnerClass.class);
        // Assert that the result is not null
        assertNotNull(result);
        // Assert that the result is an instance of the specified class
        assertTrue(result instanceof InnerClass);
        // Assert that the result's outer class instance is the same as the one passed in
        assertTrue(result.getOuterClassInstance() == outerClass);
    }
    
    // Test for the case when the outer class instance is null, but the constructor with outer class is called
    @Test(expected = InstantationException.class)
    public void testNullOuterClassInstanceWithOuterClassConstructor() {
        ConstructorInstantiator ci = new ConstructorInstantiator(null);
        ci.newInstance(InnerClass.class);
    }
    
    // Test for the case when the specified class does not have a parameter-less constructor
    @Test(expected = InstantationException.class)
    public void testClassWithNoParameterlessConstructor() {
        ConstructorInstantiator ci = new ConstructorInstantiator(null);
        ci.newInstance(ClassWithConstructor.class);
    }
}

class OuterClass {
    
}

class InnerClass {
    private final OuterClass outerClassInstance;
    
    public InnerClass(OuterClass outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
    }
    
    public OuterClass getOuterClassInstance() {
        return outerClassInstance;
    }
}

class ClassWithConstructor {
    
    public ClassWithConstructor(int param) {
        
    }
}