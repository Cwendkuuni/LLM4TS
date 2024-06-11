import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionWithMessage() {
        JsonProcessor processor = MockJsonProcessor.getMockJsonParser();
        JsonMappingException exception = new JsonMappingException(processor, "Test Message");
        assertEquals("Test Message", exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithLocation() {
        JsonProcessor processor = MockJsonProcessor.getMockJsonParser();
        JsonLocation location = new JsonLocation(1, 1, "test.json");
        JsonMappingException exception = new JsonMappingException(processor, "Test Message", location);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(location, exception.getLocation());
    }

    @Test
    public void testJsonMappingExceptionWithRootCause() {
        JsonProcessor processor = MockJsonProcessor.getMockJsonParser();
        IOException rootCause = new IOException("Root Cause Message");
        JsonMappingException exception = new JsonMappingException(processor, "Test Message", rootCause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(rootCause, exception.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        JsonProcessor processor = MockJsonProcessor.getMockJsonParser();
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException exception = mapper.readValue(new ObjectMapper().createBuffer("{\"a\":[{\"b\": {\"c\": 1}, \"d\": 2}]}")
                , List.class).mapException(JsonMappingException.class);
        assertNotNull(exception.getPath());
        assertEquals(1, exception.getPath().size());
        assertEquals("a[0].b.c", exception.getPathReference());
    }

    @Test
    public void testJsonMappingExceptionWithEmptyPath() {
        JsonProcessor processor = MockJsonProcessor.getMockJsonParser();
        JsonMappingException exception = new JsonMappingException(processor, "Test Message");
        assertNull(exception.getPath());
    }
}

class MockJsonProcessor {

    public static JsonProcessor getMockJsonParser() {
        return new MockJsonParser();
    }

    static class MockJsonParser implements JsonParser {
        @Override
        public void close() throws IOException {
        }

        @Override
        public void nextToken() throws IOException {
        }

        @Override
        public int nextValue() throws IOException {
            return 0;
        }

        @Override
        public JsonToken currentToken() {
            return JsonToken.VALUE_NULL;
        }

        @Override
        public JsonLocation getTokenLocation() {
            return null;
        }

        @Override
        public void setCodec(JsonCodec codec) {
        }
    }
}