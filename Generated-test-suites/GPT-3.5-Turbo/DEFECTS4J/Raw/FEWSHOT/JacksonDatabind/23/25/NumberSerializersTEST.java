
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer();
        
        assertEquals("integer", serializer._schemaType);
        
        // Test serialization
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        serializer.serialize(10, generator, provider);
        assertEquals(10, generator.getOutput());
        
        // Test serializeWithType
        generator.reset();
        serializer.serializeWithType(10, generator, provider, null);
        assertEquals(10, generator.getOutput());
        
        // Test getSchema
        assertNull(serializer.getSchema(provider, null));
        
        // Test acceptJsonFormatVisitor
        JsonFormatVisitorStub visitor = new JsonFormatVisitorStub();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(JsonParser.NumberType.INT, visitor.getNumberType());
    }
    
    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        
        assertEquals("integer", serializer._schemaType);
        
        // Test serialization
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        serializer.serialize(10, generator, provider);
        assertEquals(10, generator.getOutput());
        
        // Test getSchema
        assertNull(serializer.getSchema(provider, null));
        
        // Test acceptJsonFormatVisitor
        JsonFormatVisitorStub visitor = new JsonFormatVisitorStub();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(JsonParser.NumberType.INT, visitor.getNumberType());
    }
    
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = ShortSerializer.instance;
        
        assertEquals("number", serializer._schemaType);
        
        // Test serialization
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        serializer.serialize((short) 5, generator, provider);
        assertEquals((short) 5, generator.getOutput());
        
        // Test getSchema
        assertNull(serializer.getSchema(provider, null));
        
        // Test acceptJsonFormatVisitor
        JsonFormatVisitorStub visitor = new JsonFormatVisitorStub();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(JsonParser.NumberType.INT, visitor.getNumberType());
    }
    
    @Test
    public void testLongSerializer() {
        LongSerializer serializer = LongSerializer.instance;
        
        assertEquals("number", serializer._schemaType);
        
        // Test serialization
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        serializer.serialize(100L, generator, provider);
        assertEquals(100L, generator.getOutput());
        
        // Test getSchema
        assertNull(serializer.getSchema(provider, null));
        
        // Test acceptJsonFormatVisitor
        JsonFormatVisitorStub visitor = new JsonFormatVisitorStub();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(JsonParser.NumberType.LONG, visitor.getNumberType());
    }
    
    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = FloatSerializer.instance;
        
        assertEquals("number", serializer._schemaType);
        
        // Test serialization
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        serializer.serialize(10.5f, generator, provider);
        assertEquals(10.5f, generator.getOutput());
        
        // Test getSchema
        assertNull(serializer.getSchema(provider, null));
        
        // Test acceptJsonFormatVisitor
        JsonFormatVisitorStub visitor = new JsonFormatVisitorStub();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(JsonParser.NumberType.FLOAT, visitor.getNumberType());
    }
    
    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = DoubleSerializer.instance;
        
        assertEquals("number", serializer._schemaType);
        
        // Test serialization
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        serializer.serialize(10.5, generator, provider);
        assertEquals(10.5, generator.getOutput());
        
        // Test serializeWithType
        generator.reset();
        serializer.serializeWithType(10.5, generator, provider, null);
        assertEquals(10.5, generator.getOutput());
        
        // Test getSchema
        assertNull(serializer.getSchema(provider, null));
        
        // Test acceptJsonFormatVisitor
        JsonFormatVisitorStub visitor = new JsonFormatVisitorStub();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(JsonParser.NumberType.DOUBLE, visitor.getNumberType());
    }
}
