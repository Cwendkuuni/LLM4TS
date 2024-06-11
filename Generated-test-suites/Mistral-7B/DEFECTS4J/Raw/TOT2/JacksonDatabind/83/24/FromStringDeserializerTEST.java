import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);

        String jsonString = "\"C:\\test.txt\"";
        File result = mapper.readValue(jsonString, File.class, deserializer);
        assertNotNull(result);
        assertEquals("C:\\test.txt", result.getAbsolutePath());
    }

    @Test
    public void testURLDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);

        String jsonString = "\"http://www.example.com\"";
        URL result = mapper.readValue(jsonString, URL.class, deserializer);
        assertNotNull(result);
        assertEquals("http://www.example.com", result.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);

        String jsonString = "\"http://www.example.com/test?param=value\"";
        URI result = mapper.readValue(jsonString, URI.class, deserializer);
        assertNotNull(result);
        assertEquals("http://www.example.com/test?param=value", result.toString());
    }

    @Test
    public void testClassDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);

        String jsonString = "\"java.lang.StringBuilder\"";
        StringBuilder result = mapper.readValue(jsonString, StringBuilder.class, deserializer);
        assertNotNull(result);
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);

        String jsonString = "\"hello\"";
        try {
            mapper.readValue(jsonString, Integer.class, deserializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testNullDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);

        String jsonString = "\"null\"";
        String result = mapper.readValue(jsonString, String.class, deserializer);
        assertNull(result);
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);

        String jsonString = "\"\"";
        String result = mapper.readValue(jsonString, String.class, deserializer);
        assertNull(result);
    }

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.class;

        String jsonString = "[ ]";
        Object result = mapper.readValue(jsonString, Object[].class, deserializer);
        assertArrayEquals(new Object[0], result);
    }

    @Test
    public void testEmbeddedObjectDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "{\"class\":\"java.lang.StringBuilder\",\"value\":\"test\"}";
        StringBuilder result = mapper.readValue(jsonString, StringBuilder.class);
        assertNotNull(result);
        assertEquals("test", result.toString());
    }

    @Test
    public void testInvalidEmbeddedObjectDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "{\"class\":\"java.util.ArrayList\",\"value\":\"test\"}";
        try {
            mapper.readValue(jsonString, StringBuilder.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}