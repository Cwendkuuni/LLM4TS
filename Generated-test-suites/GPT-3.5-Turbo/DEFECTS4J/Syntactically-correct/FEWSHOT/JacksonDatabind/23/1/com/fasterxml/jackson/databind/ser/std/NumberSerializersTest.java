package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

@SuppressWarnings("serial")
public class NumberSerializersTest {
    
    private Map<String, JsonSerializer<?>> allDeserializers;
    private JsonSerializer<?> integerSerializer;
    private JsonSerializer<?> longSerializer;
    private JsonSerializer<?> intLikeSerializer;
    private JsonSerializer<?> shortSerializer;
    private JsonSerializer<?> floatSerializer;
    private JsonSerializer<?> doubleSerializer;
    
    @Before
    public void setup() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        integerSerializer = allDeserializers.get(Integer.class.getName());
        longSerializer = allDeserializers.get(Long.class.getName());
        intLikeSerializer = allDeserializers.get(Byte.class.getName());
        shortSerializer = allDeserializers.get(Short.class.getName());
        floatSerializer = allDeserializers.get(Float.class.getName());
        doubleSerializer = allDeserializers.get(Double.class.getName());
    }

    @Test
    public void testAddAll() {
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
    
    @JacksonStdImpl
    private static class TestNumberSerializer extends NumberSerializers.Base<Number> {

        protected TestNumberSerializer(JsonParser.NumberType numberType, String schemaType) {
            super(Number.class, numberType, schemaType);
        }

        @Override
        public void serialize(Number value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            // Do nothing
        }

        @Override
        public void serializeWithType(Number value, JsonGenerator gen, SerializerProvider provider,
                TypeSerializer typeSer) throws IOException {
            // Do nothing
        }
        
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("number", true);
        }

        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                throws JsonMappingException {
            JsonNumberFormatVisitor v2 = visitor.expectNumberFormat(typeHint);
            if (v2 != null) {
                v2.numberType(_numberType);
            }
        }   
    }

    @Test
    public void testBase() {
        NumberSerializers.Base<Number> testNumberSerializer = new TestNumberSerializer(JsonParser.NumberType.DOUBLE, "number");
        assertEquals(JsonParser.NumberType.DOUBLE, testNumberSerializer._numberType);
        assertEquals("number", testNumberSerializer._schemaType);
        assertTrue(testNumberSerializer._isInt);
        
        JsonSerializer<?> contextualSerializer = testNumberSerializer.createContextual(null, null);
        assertEquals(testNumberSerializer, contextualSerializer);
    }
    
    @Test
    public void testShortSerializer() {
        assertEquals(Short.class, shortSerializer.handledType());
        assertEquals(false, shortSerializer.isEmpty(null));
        
        short value = 1;
        try {
            ShortSerializer shortSerializer = (ShortSerializer) this.shortSerializer;
            shortSerializer.serialize(value, null, null);
        } catch (Exception e) {
            // Do nothing
        }
        
        JsonNode schema = shortSerializer.getSchema(null, null);
        assertEquals("number", schema.get("type").textValue());
        
        try {
            ShortSerializer shortSerializer = (ShortSerializer) this.shortSerializer;
            shortSerializer.acceptJsonFormatVisitor(null, null);
        } catch (Exception e) {
            // Do nothing
        }
    }
    
    @Test
    public void testIntegerSerializer() {
        assertEquals(Integer.class, integerSerializer.handledType());
        assertEquals(false, integerSerializer.isEmpty(null));
        
        int value = 1;
        try {
            IntegerSerializer integerSerializer = (IntegerSerializer) this.integerSerializer;
            integerSerializer.serialize(value, null, null);
        } catch (Exception e) {
            // Do nothing
        }
        
        JsonNode schema = integerSerializer.getSchema(null, null);
        assertEquals("integer", schema.get("type").textValue());
        
        try {
            IntegerSerializer integerSerializer = (IntegerSerializer) this.integerSerializer;
            integerSerializer.acceptJsonFormatVisitor(null, null);
        } catch (Exception e) {
            // Do nothing
        }
    }
    
    @Test
    public void testIntLikeSerializer() {
        assertEquals(Number.class, intLikeSerializer.handledType());
        assertEquals(false, intLikeSerializer.isEmpty(null));
        
        byte value = 1;
        try {
            IntLikeSerializer intLikeSerializer = (IntLikeSerializer) this.intLikeSerializer;
            intLikeSerializer.serialize(value, null, null);
        } catch (Exception e) {
            // Do nothing
        }
        
        JsonNode schema = intLikeSerializer.getSchema(null, null);
        assertEquals("integer", schema.get("type").textValue());
        
        try {
            IntLikeSerializer intLikeSerializer = (IntLikeSerializer) this.intLikeSerializer;
            intLikeSerializer.acceptJsonFormatVisitor(null, null);
        } catch (Exception e) {
            // Do nothing
        }
    }
    
    @Test
    public void testLongSerializer() {
        assertEquals(Long.class, longSerializer.handledType());
        assertEquals(false, longSerializer.isEmpty(null));
        
        long value = 1;
        try {
            LongSerializer longSerializer = (LongSerializer) this.longSerializer;
            longSerializer.serialize(value, null, null);
        } catch (Exception e) {
            // Do nothing
        }
        
        JsonNode schema = longSerializer.getSchema(null, null);
        assertEquals("number", schema.get("type").textValue());
        
        try {
            LongSerializer longSerializer = (LongSerializer) this.longSerializer;
            longSerializer.acceptJsonFormatVisitor(null, null);
        } catch (Exception e) {
            // Do nothing
        }
    }
    
    @Test
    public void testFloatSerializer() {
        assertEquals(Float.class, floatSerializer.handledType());
        assertEquals(false, floatSerializer.isEmpty(null));
        
        float value = 1.0f;
        try {
            FloatSerializer floatSerializer = (FloatSerializer) this.floatSerializer;
            floatSerializer.serialize(value, null, null);
        } catch (Exception e) {
            // Do nothing
        }
        
        JsonNode schema = floatSerializer.getSchema(null, null);
        assertEquals("number", schema.get("type").textValue());
        
        try {
            FloatSerializer floatSerializer = (FloatSerializer) this.floatSerializer;
            floatSerializer.acceptJsonFormatVisitor(null, null);
        } catch (Exception e) {
            // Do nothing
        }
    }
    
    @Test
    public void testDoubleSerializer() {
        assertEquals(Double.class, doubleSerializer.handledType());
        assertEquals(false, doubleSerializer.isEmpty(null));
        
        double value = 1.0;
        try {
            DoubleSerializer doubleSerializer = (DoubleSerializer) this.doubleSerializer;
            doubleSerializer.serialize(value, null, null);
        } catch (Exception e) {
            // Do nothing
        }
        
        JsonNode schema = doubleSerializer.getSchema(null, null);
        assertEquals("number", schema.get("type").textValue());
        
        try {
            DoubleSerializer doubleSerializer = (DoubleSerializer) this.doubleSerializer;
            doubleSerializer.acceptJsonFormatVisitor(null, null);
        } catch (Exception e) {
            // Do nothing
        }
    }
}