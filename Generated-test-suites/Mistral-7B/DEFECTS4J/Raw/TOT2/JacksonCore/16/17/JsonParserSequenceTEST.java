import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {
    @Test
    public void testNextTokenWithSingleParser() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parserSequence = new JsonParserSequence(new JsonParser[] { parser1 });

        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());
        parserSequence.close();
        parser1.close();
    }

    @Test
    public void testNextTokenWithTwoParsers() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();
        JsonParser parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        parserSequence.close();
        parser1.close();
        parser2.close();
    }

    @Test(expected = IOException.class)
    public void testNextTokenWithEmptyParserArray() throws IOException {
        JsonParser[] parserArray = new JsonParser[0];
        JsonParser parserSequence = new JsonParserSequence(parserArray);
        parserSequence.nextToken(); // Should throw an IOException
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenWithNullParser() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = null;
        JsonParser parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
        parserSequence.nextToken(); // Should throw a JsonParseException
    }

    @Test
    public void testContainedParsersCount() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();
        JsonParser parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
        assertEquals(2, parserSequence.containedParsersCount());
    }

    private JsonParser createMockParser() {
        // Create a mock parser for testing
    }
}