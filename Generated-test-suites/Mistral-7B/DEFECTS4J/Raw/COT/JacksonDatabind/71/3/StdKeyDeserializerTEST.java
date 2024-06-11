package com.example; // replace with your test package

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * StdKeyDeserializerTest
 */
public class StdKeyDeserializerTest {

    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setup() throws Exception {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBooleanTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", new TestContext()));
        assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", new TestContext()));
    }

    @Test
    public void testByteTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals((byte) 1, deserializer.deserializeKey("1", new TestContext()));
        assertThrows(IllegalArgumentException.class, () -> deserializer.deserializeKey("256", new TestContext()));
    }

    @Test
    public void testShortTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        assertEquals((short) 1, deserializer.deserializeKey("1", new TestContext()));
        assertThrows(IllegalArgumentException.class, () -> deserializer.deserializeKey("32768", new TestContext()));
    }

    @Test
    public void testCharTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        assertEquals('A', deserializer.deserializeKey("A", new TestContext()));
    }

    @Test
    public void testIntTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(1, deserializer.deserializeKey("1", new TestContext()));
    }

    @Test
    public void testLongTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        assertEquals(1L, deserializer.deserializeKey("1", new TestContext()));
    }

    @Test
    public void testFloatTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        assertEquals(1.0f, deserializer.deserializeKey("1.0", new TestContext()), 0.001f);
    }

    @Test
    public void testDoubleTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        assertEquals(1.0, deserializer.deserializeKey("1.0", new TestContext()), 0.001);
    }

    @Test
    public void testLocaleTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        assertEquals(Locale.US, deserializer.deserializeKey("en_US", new TestContext()));
    }

    @Test
    public void testDateTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        assertEquals(new Date(), deserializer.deserializeKey("2022-01-01", new TestContext()));
    }

    @Test
    public void testCalendarTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Calendar expected = Calendar.getInstance();
        expected.set(2022, 0, 1);
        assertEquals(expected, deserializer.deserializeKey("2022-01-01", new TestContext()));
    }

    @Test
    public void testUriTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        assertEquals(new URI("https://example.com"), deserializer.deserializeKey("https://example.com", new TestContext()));
    }

    @Test
    public void testUrlTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        assertEquals(new URL("https://example.com"), deserializer.deserializeKey("https://example.com", new TestContext()));
    }

    @Test
    public void testClassTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
        assertEquals(String.class, deserializer.deserializeKey("java.lang.String", new TestContext()));
    }

    @Test
    public void testEnumTypeDeserialization() throws JsonProcessingException {
        enum TestEnum {A, B, C}
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);
        assertEquals(TestEnum.A, deserializer.deserializeKey("A", new TestContext()));
        assertNull(deserializer.deserializeKey("D", new TestContext()));
    }

    @Test
    public void testInvalidKeyTypeDeserialization() throws JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
        assertNull(deserializer.deserializeKey(null, new TestContext()));
        assertThrows(IllegalArgumentException.class, () -> deserializer.deserializeKey("x", new TestContext()));
    }

    @Test
    public void testCustomDeserializer() throws Exception {
        class CustomDeserializer extends StdKeyDeserializer {
            public CustomDeserializer() {
                super(1, CustomClass.class);
            }

            @Override
            protected Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return new CustomClass();
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerForMap();
        reader.setKeyDeserializer(CustomDeserializer.class);

        CustomClass customClass = reader.readValue("{\"key\":\"custom\"}", CustomClass.class);
        assertEquals(new CustomClass(), customClass);
    }

    // Test helper class
    public static class TestContext extends DeserializationContext {
        @Override
        public ObjectReader _treeReader() {
            return null;
        }

        @Override
        public AnnotationIntrospector _annotationIntrospector() {
            return null;
        }

        @Override
        public JavaType _constructType(Class<?> rawType) throws JavaTypeValidationException {
            return null;
        }

        @Override
        public JavaType _constructType(Class<?> rawType, Class<?> containerType) throws JavaTypeValidationException {
            return null;
        }

        @Override
        public ObjectFactory _valueInstantiator() {
            return null;
        }

        @Override
        public ObjectReader _valueReaderForRootValue() {
            return null;
        }

        @Override
        public ObjectReader _valueReaderForProperty(BeanProperty property) {
            return null;
        }

        @Override
        public ObjectReader _valueReaderForField(FieldDesc fieldDesc) {
            return null;
        }

        @Override
        public JsonParser _createParser(String text, byte[] bytes) throws IOException {
            return null;
        }

        @Override
        public JsonParser _createParser(File file) throws IOException {
            return null;
        }

        @Override
        public JsonParser _createParser(CharArrayBuilder cab) throws IOException {
            return null;
        }

        @Override
        public void _reset() {
        }
    }

    // Test helper class for StdKeyDeserializerTest
    static class CustomClass {
        @JsonAutoDetect
        public static class CustomDeserializer extends JsonDeserializer<CustomClass> {
            @Override
            public CustomClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new CustomClass();
            }

            @JsonCreator
            public static CustomClass create(@JsonProperty String key) {
                return new CustomClass();
            }
        }

        @JsonValue
        public String toString() {
            return "custom";
        }
    }
}