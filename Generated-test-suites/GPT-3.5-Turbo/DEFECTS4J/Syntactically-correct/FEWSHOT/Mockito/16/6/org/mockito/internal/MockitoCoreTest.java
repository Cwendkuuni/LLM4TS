package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
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

public class MockitoCoreTest {
    private MockitoCore mockitoCore = new MockitoCore();
    
    @Test
    public void testMock() {
        MockSettings mockSettings = mock(MockSettingsImpl.class);
        assertEquals("mockObject", mockitoCore.mock(MockObject.class, mockSettings));
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        Reporter reporter = mock(Reporter.class);
        
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        mockitoCore.stub();
        
        verify(mockingProgress, times(1)).pullOngoingStubbing();
        verify(mockingProgress, times(1)).reset();
        verify(reporter, times(0)).missingMethodInvocation();
    }
    
    @Test
    public void testDeprecatedStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        Reporter reporter = mock(Reporter.class);
        
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        mockitoCore.stub(new MockObject());
        
        verify(mockingProgress, times(1)).pullOngoingStubbing();
        verify(mockingProgress, times(1)).reset();
        verify(reporter, times(0)).missingMethodInvocation();
    }
    
    @Test
    public void testWhen() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        Reporter reporter = mock(Reporter.class);
        
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        mockitoCore.when(new MockObject());
        
        verify(mockingProgress, times(1)).pullOngoingStubbing();
        verify(mockingProgress, times(1)).reset();
        verify(reporter, times(0)).missingMethodInvocation();
    }
    
    @Test
    public void testVerify() {
        MockObject mock = mock(MockObject.class);
        VerificationMode mode = mock(VerificationMode.class);
        MockUtil mockUtil = mock(MockUtil.class);
        
        when(mockUtil.isMock(mock)).thenReturn(true);
        assertEquals(mock, mockitoCore.verify(mock, mode));
        
        verify(mockUtil, times(1)).isMock(mock);
    }
    
    @Test
    public void testReset() {
        MockObject mock1 = mock(MockObject.class);
        MockObject mock2 = mock(MockObject.class);
        MockUtil mockUtil = mock(MockUtil.class);
        
        mockitoCore.reset(mock1, mock2);
        
        verify(mockUtil, times(1)).resetMock(mock1);
        verify(mockUtil, times(1)).resetMock(mock2);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        MockObject mock = mock(MockObject.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockHandlerInterface handler = mock(MockHandlerInterface.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        when(handler.getRegisteredInvocations()).thenReturn(Arrays.asList(new Invocation()));
        
        mockitoCore.verifyNoMoreInteractions(mock);
        
        verify(mockUtil, times(1)).getMockHandler(mock);
        verify(handler, times(1)).verifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        MockObject mock1 = mock(MockObject.class);
        MockObject mock2 = mock(MockObject.class);
        MockUtil mockUtil = mock(MockUtil.class);
        
        when(mockUtil.isMock(mock1)).thenReturn(true);
        when(mockUtil.isMock(mock2)).thenReturn(true);
        
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        
        assertEquals(mock1, inOrder.getMocks().get(0));
        assertEquals(mock2, inOrder.getMocks().get(1));
        
        verify(mockUtil, times(2)).isMock(anyObject());
    }
    
    @Test
    public void testDoAnswer() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        StubberImpl stubber = mock(StubberImpl.class);
        
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        when(mockitoCore.doAnswer(any(Answer.class))).thenReturn(stubber);
        
        mockitoCore.doAnswer(mock(Answer.class));
        
        verify(mockingProgress, times(1)).pullOngoingStubbing();
        verify(mockingProgress, times(1)).resetOngoingStubbing();
    }
    
    @Test
    public void testStubVoid() {
        MockObject mock = mock(MockObject.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockHandlerInterface handler = mock(MockHandlerInterface.class);
        
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        
        mockitoCore.stubVoid(mock);
        
        verify(mockUtil, times(1)).getMockHandler(mock);
        verify(handler, times(1)).voidMethodStubbable(mock);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        
        mockitoCore.validateMockitoUsage();
        
        verify(mockingProgress, times(1)).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class));
        
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        
        assertEquals(invocations.get(0), mockitoCore.getLastInvocation());
        
        verify(mockingProgress, times(1)).pullOngoingStubbing();
        verify(ongoingStubbing, times(1)).getRegisteredInvocations();
    }
}