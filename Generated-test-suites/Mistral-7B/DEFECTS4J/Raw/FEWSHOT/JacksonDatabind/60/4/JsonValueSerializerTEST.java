package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

/**
 * Test cases for {@link JsonValueSerializer}.
 */
public class JsonValueSerializerTest {

    @SuppressWarnings("unchecked")
    private static <T> JsonSerializer<T> createJsonSerializerMock(Class<T> type) {
        return mock(JsonSerializer.class);
    }

    private static class TestBean {
        @JsonSerialize(using = TestSerializer.class)
        public int getValue() {
            return 42;
        }
    }

    private static class TestSerializer extends StdSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(value);
        }
    }

    /**
     * Test constructor with explicit serializer.
     */
    @Test
    public void testConstructorExplicitSerializer() throws Exception {
        Object target = new TestBean();
        Method getter = TestBean.class.getMethod("getValue");

        JsonSerializer<Object> serializer = createJsonSerializerMock(Integer.TYPE);
        JsonValueSerializer serializerUnderTest = new JsonValueSerializer(getter, serializer);

        assertEquals(TestBean.class, serializerUnderTest.handledType());
        assertEquals(getter, serializerUnderTest._accessorMethod);
        assertEquals(serializer, serializerUnderTest._valueSerializer);
        assertNull(serializerUnderTest._property);
        assertNull(serializerUnderTest._forceTypeInformation);
    }

    /**
     * Test constructor with property.
     */
    @Test
    public void testConstructorWithProperty() throws Exception {
        Object target = new TestBean();
        Method getter = TestBean.class.getMethod("getValue");

        JsonSerializer<Object> serializer = createJsonSerializerMock(Integer.TYPE);

        BeanProperty property = new SimpleBeanProperty("property", getter, null);
        JsonValueSerializer serializerUnderTest = new JsonValueSerializer(getter, serializer, property, false);

        assertEquals(TestBean.class, serializerUnderTest.handledType());
        assertEquals(getter, serializerUnderTest._accessorMethod);
        assertEquals(serializer, serializerUnderTest._valueSerializer);
        assertEquals(property, serializerUnderTest._property);
        assertFalse(serializerUnderTest._forceTypeInformation);
    }

    /**
     * Test creation of contextual serializer.
     */
    @Test
    public void testCreateContextual() throws Exception {
        Object target = new TestBean();
        Method getter = TestBean.class.getMethod("getValue");

        JsonSerializer<Object> serializer = createJsonSerializerMock(Integer.TYPE);

        BeanProperty property = new SimpleBeanProperty("property", getter, null);
        JsonValueSerializer baseSerializer = new JsonValueSerializer(getter, serializer, property, false);

        JsonSerializer<Object> contextualSerializer = baseSerializer.createContextual(null, property);

        assertEquals(TestBean.class, contextualSerializer.handledType());
        assertEquals(getter, contextualSerializer._accessorMethod);
        assertEquals(serializer, contextualSerializer._valueSerializer);
        assertEquals(property, contextualSerializer._property);
        assertFalse(contextualSerializer._forceTypeInformation);
    }

    /**
     * Test serialization of null value.
     */
    @Test
    public void testSerializeNull() throws Exception {
        Object target = new TestBean();
        Method getter = TestBean.class.getMethod("getValue");

        JsonSerializer<Object> serializer = createJsonSerializerMock(Integer.TYPE);

        JsonValueSerializer serializerUnderTest = new JsonValueSerializer(getter, serializer, null, false);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        serializerUnderTest.serialize(target, gen, serializers);

        verify(gen).writeNull();
    }

    /**
     * Test serialization of non-null value.
     */
    @Test
    public void testSerializeValue() throws Exception {
        Object target = new TestBean();
        Method getter = TestBean.class.getMethod("getValue");

        JsonSerializer<Object> serializer = createJsonSerializerMock(Integer.TYPE);

        JsonValueSerializer serializerUnderTest = new JsonValueSerializer(getter, serializer, null, false);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        Object value = getter.invoke(target);
        when(serializer.serialize(value, gen, serializers)).thenReturn(null);

        serializerUnderTest.serialize(target, gen, serializers);

        verify(serializer).serialize(value, gen, serializers);
    }

    /**
     * Test serialization with type serialization.
     */
    @Test
    public void testSerializeWithType() throws Exception {
        Object target = new TestBean();
        Method getter = TestBean.class.getMethod("getValue");

        JsonSerializer<Object> serializer = createJsonSerializerMock(Integer.TYPE);

        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        when(typeSerializer.writeTypePrefixForScalar(target, any(JsonGenerator.class))).thenReturn(null);
        when(typeSerializer.writeTypeSuffixForScalar(target, any(JsonGenerator.class))).thenReturn(null);

        JsonValueSerializer serializerUnderTest = new JsonValueSerializer(getter, serializer, null, false);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        TypeSerializer typeSerializerProvided = mock(TypeSerializer.class);

        Object value = getter.invoke(target);
        when(serializer.serializeWithType(value, gen, serializers, typeSerializerProvided)).thenReturn(null);

        serializerUnderTest.serializeWithType(target, gen, serializers, typeSerializer);

        verify(typeSerializer).writeTypePrefixForScalar(target, gen);
        verify(typeSerializerProvided).serializeWithType(value, gen, serializers, any(TypeSerializer.class));
        verify(typeSerializer).writeTypeSuffixForScalar(target, gen);
    }

    /**
     * Test schema generation.
     */
    @Test
    public void testGetSchema() throws Exception {
        Object target = new TestBean();
        Method getter = TestBean.class.getMethod("getValue");

        JsonSerializer<Object> serializer = createJsonSerializerMock(Integer.TYPE);
        when(serializer.getSchema(any(SerializerProvider.class), any(Type.class))).thenReturn(mock(JsonNode.class));

        JsonValueSerializer serializerUnderTest = new JsonValueSerializer(getter, serializer, null, false);

        SchemaAware schemaAware = mock(SchemaAware.class);
        when(schemaAware.getSchema(any(SerializerProvider.class), any(JsonFormatVisitorWrapper.class))).thenReturn(mock(JsonNode.class));

        JsonSerializerProvider serializers = mock(JsonSerializerProvider.class);
        when(serializers.findValueSerializer(eq(Integer.class), any(BeanPropertyFilter.FilterProvider.class))).thenReturn(serializer);
        when(serializers.findSchemaAwareSerializer(eq(TestBean.class), any(JsonFormatVisitorWrapper.class))).thenReturn(schemaAware);

        JsonNode schema = serializerUnderTest.getSchema(serializers, null);

        assertNotNull(schema);
        verify(serializer).getSchema(serializers, null);
        verify(schemaAware).getSchema(serializers, any(JsonFormatVisitorWrapper.class));
    }
}