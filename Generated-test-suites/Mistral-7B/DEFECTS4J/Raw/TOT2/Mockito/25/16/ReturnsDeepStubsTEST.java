import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*; // Using JUnit 5 Mockito instead of JUnit 4

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod() {
        // Given
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class).withArguments(mock);

        // When
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.answer(invocation);

        // Then
        assertSame(mock, result);
    }
}