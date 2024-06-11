import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    private JsonParserSequence parserSequence;

    @Before
    public void setUp() {
        JsonParser parser1 = new MockJsonParser();
        JsonParser parser2 = new MockJsonParser();
        parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
    }

    @Test
    public void testNextToken() throws Exception {
        assertNull(parserSequence.nextToken());

        // Scenario 1: Typical use case - Both parsers have tokens
        ((MockJsonParser) parserSequence._parsers[0]).addToken(JsonToken.FIELD_NAME);
        ((MockJsonParser) parserSequence._parsers[1]).addToken(JsonToken.VALUE_STRING);
        assertEquals(JsonToken.FIELD_NAME, parserSequence.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parserSequence.nextToken());

        // Scenario 2: Edge case - One parser has tokens, the other does not
        ((MockJsonParser) parserSequence._parsers[0]).addToken(JsonToken.FIELD_NAME);
        assertNull(parserSequence.nextToken());

        // Scenario 3: Error scenario - Both parsers throw IOException
        ((MockJsonParser) parserSequence._parsers[0]).setThrowIOException(true);
        ((MockJsonParser) parserSequence._parsers[1]).setThrowIOException(true);
        try {
            parserSequence.nextToken();
            fail("IOException not thrown");
        } catch (IOException e) {
            // expected IOException
        }
    }

    @Test
    public void testClose() throws Exception {
        // Scenario 1: Typical use case - Both parsers are closed
        parserSequence.close();
        assertTrue(((MockJsonParser) parserSequence._parsers[0]).isClosed());
        assertTrue(((MockJsonParser) parserSequence._parsers[1]).isClosed());

        // Scenario 2: Edge case - One parser is closed, the other is not
        parserSequence._nextParser = 1; // set next parser to the unclosed one
        parserSequence.close();
        assertFalse(((MockJsonParser) parserSequence._parsers[0]).isClosed());
        assertTrue(((MockJsonParser) parserSequence._parsers[1]).isClosed());

        // Scenario 3: Error scenario - Both parsers throw IOException during close
        ((MockJsonParser) parserSequence._parsers[0]).setThrowIOException(true);
        ((MockJsonParser) parserSequence._parsers[1]).setThrowIOException(true);
        try {
            parserSequence.close();
            fail("IOException not thrown");
        } catch (IOException e) {
            // expected IOException
            assertTrue(((MockJsonParser) parserSequence._parsers[0]).isClosed());
            assertTrue(((MockJsonParser) parserSequence._parsers[1]).isClosed());
        }
    }
}