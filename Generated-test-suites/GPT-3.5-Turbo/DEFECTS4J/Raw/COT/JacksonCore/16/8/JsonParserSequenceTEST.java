
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testClose() {
        // Create test objects
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Call the method to be tested
        try {
            sequence.close();
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        // Assertions
        // Check if close() has been called on both parsers
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }

    @Test
    public void testNextToken() {
        // Create test objects
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Call the method to be tested
        try {
            JsonToken token = sequence.nextToken();

            // Assertions
            // Check if the nextToken() method was correctly called on the active parser
            assertEquals(JsonToken.START_OBJECT, token);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testContainedParsersCount() {
        // Create test objects
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Call the method to be tested
        int count = sequence.containedParsersCount();

        // Assertions
        // Check if the correct number of parsers is returned
        assertEquals(2, count);
    }
}
