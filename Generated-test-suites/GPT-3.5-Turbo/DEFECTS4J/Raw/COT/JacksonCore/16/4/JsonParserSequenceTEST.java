
// Import necessary JUnit 4 classes
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    // Create an instance of JsonParserSequence for testing
    private JsonParserSequence parserSequence;

    @Test
    public void testConstructor() {
        // Create an array of JsonParsers to pass to the constructor
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = new JsonParser();
        parsers[1] = new JsonParser();
        
        // Create JsonParserSequence instance using the constructor
        parserSequence = new JsonParserSequence(parsers);
        
        // Verify that the constructor was successful by checking the containedParsersCount
        assertEquals(2, parserSequence.containedParsersCount());
    }

    @Test
    public void testCreateFlattened() {
        // Create two instances of JsonParser
        JsonParser first = new JsonParser();
        JsonParser second = new JsonParser();
        
        // Create JsonParserSequence using the createFlattened method
        parserSequence = JsonParserSequence.createFlattened(first, second);

        // Verify that the createFlattened method was successful by checking the containedParsersCount
        assertEquals(2, parserSequence.containedParsersCount());
    }

    @Test
    public void testClose() {
        // Create an array of JsonParsers to pass to the constructor
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = new JsonParser();
        parsers[1] = new JsonParser();
        
        // Create JsonParserSequence instance using the constructor
        parserSequence = new JsonParserSequence(parsers);
        
        // Call the close method
        parserSequence.close();
        
        // Verify that the close method was successful by checking that all parsers are closed
        assertTrue(parsers[0].isClosed());
        assertTrue(parsers[1].isClosed());
    }

    @Test
    public void testNextToken() {
        // Create an array of JsonParsers to pass to the constructor
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = new JsonParser();
        parsers[1] = new JsonParser();
        
        // Create JsonParserSequence instance using the constructor
        parserSequence = new JsonParserSequence(parsers);
        
        // Call the nextToken method
        JsonToken token = parserSequence.nextToken();
        
        // Verify that the nextToken method returns a valid token
        assertNotNull(token);
    }

    @Test
    public void testContainedParsersCount() {
        // Create an array of JsonParsers to pass to the constructor
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = new JsonParser();
        parsers[1] = new JsonParser();
        
        // Create JsonParserSequence instance using the constructor
        parserSequence = new JsonParserSequence(parsers);
        
        // Call the containedParsersCount method
        int count = parserSequence.containedParsersCount();
        
        // Verify that the containedParsersCount method returns the correct count
        assertEquals(2, count);
    }
}

