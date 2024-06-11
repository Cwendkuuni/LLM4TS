import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    @Test
    public void testNextTokenNormalCase() throws IOException {
        JsonParser firstParser = new JsonFactory().createParser("{\"name\": \"John\"}");
        JsonParser secondParser = new JsonFactory().createParser("{\"age\": 30}");
        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", token.asString());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", token.asString());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", token.asString());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(30, token.asInt());

        token = parserSequence.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextTokenEmptyParsers() throws IOException {
        JsonParser firstParser = new JsonFactory();
        JsonParser secondParser = new JsonFactory();
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        JsonToken token = parserSequence.nextToken();
        assertNull(token);
    }

    @Test(expected = IOException.class)
    public void testNextTokenIOException() throws IOException {
        JsonParser firstParser = new JsonFactory().createParser("{\"name\": \"John\"}");
        JsonParser secondParser = new JsonFactory();
        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        parserSequence.nextToken();
        firstParser.close();

        JsonToken token = parserSequence.nextToken();
        throw new AssertionError("Unexpected token: " + token);
    }
}