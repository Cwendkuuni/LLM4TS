import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<MockedClass> mockHandler;
    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
        mockHandler = new MockHandler<>(mockSettings);
    }

    @Test
    public void handle_InvocationWithStubbingVoidMethod_ReturnsNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.getInvocationContainer().hasAnswersForStubbing()).thenReturn(true);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockHandler.getMockingProgress().getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(mockHandler.getMatchersBinder().bindMatchers(any(), any())).thenReturn(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(mockHandler.getInvocationContainer()).setMethodForStubbing(eq(invocationMatcher));
        assertNull(result);
    }

    @Test
    public void handle_InvocationWithVerificationMode_CallsVerification() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.getVerificationMode()).thenReturn(mock(VerificationMode.class));
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockHandler.getMockingProgress().getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(mockHandler.getMatchersBinder().bindMatchers(any(), any())).thenReturn(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(mockHandler.getMockingProgress()).pullVerificationMode();
        verify(mockHandler.getInvocationContainer()).getInvocations();
        verify(mockHandler.getVerificationMode()).verify(any());
        assertNull(result);
    }

    @Test
    public void handle_InvocationWithStubbedInvocation_ReturnsExpectedResult() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.getVerificationMode()).thenReturn(null);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockHandler.getMockingProgress().getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(mockHandler.getMatchersBinder().bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.getInvocationContainer().findAnswerFor(eq(invocation))).thenReturn(mock(StubbedInvocationMatcher.class));
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(mock(Answer.class));

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(mockHandler.getInvocationContainer(), times(1)).findAnswerFor(eq(invocation));
        assertNotNull(result);
    }

    @Test
    public void voidMethodStubbable_ReturnsVoidMethodStubbableImpl() {
        // Arrange
        MockedClass mockedClass = mock(MockedClass.class);

        // Act
        VoidMethodStubbable<MockedClass> result = mockHandler.voidMethodStubbable(mockedClass);

        // Assert
        assertNotNull(result);
        assertEquals(VoidMethodStubbableImpl.class, result.getClass());
        assertEquals(mockedClass, result.getMock());
        assertEquals(mockHandler.getInvocationContainer(), result.getInvocationContainer());
    }

    @Test
    public void getMockSettings_ReturnsMockSettingsImpl() {
        // Act
        MockSettingsImpl result = mockHandler.getMockSettings();

        // Assert
        assertNotNull(result);
        assertEquals(MockSettingsImpl.class, result.getClass());
    }

    @Test
    public void setAnswersForStubbing_SetsAnswersForStubbing() {
        // Arrange
        List<Answer> answers = mock(List.class);

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(mockHandler.getInvocationContainer()).setAnswersForStubbing(eq(answers));
    }

    @Test
    public void getInvocationContainer_ReturnsInvocationContainerImpl() {
        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();

        // Assert
        assertNotNull(result);
        assertEquals(InvocationContainerImpl.class, result.getClass());
    }

}