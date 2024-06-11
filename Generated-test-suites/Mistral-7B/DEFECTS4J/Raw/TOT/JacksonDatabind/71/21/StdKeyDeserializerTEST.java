package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializerProvider;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class StdKeyDeserializerTest extends BaseTest {

    @InjectMocks
    private StdKeyDeserializer deserializer;

    @SuppressWarnings("unchecked")
    private static <T> T newInstance(Class<T> keyClass) throws Exception {
        Constructor<T> constructor = keyClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        return constructor.newInstance("testKey");
    }

    @Test
    public void testBoolean() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        boolean result = deserializer.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, result);

        result = deserializer.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, result);

        assertThrows(IllegalArgumentException.class, () -> deserializer.deserializeKey("notBoolean", context));
    }

    @Test
    public void testByte() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        byte result = (byte) deserializer.deserializeKey("127", context);
        assertEquals((byte) 127, result);

        try {
            result = (byte) deserializer.deserializeKey("-129", context);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // Expected
        }

        assertThrows(IllegalArgumentException.class, () -> deserializer.deserializeKey("256", context));
    }

    @Test
    public void testShort() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        short result = deserializer.deserializeKey("32767", context);
        assertEquals((short) 32767, result);

        try {
            result = deserializer.deserializeKey("-32769", context);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // Expected
        }

        assertThrows(IllegalArgumentException.class, () -> deserializer.deserializeKey("65537", context));
    }

    @Test
    public void testChar() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        char result = deserializer.deserializeKey("'a'", context);
        assertEquals((char) 'a', result);

        try {
            result = deserializer.deserializeKey("'\\u00FF'", context);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testInt() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        int result = deserializer.deserializeKey("2147483647", context);
        assertEquals(2147483647, result);

        try {
            result = deserializer.deserializeKey("-2147483649", context);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // Expected
        }

        assertThrows(IllegalArgumentException.class, () -> deserializer.deserializeKey("4611686018427388888", context));
    }

    @Test
    public void testLong() throws IOException, ParseException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        long result = deserializer.deserializeKey("9223372036854775807", context);
        assertEquals(9223372036854775807L, result);

        try {
            result = deserializer.deserializeKey("-9223372036854775809", context);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // Expected
        }

        assertThrows(IllegalArgumentException.class, () -> deserializer.deserializeKey("18446744073709551617", context));

        result = deserializer.deserializeKey("1234567890123456789", context);
        Date date = new Date(result);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        assertEquals(2023, calendar.get(Calendar.YEAR));
        assertEquals(3, calendar.get(Calendar.MONTH) + 1);
        assertEquals(15, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(14, calendar.get(Calendar.DAY_OF_WEEK));
        assertEquals(Calendar.AM, calendar.get(Calendar.AM_PM));
        assertEquals(5, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(24, calendar.get(Calendar.HOUR));
        assertEquals(45, calendar.get(Calendar.MINUTE));
        assertEquals(30, calendar.get(Calendar.SECOND));
        assertEquals(2, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    public void testDate() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Date result = deserializer.deserializeKey("2023-03-15", context);
        assertEquals(new Date(1678844800000L), result);

        try {
            deserializer.deserializeKey("2023-13-15", context);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCalendar() throws IOException, InterruptedException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Calendar result = deserializer.deserializeKey("2023-03-15 14:30:45", context);

        assertEquals(2023, result.get(Calendar.YEAR));
        assertEquals(3, result.get(Calendar.MONTH) + 1);
        assertEquals(15, result.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.AM, result.get(Calendar.AM_PM));
        assertEquals(14, result.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, result.get(Calendar.MINUTE));
        assertEquals(45, result.get(Calendar.SECOND));
        assertEquals(0, result.get(Calendar.MILLISECOND));

        assertEquals(Calendar.MARCH, result.get(Calendar.MONTH));
        assertEquals(Calendar.FRIDAY, result.get(Calendar.DAY_OF_WEEK));

        assertEquals(2023, result.getActualMaximum(Calendar.YEAR));
        assertEquals(1, result.getActualMaximum(Calendar.DAY_OF_MONTH));
        assertEquals(12, result.getActualMaximum(Calendar.MONTH));
        assertEquals(31, result.getActualMaximum(Calendar.DAY_OF_MONTH));

        assertEquals(1, result.getActualMinimum(Calendar.DAY_OF_MONTH));
        assertEquals(0, result.getActualMinimum(Calendar.MONTH));
        assertEquals(1, result.getActualMinimum(Calendar.YEAR));

        assertEquals(Calendar.MARCH, result.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        assertEquals("MAR", result.getDisplayName(Calendar.MONTH, Calendar.ABBREVIATED, Locale.ENGLISH));

        assertEquals(2023, result.getTime().getYear());
        assertEquals(3, result.getTime().getMonth() + 1);
        assertEquals(15, result.getTime().getDate());
    }

    @Test
    public void testUUID() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        UUID result = deserializer.deserializeKey("123e4567-785b-11d1-80b4-00c0ap9f1860", context);
        assertEquals(new UUID("123e4567-785b-11d1-80b4-00c0ap9f1860"), result);

        try {
            deserializer.deserializeKey("notUUID", context);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testURI() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        URI result = deserializer.deserializeKey("http://localhost:8080/test/path", context);
        assertEquals(new URI("http://localhost:8080/test/path"), result);

        try {
            deserializer.deserializeKey("notURI", context);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testURL() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        URL result = deserializer.deserializeKey("http://localhost:8080/test/path", context);
        assertEquals(new URL("http://localhost:8080/test/path"), result);

        try {
            deserializer.deserializeKey("notURL", context);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLocale() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Locale result = deserializer.deserializeKey("en_US", context);
        assertEquals(Locale.US, result);

        try {
            deserializer.deserializeKey("notLocale", context);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCurrency() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Currency result = deserializer.deserializeKey("USD", context);
        assertEquals(Currency.getInstance("USD"), result);

        try {
            deserializer.deserializeKey("notCurrency", context);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testEnum() throws IOException {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new JsonMapper();
        module.addDeserializer(EnumSet.class, new EnumMapDeserializer());
        mapper.registerModule(module);

        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        DeserializerProvider provider = Mockito.mock(DeserializerProvider.class);
        Mockito.when(context.getDeserializerProvider()).thenReturn(provider);
        Mockito.when(provider.findValueDeserializer(EnumSet.class)).thenReturn(mock(JsonDeserializer.class));

        EnumSet<Weekday> days = EnumSet.allOf(Weekday.class);
        Weekday result = deserializer.deserializeKey("SUNDAY", context);
        assertEquals(Weekday.SUNDAY, result);

        assertNull(deserializer.deserializeKey("NOT_VALID", context));
    }

    @Test
    public void testClass() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Class<?> result = deserializer.deserializeKey(String.class.getName(), context);
        assertEquals(String.class, result);

        try {
            deserializer.deserializeKey("notClass", context);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testUnknownType() throws IOException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        assertNull(deserializer.deserializeKey("unknownType", context));
    }

    @Test
    public void testDeserializationFeature() throws IOException {
        ObjectMapper mapper = new JsonMapper();
        mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        // Test with nullable enum
        try {
            deserializer.deserializeKey("UNKNOWN_VALUE", context);
            fail("Should have thrown exception");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test with non-nullable enum
        Class<?> enumClass = Enum.class;
        EnumKD enumDeserializer = new EnumKD(Mockito.mock(EnumResolver.class), null);
        Mockito.when(context.getValueFactory())
                .thenReturn(mapper.getValueFactory());
        Mockito.when(mapper.findValueDeserializer(enumClass)).thenReturn(enumDeserializer);

        Enum<?> result = deserializer.deserializeKey("UNKNOWN_VALUE", context);
        assertNull(result);
    }

    @Test
    public void testStringKey() throws IOException {
        ObjectMapper mapper = new JsonMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        DeserializerProvider provider = mapper.getDeserializerProvider();

        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        Mockito.when(provider.findValueDeserializer(String.class)).thenReturn(stringDeserializer);

        String key = deserializer.deserializeKey("{\"some\":\"value\"}", context);
        assertEquals("value", key);
    }
}