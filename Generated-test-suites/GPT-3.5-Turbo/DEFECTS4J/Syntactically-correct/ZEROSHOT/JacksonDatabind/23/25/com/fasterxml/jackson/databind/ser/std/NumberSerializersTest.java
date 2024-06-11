package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class NumberSerializersTest {
    
    private NumberSerializers numberSerializers;
    
    @Before
    public void setup() {
        numberSerializers = new NumberSerializers();
    }
    
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        numberSerializers.addAll(allDeserializers);
        
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }
    
    private static class MockSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // Do nothing
        }

        @Override
        public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
            // Do nothing
        }
    }
    
    @Test
    public void testBaseGetSchema() {
        JsonSerializer<Object> serializer = new MockSerializer();
        
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        Type mockTypeHint = mock(Type.class);
        
        JsonNode schema = serializer.getSchema(mockProvider, mockTypeHint);
        
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("isNumber").asBoolean());
    }
    
    @Test
    public void testBaseAcceptJsonFormatVisitor_IntegerType() throws JsonMappingException {
        JsonSerializer<Object> serializer = new MockSerializer();
        
        JsonFormatVisitorWrapper mockVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType mockTypeHint = mock(JavaType.class);
        JsonIntegerFormatVisitor mockVisitor = mock(JsonIntegerFormatVisitor.class);
        when(mockVisitorWrapper.expectIntegerFormat(mockTypeHint)).thenReturn(mockVisitor);
        
        serializer.acceptJsonFormatVisitor(mockVisitorWrapper, mockTypeHint);
        
        verify(mockVisitor).numberType(JsonParser.NumberType.INT);
    }
    
    @Test
    public void testBaseAcceptJsonFormatVisitor_NonIntegerType() throws JsonMappingException {
        JsonSerializer<Object> serializer = new MockSerializer();
        
        JsonFormatVisitorWrapper mockVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType mockTypeHint = mock(JavaType.class);
        JsonNumberFormatVisitor mockVisitor = mock(JsonNumberFormatVisitor.class);
        when(mockVisitorWrapper.expectNumberFormat(mockTypeHint)).thenReturn(mockVisitor);
        
        serializer.acceptJsonFormatVisitor(mockVisitorWrapper, mockTypeHint);
        
        verify(mockVisitor).numberType(JsonParser.NumberType.INT);
    }
    
    @Test
    public void testBaseCreateContextual() throws JsonMappingException {
        JsonSerializer<Object> serializer = new MockSerializer();
        
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        BeanProperty mockProperty = mock(BeanProperty.class);
        AnnotatedMember mockMember = mock(AnnotatedMember.class);
        when(mockProperty.getMember()).thenReturn(mockMember);
        
        JsonFormat.Value mockFormat = mock(JsonFormat.Value.class);
        when(mockProvider.getAnnotationIntrospector().findFormat(mockMember)).thenReturn(mockFormat);
        when(mockFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
        
        JsonSerializer<?> contextualSerializer = serializer.createContextual(mockProvider, mockProperty);
        
        assertEquals(ToStringSerializer.instance, contextualSerializer);
    }
    
    private abstract static class BaseTestSerializer<T> extends NumberSerializers.Base<T> {
        protected BaseTestSerializer(Class<?> cls, JsonParser.NumberType numberType, String schemaType) {
            super(cls, numberType, schemaType);
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            // Do nothing
        }
    }
    
    @Test
    public void testBaseTestSerializer_SerializeWithType() throws IOException {
        BaseTestSerializer<Object> serializer = new BaseTestSerializer<Object>(Object.class, JsonParser.NumberType.INT, "integer") {
            @Override
            public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
                super.serializeWithType(value, gen, provider, typeSer);
            }
        };
        
        Object mockValue = mock(Object.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        TypeSerializer mockTypeSerializer = mock(TypeSerializer.class);
        
        serializer.serializeWithType(mockValue, mockGenerator, mockProvider, mockTypeSerializer);
        
        verify(serializer).serialize(mockValue, mockGenerator, mockProvider);
    }
    
    private static class ShortTestSerializer extends NumberSerializers.ShortSerializer {
        @Override
        public void serialize(Short value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            super.serialize(value, gen, provider);
        }
    }
    
    @Test
    public void testShortSerializer_Serialize() throws IOException {
        ShortTestSerializer serializer = new ShortTestSerializer();
        
        Short mockValue = mock(Short.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.serialize(mockValue, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeNumber(mockValue.shortValue());
    }
    
    private static class IntegerTestSerializer extends NumberSerializers.IntegerSerializer {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            super.serialize(value, gen, provider);
        }
    }
    
    @Test
    public void testIntegerSerializer_Serialize() throws IOException {
        IntegerTestSerializer serializer = new IntegerTestSerializer();
        
        Integer mockValue = mock(Integer.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.serialize(mockValue, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeNumber(mockValue.intValue());
    }
    
    @Test
    public void testIntegerSerializer_SerializeWithType() throws IOException {
        IntegerTestSerializer serializer = new IntegerTestSerializer();
        
        Integer mockValue = mock(Integer.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        TypeSerializer mockTypeSerializer = mock(TypeSerializer.class);
        
        serializer.serializeWithType(mockValue, mockGenerator, mockProvider, mockTypeSerializer);
        
        verify(serializer).serialize(mockValue, mockGenerator, mockProvider);
    }
    
    private static class IntLikeTestSerializer extends NumberSerializers.IntLikeSerializer {
        @Override
        public void serialize(Number value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            super.serialize(value, gen, provider);
        }
    }
    
    @Test
    public void testIntLikeSerializer_Serialize() throws IOException {
        IntLikeTestSerializer serializer = new IntLikeTestSerializer();
        
        Number mockValue = mock(Number.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.serialize(mockValue, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeNumber(mockValue.intValue());
    }
    
    private static class LongTestSerializer extends NumberSerializers.LongSerializer {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            super.serialize(value, gen, provider);
        }
    }
    
    @Test
    public void testLongSerializer_Serialize() throws IOException {
        LongTestSerializer serializer = new LongTestSerializer();
        
        Long mockValue = mock(Long.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.serialize(mockValue, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeNumber(mockValue.longValue());
    }
    
    private static class FloatTestSerializer extends NumberSerializers.FloatSerializer {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            super.serialize(value, gen, provider);
        }
    }
    
    @Test
    public void testFloatSerializer_Serialize() throws IOException {
        FloatTestSerializer serializer = new FloatTestSerializer();
        
        Float mockValue = mock(Float.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.serialize(mockValue, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeNumber(mockValue.floatValue());
    }
    
    private static class DoubleTestSerializer extends NumberSerializers.DoubleSerializer {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            super.serialize(value, gen, provider);
        }
    }
    
    @Test
    public void testDoubleSerializer_Serialize() throws IOException {
        DoubleTestSerializer serializer = new DoubleTestSerializer();
        
        Double mockValue = mock(Double.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.serialize(mockValue, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeNumber(mockValue.doubleValue());
    }
    
    @Test
    public void testDoubleSerializer_SerializeWithType() throws IOException {
        DoubleTestSerializer serializer = new DoubleTestSerializer();
        
        Double mockValue = mock(Double.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        TypeSerializer mockTypeSerializer = mock(TypeSerializer.class);
        
        serializer.serializeWithType(mockValue, mockGenerator, mockProvider, mockTypeSerializer);
        
        verify(serializer).serialize(mockValue, mockGenerator, mockProvider);
    }
}