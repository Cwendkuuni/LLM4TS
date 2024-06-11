package com.fasterxml.jackson.databind.ser.std;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.SerializerProvider;

import org.junit.BeforeClass;
import org.junit.Test;

public class NumberSerializersTest {
    private static Map<String, JsonSerializer<?>> allDeserializers;

    @BeforeClass
    public static void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testShortSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Short.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof NumberSerializers.ShortSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize((short) 5, gen, provider);

        verify(gen, times(1)).writeNumber((short) 5);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Integer.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof NumberSerializers.IntegerSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize(10, gen, provider);

        verify(gen, times(1)).writeNumber(10);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Number.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof NumberSerializers.IntLikeSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize((short) 6, gen, provider);

        verify(gen, times(1)).writeNumber(6);
    }

    @Test
    public void testLongSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Long.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof NumberSerializers.LongSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize(100L, gen, provider);

        verify(gen, times(1)).writeNumber(100L);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Float.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof NumberSerializers.FloatSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize(1.5f, gen, provider);

        verify(gen, times(1)).writeNumber(1.5f);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Double.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof NumberSerializers.DoubleSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize(2.75, gen, provider);

        verify(gen, times(1)).writeNumber(2.75);
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializers.Base<?> serializer = new NumberSerializers.ShortSerializer();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor, times(1)).expectIntFormat(typeHint);
    }

    @Test
    public void testBaseGetSchema() {
        NumberSerializers.Base<?> serializer = new NumberSerializers.ShortSerializer();
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        serializer.getSchema(provider, typeHint);
        verify(serializer, times(1)).createSchemaNode(eq("number"), eq(true));
    }

    @Test
    public void testBaseCreateContextualWithFormat() throws JsonMappingException {
        NumberSerializers.Base<?> serializer = new NumberSerializers.ShortSerializer();
        BeanProperty property = mock(BeanProperty.class);
        SerializerProvider prov = mock(SerializerProvider.class);

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        when(serializer.findFormatOverrides(prov, property, serializer.handledType())).thenReturn(format);

        JsonSerializer<?> contextualSerializer = serializer.createContextual(prov, property);
        assertTrue(contextualSerializer instanceof ToStringSerializer);
    }

    @Test
    public void testBaseCreateContextualWithoutFormat() throws JsonMappingException {
        NumberSerializers.Base<?> serializer = new NumberSerializers.ShortSerializer();
        BeanProperty property = mock(BeanProperty.class);
        SerializerProvider prov = mock(SerializerProvider.class);

        when(serializer.findFormatOverrides(prov, property, serializer.handledType())).thenReturn(null);

        JsonSerializer<?> contextualSerializer = serializer.createContextual(prov, property);
        assertEquals(serializer, contextualSerializer);
    }

    @Test
    public void testIntegerSerializeWithType() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);

        serializer.serializeWithType(10, gen, provider, typeSer);

        verify(serializer, times(1)).serialize(eq(10), eq(gen), eq(provider));
    }

    @Test
    public void testDoubleSerializeWithType() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);

        serializer.serializeWithType(2.75, gen, provider, typeSer);

        verify(serializer, times(1)).serialize(eq(2.75), eq(gen), eq(provider));
    }
}