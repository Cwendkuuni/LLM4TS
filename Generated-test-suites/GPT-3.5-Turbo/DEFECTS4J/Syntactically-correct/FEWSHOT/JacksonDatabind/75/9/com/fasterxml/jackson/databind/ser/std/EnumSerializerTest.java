package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.EnumSerializerTest.SampleEnum;

public class EnumSerializerTest {
    
    private EnumSerializer enumSerializer;
    private EnumValues enumValues;
    private Boolean serializeAsIndex;
    
    @Before
    public void setUp() {
        enumValues = mock(EnumValues.class);
        serializeAsIndex = Boolean.TRUE;
        enumSerializer = new EnumSerializer(enumValues, serializeAsIndex);
    }
    
    @Test
    public void testSerializeAsIndex() throws IOException {
        Enum<SampleEnum> sampleEnum = SampleEnum.ONE;
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        
        enumSerializer.serialize(sampleEnum, jsonGenerator, serializerProvider);
        
        // Verify that writeNumber() was called
        verify(jsonGenerator).writeNumber(mockEnum.ordinal());
    }
    
    @Test
    public void testSerializeAsString() throws IOException {
        Enum<SampleEnum> sampleEnum = SampleEnum.TWO;
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        
        enumSerializer.serialize(sampleEnum, jsonGenerator, serializerProvider);
        
        // Verify that writeString() was called
        verify(jsonGenerator).writeString("TWO");
    }
    
    @Test
    public void testSerializeUsingToString() throws IOException {
        Enum<SampleEnum> sampleEnum = SampleEnum.THREE;
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        
        enumSerializer.serialize(sampleEnum, jsonGenerator, serializerProvider);
        
        // Verify that writeString() was called
        verify(jsonGenerator).writeString("THREE");
    }
    
    @Test
    public void testGetSchemaAsIndex() {
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        
        enumSerializer.getSchema(serializerProvider, typeHint);
        
        // Verify that createSchemaNode() was called with type "integer"
        verify(enumSerializer).createSchemaNode("integer", true);
    }
    
    @Test
    public void testGetSchemaAsString() {
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);
        ObjectNode objectNode = mock(ObjectNode.class);
        when(enumSerializer.createSchemaNode("string", true)).thenReturn(objectNode);
        when(typeHint.isEnumType()).thenReturn(true);
        ArrayNode enumNode = mock(ArrayNode.class);
        when(objectNode.putArray("enum")).thenReturn(enumNode);
        
        Enum<SampleEnum> sampleEnum = SampleEnum.THREE;
        when(enumValues.values()).thenReturn(new EnumValues[]{sampleEnum});
        
        enumSerializer.getSchema(serializerProvider, typeHint);
        
        // Verify that createSchemaNode() was called with type "string"
        verify(enumSerializer).createSchemaNode("string", true);
        
        // Verify that getValues() was called
        verify(enumValues).values();
        
        // Verify that add() was called on enumNode
        verify(enumNode).add("THREE");
    }
    
    @Test
    public void testAcceptJsonFormatVisitorAsIndex() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(enumSerializer._serializeAsIndex(serializers)).thenReturn(true);
        
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Verify that visitIntFormat() was called
        verify(visitor).visitIntFormat(typeHint, JsonParser.NumberType.INT);
    }
    
    @Test
    public void testAcceptJsonFormatVisitorAsString() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(enumSerializer._serializeAsIndex(serializers)).thenReturn(false);
        
        JsonStringFormatVisitor stringVisitor = mock(JsonStringFormatVisitor.class);
        when(visitor.expectStringFormat(typeHint)).thenReturn(stringVisitor);
        
        when(serializationFeature.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        Enum<SampleEnum> sampleEnum = SampleEnum.THREE;
        when(enumValues.values()).thenReturn(sampleEnum);
        
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Verify that expectStringFormat() was called
        verify(visitor).expectStringFormat(typeHint);
        
        // Verify that add() was called on enumNode
        verify(stringVisitor).enumTypes("THREE");
    }
}