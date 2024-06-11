
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

@SuppressWarnings("serial")
public class NumberSerializersTest {
    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        JsonSerializer<?> contextualSerializer = serializer.createContextual(mockProvider(), mockProperty());
        assertEquals(serializer, contextualSerializer);

        Object value = (short) 42;
        JsonGenerator jsonGenerator = mockGenerator();
        SerializerProvider provider = mockProvider();
        serializer.serialize(value, jsonGenerator, provider);
        verify(jsonGenerator).writeNumber(42);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        JsonSerializer<?> contextualSerializer = serializer.createContextual(mockProvider(), mockProperty());
        assertEquals(serializer, contextualSerializer);
        
        Object value = 42;
        JsonGenerator jsonGenerator = mockGenerator();
        SerializerProvider provider = mockProvider();
        serializer.serialize(value, jsonGenerator, provider);
        verify(jsonGenerator).writeNumber(42);
        serializer.serializeWithType(value, jsonGenerator, provider, null);
        verify(jsonGenerator).writeNumber(42);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        JsonSerializer<?> contextualSerializer = serializer.createContextual(mockProvider(), mockProperty());
        assertEquals(serializer, contextualSerializer);
        
        Object value = 42;
        JsonGenerator jsonGenerator = mockGenerator();
        SerializerProvider provider = mockProvider();
        serializer.serialize(value, jsonGenerator, provider);
        verify(jsonGenerator).writeNumber(42);
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer serializer = new LongSerializer(Long.class);
        JsonSerializer<?> contextualSerializer = serializer.createContextual(mockProvider(), mockProperty());
        assertEquals(serializer, contextualSerializer);
        
        Object value = 42L;
        JsonGenerator jsonGenerator = mockGenerator();
        SerializerProvider provider = mockProvider();
        serializer.serialize(value, jsonGenerator, provider);
        verify(jsonGenerator).writeNumber(42L);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        JsonSerializer<?> contextualSerializer = serializer.createContextual(mockProvider(), mockProperty());
        assertEquals(serializer, contextualSerializer);
        
        Object value = 42.0f;
        JsonGenerator jsonGenerator = mockGenerator();
        SerializerProvider provider = mockProvider();
        serializer.serialize(value, jsonGenerator, provider);
        verify(jsonGenerator).writeNumber(42.0f);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        JsonSerializer<?> contextualSerializer = serializer.createContextual(mockProvider(), mockProperty());
        assertEquals(serializer, contextualSerializer);
        
        Object value = 42.0;
        JsonGenerator jsonGenerator = mockGenerator();
        SerializerProvider provider = mockProvider();
        serializer.serialize(value, jsonGenerator, provider);
        verify(jsonGenerator).writeNumber(42.0);
        serializer.serializeWithType(value, jsonGenerator, provider, null);
        verify(jsonGenerator).writeNumber(42.0);
    }

    @Test
    public void testBaseClass_createContextual() throws JsonMappingException {
        Base<?> serializer = new Base<Object>(Number.class, JsonParser.NumberType.INT, "number") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {}
        };
        JsonSerializer<?> contextualSerializer = serializer.createContextual(mockProvider(), mockProperty());
        assertEquals(serializer, contextualSerializer);
    }

    @Test
    public void testBaseClass_getSchema() {
        Base<?> serializer = new Base<Object>(Number.class, JsonParser.NumberType.INT, "number") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {}
        };
        SerializerProvider provider = mockProvider();
        Type typeHint = mockType();
        JsonNode schema = serializer.getSchema(provider, typeHint);
        assertNotNull(schema);
        assertTrue(schema.isObject());
    }

    @Test
    public void testBaseClass_acceptJsonFormatVisitor() throws JsonMappingException {
        Base<?> serializer = new Base<Object>(Number.class, JsonParser.NumberType.INT, "number") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {}
        };
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mockJavaType();
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectNumberFormat(typeHint);
    }

    private SerializerProvider mockProvider() {
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.createInstance(any(), any())).thenReturn(new SerializerProvider());
        return provider;
    }

    private BeanProperty mockProperty() {
        BeanProperty property = mock(BeanProperty.class);
        when(property.getType()).thenReturn(mockJavaType());
        return property;
    }

    private JavaType mockJavaType() {
        return mock(JavaType.class);
    }

    private Type mockType() {
        return mock(Type.class);
    }

    private JsonGenerator mockGenerator() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        when(generator.getOutputContext()).thenReturn(JsonWriteContext.createRootContext(null));
        return generator;
    }

    private void verify(JsonGenerator generator) throws IOException {
        verify(generator, times(1)).writeNumber(anyInt());
        reset(generator);
    }
}
