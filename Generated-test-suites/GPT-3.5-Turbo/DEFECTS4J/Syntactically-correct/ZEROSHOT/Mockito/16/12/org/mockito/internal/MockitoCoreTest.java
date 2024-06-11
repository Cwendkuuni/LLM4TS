import org.junit.*;
import org.mockito.*;
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

@SuppressWarnings("unchecked")
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

    @Test
    public void testMock_withClassToMockAndMockSettings_shouldCreateMock() {
        //Arrange
        Class<T> classToMock = T.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        //Act
        T result = mockitoCore.mock(classToMock, mockSettings);
        
        //Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testStub_shouldReturnStubbing() {
        //Arrange
        IOngoingStubbing expected = new OngoingStubbingImpl();
        mockingProgress.stubbingStarted();
        
        //Act
        IOngoingStubbing result = mockitoCore.stub();
        
        //Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testStub_withMethodCall_shouldReturnDeprecatedOngoingStubbing() {
        //Arrange
        T methodCall = new T();
        mockingProgress.stubbingStarted();
        
        //Act
        DeprecatedOngoingStubbing<T> result = mockitoCore.stub(methodCall);
        
        //Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testWhen_withMethodCall_shouldReturnOngoingStubbing() {
        //Arrange
        T methodCall = new T();
        mockingProgress.stubbingStarted();
        
        //Act
        OngoingStubbing<T> result = mockitoCore.when(methodCall);
        
        //Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testVerify_withMockAndMode_shouldReturnMock() {
        //Arrange
        T mock = new T();
        VerificationMode mode = VerificationMode.STRICT;
        
        //Act
        T result = mockitoCore.verify(mock, mode);
        
        //Assert
        Assert.assertEquals(mock, result);
    }

    @Test
    public void testReset_withMocks_shouldResetMocks() {
        //Arrange
        T mock1 = new T();
        T mock2 = new T();
        T[] mocks = {mock1, mock2};
        
        //Act
        mockitoCore.reset(mocks);
        
        //Assert
        Assert.assertTrue(mockUtil.isMockReset(mock1));
        Assert.assertTrue(mockUtil.isMockReset(mock2));
    }

    @Test
    public void testVerifyNoMoreInteractions_withMocks_shouldVerifyNoMoreInteractions() {
        //Arrange
        T mock = new T();
        Object[] mocks = {mock};
        
        //Act
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        //Assert
        Mockito.verify(mockUtil.getMockHandler(mock)).verifyNoMoreInteractions();
    }

    @Test
    public void testInOrder_withMocks_shouldCreateInOrder() {
        //Arrange
        T mock1 = new T();
        T mock2 = new T();
        Object[] mocks = {mock1, mock2};
        
        //Act
        InOrder result = mockitoCore.inOrder(mocks);
        
        //Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testDoAnswer_withAnswer_shouldReturnStubber() {
        //Arrange
        Answer answer = Mockito.mock(Answer.class);
        mockingProgress.stubbingStarted();
        
        //Act
        Stubber result = mockitoCore.doAnswer(answer);
        
        //Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testStubVoid_withMock_shouldReturnVoidMethodStubbable() {
        //Arrange
        T mock = new T();
        mockingProgress.stubbingStarted();
        MockHandlerInterface<T> handler = mockUtil.getMockHandler(mock);
        Mockito.when(handler.voidMethodStubbable(mock)).thenReturn(Mockito.mock(VoidMethodStubbable.class));
        
        //Act
        VoidMethodStubbable<T> result = mockitoCore.stubVoid(mock);
        
        //Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testValidateMockitoUsage_shouldValidateState() {
        //Act
        mockitoCore.validateMockitoUsage();
        
        //Assert
        Mockito.verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation_shouldReturnLastInvocation() {
        //Arrange
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Invocation> allInvocations = Arrays.asList(invocation);
        Mockito.when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        mockingProgress.pushOngoingStubbing(ongoingStubbing);
        
        //Act
        Invocation result = mockitoCore.getLastInvocation();
        
        //Assert
        Assert.assertEquals(invocation, result);
    }
}