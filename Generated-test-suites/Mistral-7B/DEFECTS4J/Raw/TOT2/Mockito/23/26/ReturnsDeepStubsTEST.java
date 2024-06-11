import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

    @InjectMocks
    private ReturnsDeepStubs returnsDeepStubs;

    @Test
    public void testAnswer() {
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(returnsDeepStubs.answer(eq(invocation))).thenReturn("Test");

        assertEquals("Test", returnsDeepStubs.answer(invocation));
    }
}