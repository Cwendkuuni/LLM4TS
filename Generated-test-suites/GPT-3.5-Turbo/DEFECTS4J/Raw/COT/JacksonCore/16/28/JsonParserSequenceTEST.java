import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    // Test constructor
    @Test
    public void testConstructor() {
        JsonParser parser1 = new MockJsonParser();
        JsonParser parser2 = new MockJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Check that sequence is constructed correctly
        assertEquals(2, sequence.containedParsersCount());
        assertEquals(parser1, sequence.getCurrentParser());
    }

    // Test nextToken method
    @Test
    public void testNextToken() throws Exception {
        JsonParser parser1 = new MockJsonParser();
        JsonParser parser2 = new MockJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Check that nextToken returns correct tokens
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        assertEquals(null, sequence.nextToken()); // Reached end of sequence
    }

    // Test close method
    @Test
    public void testClose() throws Exception {
        MockJsonParser parser1 = new MockJsonParser();
        MockJsonParser parser2 = new MockJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Check that close method is called on all parsers
        assertFalse(parser1.isClosed());
        assertFalse(parser2.isClosed());
        sequence.close();
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }

    // Test switchToNext method
    @Test
    public void testSwitchToNext() throws Exception {
        JsonParser parser1 = new MockJsonParser();
        JsonParser parser2 = new MockJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Check that switchToNext method switches to the next parser correctly
        assertTrue(sequence.switchToNext());
        assertEquals(parser2, sequence.getCurrentParser());
        assertFalse(sequence.switchToNext()); // Reached end of sequence
        assertEquals(parser2, sequence.getCurrentParser());
    }

    // Other potential edge cases and exception handling scenarios:
    // - Test constructor with more than 2 parsers
    // - Test nextToken throws IOException
    // - Test nextToken throws JsonParseException
    // - Test nextToken throws IOException and JsonParseException
    // - Test close method throws IOException and JsonParseException
    // - Test switchToNext method when there are no more parsers

}