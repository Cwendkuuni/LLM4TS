import org.junit.Before;
import org.junit.Test;
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
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    private Reporter reporter;
    private MockUtil mockUtil;
    private MockingProgress mockingProgress;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
        reporter = mock(Reporter.class);
        mockitoCore.reporter = reporter;
        mockUtil = mock(MockUtil.class);
        mockitoCore.mockUtil = mockUtil;
        mockingProgress = mock(MockingProgress.class);
        mockitoCore.mockingProgress = mockingProgress;
    }
    
    @Test
    public void testMock_CorrectClass_ShouldReturnMockObject() {
        // Arrange
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettingsImpl.class);
        Object expectedMock = mock(Object.class);
        when(mockUtil.createMock(classToMock, (MockSettingsImpl) mockSettings)).thenReturn(expectedMock);
        
        // Act
        Object result = mockitoCore.mock(classToMock, mockSettings);
        
        // Assert
        assertEquals(expectedMock, result);
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
    }
    
    @Test(expected = AssertionError.class)
    public void testMock_NullClass_ShouldThrowAssertionError() {
        // Arrange
        Class<Object> classToMock = null;
        
        // Act
        mockitoCore.mock(classToMock, mock(MockSettings.class));
        
        // Assert
        verify(reporter).nullPassedToVerify();
    }
    
    @Test(expected = AssertionError.class)
    public void testMock_NotAMockClass_ShouldThrowAssertionError() {
        // Arrange
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettingsImpl.class);
        Object notAMockObject = new Object();
        when(mockUtil.createMock(classToMock, (MockSettingsImpl) mockSettings)).thenReturn(notAMockObject);
        
        // Act
        mockitoCore.mock(classToMock, mockSettings);
        
        // Assert
        verify(reporter).notAMockPassedToVerify();
    }
    
    @Test
    public void testStub_NoOngoingStubbing_ShouldResetProgressAndReportMissingMethodInvocation() {
        // Arrange
        IOngoingStubbing expectedStubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(null);
        
        // Act
        IOngoingStubbing result = mockitoCore.stub();
        
        // Assert
        assertEquals(expectedStubbing, result);
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }
    
    @Test
    public void testDeprecatedStub_WithMethodCall_ShouldReturnOngoingStubbing() {
        // Arrange
        DeprecatedOngoingStubbing expectedStubbing = mock(DeprecatedOngoingStubbing.class);
        mockingProgress.stubbingStarted();
        when(mockitoCore.stub()).thenReturn(expectedStubbing);
        
        // Act
        DeprecatedOngoingStubbing result = mockitoCore.stub(mock(Object.class));
        
        // Assert
        assertEquals(expectedStubbing, result);
        verify(mockingProgress).stubbingStarted();
        verify(mockitoCore).stub();
    }
    
    @Test
    public void testWhen_WithMethodCall_ShouldReturnOngoingStubbing() {
        // Arrange
        OngoingStubbing expectedStubbing = mock(OngoingStubbing.class);
        mockingProgress.stubbingStarted();
        when(mockitoCore.stub()).thenReturn(expectedStubbing);
        
        // Act
        OngoingStubbing result = mockitoCore.when(mock(Object.class));
        
        // Assert
        assertEquals(expectedStubbing, result);
        verify(mockingProgress).stubbingStarted();
        verify(mockitoCore).stub();
    }
    
    // Additional test cases for the remaining methods will follow

}