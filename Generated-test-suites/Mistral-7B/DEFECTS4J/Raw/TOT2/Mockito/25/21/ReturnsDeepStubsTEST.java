import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethodWithEmptyInvocation() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object result = returnsDeepStubs.answer(invocation);

        verify(mock).answer(invocation); // Verify that the mock's answer method was called

        assertEquals(Answers.RETURNS_DEEP_STUBS, returnsDeepStubs); // Verify that ReturnsDeepStubs instance was used
        assertNotNull(result); // Verify that a non-null result was returned
    }

    @Test(expected = Throwable.class)
    public void testAnswerMethodWithNonMockableType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Class<String> clazz = String.class;
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        when(invocation.getMethod()).thenReturn(new Method(clazz.getName(), new Class[0]));
        when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));

        try {
            returnsDeepStubs.answer(invocation);
            fail();
        } catch (Throwable e) {
            // Expected exception
        }
    }
}