import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest extends BaseMapTest {

    @Test
    public void testBooleanKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String key = "true";
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class).deserializeKey(key, ctxt);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class).deserializeKey(key, ctxt);
        assertEquals(Boolean.FALSE, result);

        key = "not_a_boolean";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class).deserializeKey(key, ctxt);
        assertNull(result);
    }

    @Test
    public void testByteKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String key = "10";
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class).deserializeKey(key, ctxt);
        assertEquals((byte) 10, result);

        key = "-50";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class).deserializeKey(key, ctxt);
        assertEquals((byte) -50, result);

        key = "256";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class).deserializeKey(key, ctxt);
        assertNull(result);
    }

    // ... Continue testing for other data types: short, char, int, long, float, double, Locale, Date, Calendar, UUID, URI, URL, Class, Currency

    @Test
    public void testUnknownKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String key = "MyCustomClass";
        Object result = new StdKeyDeserializer(-1, Class.class).deserializeKey(key, ctxt);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testInvalidEnumKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String key = "InvalidEnumValue";
        EnumResolver resolver = ctxt.findEnumValues(Enum.class);
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class).deserializeKey(key, ctxt);
    }

    @Test(expected = JsonProcessingException.class)
    public void testEnumKeyDeserializationWithUnknownValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String key = "UnknownValue";
        EnumResolver resolver = EnumResolver.constructUnsafeUsingToString(Locale.class, ctxt.getAnnotationIntrospector());
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class).deserializeKey(key, ctxt);
    }

    @Test
    public void testNullKey() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String key = null;
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class).deserializeKey(key, ctxt);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testMalformedURLKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String key = "malformed://url";
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class).deserializeKey(key, ctxt);
    }

    @Test
    public void testMalformedURIKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String key = "malformed://url";
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class).deserializeKey(key, ctxt);
    }

    @Test(expected = IOException.class)
    public void testMalformedUUIDKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String key = "invalid_uuid";
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class).deserializeKey(key, ctxt);
    }
}