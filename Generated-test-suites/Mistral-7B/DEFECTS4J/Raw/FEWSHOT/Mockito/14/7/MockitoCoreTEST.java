package org.mockito.internal;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockSettings mockSettings = new MockSettingsImpl();
        Object mock = new MockitoCore().mock(String.class, mockSettings);
        verify(mock, times(1)).toString();
    }

    @Test
    public void testStub() {
        Object mock = mock(String.class);
        IOngoingStubbing stubbing = MockitoCore.stub(mock);
        stubbing.thenReturn("foo");
        assertEquals("foo", Mockito.invoke(mock, "toString"));
    }

    @Test
    public void testStubWithArgument() {
        Object mock = mock(String.class);
        IOngoingStubbing stubbing = MockitoCore.stub(mock);
        stubbing.thenReturn("foo").withArguments(eq("bar"));
        assertEquals("foo", Mockito.invoke(mock, "toString", "bar"));
    }

    @Test
    public void testWhen() {
        Object mock = mock(String.class);
        when(mock).thenReturn("foo");
        assertEquals("foo", Mockito.invoke(mock, "toString"));
    }

    @Test
    public void testWhenWithArgument() {
        Object mock = mock(String.class);
        when(mock).thenReturn("foo").given(any(Integer.class));
        assertEquals("foo", Mockito.invoke(mock, "toString", 1));
    }

    @Test
    public void testVerify() {
        Object mock = mock(String.class);
        verify(mock).toString();
    }

    @Test(expected = AssertionError.class)
    public void testVerifyThrowsError() {
        Object mock = mock(String.class);
        verify(mock).toString();
        verify(mock).toString();
    }

    @Test
    public void testReset() {
        Object mock = mock(String.class);
        verify(mock).toString();
        MockitoCore.reset(mock);
        verify(mock, never()).toString();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        MockitoCore.verifyNoMoreInteractions(mock1, mock2);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsThrowsError() {
        Object mock = mock(String.class);
        verify(mock).toString();
        MockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        InOrder inOrder = inOrder(mock1, mock2);
        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();
        MockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
    }

    @Test
    public void testAnswer() {
        Object mock = mock(String.class);
        Answer<String> answer = argument -> "foo";
        Stubber stubber = MockitoCore.doAnswer(answer);
        stubber.thenAnswer(answer);
        assertEquals("foo", Mockito.invoke(mock, "toString"));
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(String.class);
        MockitoCore.stubVoid(mock).thenCallRealMethod();
        Mockito.verify(mock).toString();
    }

    @Test
    public void testValidateMockitoUsage() {
        try {
            new MockitoCore().validateMockitoUsage();
        } catch (AssertionError e) {
            // pass
        }
    }
}