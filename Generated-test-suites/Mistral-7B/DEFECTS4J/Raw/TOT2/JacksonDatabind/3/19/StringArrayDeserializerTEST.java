import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDefaultDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"hello\", \"world\"]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = mapper.readerForArrayOf(String.class).readValue(json);
        assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test
    public void testCustomDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = new StandardDeserializer<String>(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String json = "[\"custom1\", \"custom2\"]";
        String[] result = mapper.readerForArrayOf(String.class).readValue(json, deserializer);
        assertArrayEquals(new String[]{"custom1", "custom2"}, result);
    }

    @Test
    public void testEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        String[] result = mapper.readValue(json, StringArrayDeserializer.class);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testNullArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "null";
        String[] result = mapper.readValue(json, StringArrayDeserializer.class);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testMissingStartArrayToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\"}";
        mapper.readerForArrayOf(String.class).readValue(json);
    }

    @Test(expected = IOException.class)
    public void testEndArrayTokenMissing() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"hello\", ";
        mapper.readerForArrayOf(String.class).readValue(json);
    }

    @Test(expected = IOException.class)
    public void testNullElementInArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null, \"world\"]";
        mapper.readerForArrayOf(String.class).readValue(json);
    }

    @Test(expected = IOException.class)
    public void testNonStringElementInArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        mapper.readerForArrayOf(String.class).readValue(json);
    }
}