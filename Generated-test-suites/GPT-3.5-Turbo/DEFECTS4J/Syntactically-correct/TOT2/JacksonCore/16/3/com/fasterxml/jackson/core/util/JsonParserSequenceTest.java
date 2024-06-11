import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

public class JsonParserSequenceTest {

    private JsonParser parser1;
    private JsonParser parser2;
    private JsonParserSequence seq;

    @Before
    public void setUp() {
        parser1 = new MockJsonParser();
        parser2 = new MockJsonParser();
        seq = JsonParserSequence.createFlattened(parser1, parser2);
    }

    @Test
    public void testCreateFlattened() {
        assertEquals(2, seq.containedParsersCount());
    }

    @Test
    public void testClose() throws IOException {
        seq.close();
        assertTrue(((MockJsonParser) parser1).isClosed());
        assertTrue(((MockJsonParser) parser2).isClosed());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        // Test normal case
        JsonToken token = seq.nextToken();
        assertNotNull(token);
        
        // Test case where no token is found
        token = seq.nextToken();
        assertNull(token);
        
        // Test case where switching to next parser
        ((MockJsonParser) parser1).setNextToken(JsonToken.VALUE_NUMBER_INT);
        token = seq.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
    }

    // Edge cases and exception handling scenarios can be added here

    private static class MockJsonParser extends JsonParser {

        private boolean closed;
        private JsonToken nextToken;

        @Override
        public void close() throws IOException {
            closed = true;
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            if (nextToken != null) {
                JsonToken token = nextToken;
                nextToken = null;
                return token;
            }
            return null;
        }

        public boolean isClosed() {
            return closed;
        }

        public void setNextToken(JsonToken token) {
            nextToken = token;
        }
    }
}