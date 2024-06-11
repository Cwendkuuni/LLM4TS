import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class JsonParserSequenceTest {

    private JsonParserSequence parserSequence;
    private JsonParser[] parsers;

    @Before
    public void setup() {
        JsonParser first = new JsonParserImpl();
        JsonParser second = new JsonParserImpl();
        parsers = new JsonParser[]{first, second};
        parserSequence = JsonParserSequence.createFlattened(first, second);
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(2, parserSequence.containedParsersCount());
    }

    @Test
    public void testClose() {
        try {
            parserSequence.close();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextToken() {
        try {
            assertNotNull(parserSequence.nextToken());
        } catch (IOException | JsonParseException e) {
            fail("Exception should not be thrown");
        }
    }

}

class JsonParserImpl extends JsonParser {

    @Override
    public void close() throws IOException {
        // Stub implementation
    }

    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        return JsonToken.VALUE_NULL; // Stub implementation
    }
}