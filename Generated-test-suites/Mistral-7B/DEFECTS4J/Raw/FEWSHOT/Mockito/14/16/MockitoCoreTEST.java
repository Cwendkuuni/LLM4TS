package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.mocks.MockHandlerInterface;

public class MockitoCoreTest {

    private MockitoCore core;

    @Before
    public void setUp() {
        core = new MockitoCore();
    }

    @Test
    public void testMockCreation() {
        String mockClass = String.class.getName();
        MockSettings mockSettings = new MockSettingsImpl();
        String mock = core.mock(String.class, mockSettings);

        assertNotNull(mock);
        assertEquals(mockClass, mock.getClass().getName());
        MockHandlerInterface handler = core.getMockHandler(mock);
        assertNotNull(handler);
        assertEquals(mock, handler.getMock());
    }

    @Test
    public void testStubbing() {
        String mockClass = String.class.getName();
        MockSettings mockSettings = new MockSettingsImpl();
        String mock = core.mock(String.class, mockSettings);
        Stubber stubber = core.stub();

        String returnValue = "test";
        when(mock).thenReturn(returnValue);
        assertEquals(returnValue, core.verify(mock).toString());
        assertEquals(returnValue, stubber.thenReturn(mock).toString());

        String argument = "arg";
        when(mock).thenReturn(argument).given(ArgumentMatchers.anyString());
        assertEquals(argument, core.verify(mock).toString(ArgumentMatchers.anyString()));
        assertEquals(argument, stubber.thenReturn(mock).given(ArgumentMatchers.anyString()));

        String expected = "expected";
        String argument2 = "arg2";

        Invocation invocation = new Invocation(new Object[] { mock, "test" }, 1, null);
        when(mock).thenAnswer(i -> expected);
        assertEquals(expected, core.verify(mock).toString(ArgumentMatchers.eq(argument2)));
        assertEquals(expected, stubber.thenAnswer(i -> expected).given(ArgumentMatchers.eq(argument2)));

        stubber.thenThrow(new RuntimeException());
        try {
            core.verify(mock).toString(ArgumentMatchers.anyString());
            fail();
        } catch (RuntimeException e) {
            //expected
        }

        stubber.thenCallRealMethod();
        assertEquals(mock.toString(), core.verify(mock).toString());
    }

    @Test
    public void testVerification() {
        String mockClass = String.class.getName();
        MockSettings mockSettings = new MockSettingsImpl();
        String mock = core.mock(String.class, mockSettings);

        String returnValue = "test";
        when(mock).thenReturn(returnValue);
        assertEquals(returnValue, core.verify(mock).toString());
        verify(mock).toString();

        String argument = "arg";

        verify(mock).toString(ArgumentMatchers.eq(argument));
        verify(mock, never()).toString(ArgumentMatchers.anyString());

        String expected = "expected";
        when(mock).thenAnswer(i -> expected);
        verify(mock).toString(ArgumentMatchers.eq(argument));
        verify(mock, never()).toString(ArgumentMatchers.eq(ArgumentMatchers.anyString()));

        String expectedErrorMessage = "Error message";
        try {
            core.verify(mock).toString(ArgumentMatchers.eq("not a valid argument"));
            fail();
        } catch (AssertionError e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }

        verifyNoMoreInteractions(mock);

        verifyNoMoreInteractions(mock, inOrder(inOrder(mock, mock)));

        verifyNoMoreInteractions(mock, inOrder(mock, mock, mock));

        verifyNoMoreInteractions(mock, inOrder(mock, mock, mock, mock));

        verifyNoMoreInteractions(mock, inOrder(mock, mock, mock, mock, mock));
    }

    @Test
    public void testReset() {
        String mockClass = String.class.getName();
        MockSettings mockSettings = new MockSettingsImpl();
        String mock = core.mock(String.class, mockSettings);

        String returnValue = "test";
        when(mock).thenReturn(returnValue);

        core.reset(mock);
        assertNull(mockingProgress.pullOngoingStubbing());
        assertFalse(mockUtil.isMock(mock));

        verify(mock).toString();
        assertEquals(returnValue, core.verify(mock).toString());

        core.reset(mock);

        verify(mock, never()).toString();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        String mockClass = String.class.getName();
        MockSettings mockSettings = new MockSettingsImpl();
        String mock = core.mock(String.class, mockSettings);

        String returnValue = "test";
        when(mock).thenReturn(returnValue);

        core.verifyNoMoreInteractions(mock);
        verify(mock, never()).toString();

        verifyNoMoreInteractions(mock);

        verify(mock, never()).toString(ArgumentMatchers.anyString());

        String argument = "arg";
        when(mock).thenReturn(argument);

        core.verifyNoMoreInteractions(mock, argument);
        verify(mock, never()).toString(ArgumentMatchers.anyString());
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        String mockClass = String.class.getName();
        MockSettings mockSettings = new MockSettingsImpl();
        String mock1 = core.mock(String.class, mockSettings);
        String mock2 = core.mock(String.class, mockSettings);

        String returnValue = "test";
        when(mock1).thenReturn(returnValue);
        when(mock2).thenReturn(returnValue);

        InOrder inOrder = inOrder(mock1, mock2);

        core.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
        verifyNoInteractions(mock1, mock2);
        verify(mock1).toString();
        verify(mock2).toString();

        core.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
        verify(mock1, never()).toString(ArgumentMatchers.anyString());
        verify(mock2, never()).toString(ArgumentMatchers.anyString());

        core.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
        verifyNoInteractions(mock1, mock2);
        verify(mock1).toString(ArgumentMatchers.anyString());
        verify(mock2).toString(ArgumentMatchers.anyString());

        core.verifyNoMoreInteractionsInOrder(Collections.singletonList(mock1), inOrder);
        verifyNoInteractions(mock1);
        verify(mock1).toString();

        core.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
        verifyNoInteractions(mock1, mock2);
        verifyNoInteractions(mock1);
        verify(mock2).toString();

        core.verifyNoMoreInteractionsInOrder(Collections.singletonList(mock2), inOrder);
        verifyNoInteractions(mock1);
        verifyNoInteractions(mock2);
        verify(mock1).toString();
    }

    @Test
    public void testInOrder() {
        String mockClass = String.class.getName();
        MockSettings mockSettings = new MockSettingsImpl();
        String mock1 = core.mock(String.class, mockSettings);
        String mock2 = core.mock(String.class, mockSettings);

        InOrder inOrder = core.inOrder(mock1, mock2);

        verify(mock1).toString();
        verify(mock2).toString();

        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();

        inOrder.verify(mock1, never()).toString(ArgumentMatchers.anyString());
        inOrder.verify(mock2).toString("arg");

        inOrder.verify(mock1, never()).toString(ArgumentMatchers.eq("arg"));
        inOrder.verify(mock2).toString(ArgumentMatchers.anyString());

        inOrder.verify(mock1).toString("expected");
        inOrder.verify(mock2).toString("expected");
    }

    @Test
    public void testDoAnswer() {
        String mockClass = String.class.getName();
        MockSettings mockSettings = new MockSettingsImpl();
        String mock = core.mock(String.class, mockSettings);

        String expected = "expected";
        Answer<String> answer = invocation -> expected;
        String result = core.doAnswer(answer).when(mock).toString();

        assertEquals(expected, result);
        verify(mock).toString();

        assertSame(expected, core.verify(mock).toString());
    }

    @Test
    public void testStubVoid() {
        String mockClass = String.class.getName();
        MockSettings mockSettings = new MockSettingsImpl();
        String mock = core.mock(String.class, mockSettings);

        StubberImpl stubber = (StubberImpl) core.stub();
        VoidMethodStubbable<String> stubbable = stubber.stubVoid(mock);

        stubbable.doNothing();
        verify(mock, never()).toString();

        stubbable.thenThrow(new RuntimeException());
        try {
            core.verify(mock).toString();
            fail();
        } catch (RuntimeException e) {
            //expected
        }

        stubbable.thenCallRealMethod();
        assertEquals(mock.toString(), core.verify(mock).toString());
    }

    @Test
    public void testValidateMockitoUsage() {
        String mockClass = String.class.getName();
        MockSettings mockSettings = new MockSettingsImpl();
        String mock = core.mock(String.class, mockSettings);

        core.validateMockitoUsage();
        assertEquals(mockingProgress.getState(), MockingProgress.State.MOCKED);

        String returnValue = "test";
        when(mock).thenReturn(returnValue);

        core.validateMockitoUsage();
        assertEquals(mockingProgress.getState(), MockingProgress.State.MOCKED);

        verify(mock).toString();

        core.validateMockitoUsage();
        assertEquals(mockingProgress.getState(), MockingProgress.State.MOCKED);

        core.reset(mock);

        core.validateMockitoUsage();
        assertEquals(mockingProgress.getState(), MockingProgress.State.MOCKED_AND_RESET);

        core.verifyNoMoreInteractions(mock);

        core.validateMockitoUsage();
        assertEquals(mockingProgress.getState(), MockingProgress.State.MOCKED_AND_RESET);

        core.verifyNoMoreInteractions(mock, inOrder(mock, mock));

        core.validateMockitoUsage();
        assertEquals(mockingProgress.getState(), MockingProgress.State.MOCKED_AND_RESET);

        InOrder inOrder = inOrder(mock, mock);
        core.verifyNoMoreInteractionsInOrder(Arrays.asList(mock, mock), inOrder);

        core.validateMockitoUsage();
        assertEquals(mockingProgress.getState(), MockingProgress.State.MOCKED_AND_RESET);

        core.verifyNoMoreInteractionsInOrder(Arrays.asList(mock, mock), inOrder);

        core.verifyNoMoreInteractionsInOrder(Arrays.asList(mock, mock), inOrder);

        core.verifyNoMoreInteractionsInOrder(Arrays.asList(mock, mock), inOrder);

        core.verifyNoMoreInteractionsInOrder(Arrays.asList(mock, mock), inOrder);

        core.validateMockitoUsage();
        assertEquals(mockingProgress.getState(), MockingProgress.State.MOCKED_AND_RESET);
    }
}