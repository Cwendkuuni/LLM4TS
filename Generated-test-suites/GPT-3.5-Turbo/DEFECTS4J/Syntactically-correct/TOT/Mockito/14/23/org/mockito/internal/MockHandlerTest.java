
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MockitoInvocationHandler;
import org.mockito.internal.invocation.VerifiableInvocationImpl;
import org.mockito.internal.stubbing.InvocationContainer;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private MockHandlerInterface<Object> mockHandlerInterface;
    private InvocationContainer invocationContainer;
    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
        mockHandler = new MockHandler<>(mockSettings);
        mockHandlerInterface = mock(MockHandlerInterface.class);
        invocationContainer = mock(InvocationContainer.class);
    }

    @Test
    public void handle_shouldSetMethodForStubbing_whenInvocationContainerHasAnswersForStubbing() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void handle_shouldValidateState_whenInvocationContainerHasAnswersForStubbing() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(mockingProgress).validateState();
    }

    @Test
    public void handle_shouldPullVerificationMode_whenInvocationContainerHasAnswersForStubbing() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(mockingProgress).pullVerificationMode();
    }

    @Test
    public void handle_shouldBindMatchers_whenInvocationContainerHasAnswersForStubbing() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
    }

    @Test
    public void handle_shouldReturnNull_whenInvocationContainerHasAnswersForStubbing() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
    }

    @Test
    public void handle_shouldVerifyData_whenVerificationModeIsNotNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void handle_shouldSetInvocationForPotentialStubbing_whenVerificationModeIsNotNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void handle_shouldReportOngoingStubbing_whenVerificationModeIsNotNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
    }

    @Test
    public void handle_shouldFindAnswerForInvocation_whenStubbedInvocationMatcherIsNotNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocationMatcher);

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(stubbedInvocationMatcher).captureArgumentsFrom(invocation);
    }

    @Test
    public void handle_shouldAnswerWithStubbedInvocation_whenStubbedInvocationMatcherIsNotNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocationMatcher);

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(stubbedInvocationMatcher).answer(invocation);
    }

    @Test
    public void handle_shouldAnswerWithDefaultAnswer_whenStubbedInvocationMatcherIsNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
    }

    @Test
    public void handle_shouldResetInvocationForPotentialStubbing_whenStubbedInvocationMatcherIsNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void voidMethodStubbable_shouldReturnVoidMethodStubbableImpl() {
        // Arrange
        Object mock = new Object();

        // Act
        VoidMethodStubbable<Object> stubbable = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertNotNull(stubbable);
        assertTrue(stubbable instanceof VoidMethodStubbableImpl);
        assertEquals(mock, ((VoidMethodStubbableImpl<Object>) stubbable).getMock());
        assertEquals(invocationContainerImpl, ((VoidMethodStubbableImpl<Object>) stubbable).getInvocationContainer());
    }

    @Test
    public void getMockSettings_shouldReturnMockSettings() {
        // Act
        MockSettingsImpl settings = mockHandler.getMockSettings();

        // Assert
        assertNotNull(settings);
        assertEquals(mockSettings, settings);
    }

    @Test
    public void setAnswersForStubbing_shouldSetAnswersForStubbingInInvocationContainer() {
        // Arrange
        List<Answer> answers = new ArrayList<>();

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(invocationContainerImpl).setAnswersForStubbing(eq(answers));
    }

    @Test
    public void getInvocationContainer_shouldReturnInvocationContainer() {
        // Act
        InvocationContainer container = mockHandler.getInvocationContainer();

        // Assert
        assertNotNull(container);
        assertEquals(invocationContainerImpl, container);
    }
}
