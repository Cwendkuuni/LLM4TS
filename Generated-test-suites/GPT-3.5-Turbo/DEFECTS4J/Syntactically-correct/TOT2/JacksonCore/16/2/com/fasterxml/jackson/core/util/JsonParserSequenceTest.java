import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;

    @Before
    public void setUp() {
        JsonParser firstParser = new JsonFactory().createParser("{ \"key\": \"value\" }");
        JsonParser secondParser = new JsonFactory().createParser("[1, 2, 3]");
        jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
    }

    @Test
    public void testCreateFlattened() {
        assertNotNull(jsonParserSequence);
        assertTrue(jsonParserSequence.containedParsersCount() == 2);
    }

    @Test
    public void testNextToken() throws IOException {
        assertEquals(JsonToken.START_OBJECT, jsonParserSequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, jsonParserSequence.nextToken());
        assertEquals(JsonToken.VALUE_STRING, jsonParserSequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, jsonParserSequence.nextToken());
        
        assertEquals(JsonToken.START_ARRAY, jsonParserSequence.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, jsonParserSequence.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, jsonParserSequence.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, jsonParserSequence.nextToken());
        assertEquals(JsonToken.END_ARRAY, jsonParserSequence.nextToken());
    }

    @Test
    public void testClose() throws IOException {
        jsonParserSequence.close();
        // No exceptions expected when closing the parsers
    }

    @Test
    public void testSwitchToNext() throws IOException {
        assertTrue(jsonParserSequence.switchToNext());
        assertNotNull(jsonParserSequence.nextToken());
        // Switching to the next parser works
    }

    @After
    public void tearDown() {
        jsonParserSequence.close();
    }
}