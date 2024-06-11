import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import com.fasterxml.jackson.core.*;

import org.junit.Test;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser mockFirst = mock(JsonParser.class);
        JsonParser mockSecond = mock(JsonParser.class);
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(mockFirst, mockSecond);
        
        assertNotNull(sequence);
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser mockFirst = mock(JsonParser.class);
        JsonParser mockSecond = mock(JsonParser.class);
        
        when(mockFirst.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(mockSecond.nextToken()).thenReturn(JsonToken.FIELD_NAME);

        JsonParserSequence sequence = JsonParserSequence.createFlattened(mockFirst, mockSecond);

        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
    }
    
    @Test
    public void testContainedParsersCount() {
        JsonParser mockFirst = mock(JsonParser.class);
        JsonParser mockSecond = mock(JsonParser.class);

        JsonParserSequence sequence = JsonParserSequence.createFlattened(mockFirst, mockSecond);

        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testClose() throws IOException {
        JsonParser mockFirst = mock(JsonParser.class);
        JsonParser mockSecond = mock(JsonParser.class);
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(mockFirst, mockSecond);

        sequence.close();

        verify(mockFirst, times(1)).close();
        verify(mockSecond, times(1)).close();
    }

    @Test(expected = IOException.class)
    public void testCloseException() throws IOException {
        JsonParser mockFirst = mock(JsonParser.class);
        JsonParser mockSecond = mock(JsonParser.class);

        doThrow(new IOException()).when(mockSecond).close();
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(mockFirst, mockSecond);

        sequence.close();
    }
}