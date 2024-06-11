
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonParserSequenceTest {
    private JsonParserSequence jsonParserSequence;

    @Before
    public void setUp() {
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = new JsonParser() {
            @Override
            public void close() throws IOException {
            }

            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return JsonToken.VALUE_FALSE;
            }
        };
        parsers[1] = new JsonParser() {
            @Override
            public void close() throws IOException {
            }

            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return JsonToken.VALUE_TRUE;
            }
        };
        jsonParserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testConstructor() {
        assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void testClose() throws IOException {
        jsonParserSequence.close();
        assertTrue(jsonParserSequence.isClosed());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        assertEquals(JsonToken.VALUE_FALSE, jsonParserSequence.nextToken());
        assertEquals(JsonToken.VALUE_TRUE, jsonParserSequence.nextToken());
        assertNull(jsonParserSequence.nextToken());
    }
}
