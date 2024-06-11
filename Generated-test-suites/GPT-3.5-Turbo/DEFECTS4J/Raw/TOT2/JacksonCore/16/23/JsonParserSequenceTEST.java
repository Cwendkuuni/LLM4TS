import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import java.io.IOException;
import java.util.ArrayList;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonFactory factory = new JsonFactory();
        String json1 = "{\"key1\": \"value1\"}";
        String json2 = "{\"key2\": \"value2\"}";
        
        JsonParser parser1 = factory.createParser(json1);
        JsonParser parser2 = factory.createParser(json2);
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        assertNotNull(sequence);
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals("key1", sequence.nextFieldName());
        assertEquals("value1", sequence.nextTextValue());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals("key2", sequence.nextFieldName());
        assertEquals("value2", sequence.nextTextValue());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        assertNull(sequence.nextToken());
    }

    @Test
    public void testCloseMethod() throws IOException {
        JsonFactory factory = new JsonFactory();
        String json1 = "{\"key1\": \"value1\"}";
        String json2 = "{\"key2\": \"value2\"}";
        
        JsonParser parser1 = factory.createParser(json1);
        JsonParser parser2 = factory.createParser(json2);
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        sequence.close();
        
        assertTrue(sequence.isClosed());
    }

    @Test
    public void testContainedParsersCountMethod() throws IOException {
        JsonFactory factory = new JsonFactory();
        String json1 = "{\"key1\": \"value1\"}";
        String json2 = "{\"key2\": \"value2\"}";
        
        JsonParser parser1 = factory.createParser(json1);
        JsonParser parser2 = factory.createParser(json2);
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testSwitchToNextMethod() throws IOException {
        JsonFactory factory = new JsonFactory();
        String json1 = "{\"key1\": \"value1\"}";
        String json2 = "{\"key2\": \"value2\"}";
        
        JsonParser parser1 = factory.createParser(json1);
        JsonParser parser2 = factory.createParser(json2);
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertNull(sequence.nextToken());
        assertFalse(sequence.switchToNext());
    }
}