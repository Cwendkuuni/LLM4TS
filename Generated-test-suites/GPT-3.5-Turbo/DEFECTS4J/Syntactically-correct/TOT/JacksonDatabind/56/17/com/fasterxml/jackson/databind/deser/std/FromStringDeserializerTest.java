
import org.junit.Test;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialize() {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = (File) fileDeserializer._deserialize("/path/to/file", null);
        assertNotNull(file);
        assertEquals(new File("/path/to/file"), file);
    }

    @Test
    public void testURLDeserialize() {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        String urlString = "http://www.example.com";
        URL url = (URL) urlDeserializer._deserialize(urlString, null);
        assertNotNull(url);
        assertEquals(urlString, url.toString());
    }

    @Test
    public void testURIDeserialize() {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        String uriString = "http://www.example.com";
        URI uri = (URI)  uriDeserializer._deserialize(uriString, null);
        assertNotNull(uri);
        assertEquals(uriString, uri.toString());
    }

    // Add test methods for other deserializers

}

