import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stub;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*; // import statics for readability

public class MockHandlerTest {
    private MockHandler<MockitoInvocationHandler> mockHandler;
    private InvocationOnMock invocationOnMock;
    private Stub<Answer> stub;
    private VerificationMode verificationMode;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>();
        invocationOnMock = mock(InvocationOnMock.class);
        stub = stub(Answer.class);
        verificationMode = verify(any());
    }

    @Test
    public void testHandleMethod() throws Throwable {
        when(mockHandler.handle(invocationOnMock)).thenReturn(stub.get());
        mockHandler.handle(invocationOnMock);
        verificationMode.verify(any()); // verify that handle method was called
    }

    @Test(expected = NullPointerException.class)
    public void testHandleNullInvocation() throws Throwable {
        when(mockHandler.handle(null)).thenThrow(NullPointerException.class);
        mockHandler.handle(null);
    }

    @Test
    public void testEmptyAnswersForStubbing() throws Throwable {
        when(mockHandler.setAnswersForStubbing(Collections.emptyList())).thenCallRealMethod();
        mockHandler.handle(invocationOnMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInvocationMatcher() throws Throwable {
        when(mockHandler.handle(invocationOnMock)).thenThrow(IllegalArgumentException.class);
        mockHandler.handle(invocationOnMock);
    }

    @Test
    public void testVerificationModeException() throws Throwable {
        when(mockingProgress.pullVerificationMode()).thenThrow(new RuntimeException("VerificationException"));
        mockHandler.handle(invocationOnMock);
    }

    @Test
    public void testVoidMethodStubbable() throws Throwable {
        VoidMethodStubbable<MockitoInvocationHandler> voidMethodStubbable = mockHandler.voidMethodStubbable(mock(MockitoInvocationHandler.class));
        voidMethodStubbable.thenAnswer(stub);
        voidMethodStubbable.answers(stub.get());
    }

    @Test
    public void testGetMockSettings() throws Throwable {
        assertEquals(mockSettings(), mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() throws Throwable {
        List<Answer> answers = Arrays.asList(stub, stub);
        mockHandler.setAnswersForStubbing(answers);
        Answer answer = mockHandler.getInvocationContainer().getAnswersForStubbing().get(0);
        assertEquals(stub.get(), answer);
    }

    @Test
    public void testGetInvocationContainer() throws Throwable {
        InvocationContainer invocationContainer = mockHandler.getInvocationContainer();
        assertEquals(invocationContainer, mockHandler.invocationContainerImpl);
    }
}