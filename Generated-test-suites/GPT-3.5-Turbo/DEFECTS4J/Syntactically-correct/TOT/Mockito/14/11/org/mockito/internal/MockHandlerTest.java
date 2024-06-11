import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationContainer;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationData;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainer;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
        mockHandler = new MockHandler<>(mockSettings);
        invocationContainer = new InvocationContainerImpl(mockHandler.mockingProgress);
        mockHandler.invocationContainerImpl = invocationContainer;
    }

    @Test
    public void handle_whenInvocationContainerHasAnswersForStubbing_shouldSetMethodForStubbingAndReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);

        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(mockHandler.matchersBinder.bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(mock(InvocationMatcher.class));

        Object result = mockHandler.handle(invocation);

        verify(invocationContainer).setMethodForStubbing(any(InvocationMatcher.class));
        assertNull(result);
    }

    @Test
    public void handle_whenInvocationContainerDoesNotHaveAnswersForStubbingAndVerificationModeIsNull_shouldSetInvocationForPotentialStubbingAndReturnStubbedAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);

        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.matchersBinder.bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(mock(InvocationMatcher.class));
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn("mocked value");

        Object result = mockHandler.handle(invocation);

        verify(invocationContainer).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        assertEquals("mocked value", result);
    }

    @Test
    public void handle_whenInvocationContainerDoesNotHaveAnswersForStubbingAndVerificationModeIsNotNull_shouldVerifyInvocationAndReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationData verificationData = mock(VerificationData.class);
        VerificationMode verificationMode = mock(VerificationMode.class);

        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.matchersBinder.bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(mock(InvocationMatcher.class));
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocationContainer.getInvocations()).thenReturn(Arrays.asList(mock(Invocation.class)));

        Object result = mockHandler.handle(invocation);

        verify(verificationMode).verify(verificationData);
        assertNull(result);
    }

    @Test
    public void voidMethodStubbable_shouldReturnInstanceOfVoidMethodStubbable() {
        Object mock = new Object();

        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        assertNotNull(voidMethodStubbable);
        assertTrue(voidMethodStubbable instanceof VoidMethodStubbableImpl);
    }

    @Test
    public void getMockSettings_shouldReturnMockSettingsInstance() {
        MockSettingsImpl settings = mockHandler.getMockSettings();

        assertNotNull(settings);
        assertTrue(settings instanceof MockSettingsImpl);
    }

    @Test
    public void setAnswersForStubbing_shouldSetAnswersForStubbingInTheInvocationContainer() {
        List<Answer> answers = Arrays.asList(mock(Answer.class));

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainer).setAnswersForStubbing(answers);
    }

    @Test
    public void getInvocationContainer_shouldReturnInvocationContainerInstance() {
        InvocationContainer container = mockHandler.getInvocationContainer();

        assertNotNull(container);
        assertTrue(container instanceof InvocationContainerImpl);
    }
}