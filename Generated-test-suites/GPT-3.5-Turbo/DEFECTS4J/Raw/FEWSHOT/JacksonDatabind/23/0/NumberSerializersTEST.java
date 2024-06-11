
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {

    @SuppressWarnings("unchecked")
    private Map<String, JsonSerializer<?>> createMockDeserializers() {
        return mock(Map.class);
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = createMockDeserializers();
        NumberSerializers.addAll(allDeserializers);

        verify(allDeserializers).put(eq(Integer.class.getName()), any());
        verify(allDeserializers).put(eq(Integer.TYPE.getName()), any());
        verify(allDeserializers).put(eq(Long.class.getName()), any());
        verify(allDeserializers).put(eq(Long.TYPE.getName()), any());
        verify(allDeserializers).put(eq(Byte.class.getName()), any());
        verify(allDeserializers).put(eq(Byte.TYPE.getName()), any());
        verify(allDeserializers).put(eq(Short.class.getName()), any());
        verify(allDeserializers).put(eq(Short.TYPE.getName()), any());
        verify(allDeserializers).put(eq(Float.class.getName()), any());
        verify(allDeserializers).put(eq(Float.TYPE.getName()), any());
        verify(allDeserializers).put(eq(Double.class.getName()), any());
        verify(allDeserializers).put(eq(Double.TYPE.getName()), any());
    }

    @Test
    public void testSerializeShort() throws IOException {
        Short value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
        serializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.shortValue());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = 20;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        NumberSerializers.IntegerSerializer serializer = NumberSerializers.IntegerSerializer.instance;
        serializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeIntLike() throws IOException {
        Number value = 30;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;
        serializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long value = 40L;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        NumberSerializers.LongSerializer serializer = NumberSerializers.LongSerializer.instance;
        serializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.longValue());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Float value = 50.0f;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        serializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.floatValue());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Double value = 60.0;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        NumberSerializers.DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;
        serializer.serialize(value, gen, provider);

        verify(gen).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Object value = 70;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);

        NumberSerializers.DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;
        serializer.serializeWithType(value, gen, provider, typeSer);

        verify(typeSer, never()).writeTypeSuffixForObject(any());
        verify(gen).writeNumber(((Double)value).doubleValue());
        verify(typeSer, never()).writeTypeSuffixForObject(any());
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        NumberSerializers.DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;
        JsonNode result = serializer.getSchema(provider, typeHint);

        assertTrue(result.isObject());
        assertEquals("number", result.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        JsonNumberFormatVisitor v2 = mock(JsonNumberFormatVisitor.class);
        when(visitor.expectNumberFormat(eq(typeHint))).thenReturn(v2);

        NumberSerializers.DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(v2).numberType(JsonParser.NumberType.DOUBLE);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember m = mock(AnnotatedMember.class);
        when(property.getMember()).thenReturn(m);

        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(prov.getAnnotationIntrospector().findFormat(eq(m))).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);

        NumberSerializers.DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;
        JsonSerializer<?> result = serializer.createContextual(prov, property);

        assertTrue(result instanceof ToStringSerializer);
    }
}
