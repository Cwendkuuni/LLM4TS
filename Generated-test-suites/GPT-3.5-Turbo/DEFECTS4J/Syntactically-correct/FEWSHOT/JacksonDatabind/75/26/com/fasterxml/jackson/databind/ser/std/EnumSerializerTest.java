
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.EnumSerializerTest;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {
    @Test
    public void testSerialize() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), false);
        JsonGenerator gen = null;
        SerializerProvider serializers = null;
        
        gen = null;
        serializers = null;
        enumSerializer.serialize(null, gen, serializers);
        
        gen = null;
        serializers = null;
        enumSerializer.serialize(TestEnum.ONE, gen, serializers);
        
        gen = null;
        serializers = null;
        enumSerializer.serialize(TestEnum.TWO, gen, serializers);
    }
    
    @Test
    public void testSerializeAsIndex() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), true);
        JsonGenerator gen = null;
        SerializerProvider serializers = null;
        
        gen = null;
        serializers = null;
        enumSerializer.serialize(null, gen, serializers);
        
        gen = null;
        serializers = null;
        enumSerializer.serialize(TestEnum.ONE, gen, serializers);
        
        gen = null;
        serializers = null;
        enumSerializer.serialize(TestEnum.TWO, gen, serializers);
    }
    
    @Test
    public void testCreateContextual() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), false);
        SerializerProvider serializers = null;
        assertEquals(enumSerializer, enumSerializer.createContextual(serializers, null));
        
        assertEquals(enumSerializer, enumSerializer.createContextual(null, null));
    }
    
    @Test
    public void testGetSchema() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), false);
        SerializerProvider provider = null;
        assertEquals(null, enumSerializer.getSchema(provider, null));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), false);
        SerializerProvider serializers = null;
        assertEquals(null, enumSerializer.acceptJsonFormatVisitor(null, null));
    }
    
    private enum TestEnum {
        ONE, TWO
    }
}
