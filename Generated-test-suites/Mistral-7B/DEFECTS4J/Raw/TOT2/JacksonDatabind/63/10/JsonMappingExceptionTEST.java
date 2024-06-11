import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonMappingExceptionTest {

    // Test case for constructor with message
    @Test
    public void testJsonMappingExceptionWithMessage() {
        JsonMappingException jsonMappingException = new JsonMappingException("Invalid JSON format");
        assertEquals("Invalid JSON format", jsonMappingException.getMessage());
    }

    // Test case for constructor with message and root cause
    @Test
    public void testJsonMappingExceptionWithMessageAndRootCause() {
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Invalid JSON format", new IOException("I/O error"));
        JsonMappingException jsonMappingException = new JsonMappingException("Invalid JSON format", jsonProcessingException);
        assertEquals("Invalid JSON format", jsonMappingException.getMessage());
        assertNotNull(jsonMappingException.getCause());
        assertEquals("I/O error", jsonMappingException.getCause().getMessage());
    }

    // Test case for constructor with message, location, and root cause
    @Test
    public void testJsonMappingExceptionWithMessageLocationAndRootCause() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Ferrari\"]}";
        JsonMappingException jsonMappingException = null;
        try {
            mapper.readValue(json, Person.class);
        } catch (JsonMappingException e) {
            jsonMappingException = e;
        }
        assertNotNull(jsonMappingException);
        assertEquals("Cannot deserialize value of type [simple type, class com.fasterxml.jackson.example.Person] from JSON String", jsonMappingException.getMessage());
        assertEquals("line 1, column 28", jsonMappingException.getLocation().getColumnNumber());
        assertEquals("line 1, column 27", jsonMappingException.getLocation().getLineNumber());
        assertNotNull(jsonMappingException.getCause());
        assertEquals("expected single value or an array but was of type: java.lang.String (String) (for attribute: \"cars\")", jsonMappingException.getCause().getMessage());
    }

    // Test case for constructors with JsonParser, Message, and root cause
    @Test
    public void testJsonMappingExceptionFromJsonParserWithMessageAndRootCause() {
        String json = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Ferrari\"]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException jsonMappingException = null;
        try {
            mapper.readValue(json, Person.class);
        } catch (JsonMappingException e) {
            jsonMappingException = e;
        }
        assertNotNull(jsonMappingException);
        Closeable parser = mapper.getFactory().createParser(json);
        JsonMappingException newJsonMappingException = JsonMappingException.from(parser, "Cannot deserialize value of type [simple type, class com.fasterxml.jackson.example.Person] from JSON String");
        assertEquals("Cannot deserialize value of type [simple type, class com.fasterxml.jackson.example.Person] from JSON String", newJsonMappingException.getMessage());
        assertNotNull(newJsonMappingException.getCause());
        assertEquals("expected single value or an array but was of type: java.lang.String (String) (for attribute: \"cars\")", newJsonMappingException.getCause().getMessage());
    }

    // Test case for constructors with JsonParser, Message, location, and root cause
    @Test
    public void testJsonMappingExceptionFromJsonParserWithMessageLocationAndRootCause() {
        String json = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Ferrari\"]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException jsonMappingException = null;
        try {
            mapper.readValue(json, Person.class);
        } catch (JsonMappingException e) {
            jsonMappingException = e;
        }
        assertNotNull(jsonMappingException);
        Closeable parser = mapper.getFactory().createParser(json);
        JsonMappingException newJsonMappingException = JsonMappingException.from(parser, "Cannot deserialize value of type [simple type, class com.fasterxml.jackson.example.Person] from JSON String", parser.getLocation());
        assertEquals("Cannot deserialize value of type [simple type, class com.fasterxml.jackson.example.Person] from JSON String", newJsonMappingException.getMessage());
        assertEquals("line 1, column 28", newJsonMappingException.getLocation().getColumnNumber());
        assertEquals("line 1, column 27", newJsonMappingException.getLocation().getLineNumber());
        assertNotNull(newJsonMappingException.getCause());
        assertEquals("expected single value or an array but was of type: java.lang.String (String) (for attribute: \"cars\")", newJsonMappingException.getCause().getMessage());
    }

    // Test case for constructors with JsonGenerator, Message, and root cause
    @Test
    public void testJsonMappingExceptionFromJsonGeneratorWithMessageAndRootCause() {
        String json = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Ferrari\"]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException jsonMappingException = null;
        try {
            mapper.readValue(json, Person[].class);
            mapper.writer().writeValueAsString(new Person());
        } catch (JsonMappingException e) {
            jsonMappingException = e;
        }
        assertNotNull(jsonMappingException);
        JsonMappingException newJsonMappingException = JsonMappingException.from(null, "Failed to write value of type", jsonMappingException);
        assertEquals("Failed to write value of type", newJsonMappingException.getMessage());
        assertNull(newJsonMappingException.getCause());
    }

    // Test case for constructors with JsonGenerator, Message, location, and root cause
    @Test
    public void testJsonMappingExceptionFromJsonGeneratorWithMessageLocationAndRootCause() {
        String json = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Ferrari\"]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException jsonMappingException = null;
        try {
            mapper.readValue(json, Person[].class);
            mapper.writer().writeValueAsString(new Person());
        } catch (JsonMappingException e) {
            jsonMappingException = e;
        }
        assertNotNull(jsonMappingException);
        JsonMappingException newJsonMappingException = JsonMappingException.from(null, "Failed to write value of type", jsonMappingException, new Location());
        assertEquals("Failed to write value of type", newJsonMappingException.getMessage());
        assertNull(newJsonMappingException.getLocation());
        assertNotNull(newJsonMappingException.getCause());
    }

    // Test case for constructors with IOException
    @Test
    public void testJsonMappingExceptionFromIOException() {
        IOException ioException = new IOException("Invalid I/O operation");
        JsonMappingException jsonMappingException = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type java.io.IOException): Invalid I/O operation", jsonMappingException.getMessage());
        assertEquals(ioException.getClass().getName(), jsonMappingException.getMessage().substring(0, "Unexpected IOException (of type java.io.IOException):".length()));
    }

    // Test case for wrapWithPath method
    @Test
    public void testJsonMappingExceptionWrapWithPath() {
        String json = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Ferrari\"]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException jsonMappingException = null;
        try {
            mapper.readValue(json, Person.class);
        } catch (JsonMappingException e) {
            jsonMappingException = e;
        }
        assertNotNull(jsonMappingException);
        Object referrer = mapper;
        String fieldName = "ObjectMapper";
        JsonMappingException newJsonMappingException = JsonMappingException.wrapWithPath(jsonMappingException, referrer, fieldName);
        assertEquals("Invalid JSON format (through reference chain: ObjectMapper(\"ObjectMapper\")->com.fasterxml.jackson.databind.ObjectMapper(\"ObjectMapper\")->JsonFactory(\"JsonFactory\")->JsonParser(\"JsonParser\")->JsonMappingException(\"JsonMappingException\"))", newJsonMappingException.getMessage());
        referrer = jsonMappingException;
        fieldName = "readValue";
        newJsonMappingException = JsonMappingException.wrapWithPath(jsonMappingException, referrer, fieldName);
        assertEquals("Invalid JSON format (through reference chain: JsonMappingException(\"JsonMappingException\")->JsonParser(\"JsonParser\")->ObjectMapper(\"ObjectMapper\")->JsonFactory(\"JsonFactory\")->JsonStreamParser(\"JsonStreamParser\")->JsonMappingException(\"JsonMappingException\"))", newJsonMappingException.getMessage());
    }

    // Test case for wrapWithPath method with index
    @Test
    public void testJsonMappingExceptionWrapWithPathWithIndex() {
        String json = "[1, 2, 3, 4]";
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException jsonMappingException = null;
        try {
            mapper.readValue(json, int[].class);
        } catch (JsonMappingException e) {
            jsonMappingException = e;
        }
        assertNotNull(jsonMappingException);
        Object referrer = jsonMappingException;
        int index = 2;
        JsonMappingException newJsonMappingException = JsonMappingException.wrapWithPath(jsonMappingException, referrer, index);
        assertEquals("expected single value or an array but was of type: int (at index: 2) (through reference chain: JsonMappingException(\"JsonMappingException\")->JsonParser(\"JsonParser\")->ObjectMapper(\"ObjectMapper\")->JsonFactory(\"JsonFactory\")->JsonStreamParser(\"JsonStreamParser\"))", newJsonMappingException.getMessage());
    }
}