import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class ReturnsSmartNullsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsMoreEmptyValues delegate = new ReturnsMoreEmptyValues();
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
        
        // Test case when delegate returns non-null value
        InvocationOnMock invocation1 = createInvocation(Object.class, "toString", null);
        Object result1 = returnsSmartNulls.answer(invocation1);
        assertNull(result1);
        
        // Test case when delegate returns null
        InvocationOnMock invocation2 = createInvocation(Object.class, "hashCode", null);
        Object result2 = returnsSmartNulls.answer(invocation2);
        assertNull(result2);
        
        // Test case when return type is mockable
        InvocationOnMock invocation3 = createInvocation(String.class, "charAt", new Object[]{0});
        Object result3 = returnsSmartNulls.answer(invocation3);
        assertNull(result3);
        
        // Test case when return type is not mockable
        InvocationOnMock invocation4 = createInvocation(Integer.class, "intValue", null);
        Object result4 = returnsSmartNulls.answer(invocation4);
        assertNull(result4);
    }
    
    private InvocationOnMock createInvocation(Class<?> returnType, String methodName, Object[] arguments) throws Exception {
        Method method = returnType.getDeclaredMethod(methodName);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        return invocation;
    }
}