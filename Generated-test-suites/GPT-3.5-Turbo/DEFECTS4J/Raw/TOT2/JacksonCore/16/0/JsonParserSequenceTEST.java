import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.IOException;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;
    private JsonParser[] parsers;

    @Before
    public void setUp() {
        JsonParser firstParser = mock(JsonParser.class);
        JsonParser secondParser = mock(JsonParser.class);
        parsers = new JsonParser[] { firstParser, secondParser };
        jsonParserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testContainedParsersCount() {
        int count = jsonParserSequence.containedParsersCount();
        assertEquals(2, count);
    }

    @Test
    public void testSwitchToNext() throws IOException {
        boolean switched = jsonParserSequence.switchToNext();
        assertTrue(switched);
    }

    @Test
    public void testCreateFlattened() {
        JsonParserSequence newSequence = JsonParserSequence.createFlattened(parsers[0], parsers[1]);
        assertNotNull(newSequence);
        assertEquals(2, newSequence.containedParsersCount());
    }

    @Test
    public void testNextToken() throws IOException {
        // Assuming a scenario where the first parser returns null token initially
        when(parsers[0].nextToken()).thenReturn(null);
        when(parsers[1].nextToken()).thenReturn(JsonToken.VALUE_STRING);

        JsonToken token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        // Assuming both parsers throw IOException on close
        doThrow(new IOException()).when(parsers[0]).close();
        doThrow(new IOException()).when(parsers[1]).close();

        jsonParserSequence.close();
    }
}