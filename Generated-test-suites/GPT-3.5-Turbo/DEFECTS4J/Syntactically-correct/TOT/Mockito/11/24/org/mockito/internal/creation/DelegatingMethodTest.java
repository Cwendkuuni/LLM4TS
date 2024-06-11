import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = null;
        try {
            method = getClass().getDeclaredMethod("getExceptionTypes");
        } catch (NoSuchMethodException e) {
            Assert.fail("No such method found");
        }
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        // Typical use case - No exception types
        Class<?>[] expectedExceptionTypes = new Class[0];
        Assert.assertArrayEquals(expectedExceptionTypes, delegatingMethod.getExceptionTypes());
        
        // Edge case - Single exception type
        Class<?>[] expectedExceptionTypes = new Class[]{NullPointerException.class};
        Assert.assertArrayEquals(expectedExceptionTypes, delegatingMethod.getExceptionTypes());
        
        // Error scenario - Multiple exception types
        Class<?>[] expectedExceptionTypes = new Class[]{NullPointerException.class, IllegalArgumentException.class};
        Assert.assertArrayEquals(expectedExceptionTypes, delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = null;
        try {
            method = getClass().getDeclaredMethod("getJavaMethod");
        } catch (NoSuchMethodException e) {
            Assert.fail("No such method found");
        }
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        // Typical use case - method instance returned correctly
        Assert.assertEquals(method, delegatingMethod.getJavaMethod());
        
        // Edge case - method instance is not null
        Assert.assertNotNull(delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = null;
        try {
            method = getClass().getDeclaredMethod("getName");
        } catch (NoSuchMethodException e) {
            Assert.fail("No such method found");
        }
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        // Typical use case - method name returned correctly
        Assert.assertEquals("getName", delegatingMethod.getName());
        
        // Edge case - method name is not empty
        Assert.assertFalse(delegatingMethod.getName().isEmpty());
    }

    // Other test methods for remaining methods of DelegatingMethod class

}