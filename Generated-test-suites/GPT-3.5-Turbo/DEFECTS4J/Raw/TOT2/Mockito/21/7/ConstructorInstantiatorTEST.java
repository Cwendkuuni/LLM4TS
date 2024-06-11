import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NoOuterClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        String result = constructorInstantiator.newInstance(String.class);
        assertNull(result);
    }

    @Test
    public void testNewInstance_WithOuterClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator("outerInstance");
        String result = constructorInstantiator.newInstance(String.class);
        assertNotNull(result);
    }

    @Test
    public void testWithOuterClass_Exception() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator("outerInstance");
        try {
            constructorInstantiator.withOuterClass(Integer.class);
            fail("Exception not thrown");
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'Integer'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNoArgConstructor_Exception() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        try {
            constructorInstantiator.noArgConstructor(String.class);
            fail("Exception not thrown");
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'String'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

}