package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdKeyDeserializerTest {

    @InjectMocks
    private StdKeyDeserializer deserializer;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBooleanType() {
        String[] keys = {"true", "false"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if ("true".equals(key)) {
                assertEquals(Boolean.TRUE, result);
            } else {
                assertEquals(Boolean.FALSE, result);
            }
        }
    }

    @Test
    public void testByteType() {
        String[] keys = {"1", "-127", "128", "255"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if ("1".equals(key)) {
                assertEquals((byte) 1, result);
            } else if ("-127".equals(key)) {
                assertEquals((byte) -127, result);
            } else if ("128".equals(key)) {
                assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey(key, mockDeserializationContext()));
            } else {
                assertEquals((byte) 255, result);
            }
        }
    }

    @Test
    public void testShortType() {
        String[] keys = {"-128", "0", "127", "128", "32767"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if ("0".equals(key)) {
                assertEquals((short) 0, result);
            } else if ("-128".equals(key)) {
                assertEquals((short) -128, result);
            } else if ("127".equals(key)) {
                assertEquals((short) 127, result);
            } else if ("128".equals(key)) {
                assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey(key, mockDeserializationContext()));
            } else {
                assertEquals((short) 32767, result);
            }
        }
    }

    @Test
    public void testCharType() {
        String[] keys = {"a", "\\u007F"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if ("a".equals(key)) {
                assertEquals((char) 'a', result);
            } else {
                assertEquals((char) 0x7F, result);
            }
        }
    }

    @Test
    public void testIntType() {
        String[] keys = {"Integer.MIN_VALUE", "Integer.MAX_VALUE", "0", "123", "1000000", "StringWithIntValue"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if ("0".equals(key)) {
                assertEquals(0, result);
            } else if ("Integer.MIN_VALUE".equals(key)) {
                assertEquals(Integer.MIN_VALUE, result);
            } else if ("Integer.MAX_VALUE".equals(key)) {
                assertEquals(Integer.MAX_VALUE, result);
            } else if (key.equals("StringWithIntValue")) {
                assertEquals(42, deserializeIntFromString("{\"number\":42}"));
            } else {
                assertEquals(Integer.parseInt(key), result);
            }
        }
    }

    @Test
    public void testLongType() {
        String[] keys = {"Long.MIN_VALUE", "Long.MAX_VALUE", "0", "123456789012345", "java.util.Date", "StringWithLongValue"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if ("0".equals(key)) {
                assertEquals(0L, result);
            } else if ("Long.MIN_VALUE".equals(key)) {
                assertEquals(Long.MIN_VALUE, result);
            } else if ("Long.MAX_VALUE".equals(key)) {
                assertEquals(Long.MAX_VALUE, result);
            } else if (key.equals("java.util.Date")) {
                assertEquals(new Date(), result);
            } else if (key.equals("StringWithLongValue")) {
                assertEquals(42L, deserializeLongFromString("{\"number\":42}"));
            } else {
                assertEquals(Long.parseLong(key), result);
            }
        }
    }

    @Test
    public void testFloatType() {
        String[] keys = {"-3.14", "3.14", "NaN", "Infinity", "StringWithFloatValue"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if ("NaN".equals(key)) {
                assertNull(result);
            } else if ("Infinity".equals(key)) {
                assertNull(result);
            } else {
                assertEquals(Float.parseFloat(key), result);
            }
        }
    }

    @Test
    public void testDoubleType() {
        String[] keys = {"-3.14", "3.14", "NaN", "Infinity", "StringWithDoubleValue"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if ("NaN".equals(key)) {
                assertNull(result);
            } else if ("Infinity".equals(key)) {
                assertNull(result);
            } else {
                assertEquals(Double.parseDouble(key), result);
            }
        }
    }

    @Test
    public void testLocaleType() throws NoSuchMethodException {
        AnnotatedClass localeClass = objectMapper.getTypeFactory().constructType(Locale.class);
        AnnotatedMethod localeFactory = localeClass.findMethod("forLanguageTag", String.class);
        Object result = deserializer.deserializeKey("en_US", mockDeserializationContext());
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(mockDeserializationContext().findBeanDeserializer(localeClass)).thenReturn(typeDeserializer);
        when(typeDeserializer.deserializeTypedFromAnnotation(localeFactory)).thenReturn("en_US");
        assertEquals(Locale.forLanguageTag("en_US"), result);
    }

    @Test
    public void testDateType() {
        String[] keys = {"2022-01-01", "InvalidDate"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if (key.equals("InvalidDate")) {
                assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("InvalidDate", mockDeserializationContext()));
            } else {
                assertEquals(new Date(0L), result);
            }
        }
    }

    @Test
    public void testCalendarType() {
        String[] keys = {"2022-01-01", "InvalidDate"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if (key.equals("InvalidDate")) {
                assertNull(result);
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(0L));
                assertEquals(calendar, result);
            }
        }
    }

    @Test
    public void testUUIDType() {
        String[] keys = {"00000000-0000-0000-0000-000000000000", "UUID_FROM_STRING"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if ("UUID_FROM_STRING".equals(key)) {
                assertEquals(UUID.fromString("123e4567-123e-4567-a234-123456789012"), result);
            } else {
                assertEquals(UUID.fromString(key), result);
            }
        }
    }

    @Test
    public void testURIType() {
        String[] keys = {"http://example.com", "InvalidURI"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if (key.equals("InvalidURI")) {
                assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("InvalidURI", mockDeserializationContext()));
            } else {
                assertEquals(URI.create(key), result);
            }
        }
    }

    @Test
    public void testURLType() {
        String[] keys = {"http://example.com", "InvalidURL"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if (key.equals("InvalidURL")) {
                assertThrows(MalformedURLException.class, () -> deserializer.deserializeKey("InvalidURL", mockDeserializationContext()));
            } else {
                assertEquals(new URL(key), result);
            }
        }
    }

    @Test
    public void testClassType() {
        String[] keys = {"java.lang.String", "java.util.Date", "java.util.HashMap", "java.util.Enumeration", "java.lang.ClassNotFoundException"};
        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            if (key.equals("java.lang.ClassNotFoundException")) {
                assertThrows(ClassNotFoundException.class, () -> deserializer.deserializeKey(key, mockDeserializationContext()));
            } else {
                assertEquals(Class.forName(key), result);
            }
        }
    }

    @Test
    public void testEnumType() {
        String[] keys = {"APPLE", "ORANGE", "BLACKBERRY", "UNKNOWN"};
        AnnotatedClass enumClass = objectMapper.getTypeFactory().constructType(Fruit.class);
        EnumSet<Fruit> expectedValues = EnumSet.allOf(Fruit.class);

        for (String key : keys) {
            Object result = deserializer.deserializeKey(key, mockDeserializationContext());
            Fruit expectedValue = null;
            if ("APPLE".equals(key)) {
                expectedValue = Fruit.APPLE;
            } else if ("ORANGE".equals(key)) {
                expectedValue = Fruit.ORANGE;
            } else if ("BLACKBERRY".equals(key)) {
                expectedValue = Fruit.BLACKBERRY;
            } else {
                // UNKNOWN should be null according to the code
                assertNull(result);
                continue;
            }
            assertEquals(expectedValue, result);
            assertEquals(expectedValues.contains(expectedValue), result != null);
        }
    }

    @Test
    public void testUnknownType() {
        String key = "testUnknownType";
        assertNull(deserializer.deserializeKey(key, mockDeserializationContext()));
    }

    private DeserializationContext mockDeserializationContext() {
        DeserializationContext context = mock(DeserializationContext.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(mockDeserializationContext().getMapper()).thenReturn(objectMapper);
        return context;
    }

    private int deserializeIntFromString(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, Integer.class);
    }

    private long deserializeLongFromString(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, Long.class);
    }

    private enum Fruit {
        APPLE, ORANGE, BLACKBERRY
    }
}