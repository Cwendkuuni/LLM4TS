import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URI;
import java.util.Currency;
import java.util.regex.Pattern;
import java.util.Locale;
import java.nio.charset.Charset;
import java.util.TimeZone;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class FromStringDeserializerTest {

    @Test
    public void testTypesMethod() {
        Class<?>[] types = FromStringDeserializer.types();
        assertTrue(types.length == 12);
    }

    @Test
    public void testFindDeserializerMethod() {
        Std deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(deserializer);
    }

    @Test
    public void testDeserializeMethod() throws IOException {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = (File) stdDeserializer.deserialize(createJsonParser("someFile"), new DeserializationContext(null));
        assertNotNull(file);
        assertEquals("someFile", file.getName());
    }

    private JsonParser createJsonParser(String value) {
        return new JsonParser() {
            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public JsonToken nextToken() throws IOException {
                return null;
            }

            @Override
            public JsonToken getCurrentToken() {
                return null;
            }

            @Override
            public String getValueAsString() {
                return value;
            }

            @Override
            public JsonToken nextValue() throws IOException {
                return null;
            }
        };
    }

}