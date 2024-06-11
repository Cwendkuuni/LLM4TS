import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_noOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        String testString = instantiator.newInstance(String.class);
        assertNull(testString);
    }

    @Test
    public void testNewInstance_withOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator("outerInstance");
        String testString = instantiator.newInstance(String.class);
        assertNotNull(testString);
    }

    @Test
    public void testWithOuterClass_success() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator("outerInstance");
        String testString = instantiator.withOuterClass(String.class);
        assertNotNull(testString);
    }

    @Test
    public void testWithOuterClass_exceptionHandling() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator("outerInstance");
        try {
            instantiator.withOuterClass(Integer.class);
            fail("An exception should have been thrown");
        } catch (InstantationException e) {
            // Expected behavior
        }
    }

    @Test
    public void testNoArgConstructor_success() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        String testString = instantiator.noArgConstructor(String.class);
        assertNotNull(testString);
    }

    @Test
    public void testNoArgConstructor_exceptionHandling() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.noArgConstructor(Integer.class);
            fail("An exception should have been thrown");
        } catch (InstantationException e) {
            // Expected behavior
        }
    }
}