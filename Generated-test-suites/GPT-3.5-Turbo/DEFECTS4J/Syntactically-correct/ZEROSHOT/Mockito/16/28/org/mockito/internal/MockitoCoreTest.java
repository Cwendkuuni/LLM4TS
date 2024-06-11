import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.*;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.api.VerificationMode;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {
    
    @Mock
    private Reporter reporterMock;
    
    @Mock
    private MockUtil mockUtilMock;
    
    @Mock
    private MockingProgress mockingProgressMock;
    
    @InjectMocks
    private MockitoCore mockitoCore;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testMock() {
        // Arrange
        Class classToMock = Class.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings);
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(mockingProgressMock).validateState();
        Mockito.verify(mockingProgressMock).resetOngoingStubbing();
        Mockito.verify(mockUtilMock).createMock(classToMock, (MockSettingsImpl) mockSettings);
    }
    
    @Test
    public void testStub() {
        // Arrange
        IOngoingStubbing stubbingMock = Mockito.mock(IOngoingStubbing.class);
        Mockito.when(mockingProgressMock.pullOngoingStubbing()).thenReturn(stubbingMock);
        
        // Act
        Object result = mockitoCore.stub();
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(mockingProgressMock).pullOngoingStubbing();
        Mockito.verify(mockingProgressMock).reset();
        Mockito.verify(reporterMock).missingMethodInvocation();
    }
    
    @Test
    public void testStub_DEPRECATED() {
        // Arrange
        DeprecatedOngoingStubbing deprecatedOngoingStubbingMock = Mockito.mock(DeprecatedOngoingStubbing.class);
        Mockito.when(mockingProgressMock.pullOngoingStubbing()).thenReturn(deprecatedOngoingStubbingMock);
        
        // Act
        Object result = mockitoCore.stub(new Object());
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(mockingProgressMock).stubbingStarted();
        Mockito.verify(mockingProgressMock).pullOngoingStubbing();
    }
    
    @Test
    public void testWhen() {
        // Arrange
        OngoingStubbing ongoingStubbingMock = Mockito.mock(OngoingStubbing.class);
        Mockito.when(mockingProgressMock.pullOngoingStubbing()).thenReturn(ongoingStubbingMock);
        
        // Act
        Object result = mockitoCore.when(new Object());
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(mockingProgressMock).stubbingStarted();
        Mockito.verify(mockingProgressMock).pullOngoingStubbing();
    }
    
    @Test
    public void testVerify() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        
        // Act
        Object result = mockitoCore.verify(mock, mode);
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(mockUtilMock).isMock(mock);
        Mockito.verify(mockingProgressMock).verificationStarted(mode);
    }
    
    @Test
    public void testReset() {
        // Arrange
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        mockitoCore.reset(mocks);
        
        // Assert
        Mockito.verify(mockingProgressMock).validateState();
        Mockito.verify(mockingProgressMock).reset();
        Mockito.verify(mockingProgressMock).resetOngoingStubbing();
        Mockito.verify(mockUtilMock).resetMock(mock1);
        Mockito.verify(mockUtilMock).resetMock(mock2);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        Object[] mocks = {mock};
        
        // Act
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // Assert
        Mockito.verify(reporterMock).mocksHaveToBePassedToVerifyNoMoreInteractions();
        Mockito.verify(mockingProgressMock).validateState();
        Mockito.verify(mockUtilMock).getMockHandler(mock);
        Mockito.verify(reporterMock).notAMockPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        // Arrange
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Object[] mocks = {mock1, mock2};
        
        // Act
        InOrder result = mockitoCore.inOrder(mocks);
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(reporterMock).mocksHaveToBePassedWhenCreatingInOrder();
        Mockito.verify(reporterMock, Mockito.times(2)).nullPassedWhenCreatingInOrder();
        Mockito.verify(mockUtilMock, Mockito.times(2)).isMock(Mockito.any());
        Mockito.verify(reporterMock).notAMockPassedWhenCreatingInOrder();
        
    }
    
    @Test
    public void testDoAnswer() {
        // Arrange
        Answer answer = Mockito.mock(Answer.class);
        
        // Act
        Object result = mockitoCore.doAnswer(answer);
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(mockingProgressMock).stubbingStarted();
        Mockito.verify(mockingProgressMock).resetOngoingStubbing();
        Mockito.verify(stubberImplMock).doAnswer(answer);
    }
    
    @Test
    public void testStubVoid() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        MockHandlerInterface handler = Mockito.mock(MockHandlerInterface.class);
        Mockito.when(mockUtilMock.getMockHandler(mock)).thenReturn(handler);
        
        // Act
        Object result = mockitoCore.stubVoid(mock);
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(mockingProgressMock).stubbingStarted();
        Mockito.verify(handler).voidMethodStubbable(mock);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        // Act
        mockitoCore.validateMockitoUsage();
        
        // Assert
        Mockito.verify(mockingProgressMock).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        // Arrange
        OngoingStubbingImpl ongoingStubbingMock = Mockito.mock(OngoingStubbingImpl.class);
        Invocation invocationMock = Mockito.mock(Invocation.class);
        List<Invocation> allInvocations = Arrays.asList(invocationMock);
        Mockito.when(mockingProgressMock.pullOngoingStubbing()).thenReturn(ongoingStubbingMock);
        Mockito.when(ongoingStubbingMock.getRegisteredInvocations()).thenReturn(allInvocations);
        
        // Act
        Invocation result = mockitoCore.getLastInvocation();
        
        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(mockingProgressMock).pullOngoingStubbing();
        Assert.assertEquals(invocationMock, result);
    }
}