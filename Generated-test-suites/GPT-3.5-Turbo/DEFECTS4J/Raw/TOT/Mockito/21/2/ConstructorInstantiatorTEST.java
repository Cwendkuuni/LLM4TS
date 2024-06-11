import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_withNullOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        // Test the case where the outer class instance is null
        // We expect the noArgConstructor to be used to create a new instance
        // We can use any class that has a parameter-less constructor for this test
        ExampleClass instance = instantiator.newInstance(ExampleClass.class);

        assertNotNull(instance);
        assertTrue(instance instanceof ExampleClass);
    }

    @Test
    public void testNewInstance_withValidOuterClass() {
        OuterClass outerInstance = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        // Test the case where the outer class instance is not null
        // We expect the withOuterClass method to be used to create a new instance
        // We can use the ExampleClass as the target class and pass the outerInstance as the parameter
        ExampleClass instance = instantiator.newInstance(ExampleClass.class);

        assertNotNull(instance);
        assertTrue(instance instanceof ExampleClass);
        assertEquals(outerInstance, instance.getOuterClassInstance());
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_withInvalidOuterClass() {
        InvalidOuterClass outerInstance = new InvalidOuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        // Test the case where the outer class instance is not of the correct type
        // We expect an InstantationException to be thrown
        // We can use the ExampleClass as the target class
        instantiator.newInstance(ExampleClass.class);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstance_withInvalidTargetClass() {
        OuterClass outerInstance = new OuterClass();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        // Test the case where the target class does not have a parameter-less constructor
        // We expect an InstantationException to be thrown
        // We can use the InvalidClass as the target class
        instantiator.newInstance(InvalidClass.class);
    }
}

// Helper classes for testing

class ExampleClass {
    private Object outerClassInstance;

    public ExampleClass() {
        this.outerClassInstance = null;
    }

    public ExampleClass(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
    }

    public Object getOuterClassInstance() {
        return outerClassInstance;
    }
}

class OuterClass {
    // Empty class for testing purposes
}

class InvalidOuterClass {
    // Empty class for testing purposes
}

class InvalidClass {
    public InvalidClass(int arg) {
        // Parameterized constructor for testing purposes
    }
}