import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser parser1 = new MockParser();
        JsonParser parser2 = new MockParser();
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        assertEquals(2, sequence.containedParsersCount());
    }
    
    @Test
    public void testNextToken() {
        JsonParser parser1 = new MockParser(JsonToken.START_OBJECT);
        JsonParser parser2 = new MockParser(JsonToken.FIELD_NAME, JsonToken.VALUE_STRING, JsonToken.END_OBJECT);
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        try {
            assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
            assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
            assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
            assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
            assertNull(sequence.nextToken());
        } catch (Exception e) {
            fail("Should not throw any exception");
        }
    }
    
    @Test
    public void testClose() {
        JsonParser parser1 = new MockParser();
        JsonParser parser2 = new MockParser();
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        try {
            sequence.close();
        } catch (Exception e) {
            fail("Should not throw any exception");
        }
    }
}