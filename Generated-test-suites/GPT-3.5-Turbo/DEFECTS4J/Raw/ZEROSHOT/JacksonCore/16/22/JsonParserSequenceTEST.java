
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser p1 = createParser("{\"field1\":\"value1\"}");
        JsonParser p2 = createParser("{\"field2\":\"value2\"}");

        JsonParserSequence sequence = JsonParserSequence.createFlattened(p1, p2);
        Assert.assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        Assert.assertEquals("field1", sequence.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        Assert.assertEquals("value1", sequence.getText());
        Assert.assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        Assert.assertEquals("field2", sequence.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        Assert.assertEquals("value2", sequence.getText());
        Assert.assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        Assert.assertEquals(null, sequence.nextToken());
        Assert.assertEquals(true, sequence.isClosed());

        sequence.close();
    }

    @Test
    public void testContainedParsersCount() throws IOException {
        JsonParser p1 = createParser("{\"field1\":\"value1\"}");
        JsonParser p2 = createParser("{\"field2\":\"value2\"}");

        JsonParserSequence sequence = JsonParserSequence.createFlattened(p1, p2);
        Assert.assertEquals(2, sequence.containedParsersCount());

        sequence.close();
    }

    private JsonParser createParser(String json) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        return new JsonParserStub(inputStream);
    }

    private static class JsonParserStub extends JsonParser {

        private final InputStream inputStream;
        private JsonToken currentToken;

        public JsonParserStub(InputStream inputStream) {
            this.inputStream = inputStream;
            this.currentToken = null;
        }

        @Override
        public String getCurrentName() throws IOException {
            return null;
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            if (currentToken != null) {
                currentToken = null;
                return null;
            }
            int read = inputStream.read();
            if (read < 0) {
                throw new JsonEOFException(this, null, "Expected token not available");
            }
            char ch = (char) read;
            switch (ch) {
                case '{':
                    currentToken = JsonToken.START_OBJECT;
                    break;
                case '}':
                    currentToken = JsonToken.END_OBJECT;
                    break;
                case '"':
                    currentToken = JsonToken.FIELD_NAME;
                    break;
                default:
                    currentToken = JsonToken.VALUE_STRING;
                    break;
            }
            return currentToken;
        }

        @Override
        public String getText() throws IOException {
            return null;
        }

        @Override
        public boolean isClosed() {
            return false;
        }

        @Override
        public void close() throws IOException {
            inputStream.close();
        }
    }
}
