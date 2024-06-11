
package com.fasterxml.jackson.databind.ser.std;

import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnumSerializerTest {
    
    @Mock
    private static EnumValues enumValues;
    
    @BeforeClass
    public static void setup() {
        enumValues = mock(EnumValues.class);
        // Mocking enumValues for test cases
        when(enumValues.getEnumClass()).thenReturn(TestEnum.class);
    }
    
    @Test
    public void testSerialize_When_SerializedAsIndex_Then_ReturnIndex() throws Exception {
        // Arrange
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        
        // Act
        enumSerializer.serialize(TestEnum.VALUE_A, jsonGenerator, null);
        
        // Assert
        assertTrue(enumSerializer._serializeAsIndex);
        // Verify that the index of the enum is written
        // Change this value to expected index
        verify(jsonGenerator).writeNumber(0);
    }
    
    @Test
    public void testSerialize_When_WriteEnumsUsingToString_Then_ReturnToString() throws Exception {
        // Arrange
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        
        // Act
        enumSerializer.serialize(TestEnum.VALUE_A, jsonGenerator, null);
        
        // Assert
        assertTrue(!enumSerializer._serializeAsIndex);
        // Verify that the toString value of the enum is written
        verify(jsonGenerator).writeString("VALUE_A");
    }
    
    @Test
    public void testSerialize_When_DefaultSerialization_Then_ReturnSerializedValue() throws Exception {
        // Arrange
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Act
        enumSerializer.serialize(TestEnum.VALUE_A, jsonGenerator, null);
        
        // Assert
        assertTrue(!enumSerializer._serializeAsIndex);
        // Verify that the serialized value of the enum is written
        // Change this value to expected serialized value
        verify(jsonGenerator).writeString("valueA");
    }
    
    @Test
    public void testGetSchema_When_SerializedAsIndex_Then_ReturnIntegerSchema() throws Exception {
        // Arrange
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Type typeHint = null;
        
        // Act
        JsonNode schema = enumSerializer.getSchema(serializerProvider, typeHint);
        
        // Assert
        assertTrue(enumSerializer._serializeAsIndex);
        // Verify that the schema has an integer type
        assertEquals("integer", schema.get("type").asText());
    }
    
    @Test
    public void testGetSchema_When_DefaultSerialization_Then_ReturnStringSchemaWithEnumValues() throws Exception {
        // Arrange
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Type typeHint = null;
        
        // Act
        JsonNode schema = enumSerializer.getSchema(serializerProvider, typeHint);
        
        // Assert
        assertTrue(!enumSerializer._serializeAsIndex);
        // Verify that the schema has a string type
        assertEquals("string", schema.get("type").asText());
        // Verify that the schema has expected enum values
        // Change this value to expected enum values
        assertEquals("VALUE_A", schema.get("enum").get(0).asText());
        assertEquals("VALUE_B", schema.get("enum").get(1).asText());
        assertEquals("VALUE_C", schema.get("enum").get(2).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_When_SerializedAsIndex_Then_VisitIntFormat() throws Exception {
        // Arrange
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        // Act
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        assertTrue(enumSerializer._serializeAsIndex);
        // Verify that the int format is visited
        verify(visitor).visitIntFormat(typeHint, JsonParser.NumberType.INT);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_When_DefaultSerialization_Then_VisitStringFormatWithEnumTypes() throws Exception {
        // Arrange
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(visitor.getProvider()).thenReturn(serializerProvider);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        
        // Act
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        assertTrue(!enumSerializer._serializeAsIndex);
        // Verify that the string format is visited
        verify(visitor).expectStringFormat(typeHint);
        // Verify that the enum types are visited
        verify(visitor).enumTypes(any(Set.class));
    }
    
    public enum TestEnum {
        VALUE_A,
        VALUE_B,
        VALUE_C;
    }
}
