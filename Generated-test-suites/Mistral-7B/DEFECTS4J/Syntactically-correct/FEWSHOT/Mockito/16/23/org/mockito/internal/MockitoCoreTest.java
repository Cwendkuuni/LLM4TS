package org.mockito.internal;

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
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockingProgress;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.verification.api.VerificationMode;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockingProgress mockingProgress = MockitoCoreSpy.getInstance().mockingProgress;
        MockHandlerInterface<String> mockHandler = MockitoCoreSpy.getInstance().mockUtil.getMockHandler(String.class);

        when(mockHandler.createMock(String.class, ArgumentMatchers.any(MockSettings.class))).thenReturn(spy(new String("mock")));

        String mock = MockitoCoreSpy.getInstance().mock(String.class);
        assertSame(mock, mockHandler.getMock(mock));

        MockitoCoreSpy.getInstance().verify(mock, VerificationMode.RETURN);
        mockingProgress.verifyState();
        mockingProgress.reset();
    }

    @Test
    public void testStub() {
        MockingProgress mockingProgress = MockitoCoreSpy.getInstance().mockingProgress;
        StubberImpl stubber = new StubberImpl();

        when(MockitoCoreSpy.getInstance().stub()).thenReturn(stubber);

        Object result = MockitoCoreSpy.getInstance().stub(ArgumentMatchers.any());
        assertSame(result, stubber.getMockedObject());

        mockingProgress.verifyState();
        mockingProgress.reset();
    }

    @Test
    public void testWhen() {
        MockingProgress mockingProgress = MockitoCoreSpy.getInstance().mockingProgress;
        StubberImpl stubber = new StubberImpl();
        Object mock = mock(Object.class);

        when(MockitoCoreSpy.getInstance()).thenReturn(stubber);

        when(mock).thenReturn(new Object());

        Object result = MockitoCoreSpy.getInstance().when(mock).thenReturn(new Object()).getMock();
        assertSame(result, mock);

        mockingProgress.verifyState();
        mockingProgress.reset();
    }

    @Test(expected = MockitoException.class)
    public void testWhenThrowsException() {
        MockingProgress mockingProgress = MockitoCoreSpy.getInstance().mockingProgress;
        Object mock = mock(Object.class);

        when(MockitoCoreSpy.getInstance()).thenThrow(new RuntimeException());

        MockitoCoreSpy.getInstance().when(mock).thenThrow(new RuntimeException());
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);

        MockitoCoreSpy.getInstance().verify(mock, VerificationMode.RETURN);
        MockitoCoreSpy.getInstance().verify(mock, VerificationMode.RETURN).toString();

        MockitoCoreSpy.getInstance().verify(mock);
        MockitoCoreSpy.getInstance().verify(mock, VerificationMode.NEVER);

        MockitoCoreSpy.getInstance().verify(mock, ArgumentMatchers.isA(String.class));

        Object[] mocks = new Object[] { mock, mock };
        MockitoCoreSpy.getInstance().verify(mocks);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(Object.class);

        MockitoCoreSpy.getInstance().verifyNoMoreInteractions(mock);
        MockitoCoreSpy.getInstance().verifyNoMoreInteractions(mock, ArgumentMatchers.isA(String.class));

        Object[] mocks = new Object[] { mock, mock };
        MockitoCoreSpy.getInstance().verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testReset() {
        Object mock = mock(Object.class);

        MockitoCoreSpy.getInstance().reset(mock);
        MockitoCoreSpy.getInstance().verify(mock).toString();

        MockitoCoreSpy.getInstance().reset(mock, mock);
        MockitoCoreSpy.getInstance().verify(mock).toString();
        MockitoCoreSpy.getInstance().verify(mock).toString();
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        InOrder inOrder = MockitoCoreSpy.getInstance().inOrder(mock1, mock2);

        inOrder.verify(mock1).toString();
        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();

        inOrder.verify(mock1).toString();
        inOrder.verifyNoMoreInteractions();

        List<Object> mocks = Arrays.asList(mock1, mock2);
        InOrder inOrderList = MockitoCoreSpy.getInstance().inOrder(mocks.toArray(new Object[0]));

        inOrderList.verify(mock1).toString();
        inOrderList.verify(mock2).toString();
        inOrderList.verifyNoMoreInteractions();
    }

    @Test
    public void testDoAnswer() {
        Object mock = mock(Object.class);
        Answer<Integer> answer = invocation -> invocation.getArgument(0).hashCode();

        when(MockitoCoreSpy.getInstance()).thenReturn(new StubberImpl().doAnswer(answer));

        int result = MockitoCoreSpy.getInstance().doAnswer(answer).when(mock).hashCode();
        assertSame(result, mock.hashCode());
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);

        when(MockitoCoreSpy.getInstance()).thenReturn(new StubberImpl().voidMethodStubbable(mock));

        MockitoCoreSpy.getInstance().stubVoid(mock).thenReturn(42);

        MockitoCoreSpy.getInstance().verify(mock).voidMethod();
        MockitoCoreSpy.getInstance().verify(mock).voidMethod();
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCoreSpy.getInstance().validateMockitoUsage();
    }

    private static MockitoCore MockitoCoreSpy = new MockitoCore() {
        private ThreadSafeMockingProgress mockingProgress = new ThreadSafeMockingProgress();

        @Override
        public MockSettingsImpl getMockSettings() {
            return new MockSettingsImpl();
        }

        @Override
        public ThreadSafeMockingProgress getMockingProgress() {
            return mockingProgress;
        }

        @Override
        public Reporter getReporter() {
            return new Reporter();
        }

        @Override
        public MockUtil getMockUtil() {
            return new MockUtil();
        }

        @Override
        public StubberImpl getStubber() {
            return new StubberImpl();
        }

        @Override
        public OngoingStubbingImpl getOngoingStubbing() {
            return new OngoingStubbingImpl();
        }

        @Override
        public IOngoingStubbing pullOngoingStubbing() {
            return mockingProgress.getOngoingStubbing();
        }

        @Override
        public void validateState() {
            mockingProgress.validateState();
        }

        @Override
        public void reset() {
            mockingProgress.reset();
        }

        @Override
        public void resetOngoingStubbing() {
            mockingProgress.resetOngoingStubbing();
        }

        @Override
        public void stubbingStarted() {
            mockingProgress.stubbingStarted();
        }

        @Override
        public void verificationStarted(VerificationMode verificationMode) {
            mockingProgress.verificationStarted(verificationMode);
        }
    };
}