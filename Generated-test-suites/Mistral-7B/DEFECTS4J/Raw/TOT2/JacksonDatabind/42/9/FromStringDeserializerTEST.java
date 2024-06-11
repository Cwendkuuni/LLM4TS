import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeFile() throws IOException {
        String json = "{\"@class\":\"File\",\"path\":\"test.txt\"}";
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File result = deserializer.deserialize(jp, ctxt);
        assertEquals("test.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException {
        String json = "{\"@class\":\"URL\",\"string\":\"https://www.google.com\"}";
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL result = deserializer.deserialize(jp, ctxt);
        assertEquals("https://www.google.com", result.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFile() throws IOException {
        String json = "{\"@class\":\"File\",\"path\":\"xyz\"}";
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        deserializer.deserialize(jp, ctxt); // Should throw JsonMappingException
    }

    // Add test cases for other supported types (URI, Class, JavaType, Currency, Pattern, Locale, Charset, TimeZone, InetAddress, InetSocketAddress)

    @Test
    public void testDeserializeEmptyString() throws IOException {
        String json = "{\"@class\":\"Locale\",\"string\":\"\"}";
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale result = deserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNull() throws IOException {
        String json = "{\"@class\":\"File\",\"string\":null}";
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        deserializer.deserialize(jp, ctxt); // Should throw JsonMappingException
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        String json = "{\"@class\":\"File\",\"path\":\"test.txt\"}x";
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        deserializer.deserialize(jp, ctxt); // Should throw JsonMappingException
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnsupportedType() throws IOException {
        String json = "{\"@class\":\"Integer\",\"string\":\"test.txt\"}";
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        FromStringDeserializer<Integer> deserializer = new FromStringDeserializer.Std(Integer.class, FromStringDeserializer.Std.STD_INT);
        deserializer.deserialize(jp, ctxt); // Should throw JsonMappingException
    }
}