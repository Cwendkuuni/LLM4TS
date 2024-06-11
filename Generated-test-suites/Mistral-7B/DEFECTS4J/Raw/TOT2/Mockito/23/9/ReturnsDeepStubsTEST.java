import org.junit.Test;
import static org.mockito.Mockito.*;;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Object actual = answer(mock, method(Object.class, int.class, "testMethod"));
        assertSame(mock, actual);
    }

    private Object answer(Object mock, Method method) {
        try {
            ReturnsDeepStubs returnsDeepStubs = mockito3().answer(mock);
            return returnsDeepStubs.answer(new InvocationOnMock(mock, method));
        } catch (Throwable e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}