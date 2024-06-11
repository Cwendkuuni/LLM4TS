import org.junit.Test;
import static org.mockito.Mockito.*;;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        when(mock.toString()).thenReturn("mockObject");

        Object result = mock.answer(new InvocationOnMock(mock, mock.getClass().getMethod("toString")));

        // Verify that the method returns the same mock object
        assertEquals(mock, result);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerMethodWithNullInvocation() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        Object result = mock.answer(null);
    }

    @Test
    public void testAnswerMethodWithNonMockableType() {
        Class<Integer> intClass = Integer.class;
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(intClass, new ReturnsDeepStubs());

        try {
            mock.answer(new InvocationOnMock(mock, intClass.getMethod("intValue")));
            fail("Expected TypeError should be thrown when trying to return a non-mockable type");
        } catch (TypeError e) {
            // Expected exception
        }
    }

    @Test(expected = Throwable.class)
    public void testAnswerMethodWithUnexpectedThrowable() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        doThrow(new RuntimeException("Test Exception")).when(mock).answer(any());

        try {
            mock.answer(new InvocationOnMock(mock, mock.getClass().getMethod("toString")));
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }
}