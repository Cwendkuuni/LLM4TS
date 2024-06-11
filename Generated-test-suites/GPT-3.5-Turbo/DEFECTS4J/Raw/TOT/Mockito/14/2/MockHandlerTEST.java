import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbingImpl;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private ThreadSafeMockingProgress mockingProgress;
    private MockSettingsImpl mockSettings;
    private MockHandlerInterface<Object> oldMockHandler;
    private static final ArgumentMatcher<Invocation> invocationMatcher = Mockito.argThat(
            new ArgumentMatcher<Invocation>() {
                public boolean matches(Invocation argument) {
                    // match implementation goes here
                    return true;
                }
            }
    );

    @Before
    public void setUp() {
        mockSettings = Mockito.mock(MockSettingsImpl.class);
        mockingProgress = Mockito.mock(ThreadSafeMockingProgress.class);
        matchersBinder = Mockito.mock(MatchersBinder.class);
        invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
        oldMockHandler = Mockito.mock(MockHandlerInterface.class);
        mockHandler = new MockHandler<Object>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void handle_hasAnswersForStubbing_invocationMatcherSetMethodForStubbing() throws Throwable {
        // Arrange
        Mockito.when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        Mockito.when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(Mockito.mock(InvocationMatcher.class));
        // Act
        mockHandler.handle(Mockito.mock(Invocation.class));
        // Assert
        Mockito.verify(invocationContainerImpl).setMethodForStubbing(Mockito.any(InvocationMatcher.class));
    }

    @Test
    public void handle_nullVerificationMode_invocationMatcherSetInvocationForPotentialStubbing() throws Throwable {
        // Arrange
        Mockito.when(mockingProgress.pullVerificationMode()).thenReturn(null);
        Mockito.when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(Mockito.mock(InvocationMatcher.class));
        Mockito.when(invocationContainerImpl.findAnswerFor(Mockito.any())).thenReturn(null);
        Mockito.when(mockSettings.getDefaultAnswer()).thenReturn(Mockito.mock(Answer.class));
        // Act
        mockHandler.handle(Mockito.mock(Invocation.class));
        // Assert
        Mockito.verify(invocationContainerImpl).setInvocationForPotentialStubbing(Mockito.any(InvocationMatcher.class));
    }

    @Test
    public void handle_nullVerificationMode_stubbedInvocationNotNull_captureArgumentsFrom() throws Throwable {
        // Arrange
        Mockito.when(mockingProgress.pullVerificationMode()).thenReturn(null);
        Mockito.when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(Mockito.mock(InvocationMatcher.class));
        Mockito.when(invocationContainerImpl.findAnswerFor(Mockito.any())).thenReturn(Mockito.mock(StubbedInvocationMatcher.class));
        // Act
        mockHandler.handle(Mockito.mock(Invocation.class));
        // Assert
        Mockito.verify(invocationContainerImpl).findAnswerFor(Mockito.any());
        Mockito.verify(invocationContainerImpl).findAnswerFor(Mockito.any(Invocation.class));
    }

    @Test
    public void handle_nullVerificationMode_stubbedInvocationNull_mockSettingsGetDefaultAnswer() throws Throwable {
        // Arrange
        Mockito.when(mockingProgress.pullVerificationMode()).thenReturn(null);
        Mockito.when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(Mockito.mock(InvocationMatcher.class));
        Mockito.when(invocationContainerImpl.findAnswerFor(Mockito.any())).thenReturn(null);
        Mockito.when(mockSettings.getDefaultAnswer()).thenReturn(Mockito.mock(Answer.class));
        // Act
        mockHandler.handle(Mockito.mock(Invocation.class));
        // Assert
        Mockito.verify(mockSettings).getDefaultAnswer();
    }

    @Test
    public void handle_nullVerificationMode_stubbedInvocationNull_invocationContainerImplResetInvocationForPotentialStubbing() throws Throwable {
        // Arrange
        Mockito.when(mockingProgress.pullVerificationMode()).thenReturn(null);
        Mockito.when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(Mockito.mock(InvocationMatcher.class));
        Mockito.when(invocationContainerImpl.findAnswerFor(Mockito.any())).thenReturn(null);
        Mockito.when(mockSettings.getDefaultAnswer()).thenReturn(Mockito.mock(Answer.class));
        // Act
        mockHandler.handle(Mockito.mock(Invocation.class));
        // Assert
        Mockito.verify(invocationContainerImpl).resetInvocationForPotentialStubbing(Mockito.any(InvocationMatcher.class));
    }

    @Test
    public void voidMethodStubbable_voidMethodStubbable_returnsVoidMethodStubbableImpl() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        // Act
        VoidMethodStubbable actual = mockHandler.voidMethodStubbable(mock);
        // Assert
        org.junit.Assert.assertEquals(VoidMethodStubbableImpl.class, actual.getClass());
    }

    @Test
    public void getMockSettings_getMockSettings_returnsMockSettingsImpl() {
        // Arrange

        // Act
        MockSettings actual = mockHandler.getMockSettings();
        // Assert
        org.junit.Assert.assertEquals(MockSettingsImpl.class, actual.getClass());
    }

    @Test
    public void setAnswersForStubbing_setAnswersForStubbing_invocationContainerSetAnswersForStubbing() {
        // Arrange
        List<Answer> answers = new ArrayList<Answer>();
        // Act
        mockHandler.setAnswersForStubbing(answers);
        // Assert
        Mockito.verify(invocationContainerImpl).setAnswersForStubbing(Mockito.anyList());
    }

    @Test
    public void getInvocationContainer_getInvocationContainer_returnsInvocationContainerImpl() {
        // Arrange

        // Act
        InvocationContainer actual = mockHandler.getInvocationContainer();
        // Assert
        org.junit.Assert.assertEquals(InvocationContainerImpl.class, actual.getClass());
    }
}