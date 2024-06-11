import com.fasterxml.jackson.core.base.ParserUtils;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testBooleanType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = serializer.deserializeKey("true", createContext());
        assertEquals(Boolean.TRUE, key);

        key = serializer.deserializeKey("false", createContext());
        assertEquals(Boolean.FALSE, key);

        key = serializer.deserializeKey("invalid", createContext());
        assertNull(key);
    }

    @Test
    public void testByteType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object key = serializer.deserializeKey("10", createContext());
        assertEquals((byte) 10, key);

        key = serializer.deserializeKey("-5", createContext());
        assertEquals((byte) -5, key);

        key = serializer.deserializeKey("128", createContext());
        assertNull(key);
    }

    @Test
    public void testShortType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object key = serializer.deserializeKey("12345", createContext());
        assertEquals((short) 12345, key);

        key = serializer.deserializeKey("-32768", createContext());
        assertEquals((short) -32768, key);

        key = serializer.deserializeKey("32768", createContext());
        assertNull(key);
    }

    @Test
    public void testCharType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object key = serializer.deserializeKey("a", createContext());
        assertEquals('a', key);

        key = serializer.deserializeKey("\\u0061", createContext());
        assertEquals('a', key);

        key = serializer.deserializeKey(" invalid", createContext());
        assertNull(key);
    }

    @Test
    public void testIntType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object key = serializer.deserializeKey("123456", createContext());
        assertEquals(123456, key);

        key = serializer.deserializeKey("-123456", createContext());
        assertEquals(-123456, key);

        key = serializer.deserializeKey("2147483648", createContext());
        assertNull(key);
    }

    @Test
    public void testLongType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object key = serializer.deserializeKey("12345678901234567890", createContext());
        assertEquals(12345678901234567890L, key);

        key = serializer.deserializeKey("-12345678901234567890", createContext());
        assertEquals(-12345678901234567890L, key);

        key = serializer.deserializeKey("9223372036854775808", createContext());
        assertNull(key);
    }

    @Test
    public void testFloatType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object key = serializer.deserializeKey("3.14159265358979323846", createContext());
        assertEquals(3.14159265358979323846f, key, 0.00001f);

        key = serializer.deserializeKey("-3.14159265358979323846", createContext());
        assertEquals(-3.14159265358979323846f, key, 0.00001f);

        key = serializer.deserializeKey("inf", createContext());
        assertNull(key);

        key = serializer.deserializeKey("-inf", createContext());
        assertNull(key);
    }

    @Test
    public void testDoubleType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object key = serializer.deserializeKey("3.14159265358979323846264338327950288416", createContext());
        assertEquals(3.14159265358979323846264338327950288416, key, 0.000001);

        key = serializer.deserializeKey("-3.14159265358979323846264338327950288416", createContext());
        assertEquals(-3.14159265358979323846264338327950288416, key, 0.000001);

        key = serializer.deserializeKey("nan", createContext());
        assertNull(key);

        key = serializer.deserializeKey("-nan", createContext());
        assertNull(key);

        key = serializer.deserializeKey("inf", createContext());
        assertNull(key);

        key = serializer.deserializeKey("-inf", createContext());
        assertNull(key);
    }

    @Test
    public void testLocaleType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object key = serializer.deserializeKey("en_US", createContext());
        assertEquals(Locale.US, key);

        key = serializer.deserializeKey("fr_FR", createContext());
        assertEquals(Locale.FRANCE, key);

        key = serializer.deserializeKey("invalidLocale", createContext());
        assertNull(key);
    }

    @Test
    public void testDateType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Date.class);
        ObjectNode node = mapper.createObjectNode();
        node.put("year", 2022);
        node.put("monthValue", 5);
        node.put("dayOfMonth", 23);

        Object key = serializer.deserializeKey(node.getValueAsString(), createContext());
        assertEquals(new Date(2022, 5, 23), key);

        key = serializer.deserializeKey("invalidDate", createContext());
        assertNull(key);
    }

    @Test
    public void testCalendarType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        ObjectNode node = mapper.createObjectNode();
        node.put("year", 2022);
        node.put("monthValue", 5);
        node.put("dayOfMonth", 23);
        node.put("hourOfDay", 12);
        node.put("minute", 30);
        node.put("second", 45);

        Object key = serializer.deserializeKey(node.getValueAsString(), createContext());
        Calendar calendar = (Calendar) key;
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(5, calendar.get(Calendar.MONTH));
        assertEquals(23, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
        assertEquals(45, calendar.get(Calendar.SECOND));

        key = serializer.deserializeKey("invalidCalendar", createContext());
        assertNull(key);
    }

    @Test
    public void testUUIDType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        Object key = serializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", createContext());
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), key);

        key = serializer.deserializeKey("invalidUUID", createContext());
        assertNull(key);
    }

    @Test
    public void testUriType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object key = serializer.deserializeKey("http://example.com/test", createContext());
        assertEquals(new URI("http://example.com/test"), key);

        key = serializer.deserializeKey("invalidUri", createContext());
        assertNull(key);
    }

    @Test
    public void testUrlType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object key = serializer.deserializeKey("http://example.com/test", createContext());
        assertEquals(new URL("http://example.com/test"), key);

        key = serializer.deserializeKey("invalidUrl", createContext());
        assertNull(key);
    }

    @Test
    public void testClassType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String[].class);
        Object key = serializer.deserializeKey("[Ljava.lang.String;", createContext());
        assertEquals(new String[]{"test1", "test2"}, key);

        key = serializer.deserializeKey("invalidClass", createContext());
        assertNull(key);

        serializer = StdKeyDeserializer.forType(MyClass.class);
        key = serializer.deserializeKey("com.example.MyClass@12345678", createContext());
        assertEquals(new MyClass(), key);

        key = serializer.deserializeKey("invalidClass2", createContext());
        assertNull(key);
    }

    @Test
    public void testExceptionHandling() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);

        try {
            Object key = serializer.deserializeKey("invalidJSON", createContext());
            assertNull(key);
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testNullKey() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);

        Object key = serializer.deserializeKey(null, createContext());
        assertNull(key);
    }

    @Test
    public void testEmptyStringKey() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);

        Object key = serializer.deserializeKey("", createContext());
        assertNull(key);
    }

    @Test
    public void testInvalidInput() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);

        Object key = serializer.deserializeKey("invalidInput", createContext());
        assertNull(key);
    }

    private DeserializationContext createContext() {
        return mapper.getDeserializationContext();
    }
}