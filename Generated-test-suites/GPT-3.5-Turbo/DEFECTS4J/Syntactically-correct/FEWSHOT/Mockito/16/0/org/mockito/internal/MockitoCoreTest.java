package org.mockito.internal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.base.MockitoNullPointerException;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Stubber;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    private MockUtil mockUtil;
    private MockingProgress mockingProgress;
    private MockitoCore mockitoCore;
    
    @Before
    public void setUp() {
        mockUtil = new MockUtil();
        mockingProgress = new ThreadSafeMockingProgress();
        mockitoCore = new MockitoCore();
    }

    // Test the mock() method
    @Test
    public void testMock() {
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = Mockito.withSettings();
        
        Object result = mockitoCore.mock(classToMock, mockSettings);
        
        assertTrue(mockUtil.isMock(result));
    }
    
    // Test the mock() method with shouldResetOngoingStubbing flag set to true
    @Test
    public void testMockWithResetOngoingStubbingTrue() {
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = Mockito.withSettings();
        
        Object result = mockitoCore.mock(classToMock, mockSettings, true);
        
        assertTrue(mockUtil.isMock(result));
    }

    // Test the mock() method with shouldResetOngoingStubbing flag set to false
    @Test
    public void testMockWithResetOngoingStubbingFalse() {
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = Mockito.withSettings();
        
        Object result = mockitoCore.mock(classToMock, mockSettings, false);
        
        assertTrue(mockUtil.isMock(result));
    }
    
    // Test the mock() method with null classToMock
    @Test(expected = NullPointerException.class)
    public void testMockWithNullClassToMock() {
        Class<Object> classToMock = null;
        MockSettings mockSettings = Mockito.withSettings();
        
        mockitoCore.mock(classToMock, mockSettings);
    }
    
    // Test the mock() method with null mockSettings
    @Test(expected = NullPointerException.class)
    public void testMockWithNullMockSettings() {
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = null;
        
        mockitoCore.mock(classToMock, mockSettings);
    }

    // Test the stub() method when no ongoing stubbing is present
    @Test(expected = MockitoException.class)
    public void testStubWithNoOngoingStubbing() {
        mockitoCore.stub();
    }

    // Test the stub() method when an ongoing stubbing is present
    @Test
    public void testStubWithOngoingStubbing() {
        Stubber stubber = mockitoCore.doAnswer((InvocationOnMock invocationOnMock) -> null);
        IOngoingStubbing ongoingStubbing = new OngoingStubbingImpl<>(stubber);
        mockingProgress.verificationStarted(null);
        mockingProgress.stubbingStarted();
        mockingProgress.pushOngoingStubbing(ongoingStubbing);
        
        IOngoingStubbing result = mockitoCore.stub();
        
        assertNotNull(result);
    }

    // Test the stub() method when an ongoing stubbing is present and should reset ongoing stubbing
    @Test
    public void testStubWithOngoingStubbingAndShouldResetOngoingStubbingTrue() {
        Stubber stubber = mockitoCore.doAnswer((InvocationOnMock invocationOnMock) -> null);
        IOngoingStubbing ongoingStubbing = new OngoingStubbingImpl<>(stubber);
        mockingProgress.verificationStarted(null);
        mockingProgress.stubbingStarted();
        mockingProgress.pushOngoingStubbing(ongoingStubbing);
        
        IOngoingStubbing result = mockitoCore.stub();
        
        assertNull(result);
    }

    // Test the stub() method when an ongoing stubbing is present and should not reset ongoing stubbing
    @Test
    public void testStubWithOngoingStubbingAndShouldResetOngoingStubbingFalse() {
        Stubber stubber = mockitoCore.doAnswer((InvocationOnMock invocationOnMock) -> null);
        IOngoingStubbing ongoingStubbing = new OngoingStubbingImpl<>(stubber);
        mockingProgress.verificationStarted(null);
        mockingProgress.stubbingStarted();
        mockingProgress.pushOngoingStubbing(ongoingStubbing);
        
        IOngoingStubbing result = mockitoCore.stub();
        
        assertNull(result);
    }

    // Test the when() method
    @Test
    public void testWhen() {
        Stubber stubber = mockitoCore.doAnswer((InvocationOnMock invocationOnMock) -> null);
        IOngoingStubbing ongoingStubbing = new OngoingStubbingImpl<>(stubber);
        mockingProgress.stubbingStarted();
        mockingProgress.pushOngoingStubbing(ongoingStubbing);
        
        OngoingStubbing result = mockitoCore.when(any());
        
        assertNotNull(result);
    }
    
    // Test the verify() method with null mock
    @Test(expected = MockitoNullPointerException.class)
    public void testVerifyWithNullMock() {
        VerificationMode mode = null;
        Object mock = null;
        
        mockitoCore.verify(mock, mode);
    }
    
    // Test the verify() method with a non-mock object
    @Test(expected = MockitoException.class)
    public void testVerifyWithNonMockObject() {
        VerificationMode mode = null;
        Object mock = new Object();
        
        mockitoCore.verify(mock, mode);
    }
    
    // Test the verify() method with a mock object
    @Test
    public void testVerifyWithMockObject() {
        VerificationMode mode = null;
        Object mock = mock(Object.class);
        
        Object result = mockitoCore.verify(mock, mode);
        
        assertEquals(mock, result);
    }

    // Test the reset() method
    @Test
    public void testReset() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        
        mockitoCore.reset(mock1, mock2);
        
        verify(mockUtil).resetMock(mock1);
        verify(mockUtil).resetMock(mock2);
    }
    
    // Test the reset() method with no mocks
    @Test
    public void testResetWithNoMocks() {
        Object[] mocks = new Object[0];
        
        mockitoCore.reset(mocks);
        
        verifyNoInteractions(mockUtil);
    }

    // Test the verifyNoMoreInteractions() method
    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        
        mockitoCore.verifyNoMoreInteractions(mock1, mock2, mock3);
        
        verify(mockUtil).getMockHandler(mock1);
        verify(mockUtil).getMockHandler(mock2);
        verify(mockUtil).getMockHandler(mock3);
    }
    
    // Test the verifyNoMoreInteractions() method with null mocks
    @Test(expected = MockitoNullPointerException.class)
    public void testVerifyNoMoreInteractionsWithNullMocks() {
        Object[] mocks = null;
        
        mockitoCore.verifyNoMoreInteractions(mocks);
    }
    
    // Test the inOrder() method with null mocks
    @Test
    public void testInOrderWithNullMocks() {
        Object[] mocks = null;
        
        mockitoCore.inOrder(mocks);
        
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
    }
    
    // Test the inOrder() method with no mocks
    @Test
    public void testInOrderWithNoMocks() {
        Object[] mocks = new Object[0];
        
        mockitoCore.inOrder(mocks);
        
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
    }
    
    // Test the inOrder() method with null mock
    @Test
    public void testInOrderWithNullMock() {
        Object[] mocks = { null };
        
        mockitoCore.inOrder(mocks);
        
        verify(reporter).nullPassedWhenCreatingInOrder();
    }
    
    // Test the inOrder() method with non-mock object
    @Test
    public void testInOrderWithNonMockObject() {
        Object[] mocks = { new Object() };
        
        mockitoCore.inOrder(mocks);
        
        verify(reporter).notAMockPassedWhenCreatingInOrder();
    }
    
    // Test the inOrder() method with mock objects
    @Test
    public void testInOrderWithMockObjects() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        
        InOrder result = mockitoCore.inOrder(mock1, mock2, mock3);
        
        assertNotNull(result);
    }

    // Test the doAnswer() method
    @Test
    public void testDoAnswer() {
        Answer answer = (InvocationOnMock invocationOnMock) -> null;
        mockingProgress.stubbingStarted();
        mockingProgress.resetOngoingStubbing();
        
        Stubber result = mockitoCore.doAnswer(answer);
        
        assertNotNull(result);
    }

    // Test the stubVoid() method
    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        MockHandlerInterface handler = mockitoCore.getMockHandlerInterface(mock);
        mockingProgress.stubbingStarted();
        
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);
        
        assertEquals(handler.voidMethodStubbable(mock), result);
    }

    // Test the validateMockitoUsage() method
    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        
        verify(mockingProgress).validateState();
    }

    // Test the getLastInvocation() method
    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class));
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        
        Invocation result = mockitoCore.getLastInvocation();
        
        assertEquals(invocations.get(0), result);
    }
}