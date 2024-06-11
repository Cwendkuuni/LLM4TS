import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MockitoCoreTest {

    @Test
    public void testMock() {
        // Create a MockitoCore instance
        MockitoCore mockitoCore = new MockitoCore();
        
        // Call the mock method
        MyInterface mock = mockitoCore.mock(MyInterface.class, mockSettings);
        
        // Verify that the returned object is not null
        assertNotNull(mock);
        
        // Verify that the mockingProgress.mockingStarted method was called
        verify(mockitoCore.mockingProgress).mockingStarted(mock, MyInterface.class, mockSettings);
    }
    
    @Test
    public void testStub() {
        // Create a MockitoCore instance
        MockitoCore mockitoCore = new MockitoCore();
        
        // Call the stub method
        IOngoingStubbing stubbing = mockitoCore.stub();
        
        // Verify that the returned object is not null
        assertNotNull(stubbing);
        
        // Verify that the mockingProgress.pullOngoingStubbing method was called
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
        
        // Verify that the mockingProgress.reset method was called
        verify(mockitoCore.mockingProgress).reset();
        
        // Verify that the reporter.missingMethodInvocation method was called
        verify(mockitoCore.reporter).missingMethodInvocation();
    }
    
    @Test
    public void testStub_withMethodCall() {
        // Create a MockitoCore instance
        MockitoCore mockitoCore = new MockitoCore();
        
        // Create a method call
        MyInterface methodCall = mock(MyInterface.class);
        
        // Call the stub method with the method call
        DeprecatedOngoingStubbing stubbing = mockitoCore.stub(methodCall);
        
        // Verify that the returned object is not null
        assertNotNull(stubbing);
        
        // Verify that the mockingProgress.stubbingStarted method was called
        verify(mockitoCore.mockingProgress).stubbingStarted();
        
        // Verify that the mockingProgress.pullOngoingStubbing method was called
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
    }
    
    @Test
    public void testWhen() {
        // Create a MockitoCore instance
        MockitoCore mockitoCore = new MockitoCore();
        
        // Create a method call
        MyInterface methodCall = mock(MyInterface.class);
        
        // Call the when method with the method call
        OngoingStubbing stubbing = mockitoCore.when(methodCall);
        
        // Verify that the returned object is not null
        assertNotNull(stubbing);
        
        // Verify that the mockingProgress.stubbingStarted method was called
        verify(mockitoCore.mockingProgress).stubbingStarted();
        
        // Verify that the mockingProgress.pullOngoingStubbing method was called
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
    }
    
    @Test
    public void testVerify() {
        // Create a MockitoCore instance
        MockitoCore mockitoCore = new MockitoCore();
        
        // Create a mock object
        MyInterface mock = mock(MyInterface.class);
        
        // Create a verification mode
        VerificationMode mode = VerificationModeFactory.times(1);
        
        // Call the verify method
        MyInterface result = mockitoCore.verify(mock, mode);
        
        // Verify that the returned object is the same as the passed in mock object
        assertSame(mock, result);
        
        // Verify that the reporter.nullPassedToVerify method was not called
        verify(mockitoCore.reporter, never()).nullPassedToVerify();
        
        // Verify that the mockUtil.isMock method was called
        verify(mockitoCore.mockUtil).isMock(mock);
        
        // Verify that the mockingProgress.verificationStarted method was called
        verify(mockitoCore.mockingProgress).verificationStarted(mode);
    }
    
    @Test
    public void testReset() {
        // Create a MockitoCore instance
        MockitoCore mockitoCore = new MockitoCore();
        
        // Create mock objects
        MyInterface mock1, mock2;
        
        // Call the reset method with the mock objects
        mockitoCore.reset(mock1, mock2);
        
        // Verify that the mockingProgress.validateState method was called
        verify(mockitoCore.mockingProgress).validateState();
        
        // Verify that the mockingProgress.reset method was called
        verify(mockitoCore.mockingProgress).reset();
        
        // Verify that the mockingProgress.resetOngoingStubbing method was called
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
        
        // Verify that the mockUtil.resetMock method was called for each mock object
        verify(mockitoCore.mockUtil).resetMock(mock1);
        verify(mockitoCore.mockUtil).resetMock(mock2);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        // Create a MockitoCore instance
        MockitoCore mockitoCore = new MockitoCore();
        
        // Create mock objects
        MyInterface mock1, mock2;
        
        // Call the verifyNoMoreInteractions method with the mock objects
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
        
        // Verify that the assertMocksNotEmpty method was called
        verify(mockitoCore).assertMocksNotEmpty(any(Object[].class));
        
        // Verify that the mockingProgress.validateState method was called
        verify(mockitoCore.mockingProgress).validateState();
        
        // Verify that the reporter.nullPassedToVerifyNoMoreInteractions method was not called
        verify(mockitoCore.reporter, never()).nullPassedToVerifyNoMoreInteractions();
        
        // Verify that the mockUtil.getMockHandler method was called for each mock object
        verify(mockitoCore.mockUtil).getMockHandler(mock1);
        verify(mockitoCore.mockUtil).getMockHandler(mock2);
        
        // Verify that the VerificationModeFactory.noMoreInteractions().verify method was called for each mock object
        verify(mockitoCore.mockUtil.getMockHandler(mock1).getInvocationContainer()).getInvocations();
        verify(mockitoCore.mockUtil.getMockHandler(mock2).getInvocationContainer()).getInvocations();
        verify(mockitoCore.reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        // Create a MockitoCore instance
        MockitoCore mockitoCore = new MockitoCore();
        
        // Create a list of mock objects
        List<Object> mocks = Arrays.asList(mock(MyInterface.class), mock(MyInterface.class));
        
        // Create an InOrderContext object
        InOrderContext inOrderContext = mock(InOrderContext.class);
        
        // Call the verifyNoMoreInteractionsInOrder method with the list of mock objects and the InOrderContext object
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        
        // Verify that the mockingProgress.validateState method was called
        verify(mockitoCore.mockingProgress).validateState();
        
        // Verify that the AllInvocationsFinder.find method was called
        verify(mockitoCore.mockUtil).getMockHandler(anyObject()).getInvocationContainer();
        
        // Verify that the VerificationModeFactory.noMoreInteractions().verifyInOrder method was called
        verify(mockitoCore.mockUtil.getMockHandler(anyObject()).getInvocationContainer()).getInvocations();
    }
    
    @Test
    public void testInOrder() {
        // Create a MockitoCore instance
        MockitoCore mockitoCore = new MockitoCore();
        
        // Create mock objects
        MyInterface mock1, mock2;
        
        // Call the inOrder method with the mock objects
        mockitoCore.inOrder(mock1, mock2);
        
        // Verify that the reporter.mocksHaveToBePassedWhenCreatingInOrder method was not called
        verify(mockitoCore.reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        
        // Verify that the reporter.nullPassedWhenCreatingInOrder method was not called
        verify(mockitoCore.reporter, never()).nullPassedWhenCreatingInOrder();
        
        // Verify that the reporter.notAMockPassedWhenCreatingInOrder method was not called
        verify(mockitoCore.reporter, never()).notAMockPassedWhenCreatingInOrder();
        
        // Verify that the InOrderImpl constructor was called
        verify(mockitoCore.mockUtil, times(1)).isMock(anyObject());
    }
    
    @Test
    public void testDoAnswer() {
        // Create a MockitoCore instance
        MockitoCore mockitoCore = new MockitoCore();
        
        // Create an Answer object
        Answer answer = mock(Answer.class);
        
        // Call the doAnswer method with the Answer object
        Stubber stubber = mockitoCore.doAnswer(answer);
        
        // Verify that the mockingProgress.stubbingStarted method was called
        verify(mockitoCore.mockingProgress).stubbingStarted();
        
        // Verify that the mockingProgress.resetOngoingStubbing method was called
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
        
        // Verify that the StubberImpl.doAnswer method was called
        verify(stubber).doAnswer(answer);
    }
    
    @Test
    public void testStubVoid() {
        // Create a MockitoCore instance
        MockitoCore mockitoCore = new MockitoCore();
        
        // Create a mock object
        MyInterface mock = mock(MyInterface.class);
        
        // Call the stubVoid method with the mock object
        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);
        
        // Verify that the mockingProgress.stubbingStarted method was called
        verify(mockitoCore.mockingProgress).stubbingStarted();
        
        // Verify that the mockUtil.getMockHandler method was called
        verify(mockitoCore.mockUtil).getMockHandler(mock);
        
        // Verify that the handler.voidMethodStubbable method was called
        verify(mockitoCore.mockUtil.getMockHandler(mock)).voidMethodStubbable(mock);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        // Create a MockitoCore instance
        MockitoCore mockitoCore = new MockitoCore();
        
        // Call the validateMockitoUsage method
        mockitoCore.validateMockitoUsage();
        
        // Verify that the mockingProgress.validateState method was called
        verify(mockitoCore.mockingProgress).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        // Create a MockitoCore instance
        MockitoCore mockitoCore = new MockitoCore();
        
        // Call the getLastInvocation method
        Invocation invocation = mockitoCore.getLastInvocation();
        
        // Verify that the OngoingStubbingImpl.getRegisteredInvocations method was called
        verify(((OngoingStubbingImpl) mockitoCore.mockingProgress.pullOngoingStubbing()).getRegisteredInvocations()).get(anyInt());
    }
}