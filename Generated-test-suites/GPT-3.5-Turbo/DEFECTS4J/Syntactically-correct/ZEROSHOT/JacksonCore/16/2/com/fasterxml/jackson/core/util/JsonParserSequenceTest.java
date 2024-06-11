import org.junit.Test;

import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser first = new MockParser();
        JsonParser second = new MockParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testNextToken() {
        JsonParser first = new MockParser();
        JsonParser second = new MockParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
    }

    @Test
    public void testClose() throws Exception {
        JsonParser first = new MockParser();
        JsonParser second = new MockParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        sequence.close();
        assertTrue(first.isClosed());
        assertTrue(second.isClosed());
    }

    private static class MockParser extends JsonParser {

        private boolean closed = false;

        @Override
        public JsonToken nextToken() {
            return JsonToken.START_OBJECT;
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }
}