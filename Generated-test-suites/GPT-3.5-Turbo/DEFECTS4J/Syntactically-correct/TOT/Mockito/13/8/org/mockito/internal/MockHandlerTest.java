import org.junit.Test;
import static org.junit.Assert.*;

public class MockHandlerTest {

    @Test
    public void testHandle_WhenInvocationContainerHasAnswersForStubbing_ShouldSetMethodForStubbingAndReturnNull() {
        // Arrange
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler mockHandler = new MockHandler(mockSettings);
        Invocation invocation = new Invocation();
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        assertNull(result);
        assertNotNull(mockHandler.invocationContainerImpl.getMethodForStubbing());
    }
    
    @Test
    public void testHandle_WhenVerificationModeIsNull_ShouldSetInvocationForPotentialStubbingAndReturnDefaultAnswer() {
        // Arrange
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler mockHandler = new MockHandler(mockSettings);
        Invocation invocation = new Invocation();
        mockHandler.mockSettings.setDefaultAnswer(new Answer() {
            public Object answer(Invocation invocation) throws Throwable {
                return "Default Answer";
            }
        });
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        assertEquals("Default Answer", result);
        assertNotNull(mockHandler.invocationContainerImpl.getInvocationForPotentialStubbing());
    }
    
    @Test
    public void testHandle_WhenVerificationModeIsMockAware_AndVerificationIsStartedOnTheCorrectMock_ShouldVerifyDataAndReturnNull() {
        // Arrange
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler mockHandler = new MockHandler(mockSettings);
        Invocation invocation = new Invocation();
        MockAwareVerificationMode verificationMode = new MockAwareVerificationMode(mockHandler);
        mockHandler.mockingProgress.pushVerificationMode(verificationMode);
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        assertNull(result);
        assertNotNull(mockHandler.invocationContainerImpl.getMethodForStubbing());
    }
    
    @Test
    public void testHandle_WhenVerificationModeIsMockAware_AndVerificationIsStartedOnDifferentMock_ShouldSetInvocationForPotentialStubbingAndReturnDefaultAnswer() {
        // Arrange
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler mockHandler = new MockHandler(mockSettings);
        Invocation invocation = new Invocation();
        MockAwareVerificationMode verificationMode = new MockAwareVerificationMode(mockHandler);
        mockHandler.mockingProgress.pushVerificationMode(verificationMode);
        invocation.setMock(new Object());
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        assertEquals("Default Answer", result);
        assertNotNull(mockHandler.invocationContainerImpl.getInvocationForPotentialStubbing());
    }
    
    @Test
    public void testHandle_WhenStubbedInvocationIsNotNull_ShouldCaptureArgumentsAndReturnAnswer() {
        // Arrange
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler mockHandler = new MockHandler(mockSettings);
        Invocation invocation = new Invocation();
        StubbedInvocationMatcher stubbedInvocation = new StubbedInvocationMatcher();
        mockHandler.invocationContainerImpl.addAnswerForStubbing(stubbedInvocation);
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        assertNotNull(result);
        assertTrue(stubbedInvocation.isArgumentsCaptured());
    }
    
    @Test
    public void testHandle_WhenStubbedInvocationIsNull_ShouldReturnDefaultAnswer() {
        // Arrange
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler mockHandler = new MockHandler(mockSettings);
        Invocation invocation = new Invocation();
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        assertEquals("Default Answer", result);
        assertNull(mockHandler.invocationContainerImpl.getMethodForStubbing());
    }
    
    @Test
    public void testVoidMethodStubbable_ShouldReturnVoidMethodStubbableImpl() {
        // Arrange
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler mockHandler = new MockHandler(mockSettings);
        Object mock = new Object();
        
        // Act
        VoidMethodStubbable result = mockHandler.voidMethodStubbable(mock);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof VoidMethodStubbableImpl);
    }
    
    @Test
    public void testGetMockSettings_ShouldReturnMockSettingsImpl() {
        // Arrange
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler mockHandler = new MockHandler(mockSettings);
        
        // Act
        MockSettingsImpl result = mockHandler.getMockSettings();
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof MockSettingsImpl);
    }
    
    @Test
    public void testSetAnswersForStubbing_ShouldSetAnswersForStubbingToInvocationContainer() {
        // Arrange
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler mockHandler = new MockHandler(mockSettings);
        List<Answer> answers = new ArrayList<>();
        
        // Act
        mockHandler.setAnswersForStubbing(answers);
        
        // Assert
        assertEquals(answers, mockHandler.invocationContainerImpl.getAnswersForStubbing());
    }
    
    @Test
    public void testGetInvocationContainer_ShouldReturnInvocationContainerImpl() {
        // Arrange
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler mockHandler = new MockHandler(mockSettings);
        
        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof InvocationContainerImpl);
    }
}