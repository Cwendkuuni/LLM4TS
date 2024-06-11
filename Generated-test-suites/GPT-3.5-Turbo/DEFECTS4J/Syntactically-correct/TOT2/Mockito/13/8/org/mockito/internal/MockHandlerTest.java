import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;

    @Before
    public void setUp() {
        mockHandler = new MockHandler<>(new MockSettingsImpl());
    }

    @Test
    public void testHandle_WhenInvocationHasAnswersForStubbing_ReturnsNull() throws Throwable {
        InvocationContainerImpl invocationContainer = Mockito.mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainer;

        Invocation invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);

        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void testHandle_WhenVerificationModeIsNotNull_VerifiesExpectedInvocationThenReturnsNull() throws Throwable {
        InvocationContainerImpl invocationContainer = Mockito.mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainer;

        Invocation invocation = Mockito.mock(Invocation.class);

        MockAwareVerificationMode verificationMode = Mockito.mock(MockAwareVerificationMode.class);
        Mockito.when(verificationMode.getMock()).thenReturn(invocation.getMock());

        Mockito.when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void testVoidMethodStubbable_CreatesNewVoidMethodStubbableInstance() {
        Object mock = new Object();

        assertNotNull(mockHandler.voidMethodStubbable(mock));
    }

    @Test
    public void testGetMockSettings_ReturnsCorrectMockSettings() {
        assertEquals(new MockSettingsImpl(), mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing_SetsAnswersForStubbingInInvocationContainer() {
        List<Answer> answers = Mockito.mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }

}