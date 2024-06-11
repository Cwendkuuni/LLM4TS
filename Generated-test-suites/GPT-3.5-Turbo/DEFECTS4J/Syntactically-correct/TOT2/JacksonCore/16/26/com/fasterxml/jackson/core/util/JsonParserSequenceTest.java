import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;

public class JsonParserSequenceTest {
    
    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser first = new JsonFactory().createParser("{}");
        JsonParser second = new JsonFactory().createParser("[]");
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        assertNotNull(sequence);
        assertEquals(2, sequence.containedParsersCount());
    }
    
    @Test
    public void testClose() throws IOException {
        JsonParser first = new JsonFactory().createParser("{}");
        JsonParser second = new JsonFactory().createParser("[]");
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        sequence.close();
        assertTrue(sequence.isClosed());
    }
    
    @Test
    public void testNextToken() throws IOException {
        JsonParser first = new JsonFactory().createParser("{\"key\":\"value\"}");
        JsonParser second = new JsonFactory().createParser("[1,2,3]");
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        
        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
    }

}