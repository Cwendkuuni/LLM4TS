import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.fasterxml.jackson.databind.*;

public class MappingIteratorTest {

    @Test
    public void testHasNext() throws IOException {
        JsonParser parser = createMockParser();
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, true, null);

        // Test when the iterator is empty
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext()); // Should have consumed the first element
        assertFalse(iterator.hasNext()); // Should not have next element

        // Test when the iterator has reached the end
        parser = createMockParser();
        iterator = new MappingIterator<>(null, parser, null, null, true, null);
        parser.nextToken(); // Consume the first token
        assertFalse(iterator.hasNext());

        // Test when an IOException is thrown
        parser = createMockParserWithIOException();
        iterator = new MappingIterator<>(null, parser, null, null, true, null);
        assertFalse(iterator.hasNext());

        // Test when a JsonMappingException is thrown
        parser = createMockParserWithJsonMappingException();
        iterator = new MappingIterator<>(null, parser, null, null, true, null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        JsonParser parser = createMockParser();
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, null, null, true, null);

        Object next = iterator.next();
        assertTrue(iterator.hasNext());
        Object next2 = iterator.next();
        assertFalse(iterator.hasNext());

        // Test when an IOException is thrown
        parser = createMockParserWithIOException();
        iterator = new MappingIterator<>(null, parser, null, null, true, null);
        try {
            iterator.next();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test when a JsonMappingException is thrown
        parser = createMockParserWithJsonMappingException();
        iterator = new MappingIterator<>(null, parser, null, null, true, null);
        try {
            iterator.next();
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    private JsonParser createMockParser() {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        return parser;
    }

    private JsonParser createMockParserWithIOException() {
        JsonParser parser = mock(JsonParser.class);
        when(parser.nextToken()).thenThrow(new IOException("Mock IOException"));
        return parser;
    }

    private JsonParser createMockParserWithJsonMappingException() {
        JsonParser parser = mock(JsonParser.class);
        when(parser.nextToken()).thenThrow(new JsonMappingException("Mock JsonMappingException"));
        return parser;
    }
}