import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBooleanDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);
        String key = "true";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(Boolean.TRUE, deserializedKey);

        key = "false";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(Boolean.FALSE, deserializedKey);

        key = "invalid";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testByteDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Byte.class);
        String key = "127";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals((byte) 127, deserializedKey);

        key = "-128";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals((byte) -128, deserializedKey);

        key = "256";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testShortDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Short.class);
        String key = "32767";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals((short) 32767, deserializedKey);

        key = "-32768";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals((short) -32768, deserializedKey);

        key = "65536";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testCharDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Character.class);
        String key = "A";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals('A', deserializedKey);

        key = "invalid";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);

        key = "multichar";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testIntDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);
        String key = "2147483647";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(Integer.MAX_VALUE, deserializedKey);

        key = "-2147483648";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(Integer.MIN_VALUE, deserializedKey);

        key = "2147483648";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testLongDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Long.class);
        String key = "9223372036854775807";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(Long.MAX_VALUE, deserializedKey);

        key = "-9223372036854775808";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(Long.MIN_VALUE, deserializedKey);

        key = "9223372036854775808";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testFloatDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Float.class);
        String key = "123.45";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(Float.valueOf(123.45f), deserializedKey);

        key = "invalid";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testDoubleDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Double.class);
        String key = "123.45";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(Double.valueOf(123.45), deserializedKey);

        key = "invalid";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testLocaleDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Locale.class);
        String key = "en_US";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(Locale.US, deserializedKey);

        key = "invalid";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testDateDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Date.class);
        String key = "2022-01-01";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(new Date(0), deserializedKey); // assume default date is "1970-01-01"

        key = "invalid";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testCalendarDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Calendar.class);
        String key = "2022-01-01";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNotNull(deserializedKey);

        Calendar c = (Calendar) deserializedKey;
        Assert.assertEquals(2022, c.get(Calendar.YEAR));
        Assert.assertEquals(1, c.get(Calendar.MONTH));
        Assert.assertEquals(1, c.get(Calendar.DAY_OF_MONTH));

        key = "invalid";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testURIDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(URI.class);
        String key = "http://example.com";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(new URI("http", "example.com", null), deserializedKey);

        key = "invalid";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testURLDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(URL.class);
        String key = "http://example.com";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(new URL("http", "example.com", null, null), deserializedKey);

        key = "invalid";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testEnumDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(MyEnum.class);
        String key = "MyEnum.FIRST";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(MyEnum.FIRST, deserializedKey);

        key = "MyEnum.SECOND";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals(MyEnum.SECOND, deserializedKey);

        key = "invalid";
        deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertNull(deserializedKey);
    }

    @Test
    public void testCustomDeserializer() throws Exception {
        KeyDeserializer kd = new StringFactoryKeyDeserializer(MyCustomDeserializer.class);
        String key = "myCustomKey";
        Object deserializedKey = kd.deserializeKey(key, getContext());
        Assert.assertEquals("MyCustomClass", deserializedKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDeserializer() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);
        kd.deserializeKey("invalid", getContext());
    }

    private DeserializationContext getContext() throws JsonProcessingException {
        return mapper.getDeserializationContext();
    }

    static class MyEnum {
        public static final MyEnum FIRST = new MyEnum();
        public static final MyEnum SECOND = new MyEnum();
    }

    static class MyCustomDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return "MyCustomClass";
        }
    }
}