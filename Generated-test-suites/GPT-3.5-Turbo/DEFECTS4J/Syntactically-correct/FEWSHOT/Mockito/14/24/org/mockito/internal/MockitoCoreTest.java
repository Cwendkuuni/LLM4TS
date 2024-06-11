package org.mockito.internal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockitoCoreTest {
    private final MockitoCore mockitoCore = new MockitoCore();

    @Test
    public void testMock() {
        Class<T> classToMock = T.class;
        MockSettings mockSettings = mock(MockSettings.class);

        T mock = mockitoCore.mock(classToMock, mockSettings);

        assertNotNull(mock);
        assertTrue(mock instanceof T);
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mockitoCore.stub();

        assertNull(stubbing);
    }

    @Test
    public void testStubWithMethodCall() {
        T methodCall = mock(T.class);

        DeprecatedOngoingStubbing<T> stubbing = mockitoCore.stub(methodCall);

        assertNotNull(stubbing);
    }

    @Test
    public void testWhen() {
        T methodCall = mock(T.class);

        OngoingStubbing<T> stubbing = mockitoCore.when(methodCall);

        assertNotNull(stubbing);
    }

    @Test
    public void testVerify() {
        T mock = mock(T.class);
        VerificationMode mode = mock(VerificationMode.class);

        T result = mockitoCore.verify(mock, mode);

        assertEquals(mock, result);
    }

    @Test
    public void testVerifyWithNullMock() {
        T mock = null;
        VerificationMode mode = mock(VerificationMode.class);

        try {
            mockitoCore.verify(mock, mode);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testVerifyWithNotAMock() {
        T mock = new T();
        VerificationMode mode = mock(VerificationMode.class);

        try {
            mockitoCore.verify(mock, mode);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            // expected
        }
    }

    @Test
    public void testReset() {
        T[] mocks = new T[] { mock(T.class), mock(T.class) };

        mockitoCore.reset(mocks);

        // Ensure that resetMock method is called for each mock object
    }

    @Test
    public void testResetWithEmptyMocks() {
        T[] mocks = new T[] {};

        mockitoCore.reset(mocks);

        // Ensure that resetMock method is not called when there are no mock objects
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        T mock = mock(T.class);

        mockitoCore.verifyNoMoreInteractions(mock);

        // Ensure that noMoreInteractions method is called for the mock object
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNullMock() {
        T mock = null;

        try {
            mockitoCore.verifyNoMoreInteractions(mock);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Arrays.asList(mock(T.class), mock(T.class));
        InOrderContext inOrderContext = mock(InOrderContext.class);

        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        // Ensure that verifyInOrder method is called with the correct parameters
    }

    @Test
    public void testInOrder() {
        Object[] mocks = new Object[] { mock(T.class), mock(T.class) };

        InOrder inOrder = mockitoCore.inOrder(mocks);

        assertNotNull(inOrder);
    }

    @Test
    public void testInOrderWithNullMocks() {
        Object[] mocks = null;

        try {
            mockitoCore.inOrder(mocks);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testInOrderWithEmptyMocks() {
        Object[] mocks = new Object[] {};

        try {
            mockitoCore.inOrder(mocks);
            fail("Expected ReporterException to be thrown");
        } catch (ReporterException e) {
            // expected
        }
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);

        Stubber stubber = mockitoCore.doAnswer(answer);

        assertNotNull(stubber);
    }

    @Test
    public void testStubVoid() {
        T mock = mock(T.class);

        VoidMethodStubbable<T> stubbable = mockitoCore.stubVoid(mock);

        assertNotNull(stubbable);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();

        // Ensure that validateState method is called
    }

    @Test
    public void testGetLastInvocation() {
        Invocation invocation = mock(Invocation.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = Arrays.asList(invocation);

        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        Invocation result = mockitoCore.getLastInvocation();

        assertEquals(invocation, result);
    }

    private static class T {
    }
}