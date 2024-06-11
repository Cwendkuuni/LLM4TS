import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Mock
    private JsonParser mockJsonParser;
    private JsonParserSequence parserSequence;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parserSequence = new JsonParserSequence(new JsonParser[] { mockJsonParser });
    }

    @Test
    public void testClose_SuccessfullyClosesAllParsers() throws Exception {
        // Arrange
        doReturn(jsonToken).doReturn(null).when(mockJsonParser).nextToken();

        // Act
        parserSequence.close();

        // Assert
        verify(mockJsonParser, times(2)).close();
    }

    @Test
    public void testNextToken_ReturnsNextToken() throws Exception {
        // Arrange
        doReturn(jsonToken).doReturn(null).when(mockJsonParser).nextToken();

        // Act
        JsonToken result = parserSequence.nextToken();

        // Assert
        assertEquals(jsonToken, result);
    }

    @Test
    public void testNextToken_HandlesSwitchToNext() throws Exception {
        // Arrange
        doReturn(null).doReturn(jsonToken).doReturn(null).when(mockJsonParser).nextToken();

        // Act
        JsonToken result = parserSequence.nextToken();

        // Assert
        assertEquals(jsonToken, result);
        verify(mockJsonParser).close();
    }

    @Test
    public void testNextToken_ReturnsNullWhenNoMoreTokens() throws Exception {
        // Arrange
        doReturn(null).doReturn(null).when(mockJsonParser).nextToken();

        // Act
        JsonToken result = parserSequence.nextToken();

        // Assert
        assertNull(result);
    }

    @Test
    public void testContainedParsersCount_ReturnsCorrectCount() {
        // Arrange
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { mockJsonParser, mockJsonParser });

        // Act
        int count = parserSequence.containedParsersCount();

        // Assert
        assertEquals(2, count);
    }

    // Add more test cases to cover additional scenarios such as error handling and edge cases

}