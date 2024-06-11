import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonFactory;

public class JsonParserSequenceTest {

    @Test
    public void testClose() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser1 = factory.createParser("true");
        JsonParser parser2 = factory.createParser("42");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        sequence.close();
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }
    
    @Test
    public void testNextToken() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser1 = factory.createParser("true");
        JsonParser parser2 = factory.createParser("{\"key\": \"value\"}");
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        assertEquals(JsonToken.VALUE_TRUE, sequence.nextToken());
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        assertNull(sequence.nextToken());
    }
    
    @Test
    public void testContainedParsersCount() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser1 = factory.createParser("null");
        JsonParser parser2 = factory.createParser("42");
        JsonParser parser3 = factory.createParser("[]");
        JsonParser parser4 = factory.createParser("{}");
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3, parser4);
        
        assertEquals(4, sequence.containedParsersCount());
    }
    
    @Test(expected = JsonParseException.class)
    public void testInvalidJson() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser1 = factory.createParser("true");
        JsonParser parser2 = factory.createParser("This is not valid JSON");
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        sequence.nextToken();
    }
}