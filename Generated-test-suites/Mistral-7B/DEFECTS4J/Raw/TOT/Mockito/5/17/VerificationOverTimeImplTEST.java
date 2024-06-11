package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private Timer timer;
    private Stub<Integer> stub;

    @Before
    public void setup() {
        long pollingPeriodMillis = 50;
        long durationMillis = 500;
        verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, Mockito.verifyNoMoreInteractions(Mockito.mock(Object.class)), false);
        timer = Mockito.spy(new Timer(durationMillis));
    }

    @Test
    public void testSatisfiedBeforeTimeout() {
        // Given
        Object mock = Mockito.mock(Object.class);
        verificationMode.verify(new VerificationData(mock, ArgumentMatchers.isA(InvocationOnMock.class)));

        // When
        Mockito.verify(mock).someMethod();

        // Then
        Assert.assertTrue("Returned successfully since delegate is satisfied", verificationMode.returnOnSuccess);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testNotSatisfiedBeforeTimeout() {
        // Given
        Object mock = Mockito.mock(Object.class);

        // When
        verificationMode.verify(new VerificationData(mock, ArgumentMatchers.isA(InvocationOnMock.class)));

        // Then
        // The test should time out and throw an exception
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateFailsWithAssertionError() {
        // Given
        Object mock = Mockito.mock(Object.class);
        stub = Mockito.mock(Stub.class);
        Mockito.when(stub.answer(ArgumentMatchers.isA(Answer.class))).then(answers -> {
            throw new AssertionError("Unexpected invocation");
        });
        Mockito.when(stub.verify()).thenThrow(new MockitoAssertionError("Unexpected invocation"));

        verificationMode = new VerificationOverTimeImpl(50, 500, Mockito.withSettings().defaultAnswer(stub), false);

        // When
        verificationMode.verify(new VerificationData(mock, ArgumentMatchers.isA(InvocationOnMock.class)));

        // Then
        // The test should propagate the error thrown by the delegate
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateFailsWithJUnitError() {
        // Given
        Object mock = Mockito.mock(Object.class);
        stub = Mockito.mock(Stub.class);
        Mockito.when(stub.answer(ArgumentMatchers.isA(Answer.class))).thenAnswer(invocation -> {
            throw new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent(invocation.getMock(), invocation.getArguments()[0]);
        });
        Mockito.when(stub.verify()).thenThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent(mock, ArgumentMatchers.isA(Object.class)));

        verificationMode = new VerificationOverTimeImpl(50, 500, Mockito.withSettings().defaultAnswer(stub), false);

        // When
        verificationMode.verify(new VerificationData(mock, ArgumentMatchers.isA(Object.class)));

        // Then
        // The test should propagate the error thrown by the delegate
    }

    @Test
    public void testSatisfiedAfterTimeout() {
        // Given
        Object mock = Mockito.mock(Object.class);

        // When
        verificationMode.verify(new VerificationData(mock, ArgumentMatchers.isA(InvocationOnMock.class)));

        // Then
        timer.start();
        sleep(durationMillis + pollingPeriodMillis);

        // The verification should have timed out and then been satisfied later
        Mockito.verify(mock).someMethod();

        // The test should not throw an exception
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateFailsAndDoesNotRecover() {
        // Given
        Object mock = Mockito.mock(Object.class);
        verificationMode = new VerificationOverTimeImpl(50, 500, Mockito.withSettings().defaultAnswer(Mockito.doThrow(new RuntimeException("Unexpected error")).answers()), false);

        // When
        verificationMode.verify(new VerificationData(mock, ArgumentMatchers.isA(InvocationOnMock.class)));

        // Then
        // The test should throw the exception thrown by the delegate since it does not recover
    }

    private void sleep(long sleep) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}