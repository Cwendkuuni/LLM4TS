import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {
    @Test
    public void testConstructor() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertNull(delegatingMethod.getJavaMethod());
    }
    
    @Test
    public void testGetExceptionTypes() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertNull(delegatingMethod.getExceptionTypes());
    }
    
    @Test
    public void testGetName() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertNull(delegatingMethod.getName());
    }
    
    @Test
    public void testGetParameterTypes() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertNull(delegatingMethod.getParameterTypes());
    }
    
    @Test
    public void testGetReturnType() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertNull(delegatingMethod.getReturnType());
    }
    
    @Test
    public void testIsVarArgs() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertFalse(delegatingMethod.isVarArgs());
    }
    
    @Test
    public void testIsAbstract() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertFalse(delegatingMethod.isAbstract());
    }
    
    @Test
    public void testEquals() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object obj = null;
        
        assertFalse(delegatingMethod.equals(obj));
    }
    
    @Test
    public void testHashCode() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertEquals(1, delegatingMethod.hashCode());
    }
}