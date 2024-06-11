import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_typicalUseCase() throws Throwable {
        // Create a mock object with ReturnsDeepStubs answer
        Map<String, Set<Integer>> mock = mock(Map.class, returnsDeepStubs);

        // Verify that the mock returns a deep stub for nested method calls
        assertNotNull(mock.entrySet().iterator().next().getValue().iterator().next());
    }

    @Test
    public void testAnswer_edgeCase() throws Throwable {
        // Create a mock object with ReturnsDeepStubs answer
        Map<Object, Object> mock = mock(Map.class, returnsDeepStubs);

        // Verify that the mock returns a deep stub for any type of key and value
        assertNotNull(mock.entrySet().iterator().next().getKey());
        assertNotNull(mock.entrySet().iterator().next().getValue());
    }

    @Test
    public void testAnswer_errorScenario() throws Throwable {
        // Create a mock object with ReturnsDeepStubs answer
        Map<Object, Object> mock = mock(Map.class, returnsDeepStubs);

        // Verify that an exception is thrown for an unsupported method call
        try {
            mock.put("key", "value");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Exception is expected
        }
    }

}