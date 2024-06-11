package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Test cases for {@link StdKeyDeserializer}.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testForType() {
        Class<?>[] types = {String.class, Object.class, UUID.class, Integer.class, Long.class, Date.class,
                Calendar.class, Boolean.class, Byte.class, Character.class, Short.class, Float.class,
                Double.class, URI.class, URL.class, Class.class, Locale.class, Currency.class};
        for (Class<?> type : types) {
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(type);
            assertNotNull(deserializer);
            assertEquals(type, deserializer.getKeyClass());
        }

        // Unsupported types
        assertNull(StdKeyDeserializer.forType(ByteBuffer.class));
        assertNull(StdKeyDeserializer.forType(List.class));
        assertNull(StdKeyDeserializer.forType(Map.class));
        assertNull(StdKeyDeserializer.forType(ArrayList.class));
    }

    @Test
    public void testDeserializeKey_String() throws Exception {
        StringKD stringKD = StdKeyDeserializer.forType(String.class);
        Object key = stringKD.deserializeKey("test", new TestContext());
        assertEquals(key, "test");
    }

    @Test
    public void testDeserializeKey_Boolean() throws Exception {
        StdKeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);
        Object key = booleanKD.deserializeKey("true", new TestContext());
        assertTrue((Boolean) key);

        key = booleanKD.deserializeKey("false", new TestContext());
        assertFalse((Boolean) key);
    }

    @Test
    public void testDeserializeKey_Byte() throws Exception {
        StdKeyDeserializer byteKD = StdKeyDeserializer.forType(Byte.class);
        Object key = byteKD.deserializeKey("10", new TestContext());
        assertEquals((byte) 10, key);

        try {
            key = byteKD.deserializeKey("200", new TestContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Short() throws Exception {
        StdKeyDeserializer shortKD = StdKeyDeserializer.forType(Short.class);
        Object key = shortKD.deserializeKey("123", new TestContext());
        assertEquals((short) 123, key);

        try {
            key = shortKD.deserializeKey("32768", new TestContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Character() throws Exception {
        StdKeyDeserializer charKD = StdKeyDeserializer.forType(Character.class);
        Object key = charKD.deserializeKey("a", new TestContext());
        assertEquals('a', key);

        try {
            key = charKD.deserializeKey("123", new TestContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Integer() throws Exception {
        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);
        Object key = intKD.deserializeKey("456", new TestContext());
        assertEquals(456, key);
    }

    @Test
    public void testDeserializeKey_Long() throws Exception {
        StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);
        Object key = longKD.deserializeKey("12345678901234567890", new TestContext());
        assertEquals(12345678901234567890L, key);
    }

    @Test
    public void testDeserializeKey_Float() throws Exception {
        StdKeyDeserializer floatKD = StdKeyDeserializer.forType(Float.class);
        Object key = floatKD.deserializeKey("1.5", new TestContext());
        assertEquals(1.5f, key);
    }

    @Test
    public void testDeserializeKey_Double() throws Exception {
        StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);
        Object key = doubleKD.deserializeKey("3.14159265358979323846", new TestContext());
        assertEquals(3.14159265358979323846, key, 0.000001);
    }

    @Test
    public void testDeserializeKey_Locale() throws Exception {
        StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);
        Object key = localeKD.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);
    }

    @Test
    public void testDeserializeKey_URI() throws Exception {
        StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);
        Object key = uriKD.deserializeKey("https://www.example.com", new TestContext());
        URI expected = new URI("https", "www.example.com", null);
        assertEquals(expected, key);
    }

    @Test
    public void testDeserializeKey_URL() throws Exception {
        StdKeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);
        Object key = urlKD.deserializeKey("http://www.example.com", new TestContext());
        URL expected = new URL("http", "www.example.com", -1, null);
        assertEquals(expected, key);
    }

    @Test
    public void testDeserializeKey_Class() throws Exception {
        Class<?> type = String[].class;
        StdKeyDeserializer classKD = StdKeyDeserializer.forType(type);
        Object key = classKD.deserializeKey("new [Ljava.lang.String;", new TestContext());
        assertEquals(Arrays.class.getComponentType(), key.getClass().getComponentType());
    }

    @Test
    public void testDeserializeKey_Enum() throws Exception {
        Class<MyEnum> enumClass = MyEnum.class;
        StdKeyDeserializer enumKD = StdKeyDeserializer.forType(enumClass);
        Object key = enumKD.deserializeKey("FOO", new TestContext());
        assertEquals(MyEnum.FOO, key);

        try {
            key = enumKD.deserializeKey("BAR", new TestContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_UnknownEnumValue() throws Exception {
        Class<MyEnum> enumClass = MyEnum.class;
        StdKeyDeserializer enumKD = StdKeyDeserializer.forType(enumClass);
        Object key = enumKD.deserializeKey("BAZ", new TestContext(true));
        assertNull(key);
    }

    private static class TestContext extends DeserializationContext {
        private boolean allowUnknownEnumValues;

        public TestContext() {
            this(false);
        }

        public TestContext(boolean allowUnknownEnumValues) {
            super(new JsonFactory(), null);
            this.allowUnknownEnumValues = allowUnknownEnumValues;
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            if (DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL.equals(feature)) {
                return allowUnknownEnumValues;
            }
            return super.isEnabled(feature);
        }
    }
}