import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

@SuppressWarnings("unchecked")
public class MockitoCoreTest {
    
    private MockitoCore mockitoCore;
    private MockUtil mockUtil;
    private ThreadSafeMockingProgress mockingProgress;
    private Reporter reporter;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
        mockUtil = Mockito.mock(MockUtil.class);
        mockingProgress = Mockito.mock(ThreadSafeMockingProgress.class);
        reporter = Mockito.mock(Reporter.class);
        
        // Set up dependencies
        ReflectionTestUtils.setField(mockitoCore, "mockUtil", mockUtil);
        ReflectionTestUtils.setField(mockitoCore, "mockingProgress", mockingProgress);
        ReflectionTestUtils.setField(mockitoCore, "reporter", reporter);
    }
    
    @Test
    public void testMock() {
        // Arrange
        Class clazz = Mockito.mock(Class.class);
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        
        // Act
        Object result = mockitoCore.mock(clazz, mockSettings);
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(mockUtil).createMock(clazz, (MockSettingsImpl) mockSettings);
        Mockito.verify(mockingProgress).mockingStarted(result, clazz, mockSettings);
    }
    
    @Test
    public void testStub() {
        // Arrange
        IOngoingStubbing stubbing = Mockito.mock(IOngoingStubbing.class);
        Mockito.when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        
        // Act
        Object result = mockitoCore.stub();
        
        // Assert
        Assert.assertEquals(stubbing, result);
        Mockito.verify(mockingProgress).pullOngoingStubbing();
    }
    
    @Test
    public void testStubWithMethodCall() {
        // Arrange
        Mockito.when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        
        // Act
        Object result = mockitoCore.stub("methodCall");
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(mockingProgress).stubbingStarted();
        Mockito.verify(mockingProgress).pullOngoingStubbing();
    }
    
    @Test
    public void testWhen() {
        // Arrange
        Mockito.when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        
        // Act
        Object result = mockitoCore.when("methodCall");
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(mockingProgress).stubbingStarted();
        Mockito.verify(mockingProgress).pullOngoingStubbing();
    }
    
    @Test
    public void testVerify() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        
        // Act
        Object result = mockitoCore.verify(mock, mode);
        
        // Assert
        Assert.assertEquals(mock, result);
        Mockito.verify(mockUtil).isMock(mock);
        Mockito.verify(mockingProgress).verificationStarted(mode);
    }
    
    @Test
    public void testReset() {
        // Arrange
        Object[] mocks = new Object[] {Mockito.mock(Object.class)};
        
        // Act
        mockitoCore.reset(mocks);
        
        // Assert
        Mockito.verify(mockingProgress).validateState();
        Mockito.verify(mockingProgress).reset();
        Mockito.verify(mockingProgress).resetOngoingStubbing();
        Mockito.verify(mockUtil).resetMock(mocks[0]);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        // Arrange
        Object[] mocks = new Object[] {Mockito.mock(Object.class)};
        List<Invocation> invocations = Mockito.mock(List.class);
        VerificationDataImpl data = Mockito.mock(VerificationDataImpl.class);
        Mockito.when(invocations.size()).thenReturn(1);
        Mockito.when(invocations.get(Mockito.anyInt())).thenReturn(Mockito.mock(Invocation.class));
        Mockito.when(mockUtil.getMockHandler(mocks[0])).thenReturn(Mockito.mock(MockHandlerInterface.class));
        Mockito.when(mockUtil.getMockHandler(mocks[0]).getInvocationContainer().getInvocations()).thenReturn(invocations);
        Mockito.when(VerificationModeFactory.noMoreInteractions()).thenReturn(Mockito.mock(VerificationMode.class));
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // Assert
        Mockito.verify(mockingProgress).validateState();
        Mockito.verify(mockingProgress, Mockito.times(mocks.length)).validateState();
        Mockito.verify(mockUtil, Mockito.times(mocks.length)).getMockHandler(Mockito.any());
        Mockito.verify(mockUtil).getMockHandler(mocks[0]);
        Mockito.verify(mockUtil.getMockHandler(mocks[0]).getInvocationContainer()).getInvocations();
        Mockito.verify(reporter, Mockito.never()).nullPassedToVerifyNoMoreInteractions();
        Mockito.verify(reporter, Mockito.never()).notAMockPassedToVerifyNoMoreInteractions();
        Mockito.verify(data, Mockito.never()).verify(Mockito.any());
    }
    
    @Test
    public void testVerifyNoMoreInteractionsWithNullMock() {
        // Arrange
        Object[] mocks = new Object[] {null};
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // Assert
        Mockito.verify(mockingProgress).validateState();
        Mockito.verify(reporter).nullPassedToVerifyNoMoreInteractions();
        Mockito.verify(mockingProgress, Mockito.times(mocks.length)).validateState();
        Mockito.verify(mockUtil, Mockito.never()).getMockHandler(Mockito.any());
    }
    
    @Test
    public void testVerifyNoMoreInteractionsWithNotAMock() {
        // Arrange
        Object[] mocks = new Object[] {"notAMock"};
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // Assert
        Mockito.verify(mockingProgress).validateState();
        Mockito.verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
        Mockito.verify(mockingProgress, Mockito.times(mocks.length)).validateState();
        Mockito.verify(mockUtil, Mockito.times(mocks.length)).getMockHandler(Mockito.any());
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        // Arrange
        List<Object> mocks = Mockito.mock(List.class);
        InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);
        AllInvocationsFinder finder = Mockito.mock(AllInvocationsFinder.class);
        List<Invocation> invocations = Mockito.mock(List.class);
        VerificationDataInOrderImpl data = Mockito.mock(VerificationDataInOrderImpl.class);
        Mockito.when(finder.find(mocks)).thenReturn(invocations);
        Mockito.when(VerificationModeFactory.noMoreInteractions()).thenReturn(Mockito.mock(VerificationMode.class));
        
        // Act
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        
        // Assert
        Mockito.verify(mockingProgress).validateState();
        Mockito.verify(finder).find(mocks);
        Mockito.verify(data).verifyInOrder();
        Mockito.verify(inOrderContext).verifyInOrder(data);
    }
    
    @Test
    public void testAssertMocksNotEmpty() {
        // Arrange
        Object[] mocks = null;
        
        // Act
        mockitoCore.assertMocksNotEmpty(mocks);
        
        // Assert
        Mockito.verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        // Arrange
        Object[] mocks = new Object[] {Mockito.mock(Object.class)};
        
        // Act
        InOrder result = mockitoCore.inOrder(mocks);
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        Mockito.verify(reporter, Mockito.never()).nullPassedWhenCreatingInOrder();
        Mockito.verify(mockUtil).isMock(mocks[0]);
    }
    
    @Test
    public void testInOrderWithNullMock() {
        // Arrange
        Object[] mocks = new Object[] {null};
        
        // Act
        mockitoCore.inOrder(mocks);
        
        // Assert
        Mockito.verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        Mockito.verify(reporter).nullPassedWhenCreatingInOrder();
        Mockito.verify(mockUtil, Mockito.never()).isMock(Mockito.any());
    }
    
    @Test
    public void testInOrderWithNotAMock() {
        // Arrange
        Object[] mocks = new Object[] {"notAMock"};
        
        // Act
        mockitoCore.inOrder(mocks);
        
        // Assert
        Mockito.verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        Mockito.verify(reporter, Mockito.never()).nullPassedWhenCreatingInOrder();
        Mockito.verify(mockUtil).isMock(mocks[0]);
        Mockito.verify(reporter).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testDoAnswer() {
        // Arrange
        Answer answer = Mockito.mock(Answer.class);
        
        // Act
        Stubber result = mockitoCore.doAnswer(answer);
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(mockingProgress).stubbingStarted();
        Mockito.verify(mockingProgress).resetOngoingStubbing();
    }
    
    @Test
    public void testStubVoid() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        MockHandlerInterface handler = Mockito.mock(MockHandlerInterface.class);
        VoidMethodStubbable stubbable = Mockito.mock(VoidMethodStubbable.class);
        Mockito.when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        Mockito.when(handler.voidMethodStubbable(mock)).thenReturn(stubbable);
        
        // Act
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(mockingProgress).stubbingStarted();
        Mockito.verify(mockUtil).getMockHandler(mock);
        Mockito.verify(handler).voidMethodStubbable(mock);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        // Act
        mockitoCore.validateMockitoUsage();
        
        // Assert
        Mockito.verify(mockingProgress).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = Mockito.mock(List.class);
        Mockito.when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        Mockito.when(allInvocations.size()).thenReturn(1);
        Mockito.when(allInvocations.get(Mockito.anyInt())).thenReturn(invocation);
        Mockito.when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        
        // Act
        Invocation result = mockitoCore.getLastInvocation();
        
        // Assert
        Assert.assertEquals(invocation, result);
        Mockito.verify(mockingProgress).pullOngoingStubbing();
    }

}