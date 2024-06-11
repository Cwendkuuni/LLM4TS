import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

@RunWith(MockitoJUnitRunner.class)
public class JsonParserSequenceTest {

    @Mock
    private JsonParser firstParser;
    
    @Mock
    private JsonParser secondParser;
    
    private JsonParserSequence parserSequence;
    
    @Before
    public void setUp() {
        parserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
    }
    
    @Test
    public void testContainedParsersCount() {
        assertEquals("Incorrect number of contained parsers", 2, parserSequence.containedParsersCount());
    }
    
    @Test
    public void testNextToken() throws IOException, JsonParseException {
        when(firstParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(secondParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        
        assertEquals("First token should be VALUE_STRING", JsonToken.VALUE_STRING, parserSequence.nextToken());
        assertEquals("Second token should be FIELD_NAME", JsonToken.FIELD_NAME, parserSequence.nextToken());
    }
    
    @Test
    public void testClose() throws IOException {
        doNothing().when(firstParser).close();
        doNothing().when(secondParser).close();
        
        parserSequence.close();
        
        verify(firstParser, times(1)).close();
        verify(secondParser, times(1)).close();
    }

    // Add more test cases for edge cases and exception scenarios
    
}