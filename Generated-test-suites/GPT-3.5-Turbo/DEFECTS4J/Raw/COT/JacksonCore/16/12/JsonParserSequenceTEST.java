
import org.junit.*;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {
    @Test
    public void testClose() {
        // create test objects
        JsonParser[] parsers = {new JsonParserImpl(), new JsonParserImpl()};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        
        try {
            sequence.close(); // test the close() method
        } catch (IOException e) {
            // handle exception
        }
    }

    @Test
    public void testNextToken() {
        // create test objects
        JsonParser[] parsers = {new JsonParserImpl(), new JsonParserImpl()};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        
        try {
            JsonToken token = sequence.nextToken(); // test the nextToken() method
            assertNotNull(token);
        } catch (IOException | JsonParseException e) {
            // handle exception
        }
    }

    @Test
    public void testContainedParsersCount() {
        // create test objects
        JsonParser[] parsers = {new JsonParserImpl(), new JsonParserImpl()};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        
        int count = sequence.containedParsersCount(); // test the containedParsersCount() method
        assertEquals(2, count);
    }
}
