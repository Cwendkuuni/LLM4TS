import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {
    
    // Test for constructor
    @Test
    public void testConstructor() {
        JsonParser[] parsers = {new JsonParser(), new JsonParser()};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        
        assertNotNull(sequence);
        assertEquals(2, sequence.containedParsersCount());
    }
    
    // Test for createFlattened method with non-sequence parsers
    @Test
    public void testCreateFlattenedWithNonSequenceParsers() {
        JsonParser first = new JsonParser();
        JsonParser second = new JsonParser();
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        assertNotNull(sequence);
        assertEquals(2, sequence.containedParsersCount());
    }
    
    // Test for createFlattened method with sequence parsers
    @Test
    public void testCreateFlattenedWithSequenceParsers() {
        JsonParser first = new JsonParser();
        JsonParser second = new JsonParser();
        JsonParser[] parsers = {new JsonParser(), new JsonParser()};
        JsonParserSequence sequence1 = new JsonParserSequence(parsers);
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(sequence1, second);
        
        assertNotNull(sequence);
        assertEquals(3, sequence.containedParsersCount());
    }
    
    // Test for close method
    @Test
    public void testClose() throws Exception {
        JsonParser[] parsers = {new JsonParser(), new JsonParser()};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        
        sequence.close();
        
        for (JsonParser parser : parsers) {
            assertTrue(parser.isClosed());
        }
    }
    
    // Test for nextToken method
    @Test
    public void testNextToken() throws Exception {
        JsonParser[] parsers = {new JsonParser(), new JsonParser()};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        
        JsonToken token = sequence.nextToken();
        
        assertEquals(JsonToken.START_OBJECT, token);
    }
    
}