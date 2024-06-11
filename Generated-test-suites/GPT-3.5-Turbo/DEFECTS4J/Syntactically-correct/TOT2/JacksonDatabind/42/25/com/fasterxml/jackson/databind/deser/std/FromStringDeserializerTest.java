import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class FromStringDeserializerTest {

    @Test
    public void testTypes() {
        Class<?>[] types = FromStringDeserializer.types();
        assertNotNull(types);
        assertEquals(12, types.length);
    }

    @Test
    public void testFindDeserializerForFile() {
        FromStringDeserializer.Std deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof FromStringDeserializer.Std);
    }

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"test.txt\"");
        DeserializationContext ctxt = new DeserializationContext(jsonParser);
        File file = (File) deserializer.deserialize(jsonParser, ctxt);
        assertNotNull(file);
        assertEquals("test.txt", file.getName());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"http://www.example.com\"");
        DeserializationContext ctxt = new DeserializationContext(jsonParser);
        URI uri = (URI) deserializer.deserialize(jsonParser, ctxt);
        assertNotNull(uri);
        assertEquals("http://www.example.com", uri.toString());
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"http://www.example.com\"");
        DeserializationContext ctxt = new DeserializationContext(jsonParser);
        URL url = (URL) deserializer.deserialize(jsonParser, ctxt);
        assertNotNull(url);
        assertEquals("http://www.example.com", url.toString());
    }

    @Test
    public void testDeserializeInvalidType() throws IOException {
        FromStringDeserializer.Std deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertNull(deserializer);
    }
}