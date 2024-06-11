import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.*;
import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    @Mock
    private MockUtil mockUtil;

    @Mock
    private Reporter reporter;

    @Mock
    private MockingProgress mockingProgress;

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
        mockitoCore.mockUtil = mockUtil;
        mockitoCore.reporter = reporter;
        mockitoCore.mockingProgress = mockingProgress;
    }

    @Test
    public void testMock_ValidInput_ReturnsMock() {
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        MockSettingsImpl mockSettingsImpl = Mockito.mock(MockSettingsImpl.class);
        Mockito.when(mockSettingsImpl).thenReturn(mockSettingsImpl);
        Mockito.when(mockUtil.createMock(classToMock, mockSettingsImpl)).thenReturn(Mockito.mock(Object.class));
        
        Object result = mockitoCore.mock(classToMock, mockSettingsImpl);

        Mockito.verify(mockUtil).createMock(classToMock, mockSettingsImpl);
        Mockito.verify(mockingProgress).validateState();
        Mockito.verify(mockingProgress).resetOngoingStubbing();

        assert(result != null);
        assert(result instanceof Object);
    }

    @Test
    public void testStub_StubbingNotStarted_MissingMethodInvocation() {
        Mockito.when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        
        mockitoCore.stub();

        Mockito.verify(mockingProgress).pullOngoingStubbing();
        Mockito.verify(mockingProgress).reset();
        Mockito.verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testStub_ValidInput_StubbingStarted() {
        IOngoingStubbing ongoingStubbing = Mockito.mock(IOngoingStubbing.class);
        Mockito.when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        
        Object result = mockitoCore.stub();

        Mockito.verify(mockingProgress).pullOngoingStubbing();
        assert(result.equals(ongoingStubbing));
    }

    @Test
    public void testStub_DeprecatedStubbingStarted_ReturnsDeprecatedOngoingStubbing() {
        DeprecatedOngoingStubbing deprecatedStubbing = Mockito.mock(DeprecatedOngoingStubbing.class);
        Mockito.when(mockingProgress.pullOngoingStubbing()).thenReturn(deprecatedStubbing);
        
        Object result = mockitoCore.stub(null);

        Mockito.verify(mockingProgress).pullOngoingStubbing();
        assert(result.equals(deprecatedStubbing));
    }

    @Test
    public void testWhen_StubbingStarted_ReturnsOngoingStubbing() {
        OngoingStubbing ongoingStubbing = Mockito.mock(OngoingStubbing.class);
        Mockito.when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        
        Object result = mockitoCore.when(null);

        Mockito.verify(mockingProgress).pullOngoingStubbing();
        assert(result.equals(ongoingStubbing));
    }

    @Test
    public void testVerify_NullMock_NullPassedToVerify() {
        Reporter reporter = Mockito.mock(Reporter.class);
        Mockito.doNothing().when(reporter).nullPassedToVerify();
        
        Object result = mockitoCore.verify(null, null);
        
        Mockito.verify(reporter).nullPassedToVerify();
        assert(result == null);
    }

    @Test
    public void testVerify_NotAMockMock_NotAMockPassedToVerify() {
        Object mock = new Object();
        Reporter reporter = Mockito.mock(Reporter.class);
        Mockito.doNothing().when(reporter).notAMockPassedToVerify();
        Mockito.when(mockUtil.isMock(mock)).thenReturn(false);
        
        Object result = mockitoCore.verify(mock, null);
        
        Mockito.verify(reporter).notAMockPassedToVerify();
        assert(result.equals(mock));
    }

    @Test
    public void testVerify_ValidMock_ValidationStarted() {
        Object mock = Mockito.mock(Object.class);
        VerificationMode verificationMode = Mockito.mock(VerificationMode.class);
        
        Object result = mockitoCore.verify(mock, verificationMode);
        
        Mockito.verify(mockingProgress).verificationStarted(verificationMode);
        assert(result.equals(mock));
    }

    @Test
    public void testReset_ValidMocks_ResetInvoked() {
        Object[] mocks = new Object[2];
        mocks[0] = Mockito.mock(Object.class);
        mocks[1] = Mockito.mock(Object.class);
        
        mockitoCore.reset(mocks);
        
        Mockito.verify(mockingProgress).validateState();
        Mockito.verify(mockingProgress).reset();
        Mockito.verify(mockingProgress).resetOngoingStubbing();
        Mockito.verify(mockUtil).resetMock(mocks[0]);
        Mockito.verify(mockUtil).resetMock(mocks[1]);
    }

    @Test
    public void testVerifyNoMoreInteractions_NullMock_NullPassedToVerifyNoMoreInteractions() {
        Object[] mocks = new Object[1];
        mocks[0] = null;
        Reporter reporter = Mockito.mock(Reporter.class);
        Mockito.doNothing().when(reporter).nullPassedToVerifyNoMoreInteractions();
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        Mockito.verify(reporter).nullPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractions_NotAMockMock_NotAMockPassedToVerifyNoMoreInteractions() {
        Object mock = new Object();
        Reporter reporter = Mockito.mock(Reporter.class);
        Mockito.doNothing().when(reporter).notAMockPassedToVerifyNoMoreInteractions();
        Mockito.when(mockUtil.getMockHandler(mock)).thenThrow(new NotAMockException());
        
        mockitoCore.verifyNoMoreInteractions(mock);

        Mockito.verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractions_ValidMocks_VerifyNoMoreInteractionsInvoked() {
        Object[] mocks = new Object[2];
        mocks[0] = Mockito.mock(Object.class);
        mocks[1] = Mockito.mock(Object.class);
        
        mockitoCore.verifyNoMoreInteractions(mocks);

        Mockito.verify(mockUtil, Mockito.times(2)).getMockHandler(ArgumentMatchers.any());
    }

    @Test
    public void testInOrder_NullMocks_MocksHaveToBePassedWhenCreatingInOrder() {
        Object[] mocks = new Object[1];
        mocks[0] = null;
        Reporter reporter = Mockito.mock(Reporter.class);
        Mockito.doNothing().when(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        
        mockitoCore.inOrder(mocks);

        Mockito.verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_NotAMockMock_NotAMockPassedWhenCreatingInOrder() {
        Object mock = new Object();
        Reporter reporter = Mockito.mock(Reporter.class);
        Mockito.doNothing().when(reporter).notAMockPassedWhenCreatingInOrder();
        Mockito.when(mockUtil.isMock(mock)).thenReturn(false);
        
        mockitoCore.inOrder(mock);

        Mockito.verify(reporter).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_ValidMocks_InOrderCreationSuccessful() {
        Object[] mocks = new Object[2];
        mocks[0] = Mockito.mock(Object.class);
        mocks[1] = Mockito.mock(Object.class);

        InOrder inOrder = mockitoCore.inOrder(mocks);

        Mockito.verify(reporter, Mockito.never()).mocksHaveToBePassedWhenCreatingInOrder();
        assert(inOrder != null);
    }

    @Test
    public void testDoAnswer_StubbingStarted_DoAnswerInvoked() {
        Answer answer = Mockito.mock(Answer.class);
        Stubber stubber = Mockito.mock(Stubber.class);
        Mockito.when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        Mockito.when(new StubberImpl()).thenReturn(stubber);
        Mockito.when(stubber.doAnswer(answer)).thenReturn(stubber);
        
        Stubber result = mockitoCore.doAnswer(answer);

        Mockito.verify(mockingProgress).pullOngoingStubbing();
        Mockito.verify(mockingProgress).resetOngoingStubbing();
        assert(result.equals(stubber));
    }

    @Test
    public void testStubVoid_ValidMock_StubbingStarted() {
        Object mock = Mockito.mock(Object.class);
        MockHandlerInterface mockHandlerInterface = Mockito.mock(MockHandlerInterface.class);
        Mockito.when(mockUtil.getMockHandler(mock)).thenReturn(mockHandlerInterface);
        
        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);

        Mockito.verify(mockHandlerInterface).voidMethodStubbable(mock);
        assert(stubbable != null);
    }

    @Test
    public void testValidateMockitoUsage_ValidateStateCalled() {
        mockitoCore.validateMockitoUsage();

        Mockito.verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation_ValidOngoingStubbing_GetRegisteredInvocationsCalled() {
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation);
        Mockito.when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        Mockito.when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);

        Invocation result = mockitoCore.getLastInvocation();

        Mockito.verify(mockingProgress).pullOngoingStubbing();
        assert(result.equals(invocation));
    }

}