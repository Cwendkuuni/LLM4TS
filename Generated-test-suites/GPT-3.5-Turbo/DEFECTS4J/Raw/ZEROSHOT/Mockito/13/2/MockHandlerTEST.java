import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;
import java.util.List;

public class MockHandlerTest {

    private MockHandler<TestClass> mockHandler;
    private InvocationContainerImpl invocationContainer;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;
    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
        mockingProgress = new ThreadSafeMockingProgress();
        matchersBinder = new MatchersBinder();
        invocationContainer = new InvocationContainerImpl(mockingProgress);
        mockHandler = new MockHandler<>(mockSettings);
    }

    @Test
    public void testHandle_WhenAnswersForStubbingExist() throws Throwable {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        matchersBinder = Mockito.spy(MatchersBinder.class);
        mockHandler.invocationContainerImpl = Mockito.spy(invocationContainer);
        Mockito.when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        Mockito.when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        Mockito.verify(mockHandler.invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        Mockito.verifyZeroInteractions(result); // void method, so no return value
    }

    @Test
    public void testHandle_WhenVerificationModeExists_AndVerificationModeIsMockAware_AndMockAwareVerificationModeMatchesInvocationMock() throws Throwable {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        VerificationMode verificationMode = Mockito.mock(MockAwareVerificationMode.class);
        Mockito.when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        Mockito.when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        Mockito.when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());
        matchersBinder = Mockito.spy(MatchersBinder.class);
        mockHandler.invocationContainerImpl = Mockito.spy(invocationContainer);
        Mockito.when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.never()).setInvocationForPotentialStubbing(invocationMatcher);
        Mockito.verifyZeroInteractions(result); // void method, so no return value
    }

    @Test
    public void testHandle_WhenStubbedInvocationExists() throws Throwable {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = Mockito.mock(StubbedInvocationMatcher.class);
        Mockito.when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        Mockito.when(stubbedInvocation.answer(invocation)).thenReturn("mocked response");
        matchersBinder = Mockito.spy(MatchersBinder.class);
        mockHandler.invocationContainerImpl = Mockito.spy(invocationContainer);
        Mockito.when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        Mockito.verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        Mockito.verify(stubbedInvocation).captureArgumentsFrom(invocation);
        Mockito.verify(stubbedInvocation).answer(invocation);
        Mockito.verifyZeroInteractions(result);
    }

    @Test
    public void testHandle_WhenNoStubbedInvocationExists() throws Throwable {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        mockHandler.invocationContainerImpl = Mockito.spy(invocationContainer);
        Mockito.when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        mockSettings.setDefaultAnswer(Mockito.mock(Answer.class));
        Mockito.when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn("mocked response");
        matchersBinder = Mockito.spy(MatchersBinder.class);
        Mockito.when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        Mockito.verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        Mockito.verify(mockSettings.getDefaultAnswer()).answer(invocation);
        Mockito.verifyZeroInteractions(result);
    }

    @Test
    public void testVoidMethodStubbable() {
        // Arrange
        TestClass mock = new TestClass();
        mockHandler.invocationContainerImpl = Mockito.spy(invocationContainer);

        // Act
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);

        // Assert
        Mockito.verify(invocationContainer).addVoidMethodStubbable(stubbable);
        Mockito.verifyZeroInteractions(stubbable); // since it's just a setter method
    }

    @Test
    public void testSetAnswersForStubbing() {
        // Arrange
        List<Answer> answers = Mockito.mock(List.class);
        Mockito.when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        Mockito.verify(invocationContainer).setAnswersForStubbing(answers);
    }

    private class TestClass {}

}