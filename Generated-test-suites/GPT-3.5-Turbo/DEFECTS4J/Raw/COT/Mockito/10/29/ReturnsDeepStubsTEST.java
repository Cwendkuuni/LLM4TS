
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test with non-mockable returnType
        assertEquals(null, returnsDeepStubs.answer(invocationOf(Mockito.class.getDeclaredMethod("someMethod"))));

        // Test with mockable returnType
        assertEquals("Mock answer", returnsDeepStubs.answer(invocationOf(Mockito.class.getDeclaredMethod("anotherMethod"))));

        // Test with deep stub answer
        assertEquals(mock(Object.class), returnsDeepStubs.answer(invocationOf(Mockito.class.getDeclaredMethod("yetAnotherMethod"))));
    }

    private InvocationOnMock invocationOf(java.lang.reflect.Method method) throws Exception {
        Object mock = mock(Mockito.class);
        return new InvocationBuilder().mock(mock).method(method).toInvocation();
    }
}
