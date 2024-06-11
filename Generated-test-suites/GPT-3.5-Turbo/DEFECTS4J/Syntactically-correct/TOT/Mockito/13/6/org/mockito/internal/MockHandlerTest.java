import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<TestClass> mockHandler;
    private InvocationContainerImpl invocationContainer;

    @Before
    public void setUp() {
        mockHandler = new MockHandler<>();
        invocationContainer = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainer;
    }

    @Test
    public void handle_invocationContainerHasAnswersForStubbing_shouldSetMethodForStubbingAndReturnNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
        verifyNoMoreInteractions(invocationContainer);
        assertNull(result);
    }

    @Test
    public void handle_verificationModeIsNull_shouldSetInvocationForPotentialStubbingAndReturnDefaultAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        when(mockHandler.mockSettings.getDefaultAnswer().answer(invocation)).thenReturn("default answer");

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verifyNoMoreInteractions(invocationContainer);
        verify(mockHandler.mockSettings).getDefaultAnswer();
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
        assertEquals("default answer", result);
    }

    @Test
    public void handle_verificationModeIsNull_shouldSkipStubbedInvocationAndReturnNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn("stubbed answer");

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verifyNoMoreInteractions(invocationContainer);
        verifyZeroInteractions(mockHandler.mockSettings);
        verifyZeroInteractions(invocationContainer);
        assertEquals("stubbed answer", result);
    }

    @Test
    public void handle_verificationModeIsNotNullAndMockIsDifferent_shouldVerifyAndReturnNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);

        when(invocation.getMock()).thenReturn(mock(TestClass.class));
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verifyZeroInteractions(invocationContainer);
        verify(mockHandler.mockingProgress).pullVerificationMode();
        assertNull(result);
    }

    @Test
    public void voidMethodStubbable_shouldCreateVoidMethodStubbableWithCorrectParameters() {
        // Arrange
        TestClass mock = mock(TestClass.class);

        // Act
        VoidMethodStubbable result = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof VoidMethodStubbableImpl);
        assertEquals(mock, result.getMock());
        assertEquals(invocationContainer, result.getInvocationContainer());
    }

    @Test
    public void setAnswersForStubbing_shouldSetAnswersForStubbingInInvocationContainer() {
        // Arrange
        List<Answer> answers = mock(List.class);

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(invocationContainer).setAnswersForStubbing(answers);
    }

    @Test
    public void getInvocationContainer_shouldReturnInvocationContainer() {
        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();

        // Assert
        assertEquals(invocationContainer, result);
    }
}