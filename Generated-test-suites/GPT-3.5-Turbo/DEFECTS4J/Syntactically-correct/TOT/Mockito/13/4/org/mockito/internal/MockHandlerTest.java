import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;

    @Mock
    private MockingProgress mockingProgress;

    @Mock
    private MatchersBinder matchersBinder;

    @Mock
    private InvocationContainerImpl invocationContainerImpl;

    @Before
    public void setup() {
        mockHandler = new MockHandler<Object>(new MockHandlerInterface<Object>() {
            @Override
            public MockSettingsImpl getMockSettings() {
                return null;
            }
        });
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
    }

    @Test
    public void testHandle_withStubs() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);

        mockHandler.handle(invocation);

        verify(invocationContainerImpl).setMethodForStubbing(any());
    }

    @Test
    public void testHandle_verifyModeVerification() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(Mockito.mock(VerificationMode.class));
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(Mockito.mock(InvocationMatcher.class));

        mockHandler.handle(invocation);

        verify(mockingProgress).pullVerificationMode();
        verify(matchersBinder).bindMatchers(any(), any());
    }

    @Test
    public void testHandle_verifyWrongMock() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        VerificationMode verificationMode = Mockito.mock(MockAwareVerificationMode.class);
        when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(Mockito.mock(InvocationMatcher.class));

        mockHandler.handle(invocation);

        verify(mockingProgress).pullVerificationMode();
        verify(matchersBinder, never()).bindMatchers(any(), any());
    }

    @Test
    public void testHandle_stubbedInvocationFound() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = Mockito.mock(StubbedInvocationMatcher.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(Mockito.mock(InvocationMatcher.class));
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);

        mockHandler.handle(invocation);

        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
    }

    @Test
    public void testHandle_stubbedInvocationNotFound() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer<Object> defaultAnswer = Mockito.mock(Answer.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(Mockito.mock(InvocationMatcher.class));
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn(null);

        mockHandler.handle(invocation);

        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any());
        verify(defaultAnswer).answer(invocation);
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mockObject = new Object();
        VoidMethodStubbable<Object> stubbable = mockHandler.voidMethodStubbable(mockObject);

        assertNotNull(stubbable);
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();
        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, invocationContainerImpl.getAnswersForStubbing());
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer container = mockHandler.getInvocationContainer();

        assertNotNull(container);
        assertEquals(invocationContainerImpl, container);
    }
}