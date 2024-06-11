import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantationException;

public class ConstructorInstantiatorTest {

    private Object outerInstance;

    @Test
    public void testNewInstance_withOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        String result = instantiator.newInstance(String.class);
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testNewInstance_noArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        String result = instantiator.newInstance(String.class);
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testWithOuterClass_exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        try {
            instantiator.withOuterClass(String.class);
            fail("Expected an InstantationException to be thrown");
        } catch (InstantationException e) {
            // Success
        }
    }

    @Test
    public void testNoArgConstructor_exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.noArgConstructor(String.class);
            fail("Expected an InstantationException to be thrown");
        } catch (InstantationException e) {
            // Success
        }
    }

    @Test
    public void testParamsException() {
        try {
            ConstructorInstantiator.paramsException(String.class, new Exception("Test Exception"));
            fail("Expected an InstantationException to be thrown");
        } catch (InstantationException e) {
            // Success
        }
    }
}