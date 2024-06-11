import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiator() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        assertNotNull(constructorInstantiator);
        assertEquals(outerClassInstance, constructorInstantiator.outerClassInstance);
    }

    @Test
    public void testNewInstanceWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        Class<Object> cls = Object.class;
        Object newInstance = constructorInstantiator.newInstance(cls);

        assertNotNull(newInstance);
        assertTrue(cls.isInstance(newInstance));
    }

    @Test
    public void testNewInstanceNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);

        Class<Object> cls = Object.class;
        Object newInstance = constructorInstantiator.newInstance(cls);

        assertNotNull(newInstance);
        assertTrue(cls.isInstance(newInstance));
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClassException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        Class<String> cls = String.class;
        constructorInstantiator.newInstance(cls);
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructorException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);

        Class<Integer> cls = Integer.class;
        constructorInstantiator.newInstance(cls);
    }

}