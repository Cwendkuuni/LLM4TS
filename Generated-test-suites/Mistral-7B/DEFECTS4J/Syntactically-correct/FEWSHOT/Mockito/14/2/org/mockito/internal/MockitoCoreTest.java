package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.mock.MockHandlerInterface;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.DeprecatedOngoingStubbing;
import org.mockito.internal.stubbing.OngoingStubbing;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationDataInOrder;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.AnswerWith;
import org.mockito.stubbing.AnswerWithArgument;
import org.mockito.stubbing.AnswerWithArguments;
import org.mockito.stubbing.AnswerWithMultiArguments;
import org.mockito.stubbing.AnswerWithReturnValue;
import org.mockito.stubbing.AnswerWithThrowable;
import org.mockito.stubbing.AnswerWithStub;

public class MockitoCoreTest {

    private MockingProgress mockingProgress;
    private MockUtil mockUtil;
    private Reporter reporter;

    @Before
    public void setUp() {
        this.mockingProgress = new ThreadSafeMockingProgress();
        this.mockUtil = spy(new MockUtil());
        this.reporter = new Reporter();
    }

    @Test
    public void testMock() {
        Class<Integer> integerClass = Integer.class;
        MockSettings mockSettings = new MockSettingsImpl();
        Integer mock = mock(integerClass, mockSettings);
        assertSame(integerClass, mock.getClass());
        verify(mockUtil).createMock(integerClass, mockSettings);
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mockingProgress.pullOngoingStubbing();
        assertNull(stubbing);

        stubbing = mockingProgress.stubbingStarted();
        assertSame(stubbing, mockingProgress.pullOngoingStubbing());

        mockingProgress.reset();
        assertNull(mockingProgress.pullOngoingStubbing());
    }

    @Test
    public void testStubDeprecated() {
        Object mock = mock(Object.class);
        DeprecatedOngoingStubbing<Object> stubbing = mockingProgress.stub(mock);
        assertSame(stubbing, mockingProgress.stubbing());
    }

    @Test
    public void testWhen() {
        Object mock = mock(Object.class);
        OngoingStubbing<Object> stubbing = mockingProgress.when(mock);
        assertSame(stubbing, mockingProgress.stubbing());

        Object argument = new Object();
        stubbing.thenReturn(argument);
        verify(mock).equals(argument);
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        verify(mock).equals(mock);
    }

    @Test
    public void testVerifyNull() {
        assertThrows(NullPointerException.class, () -> verify(null, MockitoCore.anyVerificationMode()));
    }

    @Test
    public void testVerifyNotAMock() {
        Object notAMock = new Object();
        assertThrows(NotAMockException.class, () -> verify(notAMock, MockitoCore.anyVerificationMode()));
    }

    @Test
    public void testReset() {
        Object mock = mock(Object.class);
        MockHandlerInterface<Object> handler = mockUtil.getMockHandler(mock);
        handler.reset();

        List<Invocation> invocations = handler.getInvocationContainer().getInvocations();
        assertEmpty(invocations);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(Object.class);
        verifyNoMoreInteractions(mock);

        List<Invocation> invocations = mockUtil.getMockHandler(mock).getInvocationContainer().getInvocations();
        assertEmpty(invocations);
    }

    @Test
    public void testVerifyNoMoreInteractionsList() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);

        InOrder inOrder = inOrder(mock1, mock2, mock3);

        verifyNoMoreInteractions(mock1, mock2, mock3, inOrder);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        InOrder inOrder = inOrder(mock1, mock2);

        verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
    }

    @Test
    public void testVerifyOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        InOrder inOrder = inOrder(mock1, mock2);

        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(mock1).thenReturn(invocation1);
        when(mock2).thenReturn(invocation2);

        inOrder.verify(mock1).someMethod();
        inOrder.verify(mock2).someMethod();

        inOrder.verify(mock1).someMethod();
        inOrder.verify(mock2).someMethod();

        inOrder.verify(mock1).someMethod();
        inOrder.verify(mock2).someMethod();

        inOrder.verify(mock1).someMethod();
        inOrder.verify(mock2).someMethod();

        inOrder.verifyVerified(mock1);
        inOrder.verifyVerified(mock2);
    }

    @Test
    public void testVerifyOrderThrownException() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        InOrder inOrder = inOrder(mock1, mock2);

        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(mock1).thenThrow(new RuntimeException("Exception1"));
        when(mock2).thenReturn(invocation2);

        inOrder.verify(mock1).someMethod();
        inOrder.verify(mock2).someMethod();

        assertThrows(AssertionError.class, () -> inOrder.verifyVerified(mock1));
        assertThrows(AssertionError.class, () -> inOrder.verifyVerified(mock2));
    }

    @Test
    public void testVerifyOrderThrownRuntimeException() {
        Object mock = mock(Object.class);

        when(mock).thenThrow(new RuntimeException("Exception"));

        try {
            verify(mock).someMethod();
        } catch (RuntimeException e) {
            assertSame(e, verifyNoMoreInteractions(mock));
        }
    }

    @Test
    public void testVerifyOrderThrownError() {
        Object mock = mock(Object.class);

        when(mock).thenThrow(new Error("Error"));

        try {
            verify(mock).someMethod();
        } catch (Error e) {
            assertSame(e, verifyNoMoreInteractions(mock));
        }
    }

    @Test
    public void testVerifyOrderThrownAssertionError() {
        Object mock = mock(Object.class);

        when(mock).thenAnswer(AnswerWithThrowable.create(new AssertionError("AssertionError")));

        try {
            verify(mock).someMethod();
        } catch (AssertionError e) {
            assertSame(e, verifyNoMoreInteractions(mock));
        }
    }

    @Test
    public void testVerifyOrderThrownVerificationError() {
        Object mock = mock(Object.class);

        when(mock).thenAnswer(AnswerWithThrowable.create(new VerificationError()));

        try {
            verify(mock).someMethod();
        } catch (VerificationError e) {
            assertSame(e, verifyNoMoreInteractions(mock));
        }
    }

    @Test
    public void testVerifyOrderThrownOtherException() {
        Object mock = mock(Object.class);

        when(mock).thenAnswer(AnswerWithThrowable.create(new IllegalStateException("IllegalStateException")));

        try {
            verify(mock).someMethod();
        } catch (IllegalStateException e) {
            assertSame(e, verifyNoMoreInteractions(mock));
        }
    }

    @Test
    public void testAnswer() {
        Object mock = mock(Object.class);

        when(mock).thenAnswer(AnswerWithReturnValue.create(42));

        assertEquals(42, mock.someMethod());
    }

    @Test
    public void testAnswerWithArgument() {
        Object mock = mock(Object.class);

        when(mock).thenAnswer(AnswerWithArgument.create(Integer.class, 42));

        assertEquals(42, mock.someMethod(42));
    }

    @Test
    public void testAnswerWithArguments() {
        Object mock = mock(Object.class);

        when(mock).thenAnswer(AnswerWithArguments.create(new Class[] { Integer.class, String.class }, new Object[] { 42, "foo" }));

        assertEquals("foo", mock.someMethod(42, "foo"));
    }

    @Test
    public void testAnswerWithMultiArguments() {
        Object mock = mock(Object.class);

        when(mock).thenAnswer(AnswerWithMultiArguments.create(new Class[] { Integer.class, String[].class }, new Object[] { 42, new String[] { "foo", "bar" } }));

        assertEquals("bar", mock.someMethod(42, new String[] { "foo", "bar" }));
    }

    @Test
    public void testAnswerWithStub() {
        Object mock = mock(Object.class);

        Object stub = mock(Object.class);

        when(mock).thenAnswer(AnswerWithStub.create(stub));

        assertEquals(stub, mock.someMethod());
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);

        VoidMethodStubbable<Object> stubbable = mockingProgress.stubVoid(mock);

        stubbable.answer(new AnswerWithVoid("Answer"));

        mock.someVoidMethod();

        verify(mockUtil).getMockHandler(mock).voidMethod();
    }

    @Test
    public void testValidateMockitoUsage() {
        mockingProgress.validateState();
    }

    private void assertEmpty(List<Invocation> invocations) {
        assertArrayEquals(new Invocation[0], invocations.toArray(new Invocation[0]));
    }

}