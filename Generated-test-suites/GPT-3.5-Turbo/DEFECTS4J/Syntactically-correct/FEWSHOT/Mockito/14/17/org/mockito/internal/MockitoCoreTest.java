package org.mockito.internal;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {
    private MockitoCore mockitoCore = new MockitoCore();
    private MockUtil mockUtil = new MockUtil();
    private ThreadSafeMockingProgress mockingProgress = new ThreadSafeMockingProgress();

    @Test
    public void testMock() {
        // Test when classToMock and mockSettings are not null
        Class<?> classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettings.class);
        Object expected = mock(classToMock);
      
        Object actual = mockitoCore.mock(classToMock, mockSettings);
      
        assertSame(expected, actual);
        verify(mockingProgress).mockingStarted(actual, classToMock, mockSettings);
      
        // Test when classToMock is null
        try {
            mockitoCore.mock(null, mockSettings);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("classToMock cannot be null", e.getMessage());
        }
    }

    @Test
    public void testStub() {
        IOngoingStubbing expected = mock(IOngoingStubbing.class);
        mockingProgress.stubbingStarted();
        mockingProgress.pullOngoingStubbing();
        when(mockingProgress.pullOngoingStubbing()).thenReturn(expected);
      
        IOngoingStubbing actual = mockitoCore.stub();
      
        assertSame(expected, actual);
      
        mockingProgress.reset();
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testStub_withMethodCall() {
        IOngoingStubbing expected = mock(IOngoingStubbing.class);
        mockingProgress.stubbingStarted();
        mockingProgress.pullOngoingStubbing();
        when(mockingProgress.pullOngoingStubbing()).thenReturn(expected);
      
        DeprecatedOngoingStubbing<Object> actual = mockitoCore.stub(null);
      
        assertSame(expected, actual);
    }

    @Test
    public void testWhen() {
        IOngoingStubbing expected = mock(IOngoingStubbing.class);
        mockingProgress.stubbingStarted();
        mockingProgress.pullOngoingStubbing();
        when(mockingProgress.pullOngoingStubbing()).thenReturn(expected);
      
        OngoingStubbing<Object> actual = mockitoCore.when(null);
      
        assertSame(expected, actual);
    }

    @Test
    public void testVerify() {
        // Test when mock is null
        try {
            mockitoCore.verify(null, VerificationModeFactory.times(1));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("mock cannot be null", e.getMessage());
        }
      
        // Test when mock is not a mock
        try {
            mockitoCore.verify(new Object(), VerificationModeFactory.times(1));
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock but is null", e.getMessage());
        }
      
        Object mock = mock(Object.class);
        VerificationMode mode = VerificationModeFactory.times(1);
      
        Object actual = mockitoCore.verify(mock, mode);
      
        assertSame(mock, actual);
    }

    @Test
    public void testReset() {
        Object mock = mock(Object.class);
        Object[] mocks = new Object[]{mock};
      
        mockingProgress.validateState();
        mockingProgress.reset();
        mockingProgress.resetOngoingStubbing();
        mockUtil.resetMock(mock);
      
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).resetMock(mock);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = new Object[1];
        mocks[0] = null;
      
        assertMocksNotEmpty(mocks);
        mockingProgress.validateState();
        Object mock = mocks[0];
      
        try {
            mockitoCore.verifyNoMoreInteractions(mock);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock but is null", e.getMessage());
        }

        MockitoCore partialMock = mock(MockitoCore.class);
        when(partialMock.getLastInvocation()).thenReturn(mock(Invocation.class));
        mockitoCore = partialMock;
      
        try {
            mockitoCore.verifyNoMoreInteractions(mock);
            fail("Expected AssertionError to be thrown");
        } catch (AssertionError e) {
            assertEquals("No interactions wanted here", e.getMessage());
        }
      
        verify(mockingProgress).validateState();
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Collections.emptyList();
        InOrderContext inOrderContext = mock(InOrderContext.class);
        AllInvocationsFinder finder = mock(AllInvocationsFinder.class);
        VerificationDataInOrderImpl data = mock(VerificationDataInOrderImpl.class);
        mockingProgress.validateState();
        when(finder.find(mocks)).thenReturn(mock(List.class));
        when(mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext)).thenReturn(data);
        VerificationModeFactory.noMoreInteractions().verifyInOrder(data);
      
        MockitoCore partialMock = mock(MockitoCore.class);
        when(partialMock.verifyNoMoreInteractionsInOrder(mocks, inOrderContext)).tthenThrow(new RuntimeException());
        mockitoCore = partialMock;
      
        try {
            mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Exception message", e.getMessage());
        }
      
        verify(mockingProgress).validateState();
    }

    @Test
    public void testAssertMocksNotEmpty() {
        Object[] mocks = null;
        try {
            assertMocksNotEmpty(mocks);
            fail("Expected AssertionError to be thrown");
        } catch (AssertionError e) {
            assertEquals("Mocks cannot be null or empty", e.getMessage());
        }
      
        mocks = new Object[0];
      try {
            assertMocksNotEmpty(mocks);
            fail("Expected AssertionError to be thrown");
        } catch (AssertionError e) {
            assertEquals("Mocks cannot be null or empty", e.getMessage());
        }
    }

    @Test
    public void testInOrder() {
        Object[] mocks = null;
        try {
            assertMocksNotEmpty(mocks);
            fail("Expected AssertionError to be thrown");
        } catch (AssertionError e) {
            assertEquals("Mocks cannot be null or empty", e.getMessage());
        }

        Object[] mocks = new Object[3];
        mocks[0] = mock(Object.class);
        mocks[1] = null;
        mocks[2] = mock(Object.class);
      
        try {
            mockitoCore.inOrder(mocks);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock but is null", e.getMessage());
        }
      
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter).nullPassedWhenCreatingInOrder();
        verify(reporter).notAMockPassedWhenCreatingInOrder();
      
        InOrderImpl expected = mock(InOrderImpl.class);
        when(mockitoCore.inOrder(Arrays.asList(mocks))).thenReturn(expected);
      
        InOrder actual = mockitoCore.inOrder(mocks);
      
        assertSame(expected, actual);
    }

    @Test
    public void testDoAnswer() {
        Answer expected = mock(Answer.class);
        mockingProgress.stubbingStarted();
        mockingProgress.resetOngoingStubbing();
        when(new StubberImpl().doAnswer(expected)).thenReturn(mock(Stubber.class));
      
        Stubber actual = mockitoCore.doAnswer(expected);
      
        assertNotNull(actual);
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        MockHandlerInterface<Object> handler = mockUtil.getMockHandler(mock);
        mockingProgress.stubbingStarted();
        handler.voidMethodStubbable(mock);
      
        VoidMethodStubbable<Object> actual = mockitoCore.stubVoid(mock);
      
        assertNotNull(actual);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockingProgress.validateState();
        mockingProgress.reset();
        mockingProgress.resetOngoingStubbing();
      
        mockitoCore.validateMockitoUsage();
      
        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        Invocation expected = mock(Invocation.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = mock(List.class);
        allInvocations.add(expected);
        mockingProgress.pullOngoingStubbing();
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(mockitoCore.getLastInvocation()).thenReturn(expected);
      
        Invocation actual = mockitoCore.getLastInvocation();
      
        assertSame(expected, actual);
    }
}