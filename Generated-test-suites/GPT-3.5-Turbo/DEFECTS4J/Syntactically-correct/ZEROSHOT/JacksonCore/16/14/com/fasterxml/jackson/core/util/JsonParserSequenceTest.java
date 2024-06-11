import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    private JsonParserSequence parser;

    @Before
    public void setup() {
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = new JsonParserMock();
        parsers[1] = new JsonParserMock();
        parser = new JsonParserSequence(parsers);
    }

    @Test
    public void testClose() throws Exception {
        parser.close();
        assertTrue(((JsonParserMock) parser.delegate).isCloseCalled());
    }

    @Test
    public void testNextToken() throws Exception {
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test
    public void testContainedParsersCount() throws Exception {
        int count = parser.containedParsersCount();
        assertEquals(2, count);
    }
}

class JsonParserMock extends JsonParser {
    private boolean closeCalled = false;

    public boolean isCloseCalled() {
        return closeCalled;
    }

    @Override
    public void close() {
        closeCalled = true;
    }

    // Mock methods (not used in the test cases)
    @Override
    public JsonToken nextToken() throws IOException {
        return null;
    }
}