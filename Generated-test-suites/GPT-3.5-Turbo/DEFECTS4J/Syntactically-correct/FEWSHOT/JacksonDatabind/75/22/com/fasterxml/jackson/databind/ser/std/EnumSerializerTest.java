package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {
    private EnumValues enumValues;
    private Boolean serializeAsIndex;
    
    public void setUp() {
        enumValues = mock(EnumValues.class);
        serializeAsIndex = mock(Boolean.class);
    }

    @Test
    public void testConstruct() {
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, serializeAsIndex);
        assertNotNull(enumSerializer);
        assertEquals(enumValues, enumSerializer.getEnumValues());
        assertEquals(serializeAsIndex, enumSerializer._serializeAsIndex);
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        SerializerProvider property = mock(SerializerProvider.class);
        BeanProperty beanProperty = mock(BeanProperty.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, serializeAsIndex);

        when(beanProperty.getType().getRawClass()).thenReturn(String.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        when(property.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        when(enumValues.enums()).thenReturn(mock(Set.class));
        when(enumValues.values()).thenReturn(mock(Set.class));
      
        JsonSerializer<?> returnedSerializer = enumSerializer.createContextual(serializers, beanProperty);
        assertFalse(returnedSerializer instanceof EnumSerializer);
        
        when(property.getType().getRawClass()).thenReturn(String.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        when(property.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        when(enumValues.enums()).thenReturn(mock(Set.class));
        when(enumValues.values()).thenReturn(mock(Set.class));
        
        returnedSerializer = enumSerializer.createContextual(serializers, beanProperty);
        assertTrue(returnedSerializer instanceof EnumSerializer);
    }

    @Test
    public void testSerializeAsIndex() throws JsonProcessingException, IOException {
        Enum<?> en = mock(Enum.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
      
        enumSerializer.serialize(en, gen, serializers);
        verify(gen, times(1)).writeNumber(Mockito.anyInt());
    }
    
    @Test
    public void testSerializeUsingToString() throws JsonProcessingException, IOException {
        Enum<?> en = mock(Enum.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        
        when(enumSerializer._values.serializedValueFor(en)).thenReturn("Some Enum");
      
        enumSerializer.serialize(en, gen, serializers);
        verify(gen, times(1)).writeString(Mockito.anyString());
    }
    
    @Test
    public void testSerializeUsingSerializedValueFor() throws JsonProcessingException, IOException {
        Enum<?> en = mock(Enum.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        
        when(enumSerializer._values.serializedValueFor(en)).thenReturn("Some Enum");
      
        enumSerializer.serialize(en, gen, serializers);
        verify(gen, times(1)).writeString(Mockito.anyString());
    }
    
    @Test
    public void testGetSchema() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, serializeAsIndex);
        ObjectNode objectNode = mock(ObjectNode.class);
        
        when(enumSerializer._serializeAsIndex(Mockito.any(SerializerProvider.class))).thenReturn(true);
        when(enumSerializer.createSchemaNode(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(objectNode);
        
        JsonNode jsonNode = enumSerializer.getSchema(provider, typeHint);
        verify(objectNode, times(1)).putArray(Mockito.anyString());
        assertTrue(jsonNode.isObject());
        
        when(enumSerializer._serializeAsIndex(Mockito.any(SerializerProvider.class))).thenReturn(false);
        when(typeHint.isEnumType()).thenReturn(true);
        when(enumValues.values()).thenReturn(mock(Set.class));
        when(enumSerializer.createSchemaNode(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(objectNode);
        
        jsonNode = enumSerializer.getSchema(provider, typeHint);
        verify(objectNode, times(1)).putArray(Mockito.anyString());
        assertTrue(jsonNode.isObject());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonStringFormatVisitor stringVisitor = mock(JsonStringFormatVisitor.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, serializeAsIndex);
        Set<String> enums = mock(LinkedHashSet.class);
        
        when(enumSerializer._serializeAsIndex(Mockito.any(SerializerProvider.class))).thenReturn(true);
        when(enumSerializer.visitIntFormat(visitor, typeHint, JsonParser.NumberType.INT)).thenReturn(visitor);
        
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor, times(1)).getProvider();
        verify(visitor, times(1)).expectStringFormat(typeHint);
        
        when(stringVisitor.getProvider()).thenReturn(serializers);
        when(stringVisitor.isEnabled(Mockito.any(SerializationFeature.class))).thenReturn(true);
        when(serializers.isEnabled(Mockito.any(SerializationFeature.class))).thenReturn(true);
        when(enumValues.enums()).thenReturn(mock(Set.class));
        when(enumSerializer._values.values()).thenReturn(enums);
        
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor, times(1)).expectStringFormat(typeHint);
        verify(stringVisitor, times(1)).enumTypes(Mockito.any(Set.class));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedSerializationShape() {
        Class<?> enumClass = mock(Class.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, serializeAsIndex);
        
        when(format.getShape()).thenReturn(JsonFormat.Shape.ARRAY);
        when(enumClass.getName()).thenReturn("MockEnum");
        enumSerializer._isShapeWrittenUsingIndex(enumClass, JsonFormat.Value.format, true);
    }
}