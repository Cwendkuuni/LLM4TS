
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationData;

public class MockHandlerTest {

    MockHandler<Object> mockHandler;

    @Before
    public void setup() {
        mockHandler = new MockHandler<Object>();
    }

    @Test
    public void handle_invocationContainsAnswersForStubbing_returnsNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void handle_verificationModeNotNull_verifiesInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationDataImpl verificationDataImpl = mock(VerificationDataImpl.class);
        VerificationMode verificationMode = mock(VerificationMode.class);

        when(mockHandler.invocationContainerImpl.getInvocations()).thenReturn(invocation);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.mockingProgress.validateState()).thenReturn(true);
        mockHandler.handle(invocation);

        verify(verificationMode).verify(verificationDataImpl);
    }

    @Test
    public void handle_stubbedInvocationNotNull_capturesArgumentsAndReturnsAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocationMatcher);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(null);
        mockHandler.handle(invocation);

        verify(stubbedInvocationMatcher).captureArgumentsFrom(invocation);
    }

    @Test
    public void handle_stubbedInvocationNull_returnsDefaultAnswer_ReturnsAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        Object expectedReturn = new Object();

        when(invocation.getMethod()).thenReturn(method);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockHandler.mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(expectedReturn);
        Object actualReturn = mockHandler.handle(invocation);

        assertEquals(expectedReturn, actualReturn);
    }

    @Test
    public VoidMethodStubbable<T> voidMethodStubbable() {
        T mock = mock(T.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        VoidMethodStubbableImpl<T> expectedStubbable = new VoidMethodStubbableImpl<T>(mock, invocationContainerImpl);

        VoidMethodStubbableImpl<T> actualStubbable = mockHandler.voidMethodStubbable(mock);

        assertEquals(expectedStubbable, actualStubbable);
    }

    @Test
    public void getMockSettings() {
        MockSettingsImpl expectedSettings = new MockSettingsImpl();

        MockSettingsImpl actualSettings = mockHandler.getMockSettings();

        assertEquals(expectedSettings, actualSettings);
    }

    @Test
    public void setAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();
        mockHandler.setAnswersForStubbing(answers);

        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void getInvocationContainer() {
        InvocationContainer expectedContainer = new InvocationContainerImpl(mock(MockingProgress.class));

        InvocationContainer actualContainer = mockHandler.getInvocationContainer();

        assertEquals(expectedContainer, actualContainer);
    }
}
