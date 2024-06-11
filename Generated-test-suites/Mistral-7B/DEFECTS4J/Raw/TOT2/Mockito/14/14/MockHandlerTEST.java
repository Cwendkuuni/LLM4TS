import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MockHandlerTest {

    private MockHandler<MockitoAnswer> mockHandler;

    @Before
    public void setup() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockHandler = new MockHandler<>(mockSettings);
    }

    @Test
    public void testHandle_NoAnswers_ReturnsDefault() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(Invocation.class);
        Object result = mockHandler.handle(invocation);
        Mockito.verifyNoMoreInteractions(invocation);
        Mockito.assertEquals(mockSettings.getDefaultAnswer().answer(invocation), result);
    }

    @Test
    public void testHandle_StubbedMethod_ReturnsAnswer() throws Throwable {
        Answer answer = Mockito.mock(Answer.class);
        InvocationOnMock invocation = Mockito.mock(Invocation.class);
        mockHandler.setAnswersForStubbing(Mockito.singletonList(answer));
        mockHandler.handle(invocation);
        Object[] arguments = invocation.getArguments();
        Mockito.verify(answer).answer(invocation);
        Mockito.verify(answer).arguments(arguments);
    }

    @Test(expected = IllegalStateException.class)
    public void testHandle_VerificationModeNotEmpty_ThrowsException() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(Invocation.class);
        VerificationMode verificationMode = Mockito.verifyNoMoreInteractions(invocation);
        mockHandler.handle(invocation);
    }
}