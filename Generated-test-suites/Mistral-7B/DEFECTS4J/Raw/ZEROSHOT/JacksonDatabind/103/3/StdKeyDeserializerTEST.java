import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.EnumSet;

@JsonAutoDetect
public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBooleanKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = deserializer.deserializeKey("true", objectMapper.getDeserializationContext());
        assertEquals(true, key);

        key = deserializer.deserializeKey("false", objectMapper.getDeserializationContext());
        assertEquals(false, key);
    }

    @Test
    public void testByteKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = deserializer.deserializeKey("127", objectMapper.getDeserializationContext());
        assertEquals((byte) 127, key);

        try {
            key = deserializer.deserializeKey("-128", objectMapper.getDeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testShortKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object key = deserializer.deserializeKey("32767", objectMapper.getDeserializationContext());
        assertEquals((short) 32767, key);

        try {
            key = deserializer.deserializeKey("-32768", objectMapper.getDeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCharKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object key = deserializer.deserializeKey("A", objectMapper.getDeserializationContext());
        assertEquals('A', key);
    }

    @Test
    public void testIntKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = deserializer.deserializeKey("100", objectMapper.getDeserializationContext());
        assertEquals(100, key);
    }

    @Test
    public void testLongKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object key = deserializer.deserializeKey("100000000000", objectMapper.getDeserializationContext());
        assertEquals(100000000000L, key);
    }

    @Test
    public void testFloatKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object key = deserializer.deserializeKey("3.1415F", objectMapper.getDeserializationContext());
        assertEquals(3.1415f, key);
    }

    @Test
    public void testDoubleKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object key = deserializer.deserializeKey("3.1415", objectMapper.getDeserializationContext());
        assertEquals(3.1415, key);
    }

    @Test
    public void testLocaleKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = deserializer.deserializeKey("en_US", objectMapper.getDeserializationContext());
        assertEquals(Locale.US, key);
    }

    @Test
    public void testDateKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(java.util.Date.class);
        Object key = deserializer.deserializeKey("2022-12-31", objectMapper.getDeserializationContext());
        assertEquals(new java.util.Date(2022, 11, 31), key);
    }

    @Test
    public void testCalendarKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(java.util.Calendar.class);
        Object key = deserializer.deserializeKey("2022-12-31", objectMapper.getDeserializationContext());
        Calendar calendar = (Calendar) key;
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testURIKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object key = deserializer.deserializeKey("http://example.com", objectMapper.getDeserializationContext());
        URI uri = (URI) key;
        assertEquals("http", uri.getScheme());
        assertEquals("example.com", uri.getHost());
    }

    @Test
    public void testURLKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object key = deserializer.deserializeKey("http://example.com", objectMapper.getDeserializationContext());
        URL url = (URL) key;
        assertEquals("http", url.getProtocol());
        assertEquals("example.com", url.getHost());
    }

    @Test
    public void testClassKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
        Object key = deserializer.deserializeKey("[Ljava.lang.String;", objectMapper.getDeserializationContext());
        assertEquals(Arrays.asList("one", "two").toArray(), key);

        StdKeyDeserializer deserializer2 = StdKeyDeserializer.forType(String[].class);
        Constructor<Object> constructor = Arrays.class.getDeclaredConstructor(int.class, Class[].class);
        constructor.setAccessible(true);
        Object keyArray = constructor.newInstance(2, new Class[] { String.class });
        Object key = deserializer2.deserializeKey("[Ljava.lang.String;", objectMapper.getDeserializationContext());
        assertArrayEquals(new String[] { "one", "two" }, (String[]) keyArray);
    }

    @Test
    public void testEnumKeyDeserializer() throws IOException {
        enum MyEnum {
            ONE,
            TWO,
            THREE
        }

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);

        Object key = deserializer.deserializeKey("ONE", objectMapper.getDeserializationContext());
        assertEquals(MyEnum.ONE, key);

        key = deserializer.deserializeKey("THREE", objectMapper.getDeserializationContext());
        assertEquals(MyEnum.THREE, key);

        key = deserializer.deserializeKey("FOO", objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testStringKeyDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object key = deserializer.deserializeKey("test", objectMapper.getDeserializationContext());
        assertEquals("test", key);
    }

    @Test
    public void testObjectKeyDeserializer() throws IOException {
        @JsonDeserialize(using = MyCustomDeserializer.class)
        static class MyCustomClass {
            @JsonProperty
            private String name;

            public String getName() {
                return name;
            }
        }

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyCustomClass.class);
        Object key = deserializer.deserializeKey("{\"name\":\"test\"}", objectMapper.getDeserializationContext());
        MyCustomClass myCustomClass = (MyCustomClass) key;
        assertEquals("test", myCustomClass.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnknownKeyType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(EnumSet.class);
        deserializer.deserializeKey("test", objectMapper.getDeserializationContext());
        fail("Expected IllegalStateException");
    }

    static class MyCustomDeserializer extends JsonDeserializer<MyCustomClass> {
        @Override
        public MyCustomClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            MyCustomClass myCustomClass = new MyCustomClass();
            myCustomClass.name = p.getValueAsString();
            return myCustomClass;
        }
    }
}