package com.example.test; // replace with your test package

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Test
    public void testForType() {
        Class<?> stringClass = String.class;
        StdKeyDeserializer actual = StdKeyDeserializer.forType(stringClass);
        StdKeyDeserializer expected = StringKD.forType(stringClass);
        assertEquals(expected, actual);

        stringClass = Object.class;
        actual = StdKeyDeserializer.forType(stringClass);
        expected = StringKD.forType(stringClass);
        assertEquals(expected, actual);

        stringClass = UUID.class;
        actual = StdKeyDeserializer.forType(stringClass);
        assertEquals(StdKeyDeserializer.class, actual.getClass());
        assertEquals(TYPE_UUID, actual._kind);
        assertEquals(UUID.class, actual._keyClass);

        stringClass = Integer.class;
        actual = StdKeyDeserializer.forType(stringClass);
        assertEquals(StdKeyDeserializer.class, actual.getClass());
        assertEquals(TYPE_INT, actual._kind);
        assertEquals(Integer.class, actual._keyClass);

        // Test for other types
    }

    @Test
    public void testDeserializeKey() throws JsonProcessingException, IOException {
        // Test for StringKeyDeserializer
        StdKeyDeserializer stringDeserializer = StdKeyDeserializer.forType(String.class);
        String key = "true";
        Object result = stringDeserializer.deserializeKey(key, null);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = stringDeserializer.deserializeKey(key, null);
        assertEquals(Boolean.FALSE, result);

        key = "123";
        result = stringDeserializer.deserializeKey(key, null);
        assertEquals(Integer.valueOf(123), result);

        key = "12345";
        result = stringDeserializer.deserializeKey(key, null);
        assertEquals(Short.valueOf((short) 12345), result);

        key = "hello";
        result = stringDeserializer.deserializeKey(key, null);
        assertEquals(String.class, result.getClass());

        // Test for BooleanKeyDeserializer
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
        key = "true";
        result = booleanDeserializer.deserializeKey(key, null);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = booleanDeserializer.deserializeKey(key, null);
        assertEquals(Boolean.FALSE, result);

        key = "invalid";
        assertThrows(DeserializationFeature.ClassIdMismatchException.class, () -> booleanDeserializer.deserializeKey(key, null));

        // Test for ByteKeyDeserializer
        StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
        key = "127";
        result = byteDeserializer.deserializeKey(key, null);
        assertEquals(Byte.valueOf((byte) 127), result);

        key = "-128";
        assertThrows(NumberFormatException.class, () -> byteDeserializer.deserializeKey(key, null));

        // Test for ShortKeyDeserializer
        StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
        key = "12345";
        result = shortDeserializer.deserializeKey(key, null);
        assertEquals(Short.valueOf((short) 12345), result);

        key = "123456";
        assertThrows(NumberFormatException.class, () -> shortDeserializer.deserializeKey(key, null));

        // Test for CharKeyDeserializer
        StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
        key = "a";
        result = charDeserializer.deserializeKey(key, null);
        assertEquals(Character.valueOf('a'), result);

        key = "ABC";
        assertThrows(DeserializationFeature.InvalidUTF8ByteSequenceException.class, () -> charDeserializer.deserializeKey(key, null));

        // Test for IntegerKeyDeserializer
        StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
        key = "12345678";
        result = intDeserializer.deserializeKey(key, null);
        assertEquals(Integer.valueOf(12345678), result);

        // Test for LongKeyDeserializer
        StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
        key = "12345678901234567890";
        result = longDeserializer.deserializeKey(key, null);
        assertEquals(Long.parseLong(key), result);

        // Test for FloatKeyDeserializer
        StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
        key = "123.45";
        result = floatDeserializer.deserializeKey(key, null);
        assertEquals(Float.parseFloat(key), result, 0.01f);

        // Test for DoubleKeyDeserializer
        StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
        key = "123.45";
        result = doubleDeserializer.deserializeKey(key, null);
        assertEquals(Double.parseDouble(key), result, 0.001);

        // Test for LocaleKeyDeserializer
        StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
        key = "en_US";
        result = localeDeserializer.deserializeKey(key, null);
        assertEquals(Locale.forLanguageTag(key), result);

        key = "fr_FR";
        result = localeDeserializer.deserializeKey(key, null);
        assertEquals(Locale.forLanguageTag(key), result);

        // Test for DateKeyDeserializer
        StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
        key = "2023-03-01";
        result = dateDeserializer.deserializeKey(key, null);
        assertEquals(new Date(1678457200000L), result);

        // Test for CalendarKeyDeserializer
        StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
        key = "2023-03-01";
        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.setTime(new Date(1678457200000L));
        result = (Calendar) calendarDeserializer.deserializeKey(key, null);
        assertEquals(expectedCalendar, result);

        // Test for UUIDKeyDeserializer
        StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);
        key = "123e4567-e89b-12d3-a456-426614174000";
        result = uuidDeserializer.deserializeKey(key, null);
        assertEquals(UUID.fromString(key), result);

        // Test for URIKeyDeserializer
        StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
        key = "http://example.com";
        result = uriDeserializer.deserializeKey(key, null);
        assertEquals(new URI(key), result);

        // Test for URLKeyDeserializer
        StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
        key = "http://example.com";
        result = urlDeserializer.deserializeKey(key, null);
        assertEquals(new URL(key), result);

        // Test for ClassKeyDeserializer
        StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(Class.class);
        key = "java.lang.String";
        Class<?> expectedClass = String.class;
        result = classDeserializer.deserializeKey(key, null);
        assertEquals(expectedClass, result.asSubclass(String.class));

        // Test for EnumKeyDeserializer
        Class<? extends Enum> enumClass = EnumSet.class;
        StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(enumClass);
        key = "Monday";
        Enum<?> expectedEnum = EnumSet.Monday;
        result = enumDeserializer.deserializeKey(key, null);
        assertEquals(expectedEnum, result);

        // Test for custom KeyDeserializer
        StdKeyDeserializer customDeserializer = new StringCtorKeyDeserializer(Arrays.stream(CustomClass.values())
                .findFirst()
                .getDeclaredConstructor(String.class)) {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                try {
                    return super.deserializeKey(key, ctxt);
                } catch (Exception e) {
                    throw new IOException("CustomKeyDeserializer: Failed to deserialize key of type " + _keyClass.getName() + ": " + e.getMessage());
                }
            }
        };
        key = "CustomValue";
        CustomClass expectedCustomClass = CustomClass.CUSTOM_VALUE;
        result = customDeserializer.deserializeKey(key, null);
        assertEquals(expectedCustomClass, result);
    }
}

class CustomClass {
    CustomClass(String name) {
        this.name = name;
    }

    String name;

    enum Values {
        CUSTOM_VALUE
    }
}