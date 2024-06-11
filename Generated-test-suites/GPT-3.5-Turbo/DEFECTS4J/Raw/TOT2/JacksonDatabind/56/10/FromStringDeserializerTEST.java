import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class FromStringDeserializerTest {

    @Test
    public void testTypes() {
        Class<?>[] types = FromStringDeserializer.types();
        assertNotNull(types);
        assertEquals(12, types.length);
    }

    @Test
    public void testFindDeserializer() {
        assertNull(FromStringDeserializer.findDeserializer(String.class));
        assertNotNull(FromStringDeserializer.findDeserializer(File.class));
        assertNotNull(FromStringDeserializer.findDeserializer(Locale.class));
    }

    @Test
    public void testStd_deserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 1);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JsonToken token = JsonToken.VALUE_STRING;
        try {
            deserializer.deserialize(token, ctxt);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testStd_deserialize_EmptyString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 2);
        try {
            deserializer.deserialize(JsonToken.VALUE_STRING, new DeserializationContext(null, null));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testInvalidDeserialization() {
        FromStringDeserializer<?> deserializer = new FromStringDeserializer.Std(String.class, 3);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JsonToken token = JsonToken.VALUE_STRING;
        assertThrows(IllegalArgumentException.class, () -> deserializer.deserialize(token, ctxt));
    }

    @Test
    public void testChameleon_deserialize() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 4);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        String value = "test";
        try {
            deserializer._deserialize(value, ctxt);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testChameleon_deserialize_Embedded() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 5);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object obj = "test";
        try {
            deserializer._deserializeEmbedded(obj, ctxt);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testChameleon_deserialize_EmptyString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 6);
        try {
            deserializer._deserializeFromEmptyString();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}