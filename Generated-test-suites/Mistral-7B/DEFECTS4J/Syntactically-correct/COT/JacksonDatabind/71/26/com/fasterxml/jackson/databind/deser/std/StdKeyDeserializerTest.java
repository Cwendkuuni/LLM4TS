package com.example.test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.ClassCastException;
import java.lang.IllegalArgumentException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumConstants;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdKeyDeserializerTest {
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testForType() {
        Class<?> stringClass = String.class;
        KeyDeserializer stringDeserializer = StdKeyDeserializer.forType(stringClass);
        assertEquals(String.class, stringDeserializer.getKeyClass());
        assertEquals(StdKeyDeserializer.StringKD.class, stringDeserializer.getClass());

        Class<?> intClass = Integer.class;
        KeyDeserializer intDeserializer = StdKeyDeserializer.forType(intClass);
        assertEquals(Integer.class, intDeserializer.getKeyClass());
        assertEquals(StdKeyDeserializer.class, intDeserializer.getClass());

        assertNull(StdKeyDeserializer.forType(null));
        assertNull(StdKeyDeserializer.forType(Object.class));
    }

    @Test
    public void testDeserializeKey_Boolean() throws IOException {
        KeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = booleanDeserializer.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, result);

        result = booleanDeserializer.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, result);

        result = booleanDeserializer.deserializeKey("invalid", context);
        assertThrows(IOException.class, () -> booleanDeserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_Byte() throws IOException {
        KeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = byteDeserializer.deserializeKey("1", context);
        assertEquals((byte) 1, result);

        result = byteDeserializer.deserializeKey("-127", context);
        assertEquals((byte) -127, result);

        result = byteDeserializer.deserializeKey("256", context);
        assertThrows(IOException.class, () -> byteDeserializer.deserializeKey("256", context));
    }

    @Test
    public void testDeserializeKey_Short() throws IOException {
        KeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = shortDeserializer.deserializeKey("1", context);
        assertEquals((short) 1, result);

        result = shortDeserializer.deserializeKey("-32767", context);
        assertEquals((short) -32767, result);

        result = shortDeserializer.deserializeKey("32768", context);
        assertThrows(IOException.class, () -> shortDeserializer.deserializeKey("32768", context));
    }

    @Test
    public void testDeserializeKey_Char() throws IOException {
        KeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = charDeserializer.deserializeKey("'", context);
        assertEquals((char) '\'', result);

        result = charDeserializer.deserializeKey("invalid", context);
        assertThrows(IOException.class, () -> charDeserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_Int() throws IOException {
        KeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = intDeserializer.deserializeKey("1", context);
        assertEquals(1, result);

        result = intDeserializer.deserializeKey("-2147483648", context);
        assertEquals(-2147483648, result);

        result = intDeserializer.deserializeKey("2147483648", context);
        assertThrows(IOException.class, () -> intDeserializer.deserializeKey("2147483648", context));
    }

    @Test
    public void testDeserializeKey_Long() throws IOException {
        KeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = longDeserializer.deserializeKey("1", context);
        assertEquals(1L, result);

        result = longDeserializer.deserializeKey("-9223372036854775808", context);
        assertEquals(-9223372036854775808L, result);

        result = longDeserializer.deserializeKey("9223372036854775809", context);
        assertThrows(IOException.class, () -> longDeserializer.deserializeKey("9223372036854775809", context));
    }

    @Test
    public void testDeserializeKey_Float() throws IOException {
        KeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = floatDeserializer.deserializeKey("1.0", context);
        assertEquals(1.0f, result);

        result = floatDeserializer.deserializeKey("invalid", context);
        assertThrows(IOException.class, () -> floatDeserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_Double() throws IOException {
        KeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = doubleDeserializer.deserializeKey("1.0", context);
        assertEquals(1.0, result, 0.0);

        result = doubleDeserializer.deserializeKey("invalid", context);
        assertThrows(IOException.class, () -> doubleDeserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_Locale() throws IOException {
        KeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = localeDeserializer.deserializeKey("en_US", context);
        assertEquals(Locale.US, result);

        result = localeDeserializer.deserializeKey("invalid", context);
        assertThrows(IOException.class, () -> localeDeserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_Date() throws IOException {
        KeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = dateDeserializer.deserializeKey("2021-12-31T23:59:59Z", context);
        assertNotNull(result);

        result = dateDeserializer.deserializeKey("invalid", context);
        assertThrows(IOException.class, () -> dateDeserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_Calendar() throws IOException {
        KeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = calendarDeserializer.deserializeKey("2021-12-31", context);
        assertNotNull(result);

        result = calendarDeserializer.deserializeKey("invalid", context);
        assertThrows(IOException.class, () -> calendarDeserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_UUID() throws IOException {
        KeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = uuidDeserializer.deserializeKey("7ba8d86e-6daa-11eb-8d87-0242ac130004", context);
        assertEquals(UUID.fromString("7ba8d86e-6daa-11eb-8d87-0242ac130004"), result);

        result = uuidDeserializer.deserializeKey("invalid", context);
        assertThrows(IOException.class, () -> uuidDeserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_URI() throws IOException {
        KeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = uriDeserializer.deserializeKey("https://example.com", context);
        assertEquals(new URI("https://example.com"), result);

        result = uriDeserializer.deserializeKey("invalid", context);
        assertThrows(IOException.class, () -> uriDeserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_URL() throws IOException {
        KeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = urlDeserializer.deserializeKey("https://example.com", context);
        assertEquals(new URL("https://example.com"), result);

        result = urlDeserializer.deserializeKey("invalid", context);
        assertThrows(IOException.class, () -> urlDeserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_Enum() throws IOException {
        KeyDeserializer enumDeserializer = StdKeyDeserializer.forType(Enum.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result;

        EnumSet<TestEnum> enumValues = EnumSet.allOf(TestEnum.class);
        for (TestEnum value : enumValues) {
            result = enumDeserializer.deserializeKey(value.name(), context);
            assertEquals(value, result);
        }

        result = enumDeserializer.deserializeKey("InvalidEnumValue", context);
        assertThrows(IOException.class, () -> enumDeserializer.deserializeKey("InvalidEnumValue", context));
    }

    @Test
    public void testDeserializeKey_Class() throws IOException {
        Class<?> stringClass = String.class;
        KeyDeserializer stringDeserializer = StdKeyDeserializer.forType(stringClass);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = stringDeserializer.deserializeKey("java.lang.String", context);
        assertEquals(String.class, result.getClass());

        result = stringDeserializer.deserializeKey("java.util.ArrayList", context);
        assertThrows(IOException.class, () -> stringDeserializer.deserializeKey("java.util.ArrayList", context));

        context = objectMapper.getSerializationConfig().getDefaultFactory();
        SerializerProvider serializerProvider = context.getSerializerProvider();
        Constructor<String> stringConstructor = String.class.getConstructor(String.class);
        KeyDeserializer stringConstructorDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(stringConstructor);

        result = stringConstructorDeserializer.deserializeKey("java.util.ArrayList", context);
        assertThrows(IOException.class, () -> stringConstructorDeserializer.deserializeKey("java.util.ArrayList", context));

        Method stringMethod = String.class.getMethod("valueOf", String.class);
        KeyDeserializer stringMethodDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(stringMethod);

        result = stringMethodDeserializer.deserializeKey("java.util.ArrayList", context);
        assertThrows(IOException.class, () -> stringMethodDeserializer.deserializeKey("java.util.ArrayList", context));

        result = stringMethodDeserializer.deserializeKey("java.lang.String", context);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testDeserializeKey_NullKey() throws IOException {
        KeyDeserializer stringDeserializer = StdKeyDeserializer.forType(String.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = stringDeserializer.deserializeKey(null, context);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidKeyType() throws IOException {
        Class<?> invalidKeyClass = Double.class;
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(invalidKeyClass);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("invalid", context);
        assertThrows(IOException.class, () -> keyDeserializer.deserializeKey("invalid", context));
    }
}

enum TestEnum {
    VALUE1, VALUE2, VALUE3;
}