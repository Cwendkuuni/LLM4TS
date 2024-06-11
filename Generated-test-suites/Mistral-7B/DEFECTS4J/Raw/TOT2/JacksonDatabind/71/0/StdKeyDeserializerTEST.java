import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBooleanDeserializer() throws Exception {
        String[] testCases = new String[]{"true", "false"};
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals(key.equals("true"), result);
        }
    }

    @Test
    public void testByteDeserializer() throws Exception {
        String[] testCases = new String[]{"-128", "0", "127", "255"};
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals((byte) Integer.parseInt(key), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("-129", ctxt));
    }

    @Test
    public void testShortDeserializer() throws Exception {
        String[] testCases = new String[]{"-32768", "0", "32767", "65535"};
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals((short) Integer.parseInt(key), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("-32769", ctxt));
    }

    @Test
    public void testCharDeserializer() throws Exception {
        String[] testCases = new String[]{"a", "\u0001"};
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals(key.charAt(0), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("abc", ctxt));
    }

    @Test
    public void testIntDeserializer() throws Exception {
        String[] testCases = new String[]{"-2147483648", "0", "2147483647", "4294967295"};
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals(Integer.parseInt(key), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("-2147483649", ctxt));
    }

    @Test
    public void testLongDeserializer() throws Exception {
        String[] testCases = new String[]{"-9223372036854775808", "0", "9223372036854775807", "18446744073709551615"};
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals(Long.parseLong(key), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("-9223372036854775809", ctxt));
    }

    @Test
    public void testFloatDeserializer() throws Exception {
        String[] testCases = new String[]{"-3.4e38f", "0.0", "3.4e-38f", "1.234e2"};
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals(Float.parseFloat(key), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("NaN", ctxt));
    }

    @Test
    public void testDoubleDeserializer() throws Exception {
        String[] testCases = new String[]{"-1.79769313486231E308", "0.0", "1.79769313486231E-308", "4.94065645841247E19"};
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals(Double.parseDouble(key), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("NaN", ctxt));
    }

    @Test
    public void testLocaleDeserializer() throws Exception {
        String[] testCases = new String[]{"en_US", "fr_FR", "es_ES"};
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals(new Locale(key.split("_")[0], key.split("_")[1]), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("es_ES_Madrid", ctxt));
    }

    @Test
    public void testDateDeserializer() throws Exception {
        String[] testCases = new String[]{"2022-01-01", "2022-12-31", "2022-02-29"}; // leap year
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals(new Date(Long.parseLong(key.substring(0, 14) + "0000")), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("2022-03-31", ctxt));
    }

    @Test
    public void testCalendarDeserializer() throws Exception {
        String[] testCases = new String[]{"2022-01-01", "2022-12-31", "2022-02-29"}; // leap year
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(Long.parseLong(key.substring(0, 14) + "0000")));
            assertEquals(calendar, result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("2022-03-31", ctxt));
    }

    @Test
    public void testUUIDDeserializer() throws Exception {
        String[] testCases = new String[]{"-123e4567-1234-5678-1234-567845678456", "00112233-4455-6677-8888-999922334455"};
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals(UUID.fromString(key), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("00000000-0000-0000-0000-000000000000", ctxt));
    }

    @Test
    public void testURIDeserializer() throws Exception {
        String[] testCases = new String[]{"http://example.com", "https://example.com/path"};
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals(new URI(key), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("ftp://example.com", ctxt));
    }

    @Test
    public void testURLDeserializer() throws Exception {
        String[] testCases = new String[]{"http://example.com", "https://example.com/path"};
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals(new URL(key), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("ftp://example.com", ctxt));
    }

    @Test
    public void testClassDeserializer() throws Exception {
        String[] testCases = new String[]{"java.lang.String", "java.util.Date", "java.util.Locale"};
        for (String className : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Class.class);
            Object result = deserializer.deserializeKey(className, ctxt);
            assertEquals(Class.forName(className), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("java.util.ArrayList", ctxt));
    }

    @Test
    public void testCurrencyDeserializer() throws Exception {
        String[] testCases = new String[]{"USD", "EUR", "JPY"};
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Currency.class);
            Object result = deserializer.deserializeKey(key, ctxt);
            assertEquals(Currency.getInstance(key), result);
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("GBP", ctxt));
    }

    @Test
    public void testEnumDeserializer() throws Exception {
        EnumSet<DaysOfWeek> testCases = EnumSet.allOf(DaysOfWeek.class);
        for (DaysOfWeek day : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(DaysOfWeek.class);
            Object result = deserializer.deserializeKey(day.name(), ctxt);
            assertEquals(day, result);
        }
        assertNull(deserializer.deserializeKey("InvalidDay", ctxt));
    }

    @Test
    public void testDelegateKeyDeserializer() throws Exception {
        String[] testCases = new String[]{"{\"name\": \"John\"}", "{\"age\": 30}"};
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "John");
        jsonMap.put("age", 30);
        for (String key : testCases) {
            DeserializationContext ctxt = mapper.getDeserializationContext();
            StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_OBJECT, Object.class, mapper.getFactory());
            Object result = deserializer.deserializeKey(key, ctxt);
            assertInstanceOf(Map.Entry.class, result);
            assertEquals(jsonMap.get(key.split(":")[0]), result.getKey());
            assertEquals(jsonMap.get(key.split(":")[0]), result.getValue());
        }
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("invalidKey", ctxt));
    }
}