package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Integer value = 42;

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Integer value = 42;

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Short value = 42;

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.shortValue());
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer serializer = new LongSerializer(Long.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Long value = 42L;

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.longValue());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Float value = 3.14f;

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.floatValue());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Double value = 3.14;

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.doubleValue());
    }

    @Test
    public void testBaseGetSchema() {
        Base<?> serializer = new IntegerSerializer(Integer.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        JsonNode schema = serializer.getSchema(provider, typeHint);

        assertEquals("integer", schema.asText());
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() throws JsonMappingException {
        Base<?> serializer = new FloatSerializer();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(serializer).visitFloatFormat(visitor, typeHint, serializer._numberType);
    }

    @Test
    public void testBaseCreateContextualWithFormatOverride() throws JsonMappingException {
        Base<?> serializer = new FloatSerializer();
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);

        when(serializer.findFormatOverrides(prov, property, serializer.handledType())).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);

        JsonSerializer<?> result = serializer.createContextual(prov, property);

        assertEquals(ToStringSerializer.instance, result);
    }

    @Test
    public void testBaseCreateContextualWithoutFormatOverride() throws JsonMappingException {
        Base<?> serializer = new FloatSerializer();
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        when(serializer.findFormatOverrides(prov, property, serializer.handledType())).thenReturn(null);

        JsonSerializer<?> result = serializer.createContextual(prov, property);

        assertEquals(serializer, result);
    }

    @Test
    public void testNumberSerializersAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();

        NumberSerializers.addAll(allDeserializers);

        assertEquals(8, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testNumberSerializersConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<NumberSerializers> constructor = NumberSerializers.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}