package com.example.test; // replace with your test package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);

        Object result = booleanDeserializer.deserializeKey("true", new DeserializationContext() {});
        assertEquals(Boolean.TRUE, result);

        result = booleanDeserializer.deserializeKey("false", new DeserializationContext() {});
        assertEquals(Boolean.FALSE, result);

        assertThrows(JsonProcessingException.class, () -> booleanDeserializer.deserializeKey("invalid", new DeserializationContext() {}));
    }

    @Test
    public void testByteDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);

        Object result = byteDeserializer.deserializeKey("127", new DeserializationContext() {});
        assertEquals((Byte) Byte.parseByte("127"), result);

        assertThrows(JsonProcessingException.class, () -> byteDeserializer.deserializeKey("-128", new DeserializationContext() {}));
        assertThrows(JsonProcessingException.class, () -> byteDeserializer.deserializeKey("256", new DeserializationContext() {}));
    }

    @Test
    public void testShortDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);

        Object result = shortDeserializer.deserializeKey("32767", new DeserializationContext() {});
        assertEquals((Short) Short.parseShort("32767"), result);

        assertThrows(JsonProcessingException.class, () -> shortDeserializer.deserializeKey("-32768", new DeserializationContext() {}));
        assertThrows(JsonProcessingException.class, () -> shortDeserializer.deserializeKey("65536", new DeserializationContext() {}));
    }

    @Test
    public void testCharDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);

        Object result = charDeserializer.deserializeKey("A", new DeserializationContext() {});
        assertEquals(Character.valueOf('A'), result);

        assertThrows(JsonProcessingException.class, () -> charDeserializer.deserializeKey("invalid", new DeserializationContext() {}));
    }

    @Test
    public void testIntDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);

        Object result = intDeserializer.deserializeKey("1234567890", new DeserializationContext() {});
        assertEquals(Integer.valueOf(1234567890), result);

        assertThrows(JsonProcessingException.class, () -> intDeserializer.deserializeKey("-1234567890", new DeserializationContext() {}));
    }

    @Test
    public void testLongDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);

        Object result = longDeserializer.deserializeKey("9223372036854775807", new DeserializationContext() {});
        assertEquals(Long.parseLong("9223372036854775807"), result.longValue());

        assertThrows(JsonProcessingException.class, () -> longDeserializer.deserializeKey("-9223372036854775808", new DeserializationContext() {}));
    }

    @Test
    public void testFloatDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);

        Object result = floatDeserializer.deserializeKey("3.14159265", new DeserializationContext() {});
        assertEquals(Float.valueOf(3.14159265f), result);

        assertThrows(JsonProcessingException.class, () -> floatDeserializer.deserializeKey("invalid", new DeserializationContext() {}));
    }

    @Test
    public void testDoubleDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);

        Object result = doubleDeserializer.deserializeKey("3.1415926535897932384626433832795028841697292453436028747135266249775724709369995", new DeserializationContext() {});
        assertEquals(Double.valueOf(3.1415926535897932384626433832795028841697292453436028747135266249775724709369995), result);

        assertThrows(JsonProcessingException.class, () -> doubleDeserializer.deserializeKey("invalid", new DeserializationContext() {}));
    }

    @Test
    public void testLocaleDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);

        Object result = localeDeserializer.deserializeKey("en_US", new DeserializationContext() {});
        assertEquals(Locale.US, result);

        assertThrows(JsonProcessingException.class, () -> localeDeserializer.deserializeKey("invalid", new DeserializationContext() {}));
    }

    @Test
    public void testDateDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);

        Object result = dateDeserializer.deserializeKey("2022-01-01", new DeserializationContext() {});
        assertEquals(new Date(0), result); // assuming Jackson's DeserializationContext.parseDate() returns the current date with zero time

        assertThrows(JsonProcessingException.class, () -> dateDeserializer.deserializeKey("invalid", new DeserializationContext() {}));
    }

    @Test
    public void testCalendarDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);

        Object result = calendarDeserializer.deserializeKey("2022-01-01", new DeserializationContext() {});
        Calendar calendar = (Calendar) result;
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        assertEquals(calendar, result);

        assertThrows(JsonProcessingException.class, () -> calendarDeserializer.deserializeKey("invalid", new DeserializationContext() {}));
    }

    @Test
    public void testUUIDDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);

        Object result = uuidDeserializer.deserializeKey("123e4567-789b-12d3-a456-426614174000", new DeserializationContext() {});
        assertEquals(UUID.fromString("123e4567-789b-12d3-a456-426614174000"), result);

        assertThrows(JsonProcessingException.class, () -> uuidDeserializer.deserializeKey("invalid", new DeserializationContext() {}));
    }

    @Test
    public void testURIDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);

        Object result = uriDeserializer.deserializeKey("https://example.com", new DeserializationContext() {});
        assertEquals(new URI("https://example.com"), result);

        assertThrows(JsonProcessingException.class, () -> uriDeserializer.deserializeKey("invalid", new DeserializationContext() {}));
    }

    @Test
    public void testURLDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);

        Object result = urlDeserializer.deserializeKey("https://example.com", new DeserializationContext() {});
        assertEquals(new URL("https://example.com"), result);

        assertThrows(JsonProcessingException.class, () -> urlDeserializer.deserializeKey("invalid", new DeserializationContext() {}));
    }

    @Test
    public void testClassDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String[].class);

        Object result = classDeserializer.deserializeKey("[Ljava.lang.String;", new DeserializationContext() {});
        assertEquals(new String[]{"test"}, result);

        assertThrows(JsonProcessingException.class, () -> classDeserializer.deserializeKey("invalid", new DeserializationContext() {}));
    }

    @Test
    public void testEnumDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(Locale.class);

        Object result = enumDeserializer.deserializeKey("en_US", new DeserializationContext() {});
        assertEquals(Locale.US, result);

        assertThrows(JsonProcessingException.class, () -> enumDeserializer.deserializeKey("INVALID_LOCAL_ID", new DeserializationContext() {}));
    }
}