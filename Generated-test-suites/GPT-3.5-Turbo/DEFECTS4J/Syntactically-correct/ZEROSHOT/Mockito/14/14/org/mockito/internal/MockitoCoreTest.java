import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    @Mock
    private Reporter reporterMock;

    @Mock
    private MockUtil mockUtilMock;

    @Mock
    private MockingProgress mockingProgressMock;

    @InjectMocks
    private MockitoCore mockitoCore;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMock() {
        Class mockClass = Class.class;
        MockSettings mockSettings = mock(MockSettings.class);

        Object result = mockitoCore.mock(mockClass, mockSettings);

        verify(mockUtilMock).createMock(mockClass, (MockSettingsImpl) mockSettings);
        verify(mockingProgressMock).mockingStarted(result, mockClass, mockSettings);
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbingMock = mock(IOngoingStubbing.class);

        when(mockingProgressMock.pullOngoingStubbing()).thenReturn(stubbingMock);

        IOngoingStubbing result = mockitoCore.stub();

        verify(mockingProgressMock).pullOngoingStubbing();
        verify(mockingProgressMock).reset();
        verify(reporterMock).missingMethodInvocation();
        verifyNoMoreInteractions(mockingProgressMock, reporterMock);
    }

    @Test
    public void testStub_withMethodCall() {
        Object methodCall = new Object();
        IOngoingStubbing stubbingMock = mock(IOngoingStubbing.class);

        when(mockingProgressMock.pullOngoingStubbing()).thenReturn(stubbingMock);

        DeprecatedOngoingStubbing result = mockitoCore.stub(methodCall);

        verify(mockingProgressMock).stubbingStarted();
        verify(mockingProgressMock).pullOngoingStubbing();
        verifyNoMoreInteractions(mockingProgressMock);
    }

    @Test
    public void testWhen() {
        Object methodCall = new Object();
        IOngoingStubbing stubbingMock = mock(IOngoingStubbing.class);

        when(mockingProgressMock.pullOngoingStubbing()).thenReturn(stubbingMock);

        OngoingStubbing result = mockitoCore.when(methodCall);

        verify(mockingProgressMock).stubbingStarted();
        verify(mockingProgressMock).pullOngoingStubbing();
        verifyNoMoreInteractions(mockingProgressMock);
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        VerificationMode mode = mock(VerificationMode.class);

        Object result = mockitoCore.verify(mock, mode);

        verify(reporterMock).nullPassedToVerify();
        verify(mockUtilMock).isMock(mock);
        verify(mockingProgressMock).verificationStarted(mode);
        verifyNoMoreInteractions(reporterMock, mockUtilMock, mockingProgressMock);
    }

    @Test
    public void testReset() {
        Object[] mocks = {mock(Object.class), mock(Object.class)};

        mockitoCore.reset(mocks);

        verify(mockingProgressMock).validateState();
        verify(mockingProgressMock).reset();
        verify(mockingProgressMock).resetOngoingStubbing();
        verify(mockUtilMock, times(mocks.length)).resetMock(any());
        verifyNoMoreInteractions(mockingProgressMock, mockUtilMock);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(Object.class);

        mockitoCore.verifyNoMoreInteractions(mock);

        verify(reporterMock).nullPassedToVerifyNoMoreInteractions();
        verify(mockUtilMock).getMockHandler(mock);
        verifyNoMoreInteractions(reporterMock, mockUtilMock);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        InOrderContext inOrderContext = mock(InOrderContext.class);

        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        verify(mockingProgressMock).validateState();
        verify(mockUtilMock).getMockHandler(any());
        verifyNoMoreInteractions(mockingProgressMock, mockUtilMock, inOrderContext);
    }

    @Test
    public void testInOrder() {
        Object[] mocks = {mock(Object.class), mock(Object.class)};

        InOrder result = mockitoCore.inOrder(mocks);

        verify(reporterMock).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockUtilMock, times(mocks.length)).isMock(any());
        verify(reporterMock, times(mocks.length)).nullPassedWhenCreatingInOrder();
        verifyNoMoreInteractions(reporterMock, mockUtilMock);
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);

        Stubber result = mockitoCore.doAnswer(answer);

        verify(mockingProgressMock).stubbingStarted();
        verify(mockingProgressMock).resetOngoingStubbing();
        verifyNoMoreInteractions(mockingProgressMock);
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        VoidMethodStubbable stubbableMock = mock(VoidMethodStubbable.class);

        when(mockUtilMock.getMockHandler(mock)).thenReturn(stubbableMock);

        VoidMethodStubbable result = mockitoCore.stubVoid(mock);

        verify(mockingProgressMock).stubbingStarted();
        verify(mockUtilMock).getMockHandler(mock);
        verifyNoMoreInteractions(mockingProgressMock, mockUtilMock);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();

        verify(mockingProgressMock).validateState();
        verifyNoMoreInteractions(mockingProgressMock);
    }

    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbingMock = mock(OngoingStubbingImpl.class);
        Invocation invocationMock = mock(Invocation.class);

        when(mockingProgressMock.pullOngoingStubbing()).thenReturn(ongoingStubbingMock);
        when(ongoingStubbingMock.getRegisteredInvocations()).thenReturn(Arrays.asList(invocationMock));

        Invocation result = mockitoCore.getLastInvocation();

        verify(mockingProgressMock).pullOngoingStubbing();
        verify(ongoingStubbingMock).getRegisteredInvocations();
        verifyNoMoreInteractions(mockingProgressMock, ongoingStubbingMock);
    }
}