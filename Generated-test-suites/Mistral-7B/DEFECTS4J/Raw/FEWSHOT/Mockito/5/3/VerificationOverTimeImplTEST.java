package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.internal.verification.api.VerificationData.verificationData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationOverTimeImpl verificationOverTime;
    private Timer timer;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        timer = mock(Timer.class);
        verificationOverTime = new VerificationOverTimeImpl(10, 100, delegate, true, timer);
    }

    @Test
    public void testVerifyDelegateIsSatisfiedImmediately() {
        VerificationData data = verificationData("mock");
        verificationOverTime.verify(data);

        verify(delegate).verify(data);
    }

    @Test
    public void testVerifyDelegateIsSatisfiedAfterTimeout() {
        VerificationData data = verificationData("mock");
        timer.stub(timer -> timer.isCounting()).returns(true);
        timer.stub(timer -> timer.elapsed(TimeUnit.MILLISECONDS)).returns(50);

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
        verify(timer).stop();
    }

    @Test
    public void testVerifyDelegateIsNotSatisfiedImmediately() {
        VerificationData data = verificationData("mock");
        timer.stub(timer -> timer.isCounting()).returns(true);
        timer.stub(timer -> timer.elapsed(TimeUnit.MILLISECONDS)).returns(50);

        AssertionError exception = assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));

        verify(delegate).verify(data);
        verify(timer).stop();
        assertSame(exception.getCause().getCause(), delegate);
    }

    @Test
    public void testVerifyDelegateNeverRecoversAfterFailure() {
        VerificationData data = verificationData("mock");
        VerificationMode verificationModeThatNeverRecovers = spy(new AtLeastOnce(1));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, 100, verificationModeThatNeverRecovers, false, timer);
        timer.stub(timer -> timer.isCounting()).returns(true);

        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
    }

    @Test
    public void testVerifyNoInteractionsDelegateIsSatisfiedImmediately() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode noInteractionsDelegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(10, 100, noInteractionsDelegate, true, timer);

        noInteractionsDelegate.verify(data);
        verificationOverTime.verify(data);

        verifyNoInteractions(delegate);
    }

    @Test
    public void testVerifyNoInteractionsDelegateIsSatisfiedAfterTimeout() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode noInteractionsDelegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(10, 100, noInteractionsDelegate, true, timer);
        timer.stub(timer -> timer.isCounting()).returns(true);
        timer.stub(timer -> timer.elapsed(TimeUnit.MILLISECONDS)).returns(50);

        noInteractionsDelegate.verifyNoInteractions(data);
        verificationOverTime.verify(data);

        verifyNoInteractions(delegate);
        verify(timer).stop();
    }

    @Test
    public void testVerifyNoInteractionsDelegateIsNotSatisfiedImmediately() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode noInteractionsDelegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(10, 100, noInteractionsDelegate, true, timer);
        timer.stub(timer -> timer.isCounting()).returns(true);

        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
        verifyNoInteractions(delegate);
    }

    @Test
    public void testVerifyZeroInteractionsDelegateIsSatisfiedImmediately() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode zeroInteractionsDelegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(10, 100, zeroInteractionsDelegate, true, timer);

        zeroInteractionsDelegate.verifyZeroInteractions();
        verificationOverTime.verify(data);

        verifyZeroInteractions(delegate);
    }

    @Test
    public void testVerifyZeroInteractionsDelegateIsSatisfiedAfterTimeout() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode zeroInteractionsDelegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(10, 100, zeroInteractionsDelegate, true, timer);
        timer.stub(timer -> timer.isCounting()).returns(true);
        timer.stub(timer -> timer.elapsed(TimeUnit.MILLISECONDS)).returns(50);

        zeroInteractionsDelegate.verifyZeroInteractions();
        verificationOverTime.verify(data);

        verifyZeroInteractions(delegate);
        verify(timer).stop();
    }

    @Test
    public void testVerifyZeroInteractionsDelegateIsNotSatisfiedImmediately() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode zeroInteractionsDelegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(10, 100, zeroInteractionsDelegate, true, timer);
        timer.stub(timer -> timer.isCounting()).returns(true);

        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
        verifyZeroInteractions(delegate);
    }

    @Test
    public void testVerifyDelegateLimitIsSatisfiedImmediately() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(1, 100, delegate, true, timer);

        verificationOverTime.verify(data);

        verify(delegate, atLeastOnce()).verify(data);
    }

    @Test
    public void testVerifyDelegateLimitIsSatisfiedAfterTimeout() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(1, 200, delegate, true, timer);
        timer.stub(timer -> timer.isCounting()).returns(true);
        timer.stub(timer -> timer.elapsed(TimeUnit.MILLISECONDS)).returns(50);

        verificationOverTime.verify(data);

        verify(delegate, atLeastOnce()).verify(data);
        verify(timer).stop();
    }

    @Test
    public void testVerifyDelegateLimitIsNotSatisfiedImmediately() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(1, 200, delegate, true, timer);
        timer.stub(timer -> timer.isCounting()).returns(true);

        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
        verify(delegate, never()).verify(data);
    }

    @Test
    public void testVerifyDelegateLimitIsSatisfiedAfterTimeoutWithMockitoAssertionError() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(1, 200, delegate, true, timer);
        timer.stub(timer -> timer.isCounting()).returns(true);
        timer.stub(timer -> timer.elapsed(TimeUnit.MILLISECONDS)).returns(201);

        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
        verify(delegate, atLeastOnce()).verify(data);
        verify(timer).stop();
    }

    @Test
    public void testVerifyDelegateLimitIsSatisfiedAfterTimeoutWithArgumentsAreDifferentException() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(1, 200, delegate, true, timer);
        timer.stub(timer -> timer.isCounting()).returns(true);
        timer.stub(timer -> timer.elapsed(TimeUnit.MILLISECONDS)).returns(201);

        assertThrows(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class, () -> verificationOverTime.verify(data));
        verify(delegate, atLeastOnce()).verify(data);
        verify(timer).stop();
    }

    @Test
    public void testVerifyDelegateLimitIsNotSatisfiedImmediatelyWithMockitoAssertionError() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(1, 200, delegate, true, timer);

        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
        verify(delegate, never()).verify(data);
    }

    @Test
    public void testVerifyDelegateLimitIsNotSatisfiedImmediatelyWithArgumentsAreDifferentException() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(1, 200, delegate, true, timer);

        assertThrows(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class, () -> verificationOverTime.verify(data));
        verify(delegate, never()).verify(data);
    }

    @Test
    public void testVerifyDelegateLimitIsSatisfiedAfterTimeoutWithReturnOnSuccess() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(1, 200, delegate, false, timer);
        timer.stub(timer -> timer.isCounting()).returns(true);
        timer.stub(timer -> timer.elapsed(TimeUnit.MILLISECONDS)).returns(201);

        verificationOverTime.verify(data);

        verify(delegate, atLeastOnce()).verify(data);
        verify(timer).stop();
    }

    @Test
    public void testVerifyDelegateLimitIsNotSatisfiedAfterTimeoutWithReturnOnSuccess() {
        VerificationData data = verificationData("mock");
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(1, 200, delegate, false, timer);
        timer.stub(timer -> timer.isCounting()).returns(true);
        timer.stub(timer -> timer.elapsed(TimeUnit.MILLISECONDS)).returns(201);

        assertNull(verificationOverTime.verify(data));
        verify(delegate, never()).verify(data);
        verify(timer).stop();
    }

    @Test
    public void testCanRecoverFromFailure() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, 100, delegate, false, timer);

        assertFalse(verificationOverTime.canRecoverFromFailure(delegate));
        assertTrue(verificationOverTime.canRecoverFromFailure(new AtLeastOnce(1)));
    }

    @Test
    public void testSleep() {
        verificationOverTime.sleep(100);
    }
}