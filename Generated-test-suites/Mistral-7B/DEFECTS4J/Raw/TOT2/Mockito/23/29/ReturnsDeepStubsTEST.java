import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;
    private Object mock;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testAnswer() {
        when(returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class)))
                .thenReturn("Test Answer");

        Object result = returnsDeepStubs.answer(mockArgument(InvocationOnMock.class, any()));

        assertEquals("Test Answer", result);
    }
}