import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class JsonMappingExceptionTest {

    @org.junit.Test
    public void testDefaultConstructor() {
        JsonMappingException exception = new JsonMappingException();
        assertNotNull(exception.getMessage());
    }

    @org.junit.Test
    public void testConstructorWithMessage() {
        JsonMappingException exception = new JsonMappingException("Test Message");
        assertEquals("Test Message", exception.getMessage());
    }

    @org.junit.Test
    public void testConstructorWithMessageAndCause() {
        RuntimeException cause = new RuntimeException("Caused by RuntimeException");
        JsonMappingException exception = new JsonMappingException("Test Message", cause);
        assertEquals("Test Message", exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @org.junit.Test
    public void testConstructorWithMessageAndLocation() {
        JsonLocation location = new JsonLocation(1, 2, "Test File");
        JsonMappingException exception = new JsonMappingException("Test Message", location);
        assertEquals("Test Message\n\tat com.fasterxml.jackson.databind.JsonMappingException: Test Message\n\t  at TestFile:1:2", exception.getMessage());
        assertEquals(location, exception.getLocation());
    }

    @org.junit.Test
    public void testConstructorWithMessageAndLocationAndCause() {
        JsonLocation location = new JsonLocation(1, 2, "Test File");
        RuntimeException cause = new RuntimeException("Caused by RuntimeException");
        JsonMappingException exception = new JsonMappingException("Test Message", location, cause);
        assertEquals("Test Message\n\tat com.fasterxml.jackson.databind.JsonMappingException: Test Message\n\t  at TestFile:1:2\n\tcaused by: java.lang.RuntimeException: Caused by RuntimeException", exception.getMessage());
        assertSame(location, exception.getLocation());
        assertSame(cause, exception.getCause());
    }

    @org.junit.Test
    public void testFromJsonParser() {
        ObjectMapper mapper = new ObjectMapper();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        JsonMappingException exception = JsonMappingException.from(mapper.getFactory().createParser(input), "Test Message");
        assertEquals("Test Message", exception.getMessage());
    }

    @org.junit.Test
    public void testFromJsonParserWithLocation() {
        ObjectMapper mapper = new ObjectMapper();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        JsonLocation location = new JsonLocation(1, 2, "Test File");
        JsonMappingException exception = JsonMappingException.from(mapper.getFactory().createParser(input), "Test Message", location);
        assertEquals("Test Message\n\tat com.fasterxml.jackson.databind.JsonMappingException: Test Message\n\t  at TestFile:1:2", exception.getMessage());
        assertEquals(location, exception.getLocation());
    }

    @org.junit.Test
    public void testFromJsonParserWithLocationAndCause() {
        ObjectMapper mapper = new ObjectMapper();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        JsonLocation location = new JsonLocation(1, 2, "Test File");
        RuntimeException cause = new RuntimeException("Caused by RuntimeException");
        JsonMappingException exception = JsonMappingException.from(mapper.getFactory().createParser(input), "Test Message", location, cause);
        assertEquals("Test Message\n\tat com.fasterxml.jackson.databind.JsonMappingException: Test Message\n\t  at TestFile:1:2\n\tcaused by: java.lang.RuntimeException: Caused by RuntimeException", exception.getMessage());
        assertEquals(location, exception.getLocation());
        assertSame(cause, exception.getCause());
    }

    @org.junit.Test
    public void testFromJsonGenerator() {
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonMappingException exception = JsonMappingException.from(mapper.getFactory().createGenerator(output), "Test Message");
        assertEquals("Test Message", exception.getMessage());
    }

    @org.junit.Test
    public void testFromJsonGeneratorWithLocation() {
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonLocation location = new JsonLocation(1, 2, "Test File");
        JsonMappingException exception = JsonMappingException.from(mapper.getFactory().createGenerator(output), "Test Message", location);
        assertEquals("Test Message\n\tat com.fasterxml.jackson.databind.JsonMappingException: Test Message\n\t  at TestFile:1:2", exception.getMessage());
        assertEquals(location, exception.getLocation());
    }

    @org.junit.Test
    public void testFromJsonGeneratorWithLocationAndCause() {
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonLocation location = new JsonLocation(1, 2, "Test File");
        RuntimeException cause = new RuntimeException("Caused by RuntimeException");
        JsonMappingException exception = JsonMappingException.from(mapper.getFactory().createGenerator(output), "Test Message", location, cause);
        assertEquals("Test Message\n\tat com.fasterxml.jackson.databind.JsonMappingException: Test Message\n\t  at TestFile:1:2\n\tcaused by: java.lang.RuntimeException: Caused by RuntimeException", exception.getMessage());
        assertEquals(location, exception.getLocation());
        assertSame(cause, exception.getCause());
    }

    @org.junit.Test
    public void testFromJsonProcessingException() {
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Test Message");
        JsonMappingException exception = JsonMappingException.from(jsonProcessingException);
        assertEquals("Test Message", exception.getMessage());
    }

    @org.junit.Test
    public void testFromJsonProcessingExceptionWithLocation() {
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Test Message", new JsonLocation(1, 2, "Test File"));
        JsonMappingException exception = JsonMappingException.from(jsonProcessingException);
        assertEquals("Test Message\n\tat com.fasterxml.jackson.databind.JsonMappingException: Test Message\n\t  at TestFile:1:2", exception.getMessage());
        assertEquals(jsonProcessingException.getLocation(), exception.getLocation());
    }

    @org.junit.Test
    public void testFromJsonProcessingExceptionWithLocationAndCause() {
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Test Message", new JsonLocation(1, 2, "Test File"), new RuntimeException("Caused by RuntimeException"));
        JsonMappingException exception = JsonMappingException.from(jsonProcessingException);
        assertEquals("Test Message\n\tat com.fasterxml.jackson.databind.JsonMappingException: Test Message\n\t  at TestFile:1:2\n\tcaused by: java.lang.RuntimeException: Caused by RuntimeException", exception.getMessage());
        assertEquals(jsonProcessingException.getLocation(), exception.getLocation());
        assertSame(jsonProcessingException.getCause(), exception.getCause());
    }

    @org.junit.Test
    public void testFromUnexpectedIOException() {
        IOException ioException = new IOException("Unexpected IOException");
        JsonMappingException exception = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type java.io.IOException): Unexpected IOException", exception.getMessage());
    }

    @org.junit.Test
    public void testWrapWithPath() {
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Test Message");
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(jsonProcessingException, mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0])), "testFieldName");
        assertEquals("Test Message\n\tat com.fasterxml.jackson.databind.JsonMappingException: Test Message\n\t  at TestFile(JsonMappingExceptionTest.java:XXX)\n\t    at com.fasterxml.jackson.databind.JsonParser$ParsingIterator.nextValue(JsonParser.java:1131)\n\tcaused by: com.fasterxml.jackson.core.JsonProcessingException: Test Message", jsonMappingException.getMessage());
    }

    @org.junit.Test
    public void testWrapWithPathWithArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.readTree(new ByteArrayInputStream(new byte[0]));
        ArrayNode arrayNode = (ArrayNode) rootNode.path("arrayField");
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Test Message");
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(jsonProcessingException, mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0])), null, arrayNode.size() - 1);
        assertEquals("Test Message\n\tat com.fasterxml.jackson.databind.JsonMappingException: Test Message\n\t  at TestFile(JsonMappingExceptionTest.java:XXX)\n\t    at com.fasterxml.jackson.databind.JsonParser$ParsingIterator.nextValue(JsonParser.java:1131)\n\t    at com.fasterxml.jackson.databind.JsonParser$ValueInputFactory.readValue(JsonParser.java:1020)\n\t    at com.fasterxml.jackson.databind.ObjectMapper._readValue(ObjectMapper.java:3672)\n\t    at com.fasterxml.jackson.databind.ObjectMapper.readValue(ObjectMapper.java:1325)\n\t    at JsonMappingExceptionTest.testWrapWithPathWithArray(JsonMappingExceptionTest.java:XXX)", jsonMappingException.getMessage());
    }

    @org.junit.Test
    public void testWrapWithPathWithMap() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.readTree(new ByteArrayInputStream(new byte[0]));
        ObjectNode mapNode = (ObjectNode) rootNode.path("mapField");
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Test Message");
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(jsonProcessingException, mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0])), null, mapNode.get("key1").asText());
        assertEquals("Test Message\n\tat com.fasterxml.jackson.databind.JsonMappingException: Test Message\n\t  at TestFile(JsonMappingExceptionTest.java:XXX)\n\t    at com.fasterxml.jackson.databind.JsonParser$ParsingIterator.nextValue(JsonParser.java:1131)\n\t    at com.fasterxml.jackson.databind.JsonParser$ValueInputFactory.readValue(JsonParser.java:1020)\n\t    at com.fasterxml.jackson.databind.ObjectMapper._readValue(ObjectMapper.java:3672)\n\t    at com.fasterxml.jackson.databind.ObjectMapper.readValue(ObjectMapper.java:1325)\n\t    at JsonMappingExceptionTest.testWrapWithPathWithMap(JsonMappingExceptionTest.java:XXX)", jsonMappingException.getMessage());
    }
}