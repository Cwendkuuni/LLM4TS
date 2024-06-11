import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler mockHandler;

    @Before
    public void setup() {
        mockHandler = new MockHandler();
    }

    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = mock(Invocation.class);

        mockHandler.handle(invocation);

        verify(invocationContainerImpl, times(1)).hasAnswersForStubbing();
        verify(matchersBinder, times(1)).bindMatchers(any(), eq(invocation));
        verify(invocationContainerImpl, times(1)).setMethodForStubbing(any());
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(mockingProgress, times(1)).validateState();
        verify(verificationMode, times(1)).verify(any());
        verify(InvocationContainerImpl, times(1)).setInvocationForPotentialStubbing(any());
        verify(mockingProgress, times(1)).reportOngoingStubbing(any());
        verify(invocationContainerImpl, times(1)).findAnswerFor(any());
        verify(stubbedInvocationMatcher, times(1)).captureArgumentsFrom(any());
        verify(stubbedInvocationMatcher, times(1)).answer(any());
        verify(mockSettings.getDefaultAnswer(), times(1)).answer(any());
        verify(invocationContainerImpl, times(1)).resetInvocationForPotentialStubbing(any());
    }

    @Test
    public void testVoidMethodStubbable() {
        mockHandler.voidMethodStubbable(null);

        verify(mock, times(1)).voidMethodStubbable(any());
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mockHandler.getMockSettings();

        assertNotNull(mockSettings);
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl, times(1)).setAnswersForStubbing(any());
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer invocationContainer = mockHandler.getInvocationContainer();

        assertNotNull(invocationContainer);
    }
}