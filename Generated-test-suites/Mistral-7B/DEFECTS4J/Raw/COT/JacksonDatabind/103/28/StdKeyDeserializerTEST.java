package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.JavaType;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class StdKeyDeserializerTest extends TestBase {

    private static ObjectMapper mapper;
    private static AnnotationIntrospector introspector;

    @BeforeClass
    public static void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        introspector = mapper.getAnnotationIntrospector();
    }

    @Test
    public void testBooleanKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, serializer.deserializeKey("true", createContext()));
        assertEquals(Boolean.FALSE, serializer.deserializeKey("false", createContext()));
    }

    @Test
    public void testByteKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals((byte) 10, serializer.deserializeKey("10", createContext()));
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("200", createContext()));
    }

    @Test
    public void testShortKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        assertEquals((short) 10, serializer.deserializeKey("10", createContext()));
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("20000", createContext()));
    }

    @Test
    public void testCharKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        assertEquals('A', serializer.deserializeKey("A", createContext()));
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("ABC", createContext()));
    }

    @Test
    public void testIntKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(10, serializer.deserializeKey("10", createContext()));
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("1000000", createContext()));
    }

    @Test
    public void testLongKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        assertEquals(10L, serializer.deserializeKey("10", createContext()));
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("1000000000000000000", createContext()));
    }

    @Test
    public void testFloatKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        assertEquals(10.0f, serializer.deserializeKey("10.0", createContext()), 0.001f);
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("1000000000.0", createContext()));
    }

    @Test
    public void testDoubleKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        assertEquals(10.0, serializer.deserializeKey("10.0", createContext()), 0.001);
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("1000000000000000000.0", createContext()));
    }

    @Test
    public void testLocaleKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        assertEquals(Locale.US, serializer.deserializeKey("en_US", createContext()));
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("invalid_locale", createContext()));
    }

    @Test
    public void testDateKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Date.class);
        assertEquals("Mon Jan 01 00:00:00 CET 1970", serializer.deserializeKey("1970-01-01", createContext()).toString());
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("invalid_date", createContext()));
    }

    @Test
    public void testCalendarKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Calendar.class);
        Calendar calendar = serializer.deserializeKey("1970-01-01", createContext());
        assertEquals(1970, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH) + 1);
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("invalid_date", createContext()));
    }

    @Test
    public void testUUIDKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        assertEquals(new UUID(1L, 2L), serializer.deserializeKey("1-2", createContext()));
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("invalid_uuid", createContext()));
    }

    @Test
    public void testURIKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        URI uri = serializer.deserializeKey("http://example.com", createContext());
        assertEquals("http", uri.getScheme());
        assertEquals("/example.com", uri.getPath());
        assertThrows(MalformedURLException.class, () -> serializer.deserializeKey("invalid_uri", createContext()));
    }

    @Test
    public void testURLKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        URL url = serializer.deserializeKey("http://example.com", createContext());
        assertEquals("http", url.getProtocol());
        assertEquals("/example.com", url.getPath());
        assertThrows(MalformedURLException.class, () -> serializer.deserializeKey("invalid_url", createContext()));
    }

    @Test
    public void testEnumKeyDeserialization() throws IOException {
        enum TestEnum {
            VALUE_1,
            VALUE_2,
            @JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
            VALUE_3
        }

        StdKeyDeserializer serializer = StdKeyDeserializer.forType(TestEnum.class);
        assertEquals(TestEnum.VALUE_1, serializer.deserializeKey("VALUE_1", createContext()));
        assertEquals(TestEnum.VALUE_2, serializer.deserializeKey("VALUE_2", createContext()));
        assertNull(serializer.deserializeKey("VALUE_3", createContext()));
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("invalid_value", createContext()));
    }

    @Test
    public void testClassKeyDeserialization() throws IOException {
        class TestClass {}

        StdKeyDeserializer serializer = StdKeyDeserializer.forType(TestClass.class);
        TestClass testClass = (TestClass) serializer.deserializeKey("TestClass", createContext());
        assertEquals(TestClass.class, testClass.getClass());
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("InvalidClass", createContext()));
    }

    @Test
    public void testEnumSetKeyDeserialization() throws IOException {
        enum TestEnum {
            VALUE_1,
            VALUE_2
        }

        StdKeyDeserializer serializer = StdKeyDeserializer.forType(EnumSet.class);
        EnumSet<TestEnum> enumSet = serializer.deserializeKey("[VALUE_1, VALUE_2]", createContext());
        assertEquals(EnumSet.of(TestEnum.VALUE_1, TestEnum.VALUE_2), enumSet);
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("[VALUE_1]", createContext()));
    }

    @Test
    public void testCustomKeyDeserializer() throws IOException {
        class CustomKey {
            private final int value;

            @JsonCreator
            public CustomKey(@JsonProperty("value") int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }

            @JsonDeserialize(using = CustomKeyDeserializer.class)
            public static CustomKey fromString(String key) throws IOException {
                return mapper.readValue(key, CustomKey.class);
            }
        }

        class CustomKeyDeserializer extends KeyDeserializer {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                return CustomKey.fromString(key);
            }
        }

        StdKeyDeserializer serializer = new StdKeyDeserializer(-1, CustomKey.class);
        CustomKey customKey = (CustomKey) serializer.deserializeKey("{value=10}", createContext());
        assertEquals(10, customKey.getValue());
        assertThrows(JsonMappingException.class, () -> serializer.deserializeKey("invalid_key", createContext()));
    }

    @Test
    public void testNullKey() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        assertNull(serializer.deserializeKey(null, createContext()));
    }

    @Test
    public void testUnknownKeyType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(new Object() {}.getClass());
        assertNull(serializer.deserializeKey("unknown_key", createContext()));
    }

    private DeserializationContext createContext() {
        return new DeserializationContext(mapper, new AnnotationIntrospector(), Collections.emptyMap(),
                Collections.emptyMap(), new EnumResolver[0], new ObjectMapper.DefaultSerializerProvider());
    }
}