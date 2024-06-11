package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Test class for JsonValueSerializer.
 */
public class JsonValueSerializerTest {

    @SuppressWarnings("unchecked")
    private static final Class<JsonValueSerializer> JSON_VALUE_SERIALIZER_CLASS = (Class<JsonValueSerializer>) Class.forName("com.fasterxml.jackson.databind.ser.std.JsonValueSerializer");

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        JsonSerializer<?> valueSerializer = Mockito.mock(JsonSerializer.class);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);
        assertEquals(JsonValueSerializer.class, jsonValueSerializer.getClass());
        assertEquals(accessorMethod, jsonValueSerializer._accessorMethod);
        assertEquals(valueSerializer, jsonValueSerializer._valueSerializer);
        assertNull(jsonValueSerializer._property);
        assertEquals(true, jsonValueSerializer._forceTypeInformation);
    }

    /**
     * Test constructor with resolved properties.
     */
    @Test
    public void testConstructorWithResolvedProperties() {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        JsonSerializer<?> valueSerializer = Mockito.mock(JsonSerializer.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(JSON_VALUE_SERIALIZER_CLASS.cast(new JsonValueSerializer(accessorMethod, valueSerializer)), property, valueSerializer, true);
        assertEquals(JsonValueSerializer.class, jsonValueSerializer.getClass());
        assertEquals(accessorMethod, jsonValueSerializer._accessorMethod);
        assertEquals(valueSerializer, jsonValueSerializer._valueSerializer);
        assertEquals(property, jsonValueSerializer._property);
        assertEquals(true, jsonValueSerializer._forceTypeInformation);
    }

    /**
     * Test createContextual method.
     */
    @Test
    public void testCreateContextual() throws Exception {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        JsonSerializer<?> valueSerializer = Mockito.mock(JsonSerializer.class);
        JsonSerializer<Object> resolvedSerializer = Mockito.mock(JsonSerializer.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);
        JsonValueSerializer contextualSerializer = jsonValueSerializer.createContextual(provider, property);

        assertEquals(jsonValueSerializer, contextualSerializer);

        when(valueSerializer.isContextualizingSerializer()).thenReturn(false);
        when(provider.findPrimaryPropertySerializer(Mockito.any(JavaType.class), property)).thenReturn(resolvedSerializer);
        JsonValueSerializer contextualSerializer2 = jsonValueSerializer.createContextual(provider, property);

        assertEquals(jsonValueSerializer, contextualSerializer);
        assertEquals(resolvedSerializer, contextualSerializer2._valueSerializer);
    }

    /**
     * Test serialize method with null value.
     */
    @Test
    public void testSerializeWithNullValue() throws Exception {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        Object bean = new Object();
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);
        jsonValueSerializer.serialize(bean, gen, provider);

        provider.defaultSerializeNull(gen);
        verify(gen, times(1)).writeNull();
    }

    /**
     * Test serialize method with null value serializer.
     */
    @Test
    public void testSerializeWithNullValueSerializer() throws Exception {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        Object bean = new Object();
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);
        jsonValueSerializer.serialize(bean, gen, provider);

        assertNull(jsonValueSerializer._valueSerializer);

        Object value = new Object();
        JsonSerializer<Object> valueSerializer = Mockito.mock(JsonSerializer.class);
        jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);

        jsonValueSerializer.serialize(bean, gen, provider);
        verify(valueSerializer, times(1)).serialize(value, gen, provider);
    }

    /**
     * Test serialize method with string value.
     */
    @Test
    public void testSerializeWithStringValue() throws Exception {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        Object bean = new Object();
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        String value = "value";

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);
        jsonValueSerializer = new JsonValueSerializer(jsonValueSerializer, null, null, false);
        jsonValueSerializer.serialize(bean, gen, provider);

        assertEquals("value", jsonValueSerializer._valueSerializer.getValue(bean));
        verify(gen, times(1)).writeStringValue(value);
    }

    /**
     * Test serialize method with int value.
     */
    @Test
    public void testSerializeWithIntValue() throws Exception {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        Object bean = new Object();
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        int value = 42;

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);
        jsonValueSerializer = new JsonValueSerializer(jsonValueSerializer, null, null, false);
        jsonValueSerializer.serialize(bean, gen, provider);

        assertEquals(Integer.TYPE, jsonValueSerializer.handledType());
        assertEquals(value, jsonValueSerializer._valueSerializer.getValue(bean));
        verify(gen, times(1)).writeNumber(value);
    }

    /**
     * Test serialize method with boolean value.
     */
    @Test
    public void testSerializeWithBooleanValue() throws Exception {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        Object bean = new Object();
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        boolean value = true;

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);
        jsonValueSerializer = new JsonValueSerializer(jsonValueSerializer, null, null, false);
        jsonValueSerializer.serialize(bean, gen, provider);

        assertEquals(Boolean.TYPE, jsonValueSerializer.handledType());
        assertEquals(value, jsonValueSerializer._valueSerializer.getValue(bean));
        verify(gen, times(1)).writeBoolean(value);
    }

    /**
     * Test serialize method with null value with type serialization.
     */
    @Test
    public void testSerializeWithNullValueWithTypeSerialization() throws Exception {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        Object bean = new Object();
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);
        jsonValueSerializer = new JsonValueSerializer(jsonValueSerializer, null, null, true);
        jsonValueSerializer.serialize(bean, gen, provider);

        provider.defaultSerializeNull(gen);
        verify(typeSerializer, times(1)).writeTypePrefixForScalar(bean, gen);
        verify(gen, times(1)).writeNull();
        verify(typeSerializer, times(1)).writeTypeSuffixForScalar(bean, gen);
    }

    /**
     * Test serialize method with int value with type serialization.
     */
    @Test
    public void testSerializeWithIntValueWithTypeSerialization() throws Exception {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        Object bean = new Object();
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        int value = 42;
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);
        jsonValueSerializer = new JsonValueSerializer(jsonValueSerializer, null, null, true);
        jsonValueSerializer.serialize(bean, gen, provider);

        assertEquals(Integer.TYPE, jsonValueSerializer.handledType());
        assertEquals(value, jsonValueSerializer._valueSerializer.getValue(bean));
        verify(gen, times(1)).writeNumber(value);
        verify(typeSerializer, times(1)).writeTypePrefixForScalar(bean, gen);
        verify(typeSerializer, times(1)).writeTypeSuffixForScalar(bean, gen);
    }

    /**
     * Test getSchema method.
     */
    @Test
    public void testGetSchema() throws Exception {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);
        JsonSchema schema = jsonValueSerializer.getSchema(provider, typeHint);

        assertNotNull(schema);
    }

    /**
     * Test acceptJsonFormatVisitor method.
     */
    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);
        jsonValueSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}