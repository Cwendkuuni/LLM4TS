import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.List;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private InvocationContainerImpl invocationContainer;
    private MockSettingsImpl mockSettings;
    private Answer<Object> defaultAnswer;

    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        mockHandler = new MockHandler<>(mockSettings);
        invocationContainer = new InvocationContainerImpl();
        defaultAnswer = mock(Answer.class);
    }

    @Test
    public void handle_ProvideInvocationWithAnswersForStubbing_ReturnsNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);

        // Act
        Object actual = mockHandler.handle(invocation);

        // Assert
        assertNull(actual);
        verify(invocationContainer).setMethodForStubbing(invocation);
    }

    @Test
    public void handle_ProvideInvocationWithVerificationMode_VerifyInvocationIfMatch() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(mockSettings.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);

        // Act
        Object actual = mockHandler.handle(invocation);

        // Assert
        assertNull(actual);
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void handle_ProvideInvocationWithoutAnswersForStubbing_InvokesDefaultAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn("Mocked Result");
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);

        // Act
        Object actual = mockHandler.handle(invocation);

        // Assert
        assertEquals("Mocked Result", actual);
        verify(defaultAnswer).answer(invocation);
    }

    @Test
    public void voidMethodStubbable_ProvideMock_ReturnsVoidMethodStubbable() {
        // Arrange
        Object mock = new Object();

        // Act
        VoidMethodStubbable<Object> stubbable = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertNotNull(stubbable);
        assertSame(mock, stubbable.getMock());
    }

    @Test
    public void getMockSettings_ReturnsMockSettingsImplObject() {
        // Act
        MockSettingsImpl actual = mockHandler.getMockSettings();

        // Assert
        assertSame(mockSettings, actual);
    }

    @Test
    public void setAnswersForStubbing_ProvideListOfAnswers_SetsAnswersForStubbing() {
        // Arrange
        List<Answer> answers = mock(List.class);

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(invocationContainer).setAnswersForStubbing(answers);
    }

    @Test
    public void getInvocationContainer_ReturnsInvocationContainerImplObject() {
        // Act
        InvocationContainer actual = mockHandler.getInvocationContainer();

        // Assert
        assertEquals(invocationContainer, actual);
    }
}