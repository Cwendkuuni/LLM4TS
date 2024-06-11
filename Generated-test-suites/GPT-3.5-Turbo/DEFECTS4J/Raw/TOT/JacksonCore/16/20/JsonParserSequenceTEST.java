import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;
    private JsonParser jsonParser1;
    private JsonParser jsonParser2;

    @Before
    public void setup() {
        jsonParser1 = new JsonParser() {
            @Override
            public void close() throws IOException {

            }
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return null;
            }
        };

        jsonParser2 = new JsonParser() {
            @Override
            public void close() throws IOException {

            }
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return null;
            }
        };

        jsonParserSequence = JsonParserSequence.createFlattened(jsonParser1, jsonParser2);
    }

    @Test
    public void nextToken_WhenNoParsers_ReturnsNull() throws IOException, JsonParseException {
        jsonParserSequence = new JsonParserSequence(new JsonParser[0]);
        assertNull(jsonParserSequence.nextToken());
    }

    @Test
    public void nextToken_WhenAllParsersReturnNull_ReturnsNull() throws IOException, JsonParseException {
        assertNull(jsonParserSequence.nextToken());
    }

    @Test
    public void nextToken_WhenOnlyFirstParserHasToken_ReturnsTokenFromFirstParser() throws IOException, JsonParseException {
        JsonToken expectedToken = JsonToken.START_OBJECT;
        jsonParser1 = new JsonParser() {
            boolean hasReturnedToken = false;

            @Override
            public void close() throws IOException {

            }
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                if (!hasReturnedToken) {
                    hasReturnedToken = true;
                    return expectedToken;
                } else {
                    return null;
                }
            }
        };
        jsonParserSequence = JsonParserSequence.createFlattened(jsonParser1, jsonParser2);
        assertEquals(expectedToken, jsonParserSequence.nextToken());
    }

    @Test
    public void nextToken_WhenSwitchingParsers_ReturnsTokenFromNextParser() throws IOException, JsonParseException {
        JsonToken expectedToken = JsonToken.END_ARRAY;
        jsonParser2 = new JsonParser() {
            boolean hasReturnedToken = false;

            @Override
            public void close() throws IOException {

            }
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                if (!hasReturnedToken) {
                    hasReturnedToken = true;
                    return expectedToken;
                } else {
                    return null;
                }
            }
        };
        jsonParserSequence = JsonParserSequence.createFlattened(jsonParser1, jsonParser2);
        assertEquals(expectedToken, jsonParserSequence.nextToken());
    }

    @Test
    public void close_WhenOnlyFirstParserOpen_ClosesFirstParser() throws IOException, JsonParseException {
        jsonParserSequence.close();
        assertTrue(jsonParser1.isClosed());
    }

    @Test
    public void close_WhenBothParsersOpen_ClosesBothParsers() throws IOException, JsonParseException {
        jsonParser1 = new JsonParser() {
            boolean isClosed = false;

            @Override
            public void close() throws IOException {
                isClosed = true;
            }
            @Override
            public boolean isClosed() {
                return isClosed;
            }
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return null;
            }
        };

        jsonParser2 = new JsonParser() {
            boolean isClosed = false;

            @Override
            public void close() throws IOException {
                isClosed = true;
            }
            @Override
            public boolean isClosed() {
                return isClosed;
            }
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return null;
            }
        };

        jsonParserSequence = JsonParserSequence.createFlattened(jsonParser1, jsonParser2);
        jsonParserSequence.close();
        assertTrue(jsonParser1.isClosed());
        assertTrue(jsonParser2.isClosed());
    }

    @Test
    public void containedParsersCount_ReturnsNumberOfParsersInSequence() {
        int expectedCount = 2;
        assertEquals(expectedCount, jsonParserSequence.containedParsersCount());
    }

}