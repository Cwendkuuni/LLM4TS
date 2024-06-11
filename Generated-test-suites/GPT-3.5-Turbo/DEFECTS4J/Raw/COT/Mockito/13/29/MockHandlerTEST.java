
import static org.mockito.Mockito.*;
import org.junit.Test;

public class MockHandlerTest {

    @Test
    public void testHandle() throws Throwable {
        //setup
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandlerInterface<T> oldMockHandler = mock(MockHandlerInterface.class);
        Invocation invocation = mock(Invocation.class);

        //create mock handler
        MockHandler<T> mockHandler = new MockHandler<>(mockSettings);

        //test
        Object result = mockHandler.handle(invocation);

        //verify
        verify(mockSettings).getDefaultAnswer();
        verify(mockSettings).getArgumentMatcherStorage();
        verify(invocation).getMock();
        verify(oldMockHandler).getMockSettings();
        verify(oldMockHandler).getArgumentMatcherStorage();
        verify(invocation).getInvocation();
        verify(invocation).getMethod();
        verify(invocation).getArguments();
    }

    @Test
    public void testVoidMethodStubbable() {
        //setup
        T mock = mock(T.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);

        //create mock handler
        MockHandler<T> mockHandler = new MockHandler<>();

        //test
        VoidMethodStubbable<T> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        //verify
        verify(voidMethodStubbable).getMock();
        verify(invocationContainerImpl).setVoidMethodStubbable(voidMethodStubbable);
    }

    @Test
    public void testSetAnswersForStubbing() {
        //setup
        List<Answer> answers = mock(List.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);

        //create mock handler
        MockHandler<T> mockHandler = new MockHandler<>();

        //test
        mockHandler.setAnswersForStubbing(answers);

        //verify
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        //setup
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);

        //create mock handler
        MockHandler<T> mockHandler = new MockHandler<>();

        //test
        InvocationContainer result = mockHandler.getInvocationContainer();

        //verify
        assertEquals(invocationContainerImpl, result);
    }
}
