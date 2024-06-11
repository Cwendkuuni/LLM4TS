package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        String mockClass = String.class.getName();
        MockSettings mockSettings = new MockSettingsImpl();
        String mock = mockitoCore.mock(String.class, mockSettings);
        verify(mockitoCore).mockingStarted(eq(mock), eq(mockClass), eq(mockSettings));
        assertSame(mockitoCore, mockUtil.getMockHandler(mock).getMockBase());
    }

    @Test
    public void testStub() {
        Stubber stubber = mockitoCore.stub();
        IOngoingStubbing ongoingStubbing = (IOngoingStubbing) stubber;
        assertSame(mockitoCore, ongoingStubbing.getMockBase());
        assertNull(ongoingStubbing.getAnswer());

        Object mock = mock(String.class);
        when(mock).thenReturn("foo");
        String result = (String) mockitoCore.stub(mock).thenReturn("bar").getValue();
        verify(mockitoCore).mockingProgressReset();
        assertEquals("bar", result);
        verify(mock).thenReturn(eq("bar"));
        assertEquals("bar", mockitoCore.stub(mock).thenReturn("baz").getReturnValue());
    }

    @Test
    public void testWhen() {
        String mockClass = String.class.getName();
        String mock = mock(String.class);
        Object result = "foo";

        OngoingStubbing ongoingStubbing = mockitoCore.when(mock).thenReturn(result);
        assertEquals(result, ongoingStubbing.getStubbingReturnValue());
        verify(mockitoCore).mockingProgressReset();
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.returned(eq(result))));

        ongoingStubbing.thenThrow(new RuntimeException());
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.thrown(RuntimeException.class)));

        ongoingStubbing.thenAnswer((Answer) -> "bar");
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.answer(eq("bar"))));

        ongoingStubbing.thenCallRealMethod();
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.doCallRealMethod()));

        ongoingStubbing.thenCallRealMethod();
        ongoingStubbing.thenThrow(new RuntimeException());
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.doCallRealMethod()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.thrown(RuntimeException.class)));
    }

    @Test
    public void testVerify() {
        String mockClass = String.class.getName();
        String mock = mock(String.class);

        mockitoCore.verify(mock, eq(VerificationModeFactory.isNull()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.nullVerified()));

        mockitoCore.verify(mock, eq(VerificationModeFactory.same(mockitoCore.mock(String.class))));
        mockitoCore.verify(mock, eq(VerificationModeFactory.same(mockitoCore.mock(String.class))));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.same(mock)));

        mockitoCore.verify(mock, eq(VerificationModeFactory.atLeastOnce()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.atLeastOnce()));
        int callCount = 0;
        when(mock).thenAnswer((Answer) -> ++callCount);
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.atLeast(1)));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.times(callCount)));

        mockitoCore.verify(mock, eq(VerificationModeFactory.never()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.never()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noMoreInteractions()));

        String mock2 = mock(String.class);
        String[] mocks = {mock, mock2};
        mockitoCore.verify(mocks);
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.inOrder()));
        InOrder inOrder = mockitoCore.inOrder(mocks);
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.inOrder(inOrder)));

        // Test reset
        mockitoCore.reset(mock);
        verify(mockitoCore).reset();
        verify(mock, eq(VerificationModeFactory.never()));

        // Test verifyNoMoreInteractions
        List<Invocation> invocations = Arrays.asList(new Invocation("foo", "bar"));
        when(mock).thenReturn("foo");
        VerificationDataImpl data = new VerificationDataImpl(invocations, null);
        mockitoCore.verifyNoMoreInteractions(mock, VerificationModeFactory.withRealCalls());
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noMoreInteractions()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noMoreInteractionsWithRealCalls()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noMoreInteractionsWithRealCalls(invocations)));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noMoreInteractions(data)));

        // Test reset and verifyNoMoreInteractions
        reset(mock);
        mockitoCore.verifyNoMoreInteractions(mock);
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noMoreInteractions()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noMoreInteractions(mock)));
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        String mockClass = String.class.getName();
        String mock1 = mock(String.class);
        String mock2 = mock(String.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        List<Invocation> invocations1 = Arrays.asList(new Invocation("foo1", "bar1"), new Invocation("foo2", "bar2"));
        when(mock1).thenAnswer((Answer) -> invocations1.get(0));
        when(mock2).thenAnswer((Answer) -> invocations1.get(1));

        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noMoreInteractions()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noMoreInteractions(Arrays.asList(mock1, mock2))));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.inOrder()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.inOrder(inOrder)));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.inOrder(inOrder, invocations1)));

        // Test verifyNoMoreInteractionsInOrder with empty mocks
        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(), inOrder);
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noMoreInteractions()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noMoreInteractions(Arrays.asList())));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.inOrder()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.inOrder(inOrder)));
    }

    @Test
    public void testVerifyZeroInteractions() {
        String mockClass = String.class.getName();
        String mock = mock(String.class);

        mockitoCore.verifyZeroInteractions(mock);
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noInteractions()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noInteractions(mock)));

        // Test verifyZeroInteractions with empty mocks
        mockitoCore.verifyZeroInteractions(Arrays.asList());
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.noInteractions()));
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNullMock() {
        mockitoCore.verify(null, VerificationModeFactory.never());
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNullMocks() {
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyMissingInteractions() {
        String mock = mock(String.class);
        mockitoCore.verifyNoMoreInteractions(mock, VerificationModeFactory.withRealCalls());
        mockitoCore.verifyZeroInteractions(mock);
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        List<Object> mocks = Arrays.asList(mock1, mock2);
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.inOrder()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.inOrder(inOrder)));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.inOrder(mocks)));

        // Test InOrder with empty mocks
        mockitoCore.inOrder();
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.inOrder()));
    }

    @Test
    public void testDoAnswer() {
        Answer answer = invocation -> "answered";
        Stubber stubber = mockitoCore.doAnswer(answer);
        Object mock = mock(Object.class);
        Object answerValue = stubber.doAnswer(answer).when(mock).answer();
        assertEquals("answered", answerValue);
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        VoidMethodStubbable voidMethodStubbable = mockitoCore.stubVoid(mock);
        voidMethodStubbable.doNothing();
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.doNothing()));
        verify(mockitoCore).verificationStarted(eq(VerificationModeFactory.doNothing(mock)));
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }
}