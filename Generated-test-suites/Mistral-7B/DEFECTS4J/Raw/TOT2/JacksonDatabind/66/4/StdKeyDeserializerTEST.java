import com.fasterxml.jackson.core.base.ParserUtils;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBooleanDeserializer() throws Exception {
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);

        DeserializationContext context = createContext();
        Object key = booleanDeserializer.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, key);

        key = booleanDeserializer.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, key);

        key = booleanDeserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testByteDeserializer() throws Exception {
        StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);

        DeserializationContext context = createContext();
        Object key = byteDeserializer.deserializeKey("127", context);
        assertEquals((byte) 127, key);

        key = byteDeserializer.deserializeKey("-128", context);
        assertEquals((byte) -128, key);

        key = byteDeserializer.deserializeKey("256", context);
        assertNull(key);
    }

    @Test
    public void testShortDeserializer() throws Exception {
        StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);

        DeserializationContext context = createContext();
        Object key = shortDeserializer.deserializeKey("32767", context);
        assertEquals((short) 32767, key);

        key = shortDeserializer.deserializeKey("-32768", context);
        assertEquals((short) -32768, key);

        key = shortDeserializer.deserializeKey("65536", context);
        assertNull(key);
    }

    @Test
    public void testCharDeserializer() throws Exception {
        StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);

        DeserializationContext context = createContext();
        Object key = charDeserializer.deserializeKey("'A'", context);
        assertEquals('A', key);

        key = charDeserializer.deserializeKey("'\u00E9'", context);
        assertEquals('\u00E9', key);

        key = charDeserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testIntDeserializer() throws Exception {
        StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);

        DeserializationContext context = createContext();
        Object key = intDeserializer.deserializeKey("2147483647", context);
        assertEquals(Integer.MAX_VALUE, key);

        key = intDeserializer.deserializeKey("-2147483648", context);
        assertEquals(Integer.MIN_VALUE, key);

        key = intDeserializer.deserializeKey("4294967297", context);
        assertNull(key);
    }

    @Test
    public void testLongDeserializer() throws Exception {
        StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);

        DeserializationContext context = createContext();
        Object key = longDeserializer.deserializeKey("9223372036854775807", context);
        assertEquals(Long.MAX_VALUE, key);

        key = longDeserializer.deserializeKey("-9223372036854775808", context);
        assertEquals(Long.MIN_VALUE, key);

        key = longDeserializer.deserializeKey("18446744073709551616", context);
        assertNull(key);
    }

    @Test
    public void testFloatDeserializer() throws Exception {
        StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);

        DeserializationContext context = createContext();
        Object key = floatDeserializer.deserializeKey("3.14", context);
        assertEquals(3.14f, key, 0.001f);

        key = floatDeserializer.deserializeKey("Inf", context);
        assertEquals(Float.POSITIVE_INFINITY, key);

        key = floatDeserializer.deserializeKey("-Inf", context);
        assertEquals(Float.NEGATIVE_INFINITY, key);

        key = floatDeserializer.deserializeKey("NaN", context);
        assertEquals(Float.NaN, key);

        key = floatDeserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testDoubleDeserializer() throws Exception {
        StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);

        DeserializationContext context = createContext();
        Object key = doubleDeserializer.deserializeKey("6.28318530718", context);
        assertEquals(6.28318530718, key, 0.00001);

        key = doubleDeserializer.deserializeKey("1.79769313486231E308", context);
        assertEquals(Double.MAX_VALUE, key);

        key = doubleDeserializer.deserializeKey("-1.79769313486231E308", context);
        assertEquals(Double.MIN_VALUE, key);

        key = doubleDeserializer.deserializeKey("NaN", context);
        assertEquals(Double.NaN, key);

        key = doubleDeserializer.deserializeKey("Invalid", context);
        assertNull(key);
    }

    @Test
    public void testLocaleDeserializer() throws Exception {
        StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);

        DeserializationContext context = createContext();
        Object key = localeDeserializer.deserializeKey("en_US", context);
        assertEquals(Locale.US, key);

        key = localeDeserializer.deserializeKey("fr_FR", context);
        assertEquals(Locale.FRANCE, key);

        key = localeDeserializer.deserializeKey("es_ES@UTF-8", context);
        assertEquals(new Locale("es", "ES"), key);

        key = localeDeserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testDateDeserializer() throws Exception {
        StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);

        DeserializationContext context = createContext();
        Object key = dateDeserializer.deserializeKey("2022-01-01", context);
        assertEquals("Mon Jan 03 00:00:00 CST 2022", key.toString());

        key = dateDeserializer.deserializeKey("1970-01-01", context);
        assertEquals("Thu Jan 01 00:00:00 CST 1970", key.toString());

        key = dateDeserializer.deserializeKey("2022-02-30", context);
        assertNull(key);
    }

    @Test
    public void testCalendarDeserializer() throws Exception {
        StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);

        DeserializationContext context = createContext();
        Object key = calendarDeserializer.deserializeKey("2022-01-01", context);
        assertEquals("Mon Jan 03 00:00:00 CST 2022", key.toString());

        key = calendarDeserializer.deserializeKey("1970-01-01", context);
        assertEquals("Thu Jan 01 00:00:00 CST 1970", key.toString());

        key = calendarDeserializer.deserializeKey("2022-13-31", context);
        assertNull(key);
    }

    @Test
    public void testURIDeserializer() throws Exception {
        StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);

        DeserializationContext context = createContext();
        Object key = uriDeserializer.deserializeKey("http://example.com/path", context);
        assertEquals("http://example.com/path", key.toString());

        key = uriDeserializer.deserializeKey("https://example.com", context);
        assertEquals("https://example.com", key.toString());

        key = uriDeserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testURLDeserializer() throws Exception {
        StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);

        DeserializationContext context = createContext();
        Object key = urlDeserializer.deserializeKey("http://example.com/path", context);
        assertEquals("http://example.com/path", key.toString());

        key = urlDeserializer.deserializeKey("https://example.com", context);
        assertEquals("https://example.com", key.toString());

        key = urlDeserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testClassDeserializer() throws Exception {
        StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String.class);

        DeserializationContext context = createContext();
        Class<?> keyClass = Class.forName("java.lang.String");
        Object key = classDeserializer.deserializeKey(keyClass.getName(), context);
        assertEquals(keyClass, key.getClass());

        key = classDeserializer.deserializeKey("java.util.ArrayList", context);
        assertEquals(ArrayList.class, key.getClass());

        key = classDeserializer.deserializeKey("java.util.HashMap", context);
        assertEquals(HashMap.class, key.getClass());

        key = classDeserializer.deserializeKey("java.lang.Math", context);
        assertEquals(Math.class, key.getClass());

        key = classDeserializer.deserializeKey("java.lang.Object", context);
        assertEquals(Object.class, key.getClass());

        key = classDeserializer.deserializeKey("com.example.MyClass", context);
        assertNull(key);
    }

    @Test
    public void testStringDeserializer() throws Exception {
        StdKeyDeserializer stringDeserializer = StdKeyDeserializer.forType(String.class);

        DeserializationContext context = createContext();
        Object key = stringDeserializer.deserializeKey("Hello World", context);
        assertEquals("Hello World", key);

        key = stringDeserializer.deserializeKey("", context);
        assertEquals("", key);

        key = stringDeserializer.deserializeKey("null", context);
        assertEquals(null, key);

        key = stringDeserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testEnumDeserializer() throws Exception {
        enum TestEnum {
            ONE, TWO, THREE
        }

        StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(TestEnum.class);

        DeserializationContext context = createContext();
        Object key = enumDeserializer.deserializeKey("ONE", context);
        assertEquals(TestEnum.ONE, key);

        key = enumDeserializer.deserializeKey("TWO", context);
        assertEquals(TestEnum.TWO, key);

        key = enumDeserializer.deserializeKey("THREE", context);
        assertEquals(TestEnum.THREE, key);

        key = enumDeserializer.deserializeKey("FOUR", context);
        assertNull(key);
    }

    @Test
    public void testNullDeserializer() throws Exception {
        StdKeyDeserializer nullDeserializer = StdKeyDeserializer.forType(Void.TYPE);

        DeserializationContext context = createContext();
        Object key = nullDeserializer.deserializeKey("null", context);
        assertNull(key);

        key = nullDeserializer.deserializeKey("", context);
        assertNull(key);

        key = nullDeserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    private DeserializationContext createContext() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper.getDeserializationContext();
    }
}