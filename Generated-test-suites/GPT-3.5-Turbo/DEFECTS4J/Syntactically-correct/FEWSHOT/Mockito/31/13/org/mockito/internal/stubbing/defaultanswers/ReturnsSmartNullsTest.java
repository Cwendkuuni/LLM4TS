
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        // Test ordinary return values
        assertEquals("foo", returnsSmartNulls.answer(createInvocationMock("getString", String.class)));
        assertEquals(Integer.valueOf(100), returnsSmartNulls.answer(createInvocationMock("getInteger", Integer.class)));
        assertEquals(Double.valueOf(3.14), returnsSmartNulls.answer(createInvocationMock("getDouble", Double.class)));

        // Test SmartNulls
        assertNotNull(returnsSmartNulls.answer(createInvocationMock("getObject", Object.class)));
        assertNotNull(returnsSmartNulls.answer(createInvocationMock("getList", List.class)));
        assertNotNull(returnsSmartNulls.answer(createInvocationMock("getMap", Map.class)));

        // Test unmockable return types
        assertNull(returnsSmartNulls.answer(createInvocationMock("getFinalString", String.class)));
        assertNull(returnsSmartNulls.answer(createInvocationMock("getFinalInteger", Integer.class)));
        assertNull(returnsSmartNulls.answer(createInvocationMock("getFinalDouble", Double.class)));

        // Verify mockito interactions
        Mockito.verifyZeroInteractions(invocationMock);
    }

    private InvocationOnMock createInvocationMock(String methodName, Class<?> returnType) throws NoSuchMethodException {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        Method method = ReturnsSmartNulls.class.getDeclaredMethod(methodName);
        when(invocationMock.getMethod()).thenReturn(method);
        when(invocationMock.getReturnType()).thenReturn(returnType);
        return invocationMock;
    }
}
