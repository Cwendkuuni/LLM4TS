
import static org.junit.Assert.*;
import org.junit.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

public class JsonParserSequenceTest {
    
    private JsonFactory jsonFactory;
    private JsonParserSequence parserSequence;
    
    @Before
    public void setUp() throws Exception {
        jsonFactory = new JsonFactory();
        JsonParser parser1 = jsonFactory.createParser("{\"key1\": \"value1\"}");
        JsonParser parser2 = jsonFactory.createParser("{\"key2\": 2}");
        JsonParser parser3 = jsonFactory.createParser("{\"key3\": [1, 2, 3]}");
        parserSequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);
    }
    
    @Test
    public void testContainedParsersCount() {
        assertEquals(3, parserSequence.containedParsersCount());
    }
    
    @Test
    public void testClose() throws Exception {
        parserSequence.close();
        assertTrue(parserSequence.isClosed());
    }
    
    @Test
    public void testNextToken() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parserSequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parserSequence.nextToken());
        assertEquals("key1", parserSequence.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parserSequence.nextToken());
        assertEquals("value1", parserSequence.getText());
        assertEquals(JsonToken.END_OBJECT, parserSequence.nextToken());
        
        assertEquals(JsonToken.START_OBJECT, parserSequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parserSequence.nextToken());
        assertEquals("key2", parserSequence.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parserSequence.nextToken());
        assertEquals(2, parserSequence.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parserSequence.nextToken());
        
        assertEquals(JsonToken.START_OBJECT, parserSequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parserSequence.nextToken());
        assertEquals("key3", parserSequence.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parserSequence.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parserSequence.nextToken());
        assertEquals(1, parserSequence.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parserSequence.nextToken());
        assertEquals(2, parserSequence.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parserSequence.nextToken());
        assertEquals(3, parserSequence.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parserSequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, parserSequence.nextToken());
        
        assertEquals(null, parserSequence.nextToken());
    }
}

