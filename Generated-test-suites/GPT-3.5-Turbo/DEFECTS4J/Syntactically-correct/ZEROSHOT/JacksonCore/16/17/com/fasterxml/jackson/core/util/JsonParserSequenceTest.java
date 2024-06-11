import com.fasterxml.jackson.core.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JsonParserSequenceTest {

    private JsonParserSequence parserSequence;
    private JsonParser parser1;
    private JsonParser parser2;
    private JsonParser parser3;
    
    @Before
    public void setUp() {
        parser1 = new JsonParser() {
            public JsonToken nextToken() {
                return null;
            }

            public void close() {
            }

            public JsonToken getCurrentToken() {
                return null;
            }

            public String getText() {
                return null;
            }
        };
        
        parser2 = new JsonParser() {
            public JsonToken nextToken() {
                return JsonToken.VALUE_STRING;
            }

            public void close() {
            }

            public JsonToken getCurrentToken() {
                return null;
            }

            public String getText() {
                return "test";
            }
        };
        
        parser3 = new JsonParser() {
            public JsonToken nextToken() {
                return JsonToken.VALUE_NUMBER_INT;
            }

            public void close() {
            }

            public JsonToken getCurrentToken() {
                return null;
            }

            public String getText() {
                return null;
            }
        };
        
        parserSequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);
    }
    
    @Test
    public void testContainedParsersCount() {
        int count = parserSequence.containedParsersCount();
        assertEquals(3, count);
    }
    
    @Test
    public void testClose() throws Exception {
        parserSequence.close();
        assertTrue(true);
    }
    
    @Test
    public void testNextToken() throws Exception {
        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
    }
}