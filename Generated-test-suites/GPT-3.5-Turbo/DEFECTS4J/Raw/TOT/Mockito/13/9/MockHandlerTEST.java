import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;
import org.junit.Before;
import org.junit.Test;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockSettings = mock(MockSettingsImpl.class);
        mockHandler = new MockHandler<>(mockSettings);
    }

    @Test
    public void handle_shouldSetMethodForStubbing_whenInvokedWithValidInvocation() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        verify(mockSettings).getDefaultAnswer();
        verifyZeroInteractions(invocation);
        assertNotNull(result);
    }

    @Test
    public void handle_shouldValidateState_whenInvokedWithValidInvocation() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        verify(mockHandler.mockingProgress).validateState();
        assertNotNull(result);
    }

    @Test
    public void handle_shouldVerifyAndReturnNull_whenVerificationModeIsNotNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(mock(VerificationMode.class));
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        verify(mockHandler.mockingProgress).pullVerificationMode();
        verifyZeroInteractions(invocation);
        assertNotNull(result);
    }

    @Test
    public void handle_shouldSetInvocationForPotentialStubbingAndReturnNull_whenStubbedInvocationIsNotNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(mock(StubbedInvocationMatcher.class));
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockHandler.mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(mockHandler.invocationContainerImpl).findAnswerFor(invocation);
        assertNotNull(result);
    }

    @Test
    public void handle_shouldCallDefaultAnswerAndReturnResult_whenStubbedInvocationIsNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        Object expectedResult = new Object();
        when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(expectedResult);
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockSettings.getDefaultAnswer()).answer(invocation);
        assertEquals(expectedResult, result);
    }

    @Test
    public void voidMethodStubbable_shouldReturnNewVoidMethodStubbableInstance() {
        // Arrange
        Object mock = new Object();
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(mockHandler.voidMethodStubbable(mock)).thenCallRealMethod();

        // Act
        VoidMethodStubbable result = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertNotNull(result);
        assertEquals(mock, result.getMock());
        assertEquals(invocationContainerImpl, result.getInvocationContainer());
    }

    @Test
    public void getMockSettings_shouldReturnMockSettings() {
        // Act
        MockSettingsImpl result = mockHandler.getMockSettings();

        // Assert
        assertEquals(mockSettings, result);
    }

    @Test
    public void setAnswersForStubbing_shouldSetAnswersForStubbingInInvocationContainer() {
        // Arrange
        List<Answer> answers = new ArrayList<>();
        when(mockHandler.invocationContainerImpl.setAnswersForStubbing(answers)).thenCallRealMethod();

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void getInvocationContainer_shouldReturnInvocationContainer() {
        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();

        // Assert
        assertEquals(mockHandler.invocationContainerImpl, result);
    }
}