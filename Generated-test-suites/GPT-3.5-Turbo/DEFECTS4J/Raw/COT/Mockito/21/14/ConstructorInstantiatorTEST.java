import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiator() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        assertNotNull(constructorInstantiator);
        assertEquals(outerClassInstance, constructorInstantiator.outerClassInstance);
    }

    @Test
    public void testNewInstanceWithNullOuterClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Object instance = constructorInstantiator.newInstance(Object.class);
        assertNotNull(instance);
        assertEquals(Object.class, instance.getClass());
    }

    @Test
    public void testNewInstanceWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Object instance = constructorInstantiator.newInstance(Object.class);
        assertNotNull(instance);
        assertEquals(Object.class, instance.getClass());
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClassThrowsException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(Constructor.class); // Constructor class does not have a parameter-less constructor
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructorThrowsException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(Constructor.class); // Constructor class does not have a parameter-less constructor
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructorWithInvalidClassThrowsException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(InvalidClass.class); // InvalidClass does not exist
    }

    public static class InvalidClass {
        // Empty class
    }

}