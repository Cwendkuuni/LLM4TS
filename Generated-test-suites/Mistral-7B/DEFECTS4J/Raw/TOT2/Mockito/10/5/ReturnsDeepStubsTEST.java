import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;
    private Object mock;

    @BeforeEach
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testAnswer_ReturnsSameInstance() throws Throwable {
        // Given
        Object expected = Mockito.mock(Number.class);

        // When
        Object result = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().equals(Number.class.getMethod("iterator"))));

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void testAnswer_ReturnsDefaultAnswerIfTypeIsNotMockable() throws Throwable {
        // Given
        Class<String> stringClass = String.class;

        // When
        Object result = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().equals(stringClass.getMethod("toString"))));

        // Then
        assertNotNull(result);
    }
}