import com.fasterxml.jackson.core.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void close_ClosesAllParsers() throws Exception {
        // Arrange
        JsonParser first = new DummyJsonParser();
        JsonParser second = new DummyJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        // Act
        sequence.close();

        // Assert
        assertTrue(first.isClosed());
        assertTrue(second.isClosed());
    }

    @Test
    public void nextToken_ReturnsNextTokenIfCurrentParserHasToken() throws Exception {
        // Arrange
        JsonParser first = new DummyJsonParser(JsonToken.START_OBJECT);
        JsonParser second = new DummyJsonParser(JsonToken.FIELD_NAME, "fieldName");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        // Act
        JsonToken result = sequence.nextToken();

        // Assert
        assertEquals(JsonToken.START_OBJECT, result);
    }

    @Test
    public void nextToken_SwitchesToNextParserIfCurrentParserHasNoToken() throws Exception {
        // Arrange
        JsonParser first = new DummyJsonParser();
        JsonParser second = new DummyJsonParser(JsonToken.START_OBJECT);
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        // Act
        JsonToken result = sequence.nextToken();

        // Assert
        assertEquals(JsonToken.START_OBJECT, result);
    }

    @Test
    public void nextToken_ReturnsNullIfNoMoreTokensAvailable() throws Exception {
        // Arrange
        JsonParser first = new DummyJsonParser();
        JsonParser second = new DummyJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        // Act
        JsonToken result = sequence.nextToken();

        // Assert
        assertNull(result);
    }

    @Test
    public void containedParsersCount_ReturnsCorrectCount() throws Exception {
        // Arrange
        JsonParser first = new DummyJsonParser();
        JsonParser second = new DummyJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        // Act
        int count = sequence.containedParsersCount();

        // Assert
        assertEquals(2, count);
    }

    private static class DummyJsonParser extends JsonParser {
        private JsonToken token;
        private String fieldName;
        private boolean isClosed;

        public DummyJsonParser() {
        }

        public DummyJsonParser(JsonToken token) {
            this.token = token;
        }

        public DummyJsonParser(JsonToken token, String fieldName) {
            this.token = token;
            this.fieldName = fieldName;
        }

        @Override
        public String nextFieldName() throws JsonParseException {
            return fieldName;
        }

        @Override
        public JsonToken nextToken() throws IOException {
            JsonToken temp = token;
            token = null;
            return temp;
        }

        @Override
        public boolean isClosed() {
            return isClosed;
        }

        @Override
        public void close() throws IOException {
            isClosed = true;
        }
    }
}