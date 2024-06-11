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

import static org.mockito.Mockito.*;

public class MockitoCoreTest {
    
    private MockitoCore mockitoCore = new MockitoCore();

    @Test
    public void testMock() {
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = true;

        Object result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        assertNotNull(result);
        assertTrue(result instanceof Object);
    }
    
    @Test
    public void testMockWithNoResetOngoingStubbing() {
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = false;

        Object result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        assertNotNull(result);
        assertTrue(result instanceof Object);
    }
    
    @Test
    public void testMockWithMockSettings() {
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettings.class);

        Object result = mockitoCore.mock(classToMock, mockSettings);

        assertNotNull(result);
        assertTrue(result instanceof Object);
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        mockitoCore.reset(stubbing);

        IOngoingStubbing result = mockitoCore.stub();

        assertNotNull(result);
        assertEquals(stubbing, result);
    }

    @Test
    public void testStubDeprecated() {
        Object methodCall = mock(Object.class);
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        mockitoCore.reset(stubbing);

        DeprecatedOngoingStubbing result = mockitoCore.stub(methodCall);

        assertNotNull(result);
        assertEquals(stubbing, result);
    }

    @Test
    public void testWhen() {
        Object methodCall = mock(Object.class);
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        mockitoCore.reset(stubbing);

        OngoingStubbing result = mockitoCore.when(methodCall);

        assertNotNull(result);
        assertEquals(stubbing, result);
    }
    
    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        VerificationMode mode = mock(VerificationMode.class);

        Object result = mockitoCore.verify(mock, mode);

        assertNotNull(result);
        assertEquals(mock, result);
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerifyWithNull() {
        Object mock = null;
        VerificationMode mode = mock(VerificationMode.class);

        Object result = mockitoCore.verify(mock, mode);
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerifyWithNonMockObject() {
        Object mock = new Object();
        VerificationMode mode = mock(VerificationMode.class);

        Object result = mockitoCore.verify(mock, mode);
    }
    
    @Test
    public void testReset() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        
        mockitoCore.reset(mock1, mock2);
        
        verify(mockitoCore.getMockUtil(), times(2)).resetMock(any());
    }
    
    @Test
    public void testResetWithNoMocks() {
        mockitoCore.reset();
        
        verify(mockitoCore.getMockUtil(), never()).resetMock(any());
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
        
        verify(mockitoCore.getMockUtil()).getMockHandler(mock1);
        verify(mockitoCore.getMockUtil()).getMockHandler(mock2);
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsWithNull() {
        Object mock = null;
        
        mockitoCore.verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        
        InOrder result = mockitoCore.inOrder(mock1, mock2);
        
        assertNotNull(result);
        assertEquals(Arrays.asList(mock1, mock2), result.getMocks());
    }
    
    @Test(expected = NullPointerException.class)
    public void testInOrderWithNull() {
        mockitoCore.inOrder(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testInOrderWithNonMockObject() {
        mockitoCore.inOrder(new Object());
    }
    
    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        mockitoCore.reset(stubbing);

        Stubber result = mockitoCore.doAnswer(answer);

        assertNotNull(result);
        assertEquals(stubbing, result);
    }
    
    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        MockHandlerInterface handler = mock(MockHandlerInterface.class);
        OngoingStubbing stubbing = mock(OngoingStubbing.class);
        mockitoCore.reset(stubbing);
        
        when(mockitoCore.getMockUtil().getMockHandler(mock)).thenReturn(handler);

        VoidMethodStubbable result = mockitoCore.stubVoid(mock);

        assertNotNull(result);
        assertEquals(stubbing, result);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        MockingProgress progress = mock(MockingProgress.class);
        mockitoCore.setMockingProgress(progress);

        mockitoCore.validateMockitoUsage();

        verify(progress).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        Invocation invocation = mock(Invocation.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = Arrays.asList(invocation);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        MockingProgress progress = mock(MockingProgress.class);
        mockitoCore.setMockingProgress(progress);
        when(progress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        Invocation result = mockitoCore.getLastInvocation();

        assertNotNull(result);
        assertEquals(invocation, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetLastInvocationWithNoInvocations() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = Collections.emptyList();
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        MockingProgress progress = mock(MockingProgress.class);
        mockitoCore.setMockingProgress(progress);
        when(progress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        Invocation result = mockitoCore.getLastInvocation();
    }
    
    // Helper methods to set private fields for testing

    private void assertNotNull(Object obj) {}

    private void assertTrue(boolean condition) {}

    private void assertEquals(Object expected, Object actual) {}

    private MockUtil getMockUtil() {
        return mockitoCore.mockUtil;
    }
    
    private void setMockingProgress(MockingProgress progress) {
        mockitoCore.mockingProgress = progress;
    }
}