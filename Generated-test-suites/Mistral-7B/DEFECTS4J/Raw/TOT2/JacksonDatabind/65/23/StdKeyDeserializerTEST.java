import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private DeserializationContext ctxt;
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.findAndRegisterModules();
        ctxt = mapper.getDeserializationContext();
    }

    // Test for standard "String as String" deserializer
    @Test
    public void testStringKeyDeserializer() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(String.class);
        Object key = stdKeyDeserializer.deserializeKey("test", ctxt);
        assertEquals("test", key);
    }

    // Test for boolean type key deserializer
    @Test
    public void testBooleanKeyDeserializer() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = stdKeyDeserializer.deserializeKey("true", ctxt);
        assertTrue((Boolean) key);

        key = stdKeyDeserializer.deserializeKey("false", ctxt);
        assertFalse((Boolean) key);
    }

    // Test for integer type key deserializer
    @Test
    public void testIntKeyDeserializer() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = stdKeyDeserializer.deserializeKey("1", ctxt);
        assertEquals(1, key);

        key = stdKeyDeserializer.deserializeKey("-1", ctxt);
        assertEquals(-1, key);

        key = stdKeyDeserializer.deserializeKey("2147483647", ctxt); // Maximum value for int
        assertEquals(2147483647, key);

        key = stdKeyDeserializer.deserializeKey("-2147483648", ctxt); // Minimum value for int
        assertEquals(-2147483648, key);
    }

    // Test for short type key deserializer
    @Test
    public void testShortKeyDeserializer() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Short.class);
        Object key = stdKeyDeserializer.deserializeKey("1", ctxt);
        assertEquals(1, key);

        key = stdKeyDeserializer.deserializeKey("-1", ctxt);
        assertEquals(-1, key);

        key = stdKeyDeserializer.deserializeKey("32767", ctxt); // Maximum value for short
        assertEquals(32767, key);

        key = stdKeyDeserializer.deserializeKey("-32768", ctxt); // Minimum value for short
        assertEquals(-32768, key);
    }

    // Test for char type key deserializer
    @Test
    public void testCharKeyDeserializer() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Character.class);
        Object key = stdKeyDeserializer.deserializeKey("a", ctxt);
        assertEquals('a', key);

        key = stdKeyDeserializer.deserializeKey("\\u0061", ctxt); // Unicode representation of 'a'
        assertEquals('a', key);

        key = stdKeyDeserializer.deserializeKey("z", ctxt);
        assertEquals('z', key);

        key = stdKeyDeserializer.deserializeKey("\\u007a", ctxt); // Unicode representation of 'z'
        assertEquals('z', key);

        try {
            key = stdKeyDeserializer.deserializeKey("\\u0061\\u0062", ctxt); // Two-character Unicode sequence
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test for long type key deserializer
    @Test
    public void testLongKeyDeserializer() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object key = stdKeyDeserializer.deserializeKey("1", ctxt);
        assertEquals(1, key);

        key = stdKeyDeserializer.deserializeKey("-1", ctxt);
        assertEquals(-1, key);

        key = stdKeyDeserializer.deserializeKey("9223372036854775807", ctxt); // Maximum value for long
        assertEquals(9223372036854775807L, key);

        key = stdKeyDeserializer.deserializeKey("-9223372036854775808", ctxt); // Minimum value for long
        assertEquals(-9223372036854775808L, key);
    }

    // Test for float type key deserializer
    @Test
    public void testFloatKeyDeserializer() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Float.class);
        Object key = stdKeyDeserializer.deserializeKey("0.0", ctxt);
        assertEquals(0.0f, key, 0.0f);

        key = stdKeyDeserializer.deserializeKey("3.14159265", ctxt);
        assertEquals(3.14159265f, key, 0.00001f);

        key = stdKeyDeserializer.deserializeKey("-3.14159265", ctxt);
        assertEquals(-3.14159265f, key, 0.00001f);
    }

    // Test for double type key deserializer
    @Test
    public void testDoubleKeyDeserializer() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Double.class);
        Object key = stdKeyDeserializer.deserializeKey("0.0", ctxt);
        assertEquals(0.0, key, 0.0);

        key = stdKeyDeserializer.deserializeKey("3.14159265", ctxt);
        assertEquals(3.14159265, key, 0.000001);

        key = stdKeyDeserializer.deserializeKey("-3.14159265", ctxt);
        assertEquals(-3.14159265, key, 0.000001);
    }

    // Test for locale type key deserializer
    @Test
    public void testLocaleKeyDeserializer() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = stdKeyDeserializer.deserializeKey("en_US", ctxt);
        assertEquals(Locale.US, key);

        key = stdKeyDeserializer.deserializeKey("es_ES", ctxt);
        assertEquals(Locale.SPAIN, key);

        key = stdKeyDeserializer.deserializeKey("fr_CA", ctxt);
        assertEquals(Locale.CANADA, key);

        try {
            key = stdKeyDeserializer.deserializeKey("invalidLocale", ctxt); // Invalid locale
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test for URI type key deserializer
    @Test
    public void testURIKeyDeserializer() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(URI.class);
        Object key = stdKeyDeserializer.deserializeKey("http://example.com", ctxt);
        assertEquals("http://example.com", key.toString());

        key = stdKeyDeserializer.deserializeKey("https://example.com", ctxt);
        assertEquals("https://example.com", key.toString());

        key = stdKeyDeserializer.deserializeKey("ftp://example.com", ctxt);
        assertEquals("ftp://example.com", key.toString());

        try {
            key = stdKeyDeserializer.deserializeKey("invalidURI", ctxt); // Invalid URI
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test for URL type key deserializer
    @Test
    public void testURLKeyDeserializer() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(URL.class);
        Object key = stdKeyDeserializer.deserializeKey("http://example.com", ctxt);
        assertEquals("http://example.com", key.toString());

        key = stdKeyDeserializer.deserializeKey("https://example.com", ctxt);
        assertEquals("https://example.com", key.toString());

        key = stdKeyDeserializer.deserializeKey("ftp://example.com", ctxt);
        assertEquals("ftp://example.com", key.toString());

        try {
            key = stdKeyDeserializer.deserializeKey("invalidURL", ctxt); // Invalid URL
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test for enum type key deserializer
    @Test
    public void testEnumKeyDeserializer() throws IOException {
        enum TestEnum {
            ONE,
            TWO,
            THREE
        }

        StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(TestEnum.class);

        TestEnum key = (TestEnum) stdKeyDeserializer.deserializeKey("ONE", ctxt);
        assertEquals(TestEnum.ONE, key);

        key = (TestEnum) stdKeyDeserializer.deserializeKey("TWO", ctxt);
        assertEquals(TestEnum.TWO, key);

        try {
            key = (TestEnum) stdKeyDeserializer.deserializeKey("INVALID", ctxt); // Invalid enum value
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test for custom key deserializer
    @Test
    public void testCustomKeyDeserializer() throws IOException {
        class CustomClass {
            private final String name;

            CustomClass(String name) {
                this.name = name;
            }

            String getName() {
                return name;
            }
        }

        class CustomKeyDeserializer extends StdKeyDeserializer {
            private final JsonDeserializer<CustomClass> _deserializer;

            CustomKeyDeserializer(JsonDeserializer<CustomClass> deserializer) {
                super(-1, CustomClass.class);
                _deserializer = deserializer;
            }

            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                try {
                    ObjectNode node = mapper.readValue(new ObjectMapper().createParser(new StringReader(key)), ObjectNode.class);
                    return _deserializer.deserialize(node, ctxt);
                } catch (JsonProcessingException e) {
                    return super.deserializeKey(key, ctxt);
                }
            }
        }

        ObjectMapper customMapper = new ObjectMapper();
        customMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        customMapper.findAndRegisterModules();
        JsonDeserializer<CustomClass> customDeserializer = customMapper.deserializationContext().findDeserializer(CustomClass.class);

        Object key = new CustomKeyDeserializer(customDeserializer).deserializeKey("{\"name\":\"test\"}", ctxt);
        CustomClass customClass = (CustomClass) key;
        assertEquals("test", customClass.getName());

        try {
            key = new CustomKeyDeserializer(customDeserializer).deserializeKey("invalid", ctxt); // Invalid key
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test for exception handling
    @Test
    public void testExceptionHandling() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = StdKeyDeserializer.forType(Integer.class);

        try {
            Object key = stdKeyDeserializer.deserializeKey("invalid", ctxt);
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        try {
            Object key = stdKeyDeserializer.deserializeKey("java.util.ArrayList", ctxt);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Object key = stdKeyDeserializer.deserializeKey("", ctxt);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Object key = stdKeyDeserializer.deserializeKey(null, ctxt);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}