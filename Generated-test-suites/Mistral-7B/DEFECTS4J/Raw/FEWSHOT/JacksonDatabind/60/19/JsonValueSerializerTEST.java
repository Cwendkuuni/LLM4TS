package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JsonValueSerializerTest {

    @Test
    public void testCreateContextual() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(Object.class, null);
        assertNotNull(serializer);

        Class<String> stringClass = String.class;
        BeanProperty property = new BeanProperty("property", stringClass);
        JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, property);
        JsonValueSerializer contextualSerializer = serializer.withResolved(property, valueSerializer, false);
        assertEquals(JsonValueSerializer.class, contextualSerializer.getClass());
        assertEquals(stringClass, contextualSerializer.handledType());
    }

    @Test
    public void testSerializeWithType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(Object.class, null);

        String value = "value";
        Object bean = new Object() {
            public Object getValue() {
                return value;
            }
        };
        JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();

        serializer.serializeWithType(bean, gen, mapper.getSerializerProvider(), typeSerializer);
        assertEquals("value", gen.getCharacterBuffers().toString());
    }

    @Test
    public void testSerializeWithTypeForEnum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(Object.class, null);

        EnumTest enumTest = new EnumTest();
        Object bean = enumTest;
        String expected = "TEST_ONE";

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();

        serializer.serializeWithType(bean, gen, mapper.getSerializerProvider(), typeSerializer);
        assertEquals(expected, gen.getCharacterBuffers().toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(Object.class, null);

        String expected = "{ \"type\": \"string\", \"properties\": { \"pattern\": \"^[a-zA-Z0-9]+$\" } }";

        JsonFormatVisitorWrapper visitor = new JsonSchemaApiMockVisitor();
        JavaType typeHint = mapper.getTypeFactory().constructType(String.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        assertEquals(expected, visitor.getSchema().toString());
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(Integer.TYPE, null));
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(Boolean.TYPE, null));
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(Double.TYPE, null));
        assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(String.class, null));
        assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(Object.class, null));
    }

    @JsonSerialize(using = JsonValueSerializer.class)
    static class EnumTest {
        public static final EnumTest TEST_ONE = new EnumTest();
        public static final EnumTest TEST_TWO = new EnumTest();

        @JsonValue
        public String getValue() {
            return "TEST_ONE"
                + (this == TEST_ONE ? "" : "_" + this.name());
        }
    }
}