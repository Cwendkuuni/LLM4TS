import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionDefaultConstructor() {
        JsonMappingException jsonMappingException = new JsonMappingException();
        assertEquals("", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getLocation());
    }

    @Test
    public void testJsonMappingExceptionMessageConstructor() {
        JsonMappingException jsonMappingException = new JsonMappingException("Test Message");
        assertEquals("Test Message", jsonMappingException.getMessage());
        assertNull(jsonMappingException.getLocation());
    }

    @Test
    public void testJsonMappingExceptionMessageAndLocationConstructor() {
        JsonLocation location = new JsonLocation(1, 1, "test.json");
        JsonMappingException jsonMappingException = new JsonMappingException("Test Message", location);
        assertEquals("Test Message", jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
    }

    @Test
    public void testJsonMappingExceptionMessageAndLocationAndRootCauseConstructor() {
        JsonLocation location = new JsonLocation(1, 1, "test.json");
        IOException rootCause = new IOException("Root Cause");
        JsonMappingException jsonMappingException = new JsonMappingException("Test Message", location, rootCause);
        assertEquals("Test Message", jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
        assertEquals(rootCause, jsonMappingException.getCause());
    }

    @Test
    public void testJsonMappingExceptionWithProcessor() {
        Closeable processor = null; // replace with a real JsonParser or JsonGenerator instance
        String msg = "Test Message";
        JsonMappingException jsonMappingException = new JsonMappingException(processor, msg);
        assertEquals(msg, jsonMappingException.getMessage());
        assertNull(jsonMappingException.getLocation());
        assertEquals(processor, jsonMappingException.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionWithProcessorMessageConstructor() {
        Closeable processor = null; // replace with a real JsonParser or JsonGenerator instance
        String msg = "Test Message";
        JsonMappingException jsonMappingException = new JsonMappingException(processor, msg, "Test Root Cause");
        assertEquals(msg, jsonMappingException.getMessage());
        assertEquals("Test Root Cause", jsonMappingException.getCause().getMessage());
        assertEquals(processor, jsonMappingException.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionWithProcessorMessageAndLocationConstructor() {
        Closeable processor = null; // replace with a real JsonParser or JsonGenerator instance
        String msg = "Test Message";
        JsonLocation location = new JsonLocation(1, 1, "test.json");
        JsonMappingException jsonMappingException = new JsonMappingException(processor, msg, location);
        assertEquals(msg, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
        assertEquals(processor, jsonMappingException.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionWithProcessorMessageLocationAndRootCauseConstructor() {
        Closeable processor = null; // replace with a real JsonParser or JsonGenerator instance
        String msg = "Test Message";
        JsonLocation location = new JsonLocation(1, 1, "test.json");
        IOException rootCause = new IOException("Test Root Cause");
        JsonMappingException jsonMappingException = new JsonMappingException(processor, msg, location, rootCause);
        assertEquals(msg, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
        assertEquals(rootCause, jsonMappingException.getCause());
        assertEquals(processor, jsonMappingException.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParser() {
        JsonParser parser = null; // replace with a real JsonParser instance
        String msg = "Test Message";
        JsonMappingException jsonMappingException = JsonMappingException.from(parser, msg);
        assertEquals(msg, jsonMappingException.getMessage());
        assertNull(jsonMappingException.getLocation());
        assertNull(jsonMappingException.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParserMessageConstructor() {
        JsonParser parser = null; // replace with a real JsonParser instance
        String msg = "Test Message";
        String rootCauseMessage = "Test Root Cause";
        JsonMappingException jsonMappingException = JsonMappingException.from(parser, msg, new IOException(rootCauseMessage));
        assertEquals(msg, jsonMappingException.getMessage());
        assertEquals(rootCauseMessage, jsonMappingException.getCause().getMessage());
        assertNull(jsonMappingException.getLocation());
        assertNull(jsonMappingException.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParserMessageLocationConstructor() {
        JsonParser parser = null; // replace with a real JsonParser instance
        String msg = "Test Message";
        JsonLocation location = new JsonLocation(1, 1, "test.json");
        JsonMappingException jsonMappingException = JsonMappingException.from(parser, msg, location);
        assertEquals(msg, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
        assertNull(jsonMappingException.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionFromJsonParserMessageLocationAndRootCauseConstructor() {
        JsonParser parser = null; // replace with a real JsonParser instance
        String msg = "Test Message";
        JsonLocation location = new JsonLocation(1, 1, "test.json");
        IOException rootCause = new IOException("Test Root Cause");
        JsonMappingException jsonMappingException = JsonMappingException.from(parser, msg, location, rootCause);
        assertEquals(msg, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
        assertEquals(rootCause, jsonMappingException.getCause());
        assertNull(jsonMappingException.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionFromJsonGenerator() {
        JsonGenerator generator = null; // replace with a real JsonGenerator instance
        String msg = "Test Message";
        JsonMappingException jsonMappingException = JsonMappingException.from(generator, msg);
        assertEquals(msg, jsonMappingException.getMessage());
        assertNull(jsonMappingException.getLocation());
        assertEquals(generator, jsonMappingException.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionFromJsonGeneratorMessageConstructor() {
        JsonGenerator generator = null; // replace with a real JsonGenerator instance
        String msg = "Test Message";
        String rootCauseMessage = "Test Root Cause";
        JsonMappingException jsonMappingException = JsonMappingException.from(generator, msg, new IOException(rootCauseMessage));
        assertEquals(msg, jsonMappingException.getMessage());
        assertEquals(rootCauseMessage, jsonMappingException.getCause().getMessage());
        assertNull(jsonMappingException.getLocation());
        assertEquals(generator, jsonMappingException.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionFromJsonGeneratorMessageLocationConstructor() {
        JsonGenerator generator = null; // replace with a real JsonGenerator instance
        String msg = "Test Message";
        JsonLocation location = new JsonLocation(1, 1, "test.json");
        JsonMappingException jsonMappingException = JsonMappingException.from(generator, msg, location);
        assertEquals(msg, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
        assertEquals(generator, jsonMappingException.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionFromJsonGeneratorMessageLocationAndRootCauseConstructor() {
        JsonGenerator generator = null; // replace with a real JsonGenerator instance
        String msg = "Test Message";
        JsonLocation location = new JsonLocation(1, 1, "test.json");
        IOException rootCause = new IOException("Test Root Cause");
        JsonMappingException jsonMappingException = JsonMappingException.from(generator, msg, location, rootCause);
        assertEquals(msg, jsonMappingException.getMessage());
        assertEquals(location, jsonMappingException.getLocation());
        assertEquals(rootCause, jsonMappingException.getCause());
        assertEquals(generator, jsonMappingException.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionFromUnexpectedIOException() {
        IOException unexpectedIOException = new IOException("Unexpected IOException");
        JsonMappingException jsonMappingException = JsonMappingException.fromUnexpectedIOE(unexpectedIOException);
        assertEquals("Unexpected IOException (of type IOException): Unexpected IOException", jsonMappingException.getMessage());
        assertNull(jsonMappingException.getLocation());
        assertNull(jsonMappingException.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionWrapWithPath() {
        IOException rootCause = new IOException("Root Cause");
        Object referrer = new Object();
        String fieldName = "fieldName";
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(rootCause, referrer, fieldName);
        assertEquals("(was IOException): Root Cause (through reference chain: " + referrer.getClass().getName() + "." + fieldName + "->", jsonMappingException.getMessage());
        assertEquals(rootCause, jsonMappingException.getCause());
        List<Reference> references = jsonMappingException.getPath();
        assertEquals(1, references.size());
        Reference reference = references.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(fieldName, reference.getFieldName());
        assertNull(reference.getIndex());
        assertEquals(referrer.getClass().getName() + "." + fieldName, reference.toString());
    }

    @Test
    public void testJsonMappingExceptionWrapWithPathWithIndex() {
        IOException rootCause = new IOException("Root Cause");
        Object referrer = new Object()[0];
        int index = 0;
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(rootCause, referrer, index);
        assertEquals("(was IOException): Root Cause (through reference chain: " + referrer.getClass().getName() + "[0]->", jsonMappingException.getMessage());
        assertEquals(rootCause, jsonMappingException.getCause());
        List<Reference> references = jsonMappingException.getPath();
        assertEquals(1, references.size());
        Reference reference = references.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(index, reference.getIndex());
        assertEquals(referrer.getClass().getName() + "[0]", reference.toString());
    }
}