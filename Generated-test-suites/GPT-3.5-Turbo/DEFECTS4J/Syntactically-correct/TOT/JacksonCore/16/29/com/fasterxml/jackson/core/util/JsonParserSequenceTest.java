import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JsonParserSequenceTest {

    @Test
    public void testClose_withMultipleParsers_shouldCloseAllParsers() throws Exception {
        // Arrange
        JsonParser[] parsers = new JsonParser[3];
        parsers[0] = mock(JsonParser.class);
        parsers[1] = mock(JsonParser.class);
        parsers[2] = mock(JsonParser.class);

        JsonParserSequence sequence = new JsonParserSequence(parsers);

        // Act
        sequence.close();

        // Assert
        verify(parsers[0], times(1)).close();
        verify(parsers[1], times(1)).close();
        verify(parsers[2], times(1)).close();
    }

    @Test
    public void testNextToken_withNoTokensInAllParsers_shouldReturnNull() throws Exception {
        // Arrange
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = mock(JsonParser.class);
        when(parsers[0].nextToken()).thenReturn(null);

        parsers[1] = mock(JsonParser.class);
        when(parsers[1].nextToken()).thenReturn(null);

        JsonParserSequence sequence = new JsonParserSequence(parsers);

        // Act
        JsonToken result = sequence.nextToken();

        // Assert
        assertNull(result);
        verify(parsers[0], times(1)).nextToken();
        verify(parsers[1], times(1)).nextToken();
    }

    @Test
    public void testNextToken_withTokenInFirstParser_shouldReturnToken() throws Exception {
        // Arrange
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = mock(JsonParser.class);
        when(parsers[0].nextToken()).thenReturn(JsonToken.VALUE_STRING);

        parsers[1] = mock(JsonParser.class);

        JsonParserSequence sequence = new JsonParserSequence(parsers);

        // Act
        JsonToken result = sequence.nextToken();

        // Assert
        assertEquals(JsonToken.VALUE_STRING, result);
        verify(parsers[0], times(1)).nextToken();
        verify(parsers[1], never()).nextToken();
    }

    @Test
    public void testNextToken_withTokenInSecondParser_shouldReturnToken() throws Exception {
        // Arrange
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = mock(JsonParser.class);
        when(parsers[0].nextToken()).thenReturn(null);

        parsers[1] = mock(JsonParser.class);
        when(parsers[1].nextToken()).thenReturn(JsonToken.START_OBJECT);

        JsonParserSequence sequence = new JsonParserSequence(parsers);

        // Act
        JsonToken result = sequence.nextToken();

        // Assert
        assertEquals(JsonToken.START_OBJECT, result);
        verify(parsers[0], times(1)).nextToken();
        verify(parsers[1], times(1)).nextToken();
    }

    @Test
    public void testContainedParsersCount_withMultipleParsers_shouldReturnCorrectCount() throws Exception {
        // Arrange
        JsonParser[] parsers = new JsonParser[4];
        parsers[0] = mock(JsonParser.class);
        parsers[1] = mock(JsonParser.class);
        parsers[2] = mock(JsonParser.class);
        parsers[3] = mock(JsonParser.class);

        JsonParserSequence sequence = new JsonParserSequence(parsers);

        // Act
        int result = sequence.containedParsersCount();

        // Assert
        assertEquals(4, result);
    }
}