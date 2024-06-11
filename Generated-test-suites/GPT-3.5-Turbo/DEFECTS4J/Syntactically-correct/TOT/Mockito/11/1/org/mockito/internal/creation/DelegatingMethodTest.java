import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {
    
    @Test
    public void testGetExceptionTypes() {
        // Test case 1: No exceptions are thrown by the method
        Method mockMethod = Mockito.mock(Method.class);
        Mockito.when(mockMethod.getExceptionTypes()).thenReturn(new Class<?>[]{});
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        Assert.assertArrayEquals(new Class<?>[]{}, delegatingMethod.getExceptionTypes());

        // Test case 2: Multiple exceptions are thrown by the method
        Method mockMethod2 = Mockito.mock(Method.class);
        Mockito.when(mockMethod2.getExceptionTypes()).thenReturn(new Class<?>[]{IOException.class, IllegalArgumentException.class});
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(mockMethod2);
        Assert.assertArrayEquals(new Class<?>[]{IOException.class, IllegalArgumentException.class}, delegatingMethod2.getExceptionTypes());
    }
    
    @Test
    public void testGetJavaMethod() {
        // Test case 1: Method is not null
        Method mockMethod = Mockito.mock(Method.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        Assert.assertEquals(mockMethod, delegatingMethod.getJavaMethod());

        // Test case 2: Method is null
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(null);
        Assert.assertNull(delegatingMethod2.getJavaMethod());
    }
    
    @Test
    public void testGetName() {
        // Test case 1: Method has a name
        Method mockMethod = Mockito.mock(Method.class);
        Mockito.when(mockMethod.getName()).thenReturn("testMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        Assert.assertEquals("testMethod", delegatingMethod.getName());

        // Test case 2: Method does not have a name
        Method mockMethod2 = Mockito.mock(Method.class);
        Mockito.when(mockMethod2.getName()).thenReturn("");
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(mockMethod2);
        Assert.assertEquals("", delegatingMethod2.getName());
    }
    
    @Test
    public void testGetParameterTypes() {
        // Test case 1: Method has parameters
        Method mockMethod = Mockito.mock(Method.class);
        Mockito.when(mockMethod.getParameterTypes()).thenReturn(new Class<?>[]{String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        Assert.assertArrayEquals(new Class<?>[]{String.class, int.class}, delegatingMethod.getParameterTypes());

        // Test case 2: Method does not have parameters
        Method mockMethod2 = Mockito.mock(Method.class);
        Mockito.when(mockMethod2.getParameterTypes()).thenReturn(new Class<?>[]{});
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(mockMethod2);
        Assert.assertArrayEquals(new Class<?>[]{}, delegatingMethod2.getParameterTypes());
    }
    
    @Test
    public void testGetReturnType() {
        // Test case 1: Method has a return type
        Method mockMethod = Mockito.mock(Method.class);
        Mockito.when(mockMethod.getReturnType()).thenReturn(String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        Assert.assertEquals(String.class, delegatingMethod.getReturnType());

        // Test case 2: Method does not have a return type
        Method mockMethod2 = Mockito.mock(Method.class);
        Mockito.when(mockMethod2.getReturnType()).thenReturn(void.class);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(mockMethod2);
        Assert.assertEquals(void.class, delegatingMethod2.getReturnType());
    }
    
    @Test
    public void testIsVarArgs() {
        // Test case 1: Method is a varargs method
        Method mockMethod = Mockito.mock(Method.class);
        Mockito.when(mockMethod.isVarArgs()).thenReturn(true);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        Assert.assertTrue(delegatingMethod.isVarArgs());

        // Test case 2: Method is not a varargs method
        Method mockMethod2 = Mockito.mock(Method.class);
        Mockito.when(mockMethod2.isVarArgs()).thenReturn(false);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(mockMethod2);
        Assert.assertFalse(delegatingMethod2.isVarArgs());
    }
    
    @Test
    public void testIsAbstract() {
        // Test case 1: Method is abstract
        Method mockMethod = Mockito.mock(Method.class);
        Mockito.when(mockMethod.getModifiers()).thenReturn(Modifier.ABSTRACT);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        Assert.assertTrue(delegatingMethod.isAbstract());

        // Test case 2: Method is not abstract
        Method mockMethod2 = Mockito.mock(Method.class);
        Mockito.when(mockMethod2.getModifiers()).thenReturn(Modifier.PUBLIC);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(mockMethod2);
        Assert.assertFalse(delegatingMethod2.isAbstract());
    }
    
    @Test
    public void testEquals() {
        // Test case 1: Two DelegatingMethod objects with the same internal Method
        Method mockMethod1 = Mockito.mock(Method.class);
        Mockito.when(mockMethod1.equals(mockMethod1)).thenReturn(true);
        Mockito.when(mockMethod1.equals(Mockito.any())).thenReturn(false);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(mockMethod1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(mockMethod1);
        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod2));

        // Test case 2: DelegatingMethod object and Method object are equal
        Method mockMethod2 = Mockito.mock(Method.class);
        Mockito.when(mockMethod2.equals(mockMethod2)).thenReturn(true);
        Mockito.when(mockMethod2.equals(mockMethod1)).thenReturn(false);
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(mockMethod2);
        Assert.assertTrue(delegatingMethod3.equals(mockMethod2));
    }
    
    @Test
    public void testHashCode() {
        // As the hashCode implementation in DelegatingMethod always returns 1, we can only test it once
        Method mockMethod = Mockito.mock(Method.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        Assert.assertEquals(1, delegatingMethod.hashCode());
    }
}