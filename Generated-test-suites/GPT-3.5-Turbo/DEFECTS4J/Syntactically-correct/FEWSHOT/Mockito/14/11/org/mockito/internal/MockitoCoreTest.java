//MockitoCore Test Cases
package org.mockito.internal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {
    private MockitoCore mockitoCore;
    private Reporter reporter;
    private MockUtil mockUtil;
    private MockingProgress mockingProgress;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
        reporter = new Reporter();
        mockUtil = new MockUtil();
        mockingProgress = new ThreadSafeMockingProgress();
    }
    
    @After
    public void tearDown() {
        mockitoCore = null;
        reporter = null;
        mockUtil = null;
        mockingProgress = null;
    }
    
    @Test
    public void testMock() {
        Class classToMock = String.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        Object mock = mockitoCore.mock(classToMock, mockSettings);
        
        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
        verify(mockingProgress).mockingStarted(mock, classToMock, mockSettings);
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        mockingProgress.stubbingStarted();
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        
        IOngoingStubbing result = mockitoCore.stub();
        
        assertNotNull(result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter, never()).missingMethodInvocation();
    }
    
    @Test
    public void testStub_whenNoStubbing() {
        mockingProgress.stubbingStarted();
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        
        IOngoingStubbing result = mockitoCore.stub();
        
        assertNull(result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }
    
    @Test
    public void testStub_T() {
        Object methodCall = new Object();
        mockingProgress.stubbingStarted();
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        
        DeprecatedOngoingStubbing result = mockitoCore.stub(methodCall);
        
        assertNotNull(result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }
    
    @Test
    public void testWhen() {
        Object methodCall = new Object();
        mockingProgress.stubbingStarted();
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        
        OngoingStubbing result = mockitoCore.when(methodCall);
        
        assertNotNull(result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }
    
    @Test
    public void testVerify() {
        Object mock = Mockito.mock(String.class);
        VerificationMode mode = VerificationModeFactory.times(1);
        
        Object result = mockitoCore.verify(mock, mode);
        
        assertEquals(mock, result);
        verify(reporter, never()).nullPassedToVerify();
        verify(reporter, never()).notAMockPassedToVerify();
        verify(mockingProgress).verificationStarted(mode);
    }
    
    @Test
    public void testVerify_withNullMock() {
        VerificationMode mode = VerificationModeFactory.times(1);
        
        Object result = mockitoCore.verify(null, mode);
        
        assertNull(result);
        verify(reporter).nullPassedToVerify();
        verify(reporter, never()).notAMockPassedToVerify();
        verify(mockingProgress).verificationStarted(mode);
    }
    
    @Test
    public void testVerify_withNotAMock() {
        Object mock = new Object();
        VerificationMode mode = VerificationModeFactory.times(1);
        
        Object result = mockitoCore.verify(mock, mode);
        
        assertEquals(mock, result);
        verify(reporter, never()).nullPassedToVerify();
        verify(reporter).notAMockPassedToVerify();
        verify(mockingProgress).verificationStarted(mode);
    }
    
    @Test
    public void testReset() {
        Object[] mocks = new Object[]{Mockito.mock(String.class), Mockito.mock(List.class)};
        
        mockitoCore.reset(mocks);
        
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).resetMock(mocks[0]);
        verify(mockUtil).resetMock(mocks[1]);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(List.class);
        Object[] mocks = new Object[]{mock1, mock2};
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(mockingProgress).validateState();
        verify(reporter, never()).nullPassedToVerifyNoMoreInteractions();
        verify(reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockUtil).getMockHandler(mock1);
        verify(mockUtil).getMockHandler(mock2);
    }
    
    @Test
    public void testVerifyNoMoreInteractions_withNullMock() {
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = null;
        Object[] mocks = new Object[]{mock1, mock2};
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(mockingProgress).validateState();
        verify(reporter).nullPassedToVerifyNoMoreInteractions();
        verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockUtil).getMockHandler(mock1);
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Arrays.asList(Mockito.mock(String.class), Mockito.mock(List.class));
        InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);
        
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        
        verify(mockingProgress).validateState();
        verify(mockUtil).getMockHandler(mocks.get(0));
        verify(mockUtil).getMockHandler(mocks.get(1));
    }
    
    @Test
    public void testAssertMocksNotEmpty_withEmptyMocks() {
        Object[] mocks = new Object[]{};
        
        mockitoCore.assertMocksNotEmpty(mocks);
        
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testAssertMocksNotEmpty_withNullMocks() {
        Object[] mocks = null;
        
        mockitoCore.assertMocksNotEmpty(mocks);
        
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(List.class);
        
        InOrder result = mockitoCore.inOrder(mock1, mock2);
        
        assertNotNull(result);
        verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(mockUtil).isMock(mock1);
        verify(mockUtil).isMock(mock2);
    }
    
    @Test
    public void testInOrder_withEmptyMocks() {
        Object[] mocks = new Object[]{};
        
        InOrder result = mockitoCore.inOrder(mocks);
        
        assertNotNull(result);
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
    }
    
    @Test
    public void testInOrder_withNullMock() {
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = null;
        
        InOrder result = mockitoCore.inOrder(mock1, mock2);
        
        assertNotNull(result);
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter).nullPassedWhenCreatingInOrder();
        verify(mockUtil).isMock(mock1);
    }
    
    @Test
    public void testInOrder_withNotAMock() {
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = new Object();
        
        InOrder result = mockitoCore.inOrder(mock1, mock2);
        
        assertNotNull(result);
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter).notAMockPassedWhenCreatingInOrder();
        verify(mockUtil).isMock(mock1);
    }
    
    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        mockingProgress.stubbingStarted();
        mockingProgress.resetOngoingStubbing();
        
        Stubber result = mockitoCore.doAnswer(answer);
        
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
    }
    
    @Test
    public void testStubVoid() {
        Object mock = Mockito.mock(String.class);
        MockHandlerInterface handler = mock(MockHandlerInterface.class);
        mockingProgress.stubbingStarted();
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);
        
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockUtil).getMockHandler(mock);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        Invocation invocation = mock(Invocation.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = Arrays.asList(invocation);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        
        Invocation result = mockitoCore.getLastInvocation();
        
        assertEquals(invocation, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
    }
}