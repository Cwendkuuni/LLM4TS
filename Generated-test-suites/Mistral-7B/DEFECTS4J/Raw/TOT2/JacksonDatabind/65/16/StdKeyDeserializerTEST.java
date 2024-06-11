import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBooleanDeserializer() throws Exception {
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = booleanDeserializer.deserializeKey("true", context);
        assert result instanceof Boolean;
        assert ((Boolean) result).booleanValue();

        result = booleanDeserializer.deserializeKey("false", context);
        assert result instanceof Boolean;
        assert !((Boolean) result).booleanValue();

        result = booleanDeserializer.deserializeKey("invalid", context);
        assert result == context.handleWeirdKey(Boolean.class, "invalid", "not a valid representation");
    }

    @Test
    public void testByteDeserializer() throws Exception {
        StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = byteDeserializer.deserializeKey("1", context);
        assert result instanceof Byte;
        assert ((Byte) result).byteValue() == 1;

        result = byteDeserializer.deserializeKey("-128", context);
        assert result instanceof Byte;
        assert ((Byte) result).byteValue() == -128;

        result = byteDeserializer.deserializeKey("256", context);
        assert result == context.handleWeirdKey(Byte.class, "256", "overflow, value can not be represented as 8-bit value");
    }

    @Test
    public void testShortDeserializer() throws Exception {
        StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = shortDeserializer.deserializeKey("123", context);
        assert result instanceof Short;
        assert ((Short) result).shortValue() == 123;

        result = shortDeserializer.deserializeKey("-32768", context);
        assert result instanceof Short;
        assert ((Short) result).shortValue() == -32768;

        result = shortDeserializer.deserializeKey("32768", context);
        assert result == context.handleWeirdKey(Short.class, "32768", "overflow, value can not be represented as 16-bit value");
    }

    @Test
    public void testCharDeserializer() throws Exception {
        StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = charDeserializer.deserializeKey("A", context);
        assert result instanceof Character;
        assert ((Character) result).charValue() == 'A';

        result = charDeserializer.deserializeKey("z", context);
        assert result instanceof Character;
        assert ((Character) result).charValue() == 'z';

        result = charDeserializer.deserializeKey("invalid", context);
        assert result == context.handleWeirdKey(Character.class, "invalid", "can only convert 1-character Strings");
    }

    @Test
    public void testIntDeserializer() throws Exception {
        StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = intDeserializer.deserializeKey("12345", context);
        assert result instanceof Integer;
        assert ((Integer) result).intValue() == 12345;

        result = intDeserializer.deserializeKey("-123456", context);
        assert result instanceof Integer;
        assert ((Integer) result).intValue() == -123456;
    }

    @Test
    public void testLongDeserializer() throws Exception {
        StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = longDeserializer.deserializeKey("1234567890123456789", context);
        assert result instanceof Long;
        assert ((Long) result).longValue() == 1234567890123456789L;

        result = longDeserializer.deserializeKey("-1234567890123456789", context);
        assert result instanceof Long;
        assert ((Long) result).longValue() == -1234567890123456789L;
    }

    @Test
    public void testFloatDeserializer() throws Exception {
        StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = floatDeserializer.deserializeKey("123.45", context);
        assert result instanceof Float;
        assert ((Float) result).floatValue() == 123.45f;
    }

    @Test
    public void testDoubleDeserializer() throws Exception {
        StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = doubleDeserializer.deserializeKey("123.45", context);
        assert result instanceof Double;
        assert ((Double) result).doubleValue() == 123.45;

        result = doubleDeserializer.deserializeKey("-123.45", context);
        assert result instanceof Double;
        assert ((Double) result).doubleValue() == -123.45;
    }

    @Test
    public void testLocaleDeserializer() throws Exception {
        StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = localeDeserializer.deserializeKey("en_US", context);
        assert result instanceof Locale;
        assert ((Locale) result).equals(Locale.US);

        result = localeDeserializer.deserializeKey("fr_FR", context);
        assert result instanceof Locale;
        assert ((Locale) result).equals(Locale.FRENCH);

        result = localeDeserializer.deserializeKey("invalid", context);
        assert result == context.handleWeirdKey(Locale.class, "invalid", "unable to parse key as locale");
    }

    @Test
    public void testEnumDeserializer() throws Exception {
        EnumSet<DaysOfWeek> expected = EnumSet.allOf(DaysOfWeek.class);

        StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(DaysOfWeek.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = enumDeserializer.deserializeKey("MONDAY", context);
        assert result instanceof DaysOfWeek;
        assert ((DaysOfWeek) result).equals(DaysOfWeek.MONDAY);

        result = enumDeserializer.deserializeKey("SUNDAY", context);
        assert result instanceof DaysOfWeek;
        assert ((DaysOfWeek) result).equals(DaysOfWeek.SUNDAY);

        result = enumDeserializer.deserializeKey("THURSDAY", context);
        assert result instanceof DaysOfWeek;
        assert ((DaysOfWeek) result).equals(DaysOfWeek.THURSDAY);

        result = enumDeserializer.deserializeKey("invalid", context);
        assert result == context.handleWeirdKey(DaysOfWeek.class, "invalid", "not one of values excepted for Enum class: [" + Arrays.toString(DaysOfWeek.values()) + "]");
    }

    @Test
    public void testUUIDDeserializer() throws Exception {
        StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = uuidDeserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", context);
        assert result instanceof UUID;
        assert ((UUID) result).equals(new UUID("123e4567-e89b-12d3-a456-426614174000"));

        result = uuidDeserializer.deserializeKey("invalid", context);
        assert result == context.handleWeirdKey(UUID.class, "invalid", "problem: %s", "Malformed UUID");
    }

    @Test
    public void testURIDeserializer() throws Exception {
        StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = uriDeserializer.deserializeKey("http://example.com", context);
        assert result instanceof URI;
        assert ((URI) result).equals(new URI("http://example.com"));

        result = uriDeserializer.deserializeKey("invalid", context);
        assert result == context.handleWeirdKey(URI.class, "invalid", "problem: %s", "Malformed URL");
    }

    @Test
    public void testURLDeserializer() throws Exception {
        StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = urlDeserializer.deserializeKey("http://example.com", context);
        assert result instanceof URL;
        assert ((URL) result).equals(new URL("http://example.com"));

        result = urlDeserializer.deserializeKey("invalid", context);
        assert result == context.handleWeirdKey(URL.class, "invalid", "problem: %s", "Malformed URL");
    }

    @Test
    public void testClassDeserializer() throws Exception {
        StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(HashMap.class);

        DeserializationContext context = mapper.getDeserializationContext();

        Object result = classDeserializer.deserializeKey("java.util.HashMap", context);
        assert result instanceof Map;
        assert ((Map) result).getClass().equals(HashMap.class);

        result = classDeserializer.deserializeKey("java.lang.String", context);
        assert result instanceof String;
    }
}