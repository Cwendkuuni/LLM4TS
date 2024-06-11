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
import org.junit.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {
    
    private MockitoCore mockitoCore;
    private MockUtil mockUtil;
    private MockingProgress mockingProgress;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
        mockUtil = mock(MockUtil.class);
        mockingProgress = mock(MockingProgress.class);
        mockitoCore.mockUtil = mockUtil;
        mockitoCore.mockingProgress = mockingProgress;
    }
    
    // test case for mock method
    @Test
    public void testMock() {
        Class<T> classToMock = T.class;
        MockSettings mockSettings = mock(MockSettings.class);
        MockSettingsImpl mockSettingsImpl = mock(MockSettingsImpl.class);
        
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        when(mockUtil.createMock(classToMock, mockSettingsImpl)).thenReturn(mockedObject);
        
        T result = mockitoCore.mock(classToMock, mockSettings, false);
        
        assertEquals(mockedObject, result);
        verify(mockingProgress).validateState();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockUtil).createMock(classToMock, mockSettingsImpl);
    }
    
    // test case for stub method
    @Test
    public void testStub() {
        OngoingStubbing ongoingStubbing = mock(OngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        
        IOngoingStubbing result = mockitoCore.stub();
        
        assertEquals(ongoingStubbing, result);
        verify(mockingProgress).pullOngoingStubbing();
    }
    
    // test case for deprecated stub method
    @Test
    public void testDeprecatedStub() {
        DeprecatedOngoingStubbing deprecatedOngoingStubbing = mock(DeprecatedOngoingStubbing.class);
        OngoingStubbing ongoingStubbing = mock(OngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        
        DeprecatedOngoingStubbing result = mockitoCore.stub(mockedMethodCall);
        
        assertEquals(deprecatedOngoingStubbing, result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).pullOngoingStubbing();
    }
    
    // test case for when method
    @Test
    public void testWhen() {
        OngoingStubbing ongoingStubbing = mock(OngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        
        OngoingStubbing result = mockitoCore.when(mockedMethodCall);
        
        assertEquals(ongoingStubbing, result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).pullOngoingStubbing();
    }
    
    // test case for verify method
    @Test
    public void testVerify() {
        T mock = mock(T.class);
        VerificationMode mode = mock(VerificationMode.class);
        when(mockUtil.isMock(mock)).thenReturn(true);
        
        T result = mockitoCore.verify(mock, mode);
        
        assertEquals(mock, result);
        verify(mockUtil).isMock(mock);
        verify(mockingProgress).verificationStarted(mode);
    }
    
    // test case for reset method
    @Test
    public void testReset() {
        T[] mocks = new T[2];
        mockitoCore.reset(mocks);
        
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        for (T mock : mocks) {
            verify(mockUtil).resetMock(mock);
        }
    }
    
    // test case for verifyNoMoreInteractions method
    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = new Object[2];
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(mockingProgress).validateState();
        for (Object mock : mocks) {
            if (mock == null) {
                verify(reporter).nullPassedToVerifyNoMoreInteractions();
            }
            else {
                verify(mockUtil).getMockHandler(mock);
                verify(mockUtil.getMockHandler(mock)).verifyNoMoreInteractions();
            }
        }
    }
    
    // test case for inOrder method
    @Test
    public void testInOrder() {
        Object[] mocks = new Object[2];
        when(mockUtil.isMock(any())).thenReturn(true);
        
        InOrder result = mockitoCore.inOrder(mocks);
        
        assertEquals(InOrderImpl.class, result.getClass());
        verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(mockUtil, times(mocks.length)).isMock(any());
    }
    
    // test case for doAnswer method
    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        
        Stubber result = mockitoCore.doAnswer(answer);
        
        assertEquals(StubberImpl.class, result.getClass());
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
    }
    
    // test case for stubVoid method
    @Test
    public void testStubVoid() {
        T mock = mock(T.class);
        MockHandlerInterface<T> handler = mock(MockHandlerInterface.class);
        VoidMethodStubbable stubbable = mock(VoidMethodStubbable.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);
        
        assertEquals(stubbable, result);
        verify(mockingProgress).stubbingStarted();
        verify(mockUtil).getMockHandler(mock);
    }
    
    // test case for validateMockitoUsage method
    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        
        verify(mockingProgress).validateState();
    }
    
    // test case for getLastInvocation method
    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = mock(List.class);
        Invocation lastInvocation = mock(Invocation.class);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(allInvocations.get(allInvocations.size()-1)).thenReturn(lastInvocation);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        
        Invocation result = mockitoCore.getLastInvocation();
        
        assertEquals(lastInvocation, result);
        verify(ongoingStubbing).getRegisteredInvocations();
        verify(allInvocations).get(allInvocations.size()-1);
        verify(mockingProgress).pullOngoingStubbing();
    }
    
    @After
    public void tearDown() {
        mockitoCore = null;
        mockUtil = null;
        mockingProgress = null;
    }
}