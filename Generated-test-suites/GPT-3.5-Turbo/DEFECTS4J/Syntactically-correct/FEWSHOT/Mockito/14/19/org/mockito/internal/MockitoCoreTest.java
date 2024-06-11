
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

public class MockitoCoreTest {
    private MockitoCore mockitoCore = new MockitoCore();

    @Test
    public void testMock() {
        Class<String> classToMock = String.class;
        MockSettings mockSettings = mock(MockSettings.class);
        
        String result = mockitoCore.mock(classToMock, mockSettings);
        
        verify(mockitoCore.getMockUtil()).createMock(classToMock, (MockSettingsImpl) mockSettings);
        verify(mockitoCore.getMockingProgress()).mockingStarted(result, classToMock, mockSettings);
        assertEquals(result, mockitoCore.mock(classToMock, mockSettings));
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        mockitoCore.getMockingProgress().pushOngoingStubbing(stubbing);
        
        IOngoingStubbing result = mockitoCore.stub();
        
        verify(mockitoCore.getMockingProgress()).pullOngoingStubbing();
        verify(mockitoCore.getMockingProgress()).reset();
        verify(mockitoCore.getReporter()).missingMethodInvocation();
        assertEquals(result, stubbing);
    }
    
    @Test
    public void testStub_withMethodCall() {
        Object methodCall = new Object();
        OngoingStubbingImpl ongoingStubbingImpl = mock(OngoingStubbingImpl.class);
        mockitoCore.getMockingProgress().pushOngoingStubbing(ongoingStubbingImpl);
        
        DeprecatedOngoingStubbing<Object> result = mockitoCore.stub(methodCall);
        
        verify(mockitoCore.getMockingProgress()).stubbingStarted();
        verify(mockitoCore.getMockingProgress()).pullOngoingStubbing();
        verify(ongoingStubbingImpl).getMock();
        assertEquals(result, ongoingStubbingImpl);
    }
    
    @Test
    public void testWhen() {
        Object methodCall = new Object();
        OngoingStubbingImpl ongoingStubbingImpl = mock(OngoingStubbingImpl.class);
        mockitoCore.getMockingProgress().pushOngoingStubbing(ongoingStubbingImpl);
        
        OngoingStubbing<Object> result = mockitoCore.when(methodCall);
        
        verify(mockitoCore.getMockingProgress()).stubbingStarted();
        verify(mockitoCore.getMockingProgress()).pullOngoingStubbing();
        verify(ongoingStubbingImpl).getMock();
        assertEquals(result, ongoingStubbingImpl);
    }
    
    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        VerificationMode mode = mock(VerificationMode.class);
        
        Object result = mockitoCore.verify(mock, mode);
        
        verify(mockitoCore.getReporter()).nullPassedToVerify();
        verify(mockitoCore.getMockUtil()).isMock(mock);
        verify(mockitoCore.getMockingProgress()).verificationStarted(mode);
        assertEquals(result, mock);
    }
    
    @Test
    public void testReset() {
        Object[] mocks = {mock(Object.class)};
        MockUtil mockUtil = mock(MockUtil.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        mockitoCore.setMockUtil(mockUtil);
        mockitoCore.setMockingProgress(mockingProgress);
        
        mockitoCore.reset(mocks);
        
        for (Object m : mocks) {
            verify(mockitoCore.getMockUtil()).resetMock(m);
        }
        verify(mockitoCore.getMockingProgress()).validateState();
        verify(mockitoCore.getMockingProgress()).reset();
        verify(mockitoCore.getMockingProgress()).resetOngoingStubbing();
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = {mock(Object.class)};
        Invocation invocation = mock(Invocation.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        mockitoCore.setMockUtil(mockUtil);
        mockitoCore.setMockingProgress(mockingProgress);
        when(mockUtil.getMockHandler(mocks[0])).thenReturn(invocation);
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        for (Object mock : mocks) {
            verify(mockitoCore).assertMocksNotEmpty(mocks);
            verify(mockitoCore.getReporter()).nullPassedToVerifyNoMoreInteractions();
            verify(mockUtil.getMockHandler(mock)).getInvocationContainer().getInvocations();
            verify(mockitoCore.getVerificationModeFactory()).noMoreInteractions();
            verify(mockitoCore.getVerificationModeFactory().noMoreInteractions()).verify(any());
        }
        verify(mockitoCore.getMockingProgress()).validateState();
        verify(mockitoCore.getMockingProgress()).resetOngoingStubbing();
        verifyZeroInteractions(mockitoCore.getMockingProgress());
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Arrays.asList(mock(Object.class));
        InOrderContext inOrderContext = mock(InOrderContext.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        mockitoCore.setMockingProgress(mockingProgress);
        
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        
        verify(mockitoCore.getMockingProgress()).validateState();
        verify(mockitoCore.getVerificationDataInOrderFactory().noMoreInteractions()).verifyInOrder(any());
    }
    
    @Test
    public void testAssertMocksNotEmpty() {
        Object[] mocks = {};
        
        mockitoCore.assertMocksNotEmpty(mocks);
        
        verify(mockitoCore.getReporter()).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        Object[] mocks = {mock(Object.class)};
        MockUtil mockUtil = mock(MockUtil.class);
        mockitoCore.setMockUtil(mockUtil);
        for (Object mock : mocks) {
            when(mockUtil.isMock(mock)).thenReturn(true);
        }
        
        InOrder result = mockitoCore.inOrder(mocks);
        
        verify(mockitoCore.getReporter()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore).assertMocksNotEmpty(mocks);
        for (Object mock : mocks) {
            verify(mockitoCore.getReporter()).nullPassedWhenCreatingInOrder();
            verify(mockitoCore.getReporter()).notAMockPassedWhenCreatingInOrder();
        }
        assertEquals(result, new InOrderImpl(Arrays.asList(mocks)));
    }
    
    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        Stubber stubber = mock(Stubber.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        mockitoCore.setMockingProgress(mockingProgress);
        when(stubber.doAnswer(answer)).thenReturn(stubber);
        
        Stubber result = mockitoCore.doAnswer(answer);
        
        verify(mockitoCore.getMockingProgress()).stubbingStarted();
        verify(mockitoCore.getMockingProgress()).resetOngoingStubbing();
        assertEquals(result, stubber);
    }
    
    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        MockHandlerInterface<Object> handler = mock(MockHandlerInterface.class);
        mockingProgress.stubbingStarted();
        when(handler.voidMethodStubbable(mock)).thenReturn(mock(Answer.class));
        
        VoidMethodStubbable<Object> result = mockitoCore.stubVoid(mock);
        
        verify(handler).voidMethodStubbable(mock);
        assertEquals(result, handler.voidMethodStubbable(mock));
    }
    
    @Test
    public void testValidateMockitoUsage() {
        mockingProgress.validateState();
                
        mockitoCore.validateMockitoUsage();
        
        verify(mockitoCore.getMockingProgress()).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        Invocation invocation = mock(Invocation.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = Arrays.asList(invocation);
        mockingProgress.pushOngoingStubbing(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        
        Invocation result = mockitoCore.getLastInvocation();
        
        assertEquals(result, invocations.get(invocations.size() - 1));
    }
}
