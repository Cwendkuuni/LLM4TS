
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Stubbed method returns non-null value
        InvocationOnMock invocation1 = createInvocation("getFoo", String.class);
        assertNull(returnsSmartNulls.answer(invocation1));
        
        // Stubbed method returns null
        InvocationOnMock invocation2 = createInvocation("getBar", String.class);
        assertNull(returnsSmartNulls.answer(invocation2));
    }
    
    private InvocationOnMock createInvocation(String methodName, Class<?> returnType) throws NoSuchMethodException {
        Class<ReturnsSmartNulls> clazz = ReturnsSmartNulls.class;
        Method method = clazz.getMethod(methodName);
        return invocation(clazz, method, returnType);
    }
    
    private InvocationOnMock invocation(Class<?> clazz, Method method, Class<?> returnType) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(returnType);
        return invocation;
    }
}
