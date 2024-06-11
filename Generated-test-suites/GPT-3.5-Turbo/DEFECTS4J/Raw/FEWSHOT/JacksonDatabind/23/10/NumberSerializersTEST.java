package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonGenerator;
import java.util.Map;
import com.fasterxml.jackson.databind.serialize.*;

public class NumberSerializersTest {
    private JsonSerializer<?> jsonSerializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private Map<String, JsonSerializer<?>> allDeserializers;
    private NumberSerializers.Base base;
    private NumberSerializers.ShortSerializer shortSerializer;
    private NumberSerializers.IntegerSerializer integerSerializer;
    private NumberSerializers.IntLikeSerializer intLikeSerializer;
    private NumberSerializers.LongSerializer longSerializer;
    private NumberSerializers.FloatSerializer floatSerializer;
    private NumberSerializers.DoubleSerializer doubleSerializer;
    
    @Test
    public void testSerializeShort() {
        jsonSerializer = new NumberSerializers.ShortSerializer();
        jsonGenerator = new JsonGenerator();
        serializerProvider = new SerializerProvider();
        shortSerializer.serialize((Short) 5, jsonGenerator, serializerProvider);
        assertEquals(jsonGenerator.getNumberType(), JsonGenerator.NumberType.INT);
    }
    
    @Test
    public void testSerializeInteger() {
        jsonSerializer = new NumberSerializers.IntegerSerializer();
        jsonGenerator = new JsonGenerator();
        serializerProvider = new SerializerProvider();
        integerSerializer.serialize((Integer) 10, jsonGenerator, serializerProvider);
        assertEquals(jsonGenerator.getNumberType(), JsonGenerator.NumberType.INT);
    }
    
    @Test
    public void testSerializeIntLike() {
        jsonSerializer = new NumberSerializers.IntLikeSerializer();
        jsonGenerator = new JsonGenerator();
        serializerProvider = new SerializerProvider();
        intLikeSerializer.serialize((Number) 50, jsonGenerator, serializerProvider);
        assertEquals(jsonGenerator.getNumberType(), JsonGenerator.NumberType.INT);
    }
    
    @Test
    public void testSerializeLong() {
        jsonSerializer = new NumberSerializers.LongSerializer();
        jsonGenerator = new JsonGenerator();
        serializerProvider = new SerializerProvider();
        longSerializer.serialize((Long) 100, jsonGenerator, serializerProvider);
        assertEquals(jsonGenerator.getNumberType(), JsonGenerator.NumberType.LONG);
    }
    
    @Test
    public void testSerializeFloat() {
        jsonSerializer = new NumberSerializers.FloatSerializer();
        jsonGenerator = new JsonGenerator();
        serializerProvider = new SerializerProvider();
        floatSerializer.serialize((Float) 3.14, jsonGenerator, serializerProvider);
        assertEquals(jsonGenerator.getNumberType(), JsonGenerator.NumberType.FLOAT);
    }
    
    @Test
    public void testSerializeDouble() {
        jsonSerializer = new NumberSerializers.DoubleSerializer();
        jsonGenerator = new JsonGenerator();
        serializerProvider = new SerializerProvider();
        doubleSerializer.serialize((Double) 2.71828, jsonGenerator, serializerProvider);
        assertEquals(jsonGenerator.getNumberType(), JsonGenerator.NumberType.DOUBLE);
    }
    
    @Test
    public void testGetSchema() {
        jsonSerializer = new NumberSerializers.IntegerSerializer();
        serializerProvider = new SerializerProvider();
        assertEquals(jsonSerializer.getSchema(serializerProvider, Integer.class), createSchemaNode("integer", true));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        jsonSerializer = new NumberSerializers.IntegerSerializer();
        jsonFormatVisitor = new JsonFormatVisitorWrapper();
        javaType = new JavaType();
        jsonSerializer.acceptJsonFormatVisitor(jsonFormatVisitor, javaType);
        assertEquals(jsonFormatVisitor.getNumberType(), JsonGenerator.NumberType.INT);
    }
    
    @Test
    public void testCreateContextual() {
        jsonSerializer = new NumberSerializers.DoubleSerializer();
        serializerProvider = new SerializerProvider();
        assertNull(jsonSerializer.createContextual(serializerProvider, null));
    }
    
    @Test
    public void testAddAll() {
        allDeserializers = new HashMap<String, JsonSerializer<?>>();
        NumberSerializers.addAll(allDeserializers);
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
    
    private JsonNode createSchemaNode(String schemaType, boolean b) {
        // TODO Auto-generated method stub
        return null;
    }

    
}