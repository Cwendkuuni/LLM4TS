import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;

    @Before
    public void setUp() {
        JsonParser parser1 = createMockParser(JsonToken.START_OBJECT);
        JsonParser parser2 = createMockParser(JsonToken.FIELD_NAME);
        JsonParser parser3 = createMockParser(JsonToken.VALUE_STRING);
        JsonParser parser4 = createMockParser(JsonToken.END_OBJECT);

        jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2, parser3, parser4);
    }

    @Test
    public void testClose() throws Exception {
        jsonParserSequence.close();

        assertTrue(jsonParserSequence.isClosed());
    }

    @Test
    public void testNextToken() throws Exception {
        assertEquals(JsonToken.START_OBJECT, jsonParserSequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, jsonParserSequence.nextToken());
        assertEquals(JsonToken.VALUE_STRING, jsonParserSequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, jsonParserSequence.nextToken());
        assertNull(jsonParserSequence.nextToken());
    }

    @Test
    public void testContainedParsersCount() throws Exception {
        assertEquals(4, jsonParserSequence.containedParsersCount());
    }

    private JsonParser createMockParser(JsonToken token) {
        JsonParser parser = createMock(JsonParser.class);
        expect(parser.nextToken()).andReturn(token);
        replay(parser);
        return parser;
    }
}