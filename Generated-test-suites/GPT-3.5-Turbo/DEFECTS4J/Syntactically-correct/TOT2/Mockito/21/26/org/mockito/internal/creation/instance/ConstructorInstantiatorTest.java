import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_noOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        Object result = instantiator.newInstance(String.class);
        
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testNewInstance_withOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator("example");
        
        Object result = instantiator.newInstance(Integer.class);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testWithOuterClass_validConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator("example");
        
        Object result = instantiator.withOuterClass(Integer.class);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testWithOuterClass_invalidConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator("example");
        
        try {
            instantiator.withOuterClass(String.class);
            fail("Exception not thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'String'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNoArgConstructor_validClass() {
        Object result = ConstructorInstantiator.noArgConstructor(Integer.class);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testNoArgConstructor_invalidClass() {
        try {
            ConstructorInstantiator.noArgConstructor(ConstructorInstantiator.class);
            fail("Exception not thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'ConstructorInstantiator'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

}