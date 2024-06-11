import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {
    
    private MockitoCore mockitoCore;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }
    
    @Test
    public void testMock() {
        Class classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettingsImpl.class);
        
        Object mock = mockitoCore.mock(classToMock, mockSettings);
        
        assertNotNull(mock);
        assertTrue(mock instanceof Object);
        verify(mockitoCore.getMockUtil()).createMock(classToMock, (MockSettingsImpl) mockSettings);
        verify(mockitoCore.getMockingProgress()).mockingStarted(mock, classToMock, mockSettings);
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockitoCore.getMockingProgress().pullOngoingStubbing()).thenReturn(stubbing);
        
        IOngoingStubbing result = mockitoCore.stub();
        
        assertEquals(stubbing, result);
        verify(mockitoCore.getMockingProgress()).pullOngoingStubbing();
        verify(mockitoCore.getMockingProgress()).reset();
        verify(mockitoCore.getReporter()).missingMethodInvocation();
    }
    
    @Test
    public void testStubMethodCall() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockitoCore.getMockingProgress().pullOngoingStubbing()).thenReturn(stubbing);
        
        Integer methodCall = 10;
        DeprecatedOngoingStubbing<Integer> result = mockitoCore.stub(methodCall);
        
        assertEquals(stubbing, result);
        verify(mockitoCore.getMockingProgress()).pullOngoingStubbing();
        verify(mockitoCore.getMockingProgress()).stubbingStarted();
    }
    
    @Test
    public void testWhen() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockitoCore.getMockingProgress().pullOngoingStubbing()).thenReturn(stubbing);
        
        Integer methodCall = 10;
        OngoingStubbing<Integer> result = mockitoCore.when(methodCall);
        
        assertEquals(stubbing, result);
        verify(mockitoCore.getMockingProgress()).pullOngoingStubbing();
        verify(mockitoCore.getMockingProgress()).stubbingStarted();
    }
    
    @Test
    public void testVerify() {
        Reporter reporter = mock(Reporter.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.setReporter(reporter);
        mockitoCore.setMockUtil(mockUtil);
        mockitoCore.setMockingProgress(mockingProgress);
        
        Object mock = mock(Object.class);
        VerificationMode mode = mock(VerificationMode.class);
        
        Object result = mockitoCore.verify(mock, mode);
        
        assertEquals(mock, result);
        verify(reporter).nullPassedToVerify();
        verify(mockUtil).isMock(mock);
        verify(mockingProgress).verificationStarted(mode);
    }
    
    @Test
    public void testReset() {
        MockUtil mockUtil = mock(MockUtil.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.setMockUtil(mockUtil);
        mockitoCore.setMockingProgress(mockingProgress);
        
        Object[] mocks = new Object[1];
        mocks[0] = mock(Object.class);
        
        mockitoCore.reset(mocks);
        
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).resetMock(mocks[0]);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        Reporter reporter = mock(Reporter.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.setReporter(reporter);
        mockitoCore.setMockUtil(mockUtil);
        mockitoCore.setMockingProgress(mockingProgress);
        
        Object[] mocks = new Object[1];
        mocks[0] = mock(Object.class);
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(mockingProgress).validateState();
        verify(mockUtil).getMockHandler(mocks[0]);
        verify(mockUtil).getMockHandler(mocks[0]);
        verify(mockUtil).getMockHandler(mocks[0]);
        verify(mockUtil).getMockHandler(mocks[0]);
        verify(mockUtil).getMockHandler(mocks[0]);
        verify(mockitoCore.getReporter()).mocksHaveToBePassedToVerifyNoMoreInteractions();
        verify(mockitoCore.getReporter()).nullPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.getReporter()).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.getReporter()).notAMockPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        Reporter reporter = mock(Reporter.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.setReporter(reporter);
        mockitoCore.setMockUtil(mockUtil);
        mockitoCore.setMockingProgress(mockingProgress);
        
        List<Object> mocks = Arrays.asList(mock(Object.class));
        InOrderContext inOrderContext = mock(InOrderContext.class);
        
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        
        verify(mockingProgress).validateState();
        verify(mockUtil).getMockHandler(mocks.get(0));
        verify(inOrderContext).getInOrder();
        verify(mockitoCore.getReporter()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore.getReporter()).nullPassedWhenCreatingInOrder();
        verify(mockitoCore.getReporter()).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testInOrder() {
        Reporter reporter = mock(Reporter.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.setReporter(reporter);
        mockitoCore.setMockUtil(mockUtil);
        
        Object[] mocks = new Object[1];
        mocks[0] = mock(Object.class);
        
        InOrder result = mockitoCore.inOrder(mocks);
        
        assertNotNull(result);
        verify(mockitoCore.getReporter()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore.getReporter()).nullPassedWhenCreatingInOrder();
        verify(mockitoCore.getMockUtil()).isMock(mocks[0]);
    }
    
    @Test
    public void testDoAnswer() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        Stubber stubber = mock(StubberImpl.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.setMockingProgress(mockingProgress);
        
        when(mockitoCore.getMockingProgress().pullOngoingStubbing()).thenReturn(stubbing);
        when(mockitoCore.getMockingProgress().resetOngoingStubbing()).thenReturn(mockingProgress);
        
        Answer answer = mock(Answer.class);
        Stubber result = mockitoCore.doAnswer(answer);
        
        assertEquals(stubber, result);
        verify(mockitoCore.getMockingProgress()).stubbingStarted();
        verify(mockitoCore.getMockingProgress()).resetOngoingStubbing();
        verify(stubber).doAnswer(answer);
    }
    
    @Test
    public void testStubVoid() {
        MockUtil mockUtil = mock(MockUtil.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.setMockUtil(mockUtil);
        
        Object mock = mock(Object.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        VoidMethodStubbable stubbable = mock(VoidMethodStubbable.class);
        
        when(mockitoCore.getMockUtil().getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.voidMethodStubbable(mock)).thenReturn(stubbable);
        
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);
        
        assertEquals(stubbable, result);
        verify(mockitoCore.getMockingProgress()).stubbingStarted();
        verify(mockitoCore.getMockUtil()).getMockHandler(mock);
        verify(mockHandler).voidMethodStubbable(mock);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.setMockingProgress(mockingProgress);
        
        mockitoCore.validateMockitoUsage();
        
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.setMockingProgress(mockingProgress);
        
        Invocation invocation = mock(Invocation.class);
        List<Invocation> allInvocations = Arrays.asList(invocation);
        
        when(mockitoCore.getMockingProgress().pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        
        Invocation result = mockitoCore.getLastInvocation();
        
        assertEquals(invocation, result);
        verify(mockitoCore.getMockingProgress()).pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
    }
}