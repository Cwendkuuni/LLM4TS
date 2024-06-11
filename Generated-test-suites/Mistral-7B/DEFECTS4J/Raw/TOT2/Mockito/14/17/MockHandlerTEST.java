import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

public class MockHandlerTest {
    @Test
    public void testHandleMethod() throws Throwable {
        MockHandler<MockitoMock> mockHandler = new MockHandler<>(new MockSettingsImpl());
        Invocation invocation = new Invocation(MockitoMock.class, "mockMethod", new Object[]{1, 2}, new Class[0]);
        Answer answer = Mockito.answer(new Object[]{3, 4});
        mockHandler.setAnswersForStubbing(Mockito.Collections.singletonList(answer));
        Mockito.doAnswer(answer).when(mockHandler).handle(ArgumentMatchers.eq(invocation));

        Object result = mockHandler.handle(invocation);
        Mockito.verify(mockHandler).handle(ArgumentMatchers.eq(invocation));
        Mockito.verifyNoMoreInteractions(mockHandler);

        assert result.equals(new Object[]{3, 4});
    }
}