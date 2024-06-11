import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubbing;
import org.mockito.verification.VerificationMode;

import java.util.List;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {
    
    private MockitoCore mockitoCore;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }
    
    // Test for mock(Class<T> classToMock, MockSettings mockSettings)
    
    @Test
    public void testMock_WhenValidParameters_ReturnMockObject() {
        Class<T> classToMock = ExampleClass.class;
        MockSettings mockSettings = mock(MockSettings.class);
        
        ExampleClass result = mockitoCore.mock(classToMock, mockSettings);
        
        assertNotNull(result);
        assertTrue(Mockito.mockingProgress().getMocks().contains(result));
        assertTrue(Mockito.mockingProgress().getInstances().contains(result));
    }
    
    // Test for stub()
    
    @Test
    public void testStub_WhenOngoingStubbingIsNull_CallMissingMethodInvocation() {
        MockitoProgress mockitoProgress = Mockito.mock(MockitoProgress.class);
        when(mockitoProgress.pullOngoingStubbing()).thenReturn(null);
        
        mockitoCore.stub();
        
        verify(mockitoProgress).reset();
        verify(mockitoCore.getReporter()).missingMethodInvocation();
    }
    
    // Test for stub(T methodCall)
    
    @Test(expected = NotAMockException.class)
    public void testStub_WhenMethodCallIsNotAMockObject_ThrowNotAMockException() {
        Object methodCall = new Object();
        
        mockitoCore.stub(methodCall);
    }
    
    // Test for when(T methodCall)
    
    @Test(expected = NotAMockException.class)
    public void testWhen_WhenMethodCallIsNotAMockObject_ThrowNotAMockException() {
        Object methodCall = new Object();
        
        mockitoCore.when(methodCall);
    }
    
    // Test for verify(T mock, VerificationMode mode)
    
    @Test
    public void testVerify_WhenMockIsNull_CallNullPassedToVerify() {
        MockitoProgress mockitoProgress = Mockito.mock(MockitoProgress.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        
        mockitoCore.setMockingProgress(mockitoProgress);
        mockitoCore.setReporter(reporter);
        
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        ExampleClass mock = null;
        
        mockitoCore.verify(mock, mode);
        
        verify(reporter).nullPassedToVerify();
    }
    
    // Test for reset(T ... mocks)
    
    @Test
    public void testReset_WhenMocksPassed_ResetMockingProgressAndResetAllMocks() {
        MockitoProgress mockitoProgress = Mockito.mock(MockitoProgress.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        
        mockitoCore.setMockingProgress(mockitoProgress);
        mockitoCore.setMockUtil(mockUtil);
        
        ExampleClass mock1 = Mockito.mock(ExampleClass.class);
        ExampleClass mock2 = Mockito.mock(ExampleClass.class);
        
        mockitoCore.reset(mock1, mock2);
        
        verify(mockitoProgress).validateState();
        verify(mockitoProgress).reset();
        verify(mockitoProgress).resetOngoingStubbing();
        verify(mockUtil).resetMock(mock1);
        verify(mockUtil).resetMock(mock2);
    }
    
    // Test for verifyNoMoreInteractions(Object... mocks)
    
    @Test
    public void testVerifyNoMoreInteractions_WhenMocksListIsEmpty_CallMocksHaveToBePassedToVerifyNoMoreInteractions() {
        Reporter reporter = Mockito.mock(Reporter.class);
        
        mockitoCore.setReporter(reporter);
        
        Object[] mocks = new Object[] {};
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }
    
    // Test for verifyNoMoreInteractionsInOrder(List<Object> mocks, InOrderContext inOrderContext)
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder_WhenMocksListIsEmpty_CallMocksHaveToBePassedWhenCreatingInOrder() {
        Reporter reporter = Mockito.mock(Reporter.class);
        List<Object> mocks = new ArrayList<>();
        InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);
        
        mockitoCore.setReporter(reporter);
        
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
    }
    
    // Test for assertMocksNotEmpty(Object[] mocks)
    
    @Test
    public void testAssertMocksNotEmpty_WhenMocksArrayIsNull_CallMocksHaveToBePassedToVerifyNoMoreInteractions() {
        Reporter reporter = Mockito.mock(Reporter.class);
        Object[] mocks = null;
        
        mockitoCore.setReporter(reporter);
        
        mockitoCore.assertMocksNotEmpty(mocks);
        
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testAssertMocksNotEmpty_WhenMocksArrayIsEmpty_CallMocksHaveToBePassedToVerifyNoMoreInteractions() {
        Reporter reporter = Mockito.mock(Reporter.class);
        Object[] mocks = new Object[] {};
        
        mockitoCore.setReporter(reporter);
        
        mockitoCore.assertMocksNotEmpty(mocks);
        
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }
    
    // Test for inOrder(Object... mocks)
    
    @Test
    public void testInOrder_WhenMocksArrayIsEmpty_CallMocksHaveToBePassedWhenCreatingInOrder() {
        Reporter reporter = Mockito.mock(Reporter.class);
        Object[] mocks = new Object[] {};
        
        mockitoCore.setReporter(reporter);
        
        mockitoCore.inOrder(mocks);
        
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
    }
    
    @Test
    public void testInOrder_WhenMockIsNotAMockObject_CallNotAMockPassedWhenCreatingInOrder() {
        Reporter reporter = Mockito.mock(Reporter.class);
        ExampleClass mock = new ExampleClass();
        Object[] mocks = new Object[] {mock};
        
        mockitoCore.setReporter(reporter);
        mockitoCore.setMockUtil(mockUtil);
        when(mockUtil.isMock(mock)).thenReturn(false);
        
        mockitoCore.inOrder(mocks);
        
        verify(reporter).notAMockPassedWhenCreatingInOrder();
    }
    
    // Test for doAnswer(Answer answer)
    
    @Test
    public void testDoAnswer_WhenOngoingStubbingIsNotNull_ResetOngoingStubbingAndReturnStubber() {
        MockitoProgress mockitoProgress = Mockito.mock(MockitoProgress.class);
        Stubber stubber = Mockito.mock(Stubber.class);
        
        mockitoCore.setMockingProgress(mockitoProgress);
        when(mockitoProgress.pullOngoingStubbing()).thenReturn(mockitoCore.stub());
        
        mockitoCore.doAnswer(stubber);
        
        verify(mockitoProgress).resetOngoingStubbing();
        verify(stubber).doAnswer(stubber);
    }
    
    // Test for stubVoid(T mock)
    
    @Test
    public void testStubVoid_WhenOngoingStubbingIsNotNull_ResetOngoingStubbingAndReturnStubber() {
        ExampleClass mock = Mockito.mock(ExampleClass.class);
        MockHandlerInterface handler = Mockito.mock(MockHandlerInterface.class);
        MockitoProgress mockitoProgress = Mockito.mock(MockitoProgress.class);
        
        mockitoCore.setMockingProgress(mockitoProgress);
        when(mockitoProgress.pullOngoingStubbing()).thenReturn(mockitoCore.stub());
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        
        mockitoCore.stubVoid(mock);
        
        verify(mockitoProgress).resetOngoingStubbing();
        verify(mockitoUtil).voidMethodStubbable(mock);
    }
    
    // Test for validateMockitoUsage()
    
    @Test
    public void testValidateMockitoUsage_WhenCalled_ValidateMockitoUsageInMockingProgress() {
        MockitoProgress mockitoProgress = Mockito.mock(MockitoProgress.class);
        
        mockitoCore.setMockingProgress(mockitoProgress);
        
        mockitoCore.validateMockitoUsage();
        
        verify(mockitoProgress).validateState();
    }
    
    // Test for getLastInvocation()
    
    @Test
    public void testGetLastInvocation_WhenCalled_ReturnLastInvocationInOngoingStubbing() {
        MockitoProgress mockitoProgress = Mockito.mock(MockitoProgress.class);
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = Mockito.mock(List.class);
        
        mockitoCore.setMockingProgress(mockitoProgress);
        when(mockitoProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        when(invocations.size()).thenReturn(5);
        when(invocations.get(4)).thenReturn(Mockito.mock(Invocation.class));
        
        Invocation result = mockitoCore.getLastInvocation();
        
        assertNotNull(result);
        verify(mockitoProgress).pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
        verify(invocations).get(4);
    }
    
}