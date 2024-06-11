import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class MockitoCoreTest {
    @Test
    public void testMock() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        // Test with shouldResetOngoingStubbing = true
        assertEquals(5, mockitoCore.mock(String.class, mock(MockSettings.class), true));
        
        // Test with shouldResetOngoingStubbing = false
        assertEquals(10, mockitoCore.mock(Integer.class, mock(MockSettings.class), false));
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        // Test when IOngoingStubbing is null
        when(mockitoCore.stub()).thenReturn(mock(IOngoingStubbing.class));
        
        // Test when IOngoingStubbing is not null
        mockitoCore.stub();
        
        verify(mockitoCore, times(2)).stub();
    }
    
    @Deprecated
    @Test
    public void testDeprecatedStub() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        // Test when DeprecatedOngoingStubbing is null
        when(mockitoCore.stub(any())).thenReturn(mock(DeprecatedOngoingStubbing.class));
        
        // Test when DeprecatedOngoingStubbing is not null
        mockitoCore.stub("");
        
        verify(mockitoCore, times(2)).stub(any());
    }
    
    @Test
    public void testWhen() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        // Test when OngoingStubbing is null
        when(mockitoCore.when(any())).thenReturn(mock(OngoingStubbing.class));
        
        // Test when OngoingStubbing is not null
        mockitoCore.when("");
        
        verify(mockitoCore, times(2)).when(any());
    }
    
    @Test
    public void testVerify() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object mock = mock(Object.class);
        VerificationMode mode = mock(VerificationMode.class);
        
        // Test when mock is null
        mockitoCore.verify(null, mode);
        
        // Test when mock is not a mock object
        mockitoCore.verify(new Object(), mode);
        
        // Test when mock is a mock object
        mockitoCore.verify(mock, mode);
        
        verify(mockitoCore, times(3)).verify(any(), any());
    }
    
    @Test
    public void testReset() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object[] mocks = { mock(Object.class), mock(Object.class) };
        
        mockitoCore.reset(mocks);

        verify(mockitoCore).reset(mocks);
        verify(mockitoCore).mockUtil.resetMock(mocks[0]);
        verify(mockitoCore).mockUtil.resetMock(mocks[1]);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object[] mocks = { mock(Object.class), mock(Object.class) };
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(mockitoCore).verifyNoMoreInteractions(mocks);
        verify(mockitoCore).mockUtil.getMockHandler(mocks[0]).verifyNoMoreInteractions();
        verify(mockitoCore).mockUtil.getMockHandler(mocks[1]).verifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object[] mocks = { mock(Object.class), mock(Object.class) };
        
        // Test with null mocks
        mockitoCore.inOrder();
        
        // Test with empty mocks
        mockitoCore.inOrder(new Object[0]);
        
        // Test with not a mock object
        mockitoCore.inOrder(new Object());
        
        // Test with mocks
        mockitoCore.inOrder(mocks);
        
        verify(mockitoCore, times(4)).inOrder(any());
    }
    
    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Answer answer = mock(Answer.class);
        
        mockitoCore.doAnswer(answer);
        
        verify(mockitoCore).doAnswer(answer);
        verify(mockitoCore).mockingProgress.stubbingStarted();
        verify(mockitoCore).mockingProgress.resetOngoingStubbing();
        verify(mockitoCore).doAnswer(any());
    }
    
    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object mock = mock(Object.class);
        VoidMethodStubbable voidMethodStubbable = mock(VoidMethodStubbable.class);
        
        when(mockitoCore.stubVoid(mock)).thenReturn(voidMethodStubbable);
        
        mockitoCore.stubVoid(mock);
        
        verify(mockitoCore, times(2)).stubVoid(mock);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        mockitoCore.validateMockitoUsage();
        
        verify(mockitoCore).validateMockitoUsage();
    }
    
    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);
        
        when(mockitoCore.getLastInvocation()).thenReturn(invocation);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(Arrays.asList(invocation));
        
        assertEquals(invocation, mockitoCore.getLastInvocation());
        
        verify(mockitoCore).getLastInvocation();
        verify(mockitoCore).mockingProgress.pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
    }
}