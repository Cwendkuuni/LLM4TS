package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;

public class ReturnsSmartNullsTest {
    @Test
    public void testAnswerReturnsSmartNull() throws Throwable {
        ReturnsMoreEmptyValues emptyValues = new ReturnsMoreEmptyValues();
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocationWithReturnValue = createInvocationWithReturnValue("testMethod", "foo");
        Object resultWithReturnValue = smartNulls.answer(invocationWithReturnValue);
        assertNotNull(resultWithReturnValue);

        InvocationOnMock invocationWithEmptyReturnValue = createInvocationWithReturnValue("emptyTestMethod", "");
        Object resultWithEmptyReturnValue = smartNulls.answer(invocationWithEmptyReturnValue);
        assertNotNull(resultWithEmptyReturnValue);

        InvocationOnMock invocationWithNullReturnValue = createInvocationWithReturnValue("nullTestMethod", null);
        Object resultWithNullReturnValue = smartNulls.answer(invocationWithNullReturnValue);
        assertNotNull(resultWithNullReturnValue);
    }

    @Test
    public void testAnswerReturnsMockedObject() throws Throwable {
        ReturnsMoreEmptyValues emptyValues = new ReturnsMoreEmptyValues();
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocationWithMockableReturnType = createInvocationWithReturnType("mockableTestMethod", Object.class);
        Object resultWithMockableReturnType = smartNulls.answer(invocationWithMockableReturnType);
        assertNotNull(resultWithMockableReturnType);

        InvocationOnMock invocationWithNonMockableReturnType = createInvocationWithReturnType("nonMockableTestMethod", String.class);
        Object resultWithNonMockableReturnType = smartNulls.answer(invocationWithNonMockableReturnType);
        assertNull(resultWithNonMockableReturnType);
    }

    private InvocationOnMock createInvocationWithReturnValue(String methodName, Object returnValue) {
        MockCreationSettings mockSettings = Mockito.mock(MockCreationSettings.class);
        MockName mockName = Mockito.mock(MockName.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod(methodName));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[] {});
        Mockito.when(invocation.getMock()).thenReturn(mockSettings);
        Mockito.when(mockSettings.getMockName()).thenReturn(mockName);
        Mockito.when(mockName.toString()).thenReturn("TestMock");

        ReturnsMoreEmptyValues emptyValues = new ReturnsMoreEmptyValues();
        Mockito.when(invocation.callRealMethod()).thenReturn(returnValue);
        return invocation;
    }

    private InvocationOnMock createInvocationWithReturnType(String methodName, Class<?> returnType) {
        MockCreationSettings mockSettings = Mockito.mock(MockCreationSettings.class);
        MockName mockName = Mockito.mock(MockName.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(getMethod(methodName));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[] {});
        Mockito.when(invocation.getMock()).thenReturn(mockSettings);
        Mockito.when(mockSettings.getMockName()).thenReturn(mockName);
        Mockito.when(mockName.toString()).thenReturn("TestMock");

        ReturnsMoreEmptyValues emptyValues = new ReturnsMoreEmptyValues();
        Mockito.when(invocation.callRealMethod()).thenReturn(null);
        return invocation;
    }

    private Method getMethod(String methodName) {
        try {
            return ReturnsSmartNulls.class.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
}