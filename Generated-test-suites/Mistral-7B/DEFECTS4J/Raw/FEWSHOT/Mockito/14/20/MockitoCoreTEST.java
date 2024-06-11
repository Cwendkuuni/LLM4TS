package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
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
import org.mockito.stubbing.AnswerWithArgs;
import org.mockito.stubbing.Invoker;
import org.mockito.stubbing.StubberAnswer;
import org.mockito.verification.VerificationMode;
import org.mockito.internal.verification.api.VerificationModeFactoryImpl;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        Class<MockitoCore> classToMock = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mock(classToMock, mockSettings);
        assertSame(mockitoCore, mockitoCore.mock(classToMock, mockSettings));
    }

    @Test
    public void testMockWithSpy() {
        Class<MockitoCore> classToMock = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCoreSpy = spy(new MockitoCore());
        MockitoCore mockitoCore = mock(classToMock, mockSettings);
        assertSame(mockitoCore, mockitoCoreSpy.mock(classToMock, mockSettings));
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        IOngoingStubbing stubbing = mockitoCore.stub();
        assertSame(stubbing, mockitoCore.stub(null));
        assertSame(stubbing, mockitoCore.stub(1));
    }

    @Test
    public void testDeprecatedOngoingStubbing() {
        MockitoCore mockitoCore = new MockitoCore();
        DeprecatedOngoingStubbing<Object> deprecatedOngoingStubbing = mockitoCore.stub(null);
        assertSame(deprecatedOngoingStubbing, mockitoCore.stub((DeprecatedOngoingStubbing<Object>) null));
        assertSame(deprecatedOngoingStubbing, mockitoCore.stub((DeprecatedOngoingStubbing<Object>) 1));
    }

    @Test
    public void testOngoingStubbing() {
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(null);
        assertSame(ongoingStubbing, mockitoCore.when(1));
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(mock, VerificationModeFactory.instance().verifyNoMoreInteractions());
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNull() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(null, VerificationModeFactory.instance().verifyNoMoreInteractions());
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNotAMock() {
        Object notAMock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(notAMock, VerificationModeFactory.instance().verifyNoMoreInteractions());
    }

    @Test
    public void testReset() {
        Object mock = mock(Object.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.reset(mock);
        verifyNoMoreInteractions(mock);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsNull() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsNotAMock() {
        Object notAMock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(notAMock);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InOrder inOrder = inOrder(mock1, mock2);
        // Perform some interactions
        inOrder.verify(mock1).someMethod();
        inOrder.verify(mock2).someMethod();
        inOrder.verifyNoMoreInteractions();
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsInOrderNull() {
        InOrder inOrder = inOrder(null);
        inOrder.verifyNoMoreInteractions();
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsInOrderNotAMock() {
        Object notAMock = new Object();
        InOrder inOrder = inOrder(notAMock);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InOrder inOrder = inOrder(mock1, mock2);
        assertSame(inOrder, inOrder.inOrder(mock1, mock2));
        assertSame(inOrder, inOrder.inOrder(Arrays.asList(mock1, mock2)));
    }

    @Test(expected = AssertionError.class)
    public void testInOrderNull() {
        InOrder inOrder = inOrder(null);
    }

    @Test(expected = AssertionError.class)
    public void testInOrderNotAMock() {
        Object notAMock = new Object();
        InOrder inOrder = inOrder(notAMock);
    }

    @Test
    public void testAnswer() {
        Object mock = mock(Object.class);
        Answer<Object> answer = org.mockito.answer.Answers.returns(42);
        Stubber stubber = mockitoCore().doAnswer(answer);
        when(mock).someMethod().then(stubber.answer());
        Object result = mock.someMethod();
        assertEquals(42, result);
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mock = mock(Object.class);
        VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore().stubVoid(mock);
        voidMethodStubbable.answers(new StubberAnswer<Object, InvocationOnMock<Object>>(null));
        voidMethodStubbable.answer(new StubberAnswer<Object, InvocationOnMock<Object>>(null));
    }

    @Test
    public void testVerificationModeFactory() {
        VerificationModeFactory verificationModeFactory = new VerificationModeFactoryImpl();
        VerificationMode verificationMode = verificationModeFactory.verifyNoMoreInteractions();
        assertEquals(VerificationMode.NO_MORE_INTERACTIONS, verificationMode.getVerificationMode());

        VerificationModeFactory verificationModeFactoryWithArgs = new VerificationModeFactoryImpl();
        VerificationMode verificationModeWithArgs = verificationModeFactoryWithArgs.verify(Arrays.asList(1, 2));
        assertEquals(VerificationMode.VERIFY, verificationModeWithArgs.getVerificationMode());

        VerificationMode verificationModeWithAnswer = verificationModeFactoryWithArgs.verify(Arrays.asList(1, 2)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return 42;
            }
        });
        assertEquals(VerificationMode.VERIFY_WITH_ANSWER, verificationModeWithAnswer.getVerificationMode());

        VerificationMode verificationModeWithAnswerAndArgs = verificationModeFactoryWithArgs.verify(Arrays.asList(1, 2)).thenAnswer(new AnswerWithArgs<Object, Object[]>() {
            @Override
            public Object answer(InvocationOnMock invocation, Object[] args) throws Throwable {
                return 42;
            }
        });
        assertEquals(VerificationMode.VERIFY_WITH_ANSWER_AND_ARGS, verificationModeWithAnswerAndArgs.getVerificationMode());
    }

    private MockitoCore mockitoCore() {
        return new MockitoCore();
    }

    private void assertEquals(Object expected, Object actual) {
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
    }

    private void assertEquals(int expected, Object actual) {
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, ((Number) actual).intValue());
    }

    private void assertEquals(Object[] expected, Object[] actual) {
        assertArrayEquals("Expected: " + Arrays.toString(expected) + ", but was: " + Arrays.toString(actual), expected, actual);
    }

    private InOrder inOrder(Object... mocks) {
        return new InOrderImpl(Arrays.asList(mocks));
    }
}