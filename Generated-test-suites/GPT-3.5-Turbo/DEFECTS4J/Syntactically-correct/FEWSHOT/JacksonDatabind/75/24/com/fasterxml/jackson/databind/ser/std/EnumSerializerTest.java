package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;

public class EnumSerializerTest {
    @Mock
    private ContextualSerializer serializer;

    
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testConstruct() {
        EnumSerializer enumSerializer = EnumSerializer.construct(Enum.class, null, null, null);
        
        // verify enumSerializer
        assertEquals(Enum.class, enumSerializer.handledType());
    }
    
    @Test
    public void testCreateContextual() {
        EnumSerializer enumSerializer = new EnumSerializer(null, null);
        EnumSerializer createContextualSerializer = (EnumSerializer) enumSerializer.createContextual(null, null);
        
        // verify createContextualSerializer
        assertEquals(enumSerializer, createContextualSerializer);
    }
    
    @Test
    public void testSerialize() {
        EnumSerializer enumSerializer = new EnumSerializer(null, null);
        // test serialize()
    }
    
    @Test
    public void testGetSchema() {
        EnumSerializer enumSerializer = new EnumSerializer(null, null);
        // test getSchema()
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        EnumSerializer enumSerializer = new EnumSerializer(null, null);
        // test acceptJsonFormatVisitor()
    }
}