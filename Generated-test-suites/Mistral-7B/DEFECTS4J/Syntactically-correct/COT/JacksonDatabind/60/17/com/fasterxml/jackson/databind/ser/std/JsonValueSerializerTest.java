package com.example; // Replace this with the actual package name of your test class

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonValueSerializerTest {
    private static final Class<JsonValueSerializer> JSON_VALUE_SERIALIZER_CLASS = JsonValueSerializer.class;

    private JsonValueSerializer jsonValueSerializer;
    private BeanProperty beanProperty;
    private JsonSerializer<Object> valueSerializer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        jsonValueSerializer = new JsonValueSerializer(null, null, null, false);
        beanProperty = mock(BeanProperty.class);
        valueSerializer = mock(JsonSerializer.class);
    }

    @Test
    public void testJsonValueSerializerConstructor() {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        assertNotNull(serializer);
    }

    @Test
    public void testJsonValueSerializerWithResolved() throws Exception {
        JsonValueSerializer srcSerializer = mock(JsonValueSerializer.class);
        when(srcSerializer.handledType()).thenReturn(Object.class);

        JsonValueSerializer serializedSerializer = jsonValueSerializer.withResolved(beanProperty, valueSerializer, false);
        assertEquals(JSON_VALUE_SERIALIZER_CLASS, serializedSerializer.getClass());
    }

    @Test
    public void testCreateContextualSerializer() throws Exception {
        JsonSerializerProvider serializerProvider = mock(JsonSerializerProvider.class);

        AnnotatedMethod accessorMethod = mock(AnnotatedMethod.class);
        when(accessorMethod.getType()).thenReturn(Integer.class);
        when(accessorMethod.getValue("")).thenReturn(1);

        ObjectSerializerProvider objectProvider = mock(ObjectSerializerProvider.class);
        when(objectProvider.handlePrimaryContextualization(valueSerializer, beanProperty)).thenReturn(valueSerializer);

        when(serializerProvider.isEnabled(MapperFeature.USE_STATIC_TYPING)).thenReturn(false);
        when(serializerProvider.findPrimaryPropertySerializer(Integer.class, beanProperty)).thenReturn(valueSerializer);

        JsonValueSerializer serializer = new JsonValueSerializer(accessorMethod, null);
        JsonSerializer<Object> contextualSerializer = serializer.createContextual(serializerProvider, beanProperty);
        assertEquals(valueSerializer, contextualSerializer);

        when(serializerProvider.isEnabled(MapperFeature.USE_STATIC_TYPING)).thenReturn(true);
        contextualSerializer = serializer.createContextual(serializerProvider, beanProperty);
        assertEquals(valueSerializer, contextualSerializer);

        when(serializerProvider.findPrimaryPropertySerializer(Integer.class, beanProperty)).thenReturn(null);
        contextualSerializer = serializer.createContextual(serializerProvider, beanProperty);
        assertNull(contextualSerializer);
    }

    @Test
    public void testSerialize() throws Exception {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Object bean = new Object();

        Object value = new Object();
        when(jsonValueSerializer._accessorMethod.getValue(bean)).thenReturn(value);

        jsonValueSerializer.serialize(bean, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeFieldName("value");
        jsonValueSerializer._valueSerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    public void testSerializeWithType() throws Exception {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        Object bean = new Object();

        Object value = new Object();
        when(jsonValueSerializer._accessorMethod.getValue(bean)).thenReturn(value);

        jsonValueSerializer.serializeWithType(bean, jsonGenerator, serializerProvider, typeSerializer);
        verify(jsonGenerator).writeStartObject();
        typeSerializer.writeTypePrefixForScalar(bean, jsonGenerator);
        jsonValueSerializer._valueSerializer.serializeWithType(value, jsonGenerator, serializerProvider, typeSerializer);
        typeSerializer.writeTypeSuffixForScalar(bean, jsonGenerator);
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    public void testGetSchema() throws Exception {
        JsonSerializerProvider serializerProvider = mock(JsonSerializerProvider.class);
        ObjectType typeHint = mock(ObjectType.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false);
        JsonSchema schema = jsonValueSerializer.getSchema(serializerProvider, typeHint);
        assertNotNull(schema);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithException() throws Exception {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Object bean = new Object();

        Object value = new Object();
        when(jsonValueSerializer._accessorMethod.getValue(bean)).thenThrow(new IOException("Reason for error"));

        jsonValueSerializer.serialize(bean, jsonGenerator, serializerProvider);
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws Exception {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        Class<?> enumType = Enum.class; // Replace this with an actual enum class

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false);
        jsonValueSerializer._acceptJsonFormatVisitorForEnum(visitor, typeHint, enumType);
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() throws Exception {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false);
        assertTrue(jsonValueSerializer.isNaturalTypeWithStdHandling(Integer.TYPE, valueSerializer));
        assertTrue(jsonValueSerializer.isNaturalTypeWithStdHandling(Integer.class, valueSerializer));
        assertTrue(jsonValueSerializer.isNaturalTypeWithStdHandling(Boolean.TYPE, valueSerializer));
        assertTrue(jsonValueSerializer.isNaturalTypeWithStdHandling(Boolean.class, valueSerializer));
        assertTrue(jsonValueSerializer.isNaturalTypeWithStdHandling(Double.TYPE, valueSerializer));
        assertTrue(jsonValueSerializer.isNaturalTypeWithStdHandling(Double.class, valueSerializer));
        assertTrue(jsonValueSerializer.isNaturalTypeWithStdHandling(String.class, valueSerializer));
        assertFalse(jsonValueSerializer.isNaturalTypeWithStdHandling(Object.class, valueSerializer));
    }
}