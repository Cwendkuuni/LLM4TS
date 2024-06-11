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
    
    // Test mock() method

    @Test
    public void testMock() {
        Class<T> classToMock = T.class;
        MockSettings mockSettings = mock(MockSettings.class);
        T result = mockitoCore.mock(classToMock, mockSettings);
        
        assertNotNull(result);
        verify(mockingProgress).validateState();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
    }
    
    // Test stub() method
    
    @Test
    public void testStub() {
        IOngoingStubbing result = mockitoCore.stub();
        
        assertNull(result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }
    
    // Test stub(T methodCall) method (deprecated)
    
    @Test
    public void testStubMethodCall() {
        T methodCall = mock(T.class);
        DeprecatedOngoingStubbing<T> result = mockitoCore.stub(methodCall);
        
        assertNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }
    
    // Test when(T methodCall) method
    
    @Test
    public void testWhenMethodCall() {
        T methodCall = mock(T.class);
        OngoingStubbing<T> result = mockitoCore.when(methodCall);
        
        assertNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }
    
    // Test verify(T mock, VerificationMode mode) method
    
    @Test
    public void testVerify() {
        T mock = mock(T.class);
        VerificationMode mode = mock(VerificationMode.class);
        T result = mockitoCore.verify(mock, mode);
        
        assertEquals(mock, result);
        verify(mockUtil).isMock(mock);
        verify(mockingProgress).verificationStarted(mode);
    }
    
    @Test
    public void testVerifyWithNullMock() {
        T mock = null;
        VerificationMode mode = mock(VerificationMode.class);
        mockitoCore.verify(mock, mode);
        
        verify(reporter).nullPassedToVerify();
    }
    
    @Test
    public void testVerifyWithNotAMockMock() {
        T mock = new T();
        VerificationMode mode = mock(VerificationMode.class);
        mockitoCore.verify(mock, mode);
        
        verify(mockUtil).isMock(mock);
        verify(reporter).notAMockPassedToVerify();
    }
    
    // Test reset(T... mocks) method
    
    @Test
    public void testReset() {
        T mock1 = mock(T.class);
        T mock2 = mock(T.class);
        
        mockitoCore.reset(mock1, mock2);
        
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).resetMock(mock1);
        verify(mockUtil).resetMock(mock2);
    }
    
    // Test verifyNoMoreInteractions(Object... mocks) method
    
    @Test
    public void testVerifyNoMoreInteractions() {
        T mock1 = mock(T.class);
        T mock2 = mock(T.class);
        
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
        
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
        verify(mockingProgress).validateState();
        verify(mockUtil).getMockHandler(mock1);
        verify(mockUtil).getMockHandler(mock2);
        verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
    }
    
    // Test inOrder(Object... mocks) method
    
    @Test
    public void testInOrder() {
        T mock1 = mock(T.class);
        T mock2 = mock(T.class);
        
        InOrder result = mockitoCore.inOrder(mock1, mock2);
        
        assertNotNull(result);
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter).nullPassedWhenCreatingInOrder();
        verify(mockUtil, times(2)).isMock(anyObject());
    }
    
    // Test doAnswer(Answer answer) method
    
    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        Stubber result = mockitoCore.doAnswer(answer);
        
        assertNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockitoCore, times(1)).doAnswer(answer);
    }
    
    // Test stubVoid(T mock) method
    
    @Test
    public void testStubVoid() {
        T mock = mock(T.class);
        MockHandlerInterface<T> handler = mockUtil.getMockHandler(mock);
        
        VoidMethodStubbable<T> result = mockitoCore.stubVoid(mock);
        
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(handler).voidMethodStubbable(mock);
    }
    
    // Test validateMockitoUsage() method
    
    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        
        verify(mockingProgress).validateState();
    }
    
    // Test getLastInvocation() method
    
    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = mock(List.class);
        Invocation lastInvocation = mock(Invocation.class);
        
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(allInvocations.get(allInvocations.size()-1)).thenReturn(lastInvocation);
        
        Invocation result = mockitoCore.getLastInvocation();
        
        assertEquals(lastInvocation, result);
        verify(mockingProgress).pullOngoingStubbing();
    }
}