import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertNotNull(instantiator);
        
        Object instance = instantiator.newInstance(Object.class);
        assertNotNull(instance);
        assertEquals(Object.class, instance.getClass());
    }
    
    @Test
    public void testWithOuterClass() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        assertNotNull(instantiator);
        
        Object instance = instantiator.newInstance(Object.class);
        assertNotNull(instance);
        assertEquals(Object.class, instance.getClass());
    }
    
    @Test
    public void testInvalidOuterClassType() {
        Object outerClassInstance = "invalid";
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        assertNotNull(instantiator);
        
        try {
            instantiator.newInstance(Object.class);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'Object'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
            assertNotNull(e.getCause());
        }
    }
    
    @Test
    public void testInvalidNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertNotNull(instantiator);
        
        try {
            instantiator.newInstance(TestClassWithoutConstructor.class);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'TestClassWithoutConstructor'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
            assertNotNull(e.getCause());
        }
    }
    
    private static class TestClassWithoutConstructor {
        
    }

}