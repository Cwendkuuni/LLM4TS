import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

public class JsonParserSequenceTest {

    // Test case for JsonParserSequence constructor
    @Test
    public void testJsonParserSequenceConstructor() {
        JsonParser[] parsers = {mock(JsonParser.class), mock(JsonParser.class)};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        assertNotNull(sequence);
    }

    // Test case for createFlattened method
    @Test
    public void testCreateFlattened() {
        JsonParser first = mock(JsonParser.class);
        JsonParser second = mock(JsonParser.class);
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        assertNotNull(sequence);
    }

    // Test case for close method
    @Test
    public void testClose() throws IOException {
        JsonParser[] parsers = {mock(JsonParser.class)};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        sequence.close();
        verify(parsers[0], times(1)).close();
    }

    // Test case for nextToken method
    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser[] parsers = {mock(JsonParser.class)};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        when(parsers[0].nextToken()).thenReturn(JsonToken.VALUE_STRING);
        assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
    }

    // Test case for containedParsersCount method
    @Test
    public void testContainedParsersCount() {
        JsonParser[] parsers = {mock(JsonParser.class), mock(JsonParser.class)};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        assertEquals(2, sequence.containedParsersCount());
    }

    // Test case for switchToNext method
    @Test
    public void testSwitchToNext() {
        JsonParser[] parsers = {mock(JsonParser.class), mock(JsonParser.class)};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        assertTrue(sequence.switchToNext());
    }
}