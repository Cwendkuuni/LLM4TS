package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class EnumSerializerTest {

    @Test
    public void testSerialize_WhenSerializeAsIndexIsTrue_ShouldWriteOrdinal() throws IOException {
        // Arrange
        EnumSerializer enumSerializer = new EnumSerializer(null, true);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        Enum<?> dummyEnum = mock(Enum.class);
    
        // Act
        enumSerializer.serialize(dummyEnum, mockJsonGenerator, mockSerializerProvider);
    
        // Assert
        verify(mockJsonGenerator, times(1)).writeNumber(dummyEnum.ordinal());
    }
    
    @Test
    public void testSerialize_WhenSerializeAsIndexIsFalseAndWriteEnumsUsingToStringIsFalse_ShouldWriteSerializedValue() throws IOException {
        // Arrange
        EnumValues enumValues = new EnumValues(null);
        enumValues.add(mock(SerializableString.class));
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        Enum<?> dummyEnum = mock(Enum.class);
    
        // Act
        enumSerializer.serialize(dummyEnum, mockJsonGenerator, mockSerializerProvider);
    
        // Assert
        verify(mockJsonGenerator, times(1)).writeString(eq(enumValues.serializedValueFor(dummyEnum)));
    }
    
    @Test
    public void testSerialize_WhenSerializeAsIndexIsFalseAndWriteEnumsUsingToStringIsTrue_ShouldWriteEnumToString() throws IOException {
        // Arrange
        EnumValues enumValues = new EnumValues(null);
        enumValues.add(mock(SerializableString.class));
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        Enum<?> dummyEnum = mock(Enum.class);
    
        // Act
        enumSerializer.serialize(dummyEnum, mockJsonGenerator, mockSerializerProvider);
    
        // Assert
        verify(mockJsonGenerator, times(1)).writeString(dummyEnum.toString());
    }
    
    @Test
    public void testGetSchema_WhenSerializeAsIndexIsTrue_ShouldReturnSchemaNodeWithIntegerType() {
        // Arrange
        EnumValues enumValues = new EnumValues(null);
        enumValues.add(mock(SerializableString.class));
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        Type dummyTypeHint = mock(Type.class);
    
        // Act
        JsonNode schema = enumSerializer.getSchema(mockSerializerProvider, dummyTypeHint);
    
        // Assert
        Assert.assertTrue(schema.isObject());
        Assert.assertEquals("integer", schema.get("type").textValue());
    }
    
    @Test
    public void testGetSchema_WhenSerializeAsIndexIsFalse_ShouldReturnSchemaNodeWithStringTypeAndEnumArray() {
        // Arrange
        EnumValues enumValues = new EnumValues(null);
        enumValues.add(mock(SerializableString.class));
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        when(mockSerializerProvider.constructType(any(Type.class))).thenReturn(mock(JavaType.class));
        when(mockJavaType.isEnumType()).thenReturn(true);
        Type dummyTypeHint = mock(Type.class);
    
        // Act
        JsonNode schema = enumSerializer.getSchema(mockSerializerProvider, dummyTypeHint);
    
        // Assert
        Assert.assertTrue(schema.isObject());
        Assert.assertEquals("string", schema.get("type").textValue());
        Assert.assertTrue(schema.get("enum").isArray());
        Assert.assertEquals(1, schema.get("enum").size());
        Assert.assertEquals(enumValues.values().iterator().next().getValue(), schema.get("enum").get(0).textValue());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenSerializeAsIndexIsTrue_ShouldCallVisitIntFormat() throws JsonMappingException {
        // Arrange
        EnumValues enumValues = new EnumValues(null);
        enumValues.add(mock(SerializableString.class));
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType mockJavaType = mock(JavaType.class);
    
        // Act
        enumSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);
    
        // Assert
        verify(mockJsonFormatVisitorWrapper, times(1)).visitIntFormat(eq(mockJavaType), eq(JsonParser.NumberType.INT));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenSerializeAsIndexIsFalseAndWriteEnumsUsingToStringIsTrue_ShouldCallExpectStringFormatWithStringSet() throws JsonMappingException {
        // Arrange
        EnumValues enumValues = new EnumValues(null);
        enumValues.add(mock(SerializableString.class));
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JsonStringFormatVisitor mockJsonStringFormatVisitor = mock(JsonStringFormatVisitor.class);
        when(mockJsonFormatVisitorWrapper.expectStringFormat(any(JavaType.class))).thenReturn(mockJsonStringFormatVisitor);
        when(mockJsonStringFormatVisitor != null).thenReturn(true);
        Set<String> enumSet = new HashSet<>();
        enumSet.add("enum");
        Enum<?> dummyEnum = mock(Enum.class);
        when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        enumValues.add(dummyEnum);
    
        // Act
        enumSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);
    
        // Assert
        verify(mockJsonStringFormatVisitor, times(1)).enumTypes(eq(enumSet));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenSerializeAsIndexIsFalseAndWriteEnumsUsingToStringIsFalse_ShouldCallExpectStringFormatWithStringSet() throws JsonMappingException {
        // Arrange
        EnumValues enumValues = new EnumValues(null);
        enumValues.add(mock(SerializableString.class));
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JsonStringFormatVisitor mockJsonStringFormatVisitor = mock(JsonStringFormatVisitor.class);
        when(mockJsonFormatVisitorWrapper.expectStringFormat(any(JavaType.class))).thenReturn(mockJsonStringFormatVisitor);
        when(mockJsonStringFormatVisitor != null).thenReturn(true);
        Set<String> enumSet = new HashSet<>();
        enumSet.add(enumValues.getValue(0).getValue());
    
        // Act
        enumSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);
    
        // Assert
        verify(mockJsonStringFormatVisitor, times(1)).enumTypes(eq(enumSet));
    }
}