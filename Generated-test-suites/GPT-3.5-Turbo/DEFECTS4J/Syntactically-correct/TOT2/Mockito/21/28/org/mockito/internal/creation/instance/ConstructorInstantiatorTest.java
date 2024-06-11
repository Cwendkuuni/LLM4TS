import static org.junit.Assert.*;
import org.junit.Test;

public class ConstructorInstantiatorTest {

    ConstructorInstantiator instantiator;

    @Test
    public void testNewInstanceWithOuterClass() {
        instantiator = new ConstructorInstantiator(new Object());
        assertNotNull(instantiator.newInstance(Object.class));
    }

    @Test
    public void testNewInstanceWithoutOuterClass() {
        instantiator = new ConstructorInstantiator(null);
        assertNotNull(instantiator.newInstance(String.class));
    }

    @Test
    public void testWithOuterClassEdgeCase() {
        instantiator = new ConstructorInstantiator(new Object());
        assertNotNull(instantiator.newInstance(Object.class));
    }

    @Test
    public void testNoArgConstructorEdgeCase() {
        instantiator = new ConstructorInstantiator(null);
        assertNotNull(instantiator.newInstance(String.class));
    }

    @Test
    public void testParamsException() {
        instantiator = new ConstructorInstantiator(new Object());
        try {
            instantiator.newInstance(Integer.class);
            fail("Expected InstantationException");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'Integer'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNoArgConstructorException() {
        instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(ConstructorInstantiator.class);
            fail("Expected InstantationException");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'ConstructorInstantiator'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }
}