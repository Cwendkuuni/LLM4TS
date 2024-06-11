package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerMethod() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        Mockito.verify(returnsSmartNulls, Mockito.atLeastOnce()).answer(invocation);
        Mockito.verifyNoMoreInteractions(returnsSmartNulls);
        Mockito.verifyZeroInteractions(invocation);
        // Verify the result is an instance of ReturnsSmartNulls
        Mockito.assertThat(result).isInstanceOf(ReturnsSmartNulls.class);
    }

    // Test answer(InvocationOnMock invocation) with null InvocationOnMock
    @Test(expected = NullPointerException.class)
    public void testAnswerWithNullInvocationOnMock() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = null;
        returnsSmartNulls.answer(invocation);
    }

    // Test method answer(InvocationOnMock invocation) when the return type is not mockable
    @Test
    public void testAnswerWithUnmockableReturnType() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(new Object() {}.getClass());
        Object result = returnsSmartNulls.answer(invocation);
        Mockito.verify(returnsSmartNulls, Mockito.atLeastOnce()).answer(invocation);
        Mockito.verifyNoMoreInteractions(returnsSmartNulls);
        Mockito.verifyZeroInteractions(invocation);
        // Verify the result is null
        Mockito.assertThat(result).isNull();
    }

    // Test method answer(InvocationOnMock invocation) when the method being invoked returns a string
    @Test
    public void testAnswerWithStringReturnType() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(String.class.getMethod("valueOf", String.class));
        Mockito.when(invocation.getArguments()).thenReturn(new String[]{"test"});
        String result = (String) returnsSmartNulls.answer(invocation);
        Mockito.verify(returnsSmartNulls, Mockito.atLeastOnce()).answer(invocation);
        Mockito.verifyNoMoreInteractions(returnsSmartNulls);
        Mockito.verifyZeroInteractions(invocation);
        // Verify the result is the expected String
        Mockito.assertThat(result).isEqualTo("SmartNull returned by unstubbed method on mock");
    }
}