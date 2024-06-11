import org.junit.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testNextToken() {
        JsonParser parser1 = new MockJsonParser();
        JsonParser parser2 = new MockJsonParser();

        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        try {
            JsonToken token = sequence.nextToken();
            assertEquals(JsonToken.START_OBJECT, token);

            token = sequence.nextToken();
            assertEquals(JsonToken.FIELD_NAME, token);

            token = sequence.nextToken();
            assertEquals(JsonToken.VALUE_TRUE, token);

            token = sequence.nextToken();
            assertEquals(JsonToken.END_OBJECT, token);
        } catch (Exception e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testContainedParsersCount() {
        JsonParser parser1 = new MockJsonParser();
        JsonParser parser2 = new MockJsonParser();

        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        int count = sequence.containedParsersCount();

        assertEquals(2, count);
    }

    private class MockJsonParser extends JsonParser {
        // Implement abstract methods of JsonParser for testing
    }
}