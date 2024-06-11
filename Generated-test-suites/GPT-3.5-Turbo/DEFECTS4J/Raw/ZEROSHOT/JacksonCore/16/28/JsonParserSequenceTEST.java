import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException, JsonParseException {
        JsonParser first = new DummyJsonParser();
        JsonParser second = new DummyJsonParser();
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(first, second);
        assertTrue(jsonParserSequence.containedParsersCount() == 2);
    }
    
    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser first = new DummyJsonParser();
        JsonParser second = new DummyJsonParser();
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(first, second);
        assertNull(jsonParserSequence.nextToken());
    }

    private static class DummyJsonParser extends JsonParser {
        @Override
        public void close() throws IOException {}

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            return null;
        }

        // Rest of the methods inherited from JsonParser

        // ...

    }

}