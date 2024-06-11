import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;

    @Before
    public void setUp() {
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();
        jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);
    }

    @Test
    public void testNextToken() throws Exception {
        // Test with multiple parsers
        assertEquals(JsonToken.START_ARRAY, jsonParserSequence.nextToken());
        assertEquals(JsonToken.END_ARRAY, jsonParserSequence.nextToken());

        // Test with single parser
        JsonParser parser = new JsonParser();
        jsonParserSequence = JsonParserSequence.createFlattened(parser);
        assertEquals(JsonToken.START_OBJECT, jsonParserSequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, jsonParserSequence.nextToken());
    }

    @Test
    public void testClose() throws Exception {
        // Test with multiple parsers
        jsonParserSequence.close();
        assertTrue(jsonParserSequence.isClosed());

        // Test with single parser
        JsonParser parser = new JsonParser();
        jsonParserSequence = JsonParserSequence.createFlattened(parser);
        jsonParserSequence.close();
        assertTrue(jsonParserSequence.isClosed());
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(2, jsonParserSequence.containedParsersCount());

        JsonParser parser = new JsonParser();
        jsonParserSequence = JsonParserSequence.createFlattened(parser);
        assertEquals(1, jsonParserSequence.containedParsersCount());
    }

}