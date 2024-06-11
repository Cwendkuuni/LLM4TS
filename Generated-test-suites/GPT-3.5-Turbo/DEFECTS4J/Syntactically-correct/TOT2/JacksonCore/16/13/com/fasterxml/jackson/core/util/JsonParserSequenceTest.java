import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;
    private JsonParser firstParser;
    private JsonParser secondParser;
    
    @Before
    public void setUp() {
        firstParser = mock(JsonParser.class);
        secondParser = mock(JsonParser.class);
        jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
    }
    
    @Test
    public void testCreateFlattened() {
        assertEquals(2, jsonParserSequence.containedParsersCount());
    }
    
    @Test
    public void testClose() throws IOException {
        jsonParserSequence.close();
        
        verify(firstParser).close();
        verify(secondParser).close();
    }
    
    @Test
    public void testNextToken() throws IOException, JsonParseException {
        when(firstParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(secondParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        
        assertEquals(JsonToken.START_OBJECT, jsonParserSequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, jsonParserSequence.nextToken());
    }
    
    @Test
    public void testSwitchToNext() {
        assertTrue(jsonParserSequence.switchToNext());
        assertFalse(jsonParserSequence.switchToNext());
    }
}