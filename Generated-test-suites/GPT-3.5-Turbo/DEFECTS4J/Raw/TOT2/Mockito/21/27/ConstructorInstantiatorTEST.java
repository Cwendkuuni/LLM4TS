import static org.junit.Assert.*;
import org.junit.Test;

public class ConstructorInstantiatorTest {

    // Extracted public methods:
    // 1. public <T> T newInstance(Class<T> cls)
    // 2. none because the other methods are private

    // Test cases for public method newInstance
    @Test
    public void testNewInstanceNoOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object obj = instantiator.newInstance(Object.class);
        assertNotNull(obj);
    }

    @Test
    public void testNewInstanceWithOuterClass() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        Object obj = instantiator.newInstance(Object.class);
        assertNotNull(obj);
    }

    // Test cases for edge cases and exception scenarios
    @Test(expected = InstantationException.class)
    public void testWithOuterClassException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        instantiator.newInstance(String.class);
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructorException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(Interface.class); // Interface has no parameter-less constructor
    }

}