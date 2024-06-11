import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_withNullOuterClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        String result = constructorInstantiator.newInstance(String.class);
        assertNull(result);
    }

    @Test
    public void testNewInstance_withNonNullOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        String result = constructorInstantiator.newInstance(String.class);
        assertNotNull(result);
    }

    @Test
    public void testWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Object result = constructorInstantiator.newInstance(Object.class);
        assertNotNull(result);
        assertEquals(outerClassInstance, result);
    }

    @Test
    public void testWithOuterClass_invalidClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            constructorInstantiator.newInstance(Integer.class);
            fail("Exception expected");
        } catch (InstantationException e) {
            // Exception expected
        }
    }

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        String result = constructorInstantiator.newInstance(String.class);
        assertNotNull(result);
    }

    @Test
    public void testNoArgConstructor_invalidClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        try {
            constructorInstantiator.newInstance(ConstructorInstantiator.class);
            fail("Exception expected");
        } catch (InstantationException e) {
            // Exception expected
        }
    }
}