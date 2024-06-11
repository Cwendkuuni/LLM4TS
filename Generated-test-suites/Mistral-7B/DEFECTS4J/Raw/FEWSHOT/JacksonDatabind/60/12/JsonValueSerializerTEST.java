package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Test cases for {@link JsonValueSerializer}.
 */
public class JsonValueSerializerTest {

    private static class TestData {
        @JsonValue
        public String getValue() {
            return "value";
        }
    }

    private static class TestDataWithJsonSerialize {
        @JsonValue
        @JsonSerialize(using = TestSerializer.class)
        public String getValue() {
            return "value";
        }

        public static class TestSerializer extends StdSerializer<String> {
            public TestSerializer() {
                this(null);
            }

            public TestSerializer(Class<String> t) {
                super(t);
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                gen.writeString("serialized value");
            }
        }
    }

    @Test
    public void testCreateContextual_valueMethod_isNull() throws Exception {
        final Class<Object> clazz = Object.class;
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        JsonSerializer<Object> result = serializer.createContextual(null, null);
        assertNull(result);
    }

    @Test
    public void testCreateContextual_valueMethod_isNotNull() throws Exception {
        final Class<Object> clazz = Object.class;
        final Method method = new MethodTestClass().getMethod("getValue");
        JsonValueSerializer serializer = new JsonValueSerializer(method, null);
        JsonSerializer<Object> result = serializer.createContextual(null, null);
        assertNotNull(result);
    }

    @Test
    public void testCreateContextual_property_isNotNull() throws Exception {
        final Class<Object> clazz = Object.class;
        final Method method = new MethodTestClass().getMethod("getValue");
        final BeanPropertyDefinition property = new AnnotatedClass(MethodTestClass.class)
                .getMember("valueProperty");
        JsonValueSerializer serializer = new JsonValueSerializer(method, null, null, false);
        JsonSerializer<Object> result = serializer.createContextual(null, property);
        assertNotNull(result);
    }

    @Test
    public void testCreateContextual_valueSerializer_isNotNull() throws Exception {
        final Class<Object> clazz = Object.class;
        final Method method = new MethodTestClass().getMethod("getValue");
        final BeanPropertyDefinition property = new AnnotatedClass(MethodTestClass.class)
                .getMember("valueProperty");
        final JsonSerializer<String> valueSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                gen.writeString("serialized value");
            }
        };
        JsonValueSerializer serializer = new JsonValueSerializer(method, null, valueSerializer, false);
        JsonSerializer<Object> result = serializer.createContextual(null, property);
        assertNotNull(result);
        assertEquals(valueSerializer, result);
    }

    @Test
    public void testCreateContextual_forceTypeInformation_isFalse() throws Exception {
        final Class<Object> clazz = Object.class;
        final Method method = new MethodTestClass().getMethod("getValue");
        final BeanPropertyDefinition property = new AnnotatedClass(MethodTestClass.class)
                .getMember("valueProperty");
        JsonValueSerializer serializer = new JsonValueSerializer(method, null, null, false);
        JsonSerializer<Object> result = serializer.createContextual(null, property);
        assertNotNull(result);
        assertFalse(result.isEnabled(SerializationFeature.WRITE_TYPE_ID_FOR_ARRAYS));
        assertFalse(result.isEnabled(SerializationFeature.WRITE_TYPE_ID_FOR_STRINGS));
    }

    @Test
    public void testCreateContextual_forceTypeInformation_isTrue() throws Exception {
        final Class<Object> clazz = Object.class;
        final Method method = new MethodTestClass().getMethod("getValue");
        final BeanPropertyDefinition property = new AnnotatedClass(MethodTestClass.class)
                .getMember("valueProperty");
        JsonValueSerializer serializer = new JsonValueSerializer(method, null, null, true);
        JsonSerializer<Object> result = serializer.createContextual(null, property);
        assertNotNull(result);
        assertTrue(result.isEnabled(SerializationFeature.WRITE_TYPE_ID_FOR_ARRAYS));
        assertTrue(result.isEnabled(SerializationFeature.WRITE_TYPE_ID_FOR_STRINGS));
    }

    @Test
    public void testSerialize_simple() throws Exception {
        final Object obj = new MethodTestClass();
        JsonValueSerializer serializer = new JsonValueSerializer(
                new MethodTestClass().getMethod("getValue"), null, null, false);
        StringWriter writer = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(writer);
        serializer.serialize(obj, gen, null);
        gen.flush();
        assertEquals("value", writer.toString());
    }

    @Test
    public void testSerialize_withTypeSerializer() throws Exception {
        final Object obj = new MethodTestClass();
        JsonValueSerializer serializer = new JsonValueSerializer(
                new MethodTestClass().getMethod("getValue"), null, null, false);
        ObjectWriter writer = new ObjectMapper().writerWithTypeSerializer(
                new TypeReference<Object>() {});
        String result = writer.writeValueAsString(obj);
        assertEquals("{\"@type\":\"Object\",\"value\":\"value\"}", result);
    }

    @Test
    public void testSerialize_withCustomValueSerializer() throws Exception {
        final Object obj = new TestDataWithJsonSerialize();
        JsonValueSerializer serializer = new JsonValueSerializer(
                new TestDataWithJsonSerialize().getClass().getMethod("getValue"), null, null, false);
        StringWriter writer = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(writer);
        serializer.serialize(obj, gen, null);
        gen.flush();
        assertEquals("serialized value", writer.toString());
    }

    @Test
    public void testSerializeWithType_simple() throws Exception {
        final Object obj = new MethodTestClass();
        JsonValueSerializer serializer = new JsonValueSerializer(
                new MethodTestClass().getMethod("getValue"), null, null, false);
        StringWriter writer = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(writer);
        TypeSerializer typeSerializer = new TypeSerializerBuilder()
                .buildTypeSerializer(Object.class, null);
        serializer.serializeWithType(obj, gen, null, typeSerializer);
        gen.flush();
        assertEquals("{\"@type\":\"Object\"}", writer.toString());
    }

    @Test
    public void testSerializeWithType_withTypeId() throws Exception {
        final Object obj = new TestDataWithJsonSerialize();
        JsonValueSerializer serializer = new JsonValueSerializer(
                new TestDataWithJsonSerialize().getClass().getMethod("getValue"), null, null, false);
        StringWriter writer = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(writer);
        TypeSerializer typeSerializer = new TypeSerializerBuilder()
                .buildTypeSerializer(Object.class, new TypeReference<String>() {});
        serializer.serializeWithType(obj, gen, null, typeSerializer);
        gen.flush();
        assertEquals("{\"@type\":\"String\"}", writer.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        final Object obj = new MethodTestClass();
        JsonValueSerializer serializer = new JsonValueSerializer(
                new MethodTestClass().getMethod("getValue"), null, null, false);
        JsonFactory factory = new JsonFactory();
        JsonFormatVisitorWrapper visitor = factory.getSchemaFactory()
                .getSchemaProvider().findSchemaVisitor(null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        JsonSchema schema = visitor.getSchema();
        assertEquals(Object.class, schema.getSchemaType());
    }

    @Test
    public void testAcceptJsonFormatVisitor_enum() throws Exception {
        final Object obj = new EnumTestClass();
        JsonValueSerializer serializer = new JsonValueSerializer(
                EnumTestClass.class.getMethod("getValue"), null, null, false);
        JsonFactory factory = new JsonFactory();
        JsonFormatVisitorWrapper visitor = factory.getSchemaFactory()
                .getSchemaProvider().findSchemaVisitor(null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        JsonSchema schema = visitor.getSchema();
        assertEquals(String.class, schema.getSchemaType());
        Set<String> expectedValues = new LinkedHashSet<>();
        expectedValues.add("ONE");
        expectedValues.add("TWO");
        expectedValues.add("THREE");
        JsonSchema enumSchema = schema.getSchemaProvider().findSchema(expectedValues);
        assertNotNull(enumSchema);
        assertEquals(String.class, enumSchema.getSchemaType());
        assertEquals(3, enumSchema.getSchemaProperties().size());
        assertEquals("ONE", enumSchema.getSchemaProperty("ONE").getSchemaName());
        assertEquals("TWO", enumSchema.getSchemaProperty("TWO").getSchemaName());
        assertEquals("THREE", enumSchema.getSchemaProperty("THREE").getSchemaName());
    }

    private static class MethodTestClass {
        private String value;

        private String getValue() {
            return value;
        }

        private void setValue(String value) {
            this.value = value;
        }

        private static class ValueProperty {
            @JsonValue
            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        private ValueProperty valueProperty = new ValueProperty();
    }

    private static class EnumTestClass {
        private EnumValue value;

        private EnumValue getValue() {
            return value;
        }

        enum EnumValue {
            ONE,
            TWO,
            THREE
        }
    }
}