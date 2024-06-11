import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import org.junit.Test;

public class JsonParserSequenceTest {
    private JsonParser[] parsers;

    @Test
    public void testCreateFlattened() {
        JsonParser first = new DummyJsonParser();
        JsonParser second = new DummyJsonParser();

        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testClose() throws IOException {
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        sequence.close();

        for (JsonParser parser : parsers) {
            assertTrue(((DummyJsonParser) parser).isCloseCalled());
        }
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        JsonToken token = sequence.nextToken();

        assertEquals(JsonToken.START_OBJECT, token);
    }

    @Test
    public void testNextToken_WithRemainingParsers() throws IOException, JsonParseException {
        JsonParser first = new DummyJsonParser();
        JsonParser second = new DummyJsonParser();
        parsers = new JsonParser[]{first, second};

        JsonParserSequence sequence = new JsonParserSequence(parsers);
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken()); // From first parser
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken()); // From second parser
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken()); // From second parser
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken()); // From second parser
    }

    @Test
    public void testNextToken_WithNoRemainingParsers() throws IOException, JsonParseException {
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken()); // From first parser
        assertFalse(sequence.switchToNext()); // No more parsers
        assertNull(sequence.nextToken());
    }
}

class DummyJsonParser extends JsonParser {
    private boolean isCloseCalled = false;

    public boolean isCloseCalled() {
        return isCloseCalled;
    }

    @Override
    public void close() throws IOException {
        isCloseCalled = true;
    }

    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        return JsonToken.START_OBJECT;
    }
}