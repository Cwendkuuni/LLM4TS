package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema.DefaultSchemaNode;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.StdSerializer;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimplePropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleValueSerializer;
import com.fasterxml.jackson.databind.ser.impl.ValueSerializerBase;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Test cases for {@link JsonValueSerializer}.
 */
public class JsonValueSerializerTest {

    @SuppressWarnings("unchecked")
    private static final JsonValueSerializer<Object> DUMMY_SERIALIZER =
            new JsonValueSerializer<Object>(null, null, null, false) {
                @Override
                public JsonSerializer<Object> createContextual(
                        SerializerProvider provider, BeanProperty property)
                        throws JsonMappingException {
                    return null;
                }
            };

    @Test
    public void testSerialization() throws Exception {
        final Object testObj = new TestClass();
        final Method testMethod = TestClass.class.getMethod("getValue");
        final JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);

        final JsonValueSerializer<Object> serializer = new JsonValueSerializer<Object>(
                testMethod, valueSerializer) {
                    @Override
                    public JsonSerializer<Object> createContextual(
                            SerializerProvider provider, BeanProperty property)
                            throws JsonMappingException {
                        return this;
                    }
                };

        final ObjectWriter writer = new ObjectMapper().writer();
        final JsonGenerator gen = writer.getGenerator();

        final JsonSerializerProvider serializerProvider = mock(JsonSerializerProvider.class);
        when(serializerProvider.findPrimaryPropertySerializer(
                any(JavaType.class), eq(null)))
                .thenReturn(DUMMY_SERIALIZER);

        serializer.setSerializerProvider(serializerProvider);

        when(valueSerializer.serializeValue(any(Object.class), any(JsonGenerator.class),
                any(SerializerProvider.class))).thenReturn(new StringWriter(1024).toString());

        serializer.serialize(testObj, gen, serializerProvider);

        gen.flush();
        gen.close();

        // Check serialization result
        final String result = writer.writeValueAsString(testObj);
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void testSerializationWithType() throws Exception {
        final Object testObj = new TestClass();
        final Method testMethod = TestClass.class.getMethod("getValue");
        final JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
        final TypeSerializer typeSerializer = mock(TypeSerializer.class);

        final JsonValueSerializer<Object> serializer = new JsonValueSerializer<Object>(
                testMethod, valueSerializer, null, false) {
                    @Override
                    public JsonSerializer<Object> createContextual(
                            SerializerProvider provider, BeanProperty property)
                            throws JsonMappingException {
                        return this;
                    }
                };

        final ObjectWriter writer = new ObjectMapper().writer();
        final JsonGenerator gen = writer.getGenerator();

        final JsonSerializerProvider serializerProvider = mock(JsonSerializerProvider.class);
        when(serializerProvider.findPrimaryPropertySerializer(
                any(JavaType.class), eq(null)))
                .thenReturn(DUMMY_SERIALIZER);
        when(serializerProvider.findValueSerializer(
                any(Class.class), eq(null)))
                .thenReturn(DUMMY_SERIALIZER);
        when(valueSerializer.serializeWithType(any(Object.class), any(JsonGenerator.class),
                any(SerializerProvider.class), any(TypeSerializer.class)))
                .thenReturn(new StringWriter(1024).toString());

        serializer.setSerializerProvider(serializerProvider);
        serializer.setTypeSerializer(typeSerializer);

        serializer.serializeWithType(testObj, gen, serializerProvider, typeSerializer);

        gen.flush();
        gen.close();

        // Check serialization result
        final String result = writer.writeValueAsString(testObj);
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void testSchemaGeneration() throws Exception {
        final Object testObj = new TestClass();

        final JsonSerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        final JsonValueSerializer<Object> serializer = new JsonValueSerializer<Object>(
                TestClass.class.getMethod("getValue"), null, null, false) {
                    @Override
                    public JsonSerializer<Object> createContextual(
                            SerializerProvider provider, BeanProperty property)
                            throws JsonMappingException {
                        return this;
                    }
                };

        final JsonSchema schema = serializer.getSchema(serializerProvider, Object.class);
        assertNotNull(schema);
        System.out.println(schema.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        final Object testObj = new TestClass();

        final JsonSerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        final JsonValueSerializer<Object> serializer = new JsonValueSerializer<Object>(
                TestClass.class.getMethod("getValue"), null, null, false) {
                    @Override
                    public JsonSerializer<Object> createContextual(
                            SerializerProvider provider, BeanProperty property)
                            throws JsonMappingException {
                        return this;
                    }
                };

        final ObjectWriter writer = new ObjectMapper().writer();
        final JsonFormatVisitorWrapper jsonFormatVisitor = writer.acceptJsonFormatVisitor(null);

        serializer.acceptJsonFormatVisitor(jsonFormatVisitor, null);

        jsonFormatVisitor.close();

        final JsonNode schemaNode = jsonFormatVisitor.getOutput();
        assertNotNull(schemaNode);
        System.out.println(schemaNode.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws Exception {
        final Object testObj = new EnumTestClass();

        final JsonSerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        final JsonValueSerializer<Object> serializer = new JsonValueSerializer<Object>(
                EnumTestClass.class.getMethod("getValue"), null, null, false) {
                    @Override
                    public JsonSerializer<Object> createContextual(
                            SerializerProvider provider, BeanProperty property)
                            throws JsonMappingException {
                        return this;
                    }
                };

        final ObjectWriter writer = new ObjectMapper().writer();
        final JsonFormatVisitorWrapper jsonFormatVisitor = writer.acceptJsonFormatVisitor(null);

        serializer._acceptJsonFormatVisitorForEnum(jsonFormatVisitor, null, EnumTestClass.class);

        jsonFormatVisitor.close();

        final JsonNode schemaNode = jsonFormatVisitor.getOutput();
        assertNotNull(schemaNode);
        System.out.println(schemaNode.toString());
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() throws Exception {
        final Object testObj = new TestClass();

        final JsonValueSerializer<Object> serializer = new JsonValueSerializer<Object>(
                TestClass.class.getMethod("getValue"), null, null, false) {
                    @Override
                    public JsonSerializer<Object> createContextual(
                            SerializerProvider provider, BeanProperty property)
                            throws JsonMappingException {
                        return this;
                    }
                };

        final boolean result = serializer.isNaturalTypeWithStdHandling(
                TestClass.class, serializer);
        assertTrue(result);

        final int intObj = 1;
        final boolean resultInt = serializer.isNaturalTypeWithStdHandling(
                int.class, serializer);
        assertFalse(resultInt);

        final String stringObj = "test";
        final boolean resultString = serializer.isNaturalTypeWithStdHandling(
                String.class, serializer);
        assertFalse(resultString);

        final Double doubleObj = 1.0;
        final boolean resultDouble = serializer.isNaturalTypeWithStdHandling(
                Double.class, serializer);
        assertTrue(resultDouble);

        final Boolean booleanObj = true;
        final boolean resultBoolean = serializer.isNaturalTypeWithStdHandling(
                Boolean.class, serializer);
        assertTrue(resultBoolean);

        final Integer integerEnum = EnumTestClass.ONE;
        final boolean resultEnum = serializer.isNaturalTypeWithStdHandling(
                integerEnum.getClass(), serializer);
        assertFalse(resultEnum);

        final JsonValueSerializer<Object> jsonValueSerializer = mock(JsonValueSerializer.class);
        final boolean resultJsonValueSerializer = serializer.isNaturalTypeWithStdHandling(
                JsonValueSerializer.class, jsonValueSerializer);
        assertTrue(resultJsonValueSerializer);
    }

    private static class TestClass {
        private final int value;

        public TestClass() {
            this.value = 42;
        }

        @JsonValue
        public int getValue() {
            return this.value;
        }
    }

    private static class EnumTestClass {
        public static final EnumTestClass ONE = new EnumTestClass("ONE");
        public static final EnumTestClass TWO = new EnumTestClass("TWO");

        private final String name;

        public EnumTestClass(String name) {
            this.name = name;
        }

        @JsonSerialize(using = JsonValueSerializer.class)
        public String getValue() {
            return this.name;
        }
    }
}

class StringWriter extends StringBuffer {
    public StringWriter(int size) {
        super(size);
    }

    public static StringWriter newInstance(int size) {
        return new StringWriter(size);
    }
}