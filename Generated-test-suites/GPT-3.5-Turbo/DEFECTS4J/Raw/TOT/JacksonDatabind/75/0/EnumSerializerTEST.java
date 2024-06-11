
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {

    @Test
    public void testSerializeUsingIndex() throws IOException {
        EnumValues enumValues = new EnumValues(ExampleEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        
        ExampleEnum exampleEnum = ExampleEnum.VALUE1;
        
        enumSerializer.serialize(exampleEnum, gen, serializers);
        
        verify(gen).writeNumber(0);
    }
    
    @Test
    public void testSerializeUsingToString() throws IOException {
        EnumValues enumValues = new EnumValues(ExampleEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        
        ExampleEnum exampleEnum = ExampleEnum.VALUE1;
        
        enumSerializer.serialize(exampleEnum, gen, serializers);
        
        verify(gen).writeString("VALUE1");
    }
    
    @Test
    public void testSerializeUsingCustomValue() throws IOException {
        EnumValues enumValues = new EnumValues(ExampleEnumCustom.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        
        ExampleEnumCustom exampleEnum = ExampleEnumCustom.VALUE1;
        
        enumSerializer.serialize(exampleEnum, gen, serializers);
        
        verify(gen).writeString("CustomValue");
    }
    
    @Test
    public void testGetSchemaWithIndex() {
        EnumValues enumValues = new EnumValues(ExampleEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = ExampleEnum.class;
        
        JsonNode schema = enumSerializer.getSchema(provider, typeHint);
        
        assertTrue(schema.isObject());
        assertTrue(schema.has("type"));
        assertEquals("integer", schema.get("type").asText());
    }
    
    @Test
    public void testGetSchemaWithString() {
        EnumValues enumValues = new EnumValues(ExampleEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = ExampleEnum.class;
        
        JsonNode schema = enumSerializer.getSchema(provider, typeHint);
        
        assertTrue(schema.isObject());
        assertTrue(schema.has("type"));
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.has("enum"));
        assertTrue(schema.get("enum").isArray());
        assertEquals(2, schema.get("enum").size());
        assertEquals("VALUE1", schema.get("enum").get(0).asText());
        assertEquals("VALUE2", schema.get("enum").get(1).asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithIndex() throws IOException {
        EnumValues enumValues = new EnumValues(ExampleEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).getProvider();
        verify(visitor).expectIntFormat(typeHint, JsonParser.NumberType.INT);
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithString() throws IOException {
        EnumValues enumValues = new EnumValues(ExampleEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).getProvider();
        verify(visitor).expectStringFormat(typeHint);
        verify(serializers).isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedSerializationShape() {
        EnumValues enumValues = new EnumValues(ExampleEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        
        JsonFormat.Shape shape = JsonFormat.Shape.OBJECT;
        Class<?> enumClass = ExampleEnum.class;
        boolean fromClass = true;
        
        enumSerializer._isShapeWrittenUsingIndex(enumClass, new JsonFormat.Value(shape), fromClass);
    }
    
    private enum ExampleEnum {
        VALUE1, VALUE2;
    }
    
    private enum ExampleEnumCustom {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        VALUE1,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy年MM月dd日")
        VALUE2
    }

}
