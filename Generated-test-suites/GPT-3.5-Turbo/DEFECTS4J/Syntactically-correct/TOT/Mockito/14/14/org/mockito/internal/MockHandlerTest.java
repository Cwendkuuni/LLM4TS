import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.verificiation.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<Object> handler;

    @Before
    public void setup() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        handler = new MockHandler<>(mockSettings);
    }

    @Test
    public void testHandle_WhenInvocationContainerHasAnswersForStubbing_ShouldBindMatchersAndReturnNull() throws Throwable {
        // Arrange
        Invocation invocation = new Invocation();
        handler.invocationContainerImpl.setAnswersForStubbing(new ArrayList<Answer>());

        // Act
        Object result = handler.handle(invocation);

        // Assert
        InvocationMatcher invocationMatcher = handler.matchersBinder.bindMatchers(handler.mockingProgress.getArgumentMatcherStorage(), invocation);
        assertEquals(result, null);
        assertEquals(handler.invocationContainerImpl.getMethodForStubbing(), invocationMatcher);
    }

    @Test
    public void testHandle_WhenVerificationModeNotNull_ShouldVerifyInvocation() throws Throwable {
        // Arrange
        Invocation invocation = new Invocation();
        VerificationMode verificationMode = mock(VerificationMode.class);
        handler.mockingProgress.pushVerificationMode(verificationMode);

        // Act
        Object result = handler.handle(invocation);

        // Assert
        InvocationMatcher invocationMatcher = handler.matchersBinder.bindMatchers(handler.mockingProgress.getArgumentMatcherStorage(), invocation);
        verify(verificationMode).verify(new VerificationDataImpl(handler.invocationContainerImpl.getInvocations(), invocationMatcher));
        assertEquals(result, null);
    }

    @Test
    public void testHandle_WhenInvocationIsStubbed_ShouldReturnStubbedAnswer() throws Throwable {
        // Arrange
        Invocation invocation = new Invocation();
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(handler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn("Stubbed Answer");

        // Act
        Object result = handler.handle(invocation);

        // Assert
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        assertEquals(result, "Stubbed Answer");
    }

    @Test
    public void testHandle_WhenNoStubbedInvocation_ShouldReturnDefaultAnswer() throws Throwable {
        // Arrange
        Invocation invocation = new Invocation();
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.setDefaultAnswer(invocation1 -> "Default Answer");
        handler = new MockHandler<>(mockSettings);

        // Act
        Object result = handler.handle(invocation);

        // Assert
        verify(handler.invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
        assertEquals(result, "Default Answer");
    }

    @Test
    public void testVoidMethodStubbable_ShouldReturnVoidMethodStubbable() {
        // Arrange
        Object mock = new Object();

        // Act
        VoidMethodStubbable<Object> stubbable = handler.voidMethodStubbable(mock);

        // Assert
        assertEquals(stubbable.getMock(), mock);
        assertEquals(stubbable.getInvocationContainer(), handler.invocationContainerImpl);
    }

    @Test
    public void testSetAnswersForStubbing_ShouldSetAnswersForStubbing() {
        // Arrange
        List<Answer> answers = new ArrayList<>();

        // Act
        handler.setAnswersForStubbing(answers);

        // Assert
        assertEquals(handler.invocationContainerImpl.getAnswersForStubbing(), answers);
    }

    @Test
    public void testGetMockSettings_ShouldReturnMockSettings() {
        // Act
        MockSettingsImpl mockSettings = handler.getMockSettings();

        // Assert
        assertEquals(mockSettings, handler.mockSettings);
    }

    @Test
    public void testGetInvocationContainer_ShouldReturnInvocationContainer() {
        // Act
        InvocationContainer container = handler.getInvocationContainer();

        // Assert
        assertEquals(container, handler.invocationContainerImpl);
    }
}