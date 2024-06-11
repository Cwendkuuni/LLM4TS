import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;

    @Test
    public void testClose() throws IOException {
        JsonParser[] parsers = {new JsonFactory().createParser("{\"name\":\"John\", \"age\":30}"), new JsonFactory().createParser("{\"name\":\"Jane\", \"age\":25}")};
        jsonParserSequence = new JsonParserSequence(parsers);
        jsonParserSequence.close();
        assertTrue(jsonParserSequence.delegate.isClosed());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser[] parsers = {new JsonFactory().createParser("{\"name\":\"John\", \"age\":30}"), new JsonFactory().createParser("{\"name\":\"Jane\", \"age\":25}")};
        jsonParserSequence = new JsonParserSequence(parsers);
        assertEquals(JsonToken.START_OBJECT, jsonParserSequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, jsonParserSequence.nextToken());
        assertEquals("name", jsonParserSequence.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, jsonParserSequence.nextToken());
        assertEquals("John", jsonParserSequence.getText());
        assertEquals(JsonToken.FIELD_NAME, jsonParserSequence.nextToken());
        assertEquals("age", jsonParserSequence.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, jsonParserSequence.nextToken());
        assertEquals(30, jsonParserSequence.getIntValue());
        assertEquals(JsonToken.END_OBJECT, jsonParserSequence.nextToken());
        assertEquals(JsonToken.START_OBJECT, jsonParserSequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, jsonParserSequence.nextToken());
        assertEquals("name", jsonParserSequence.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, jsonParserSequence.nextToken());
        assertEquals("Jane", jsonParserSequence.getText());
        assertEquals(JsonToken.FIELD_NAME, jsonParserSequence.nextToken());
        assertEquals("age", jsonParserSequence.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, jsonParserSequence.nextToken());
        assertEquals(25, jsonParserSequence.getIntValue());
        assertEquals(JsonToken.END_OBJECT, jsonParserSequence.nextToken());
        assertNull(jsonParserSequence.nextToken());
    }

    @Test
    public void testCreateFlattened() {
        JsonParser firstParser = new JsonFactory().createParser("{\"name\":\"John\", \"age\":30}");
        JsonParser secondParser = new JsonFactory().createParser("{\"name\":\"Jane\", \"age\":25}");
        jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
        assertEquals(2, jsonParserSequence.containedParsersCount());
    }
}