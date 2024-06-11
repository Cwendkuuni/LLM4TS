import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.*;
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
    
    private MockitoCore mockitoCore;
    private MockSettingsImpl mockSettings;
    private MockUtil mockUtil;
    private MockingProgress mockingProgress;
    private Reporter reporter;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
        mockSettings = mock(MockSettingsImpl.class);
        mockUtil = mock(MockUtil.class);
        mockingProgress = mock(ThreadSafeMockingProgress.class);
        reporter = mock(Reporter.class);
        
        mockitoCore.reporter = reporter;
        mockitoCore.mockUtil = mockUtil;
        mockitoCore.mockingProgress = mockingProgress;
    }
    
    @Test
    public void testMockWithClassAndSettings() {
        Class<?> classToMock = Object.class;
        boolean shouldResetOngoingStubbing = true;
        
        mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        verify(mockUtil).createMock(classToMock, mockSettings);
        verify(mockingProgress).validateState();
        verify(mockingProgress).resetOngoingStubbing();
    }
    
    @Test
    public void testMockWithClassOnly() {
        Class<?> classToMock = Object.class;
        
        mockitoCore.mock(classToMock, mockSettings);
        
        verify(mockUtil).createMock(classToMock, mockSettings);
        verify(mockingProgress).validateState();
        verify(mockingProgress).resetOngoingStubbing();
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        
        mockitoCore.stub();
        
        verify(mockingProgress).pullOngoingStubbing();
        verify(stubbing).when();
    }
    
    @Test
    public void testDeprecatedStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        
        Object methodCall = new Object();
        DeprecatedOngoingStubbing<?> result = mockitoCore.stub(methodCall);
        
        verify(mockingProgress).pullOngoingStubbing();
        verify(stubbing).when();
        verify(mockingProgress).stubbingStarted();
        verify(reporter).missingMethodInvocation();
        assert(result instanceof DeprecatedOngoingStubbing);
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testWhen() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        
        Object methodCall = new Object();
        OngoingStubbing<?> result = mockitoCore.when(methodCall);
        
        verify(mockingProgress).pullOngoingStubbing();
        verify(stubbing).when();
        verify(mockingProgress).stubbingStarted();
        assert(result instanceof OngoingStubbing);
    }
    
    @Test
    public void testVerifyWithMockAndMode() {
        Object mock = mock(Object.class);
        VerificationMode mode = Mockito.times(1);
        
        mockitoCore.verify(mock, mode);
        
        verify(mockUtil).isMock(mock);
        verify(mockingProgress).verificationStarted(mode);
    }
    
    @Test
    public void testVerifyWithNullMock() {
        VerificationMode mode = Mockito.times(1);
        
        mockitoCore.verify(null, mode);
        
        verify(reporter).nullPassedToVerify();
    }
    
    @Test
    public void testVerifyWithNotAMock() {
        Object mock = new Object();
        VerificationMode mode = Mockito.times(1);
        
        mockitoCore.verify(mock, mode);
        
        verify(reporter).notAMockPassedToVerify();
    }
    
    @Test
    public void testReset() {
        Object[] mocks = { mock(Object.class), mock(Object.class) };
        
        mockitoCore.reset(mocks);
        
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil, times(2)).resetMock(any());
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = { mock(Object.class), mock(Object.class) };
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(reporter, times(2)).mocksHaveToBePassedToVerifyNoMoreInteractions();
        verify(mockingProgress).validateState();
        verify(mockUtil, times(2)).getMockHandler(any()).verifyNoMoreInteractions();
    }
    
    @Test
    public void testVerifyNoMoreInteractionsWithNullMock() {
        Object[] mocks = { null, mock(Object.class) };
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(reporter).nullPassedToVerifyNoMoreInteractions();
        verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockingProgress).validateState();
        verify(mockUtil).getMockHandler(any()).verifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        Object[] mocks = { mock(Object.class), mock(Object.class) };
        
        mockitoCore.inOrder(mocks);
        
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, times(2)).nullPassedWhenCreatingInOrder();
        verify(reporter, times(2)).notAMockPassedWhenCreatingInOrder();
        verify(mockUtil, times(2)).isMock(any());
    }
    
    @Test
    public void testDoAnswer() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        
        Answer answer = mock(Answer.class);
        mockitoCore.doAnswer(answer);
        
        verify(mockingProgress).pullOngoingStubbing();
        verify(stubbing).when();
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockitoCore).doAnswer(answer);
    }
    
    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        MockHandlerInterface<?> handler = mock(MockHandlerInterface.class);
        VoidMethodStubbable<?> result = mock(VoidMethodStubbable.class);
        
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        when(handler.voidMethodStubbable(mock)).thenReturn(result);
        
        mockitoCore.stubVoid(mock);
        
        verify(mockingProgress).stubbingStarted();
        verify(mockUtil).getMockHandler(mock);
        verify(handler).voidMethodStubbable(mock);
        assert(result instanceof VoidMethodStubbable);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);
        
        List<Invocation> allInvocations = Arrays.asList(invocation);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        
        Invocation result = mockitoCore.getLastInvocation();
        
        verify(ongoingStubbing).getRegisteredInvocations();
        verify(allInvocations).get(0);
        verify(mockingProgress).pullOngoingStubbing();
        assert(result == invocation);
    }
}