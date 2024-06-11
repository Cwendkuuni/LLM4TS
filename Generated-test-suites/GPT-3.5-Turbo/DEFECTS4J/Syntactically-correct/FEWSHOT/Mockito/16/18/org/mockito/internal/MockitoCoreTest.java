
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.mockito.InOrder;
import org.mockito.MockSettings;
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
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.*;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class MockitoCoreTest {
    
    @Test
    public void testMock() {
        MockUtil mockUtil = mock(MockUtil.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore(reporter, mockUtil, mockingProgress);
        
        Class<?> classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettingsImpl.class);
        boolean shouldResetOngoingStubbing = false;
        
        Object result = new Object();
        when(mockUtil.createMock(classToMock, (MockSettingsImpl) mockSettings)).thenReturn(result);
        
        Object actual = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        assertEquals(result, actual);
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
        verify(mockingProgress).validateState();
        verify(mockingProgress).resetOngoingStubbing();
    }
    
    @Test
    public void testStub() {
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore(reporter, mockUtil, mockingProgress);
        
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        
        IOngoingStubbing actual = mockitoCore.stub();
        
        assertEquals(stubbing, actual);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }
    
    @Test
    public void testStub_WithDeprecated() {
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore(reporter, mockUtil, mockingProgress);
        
        Object methodCall = new Object();
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        
        DeprecatedOngoingStubbing<?> actual = mockitoCore.stub(methodCall);
        
        assertEquals(stubbing, actual);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }
    
    @Test
    public void testWhen() {
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore(reporter, mockUtil, mockingProgress);
        
        Object methodCall = new Object();
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        
        OngoingStubbing<?> actual = mockitoCore.when(methodCall);
        
        assertEquals(stubbing, actual);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }
    
    @Test
    public void testVerify() {
        MockUtil mockUtil = mock(MockUtil.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore(reporter, mockUtil, mockingProgress);
        
        Object mock = mock(Object.class);
        VerificationMode mode = VerificationMode.times(1);
        
        Object actual = mockitoCore.verify(mock, mode);
        
        assertEquals(mock, actual);
        verify(mockUtil).isMock(mock);
        verify(mockingProgress).verificationStarted(mode);
        verify(reporter).nullPassedToVerify();
        verify(reporter).notAMockPassedToVerify();
    }
    
    @Test
    public void testReset() {
        MockUtil mockUtil = mock(MockUtil.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore(reporter, mockUtil, mockingProgress);
        
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);
        
        mockitoCore.reset(mocks);
        
        verify(mockUtil).resetMock(mocks[0]);
        verify(mockUtil).resetMock(mocks[1]);
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        MockUtil mockUtil = mock(MockUtil.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore(reporter, mockUtil, mockingProgress);
        
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
        verify(mockingProgress).validateState();
        verify(mockUtil).getMockHandler(mocks[0]);
        verify(mockUtil).getMockHandler(mocks[1]);
        verify(mockUtil).getMockHandler(null);
        verify(reporter).nullPassedToVerifyNoMoreInteractions();
        verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        MockUtil mockUtil = mock(MockUtil.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore(reporter, mockUtil, mockingProgress);
        
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);
        
        InOrder actual = mockitoCore.inOrder(mocks);
        
        assertEquals(mocks, actual.getMocks());
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter).nullPassedWhenCreatingInOrder();
        verify(mockUtil).isMock(mocks[0]);
        verify(mockUtil).isMock(mocks[1]);
        verify(reporter).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testDoAnswer() {
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore(reporter, mockUtil, mockingProgress);
        
        Answer<?> answer = mock(Answer.class);
        Stubber stubber = mock(Stubber.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        when(mockitoCore.stub()).thenReturn(stubber);
        
        Stubber actual = mockitoCore.doAnswer(answer);
        
        assertEquals(stubber, actual);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockitoCore).stub();
    }
    
    @Test
    public void testStubVoid() {
        MockUtil mockUtil = mock(MockUtil.class);
        MockitoCore mockitoCore = new MockitoCore(reporter, mockUtil, mockingProgress);
        
        Object mock = mock(Object.class);
        MockHandlerInterface<?> handler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        
        VoidMethodStubbable<?> actual = mockitoCore.stubVoid(mock);
        
        assertEquals(handler.voidMethodStubbable(mock), actual);
        verify(mockUtil).getMockHandler(mock);
        verify(mockingProgress).stubbingStarted();
    }
    
    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore(reporter, mockUtil, mockingProgress);
        
        mockitoCore.validateMockitoUsage();
        
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore(reporter, mockUtil, mockingProgress);
        
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);
        List<Invocation> allInvocations = Arrays.asList(invocation);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        
        Invocation actual = mockitoCore.getLastInvocation();
        
        assertEquals(invocation, actual);
        verify(mockingProgress).pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
    }
}

