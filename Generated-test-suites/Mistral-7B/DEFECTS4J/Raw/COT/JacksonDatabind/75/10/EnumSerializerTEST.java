package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.test.BaseMapTest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link EnumSerializer} class.
 */
public class EnumSerializerTest extends BaseMapTest {

    private static final EnumSerializer SERIALIZER = new EnumSerializer(new EnumValues(MyEnum.class), true);

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum MyEnum {
        VALUE_1, VALUE_2, VALUE_3;

        public String getValueForSerialization() {
            return name();
        }
    }

    

    // Test constructor
    @org.junit.Test
    public void testConstructor() {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class), null);
        assertSame(MyEnum.class, serializer.handledType());
        assertEquals(MyEnum.class, serializer.getEnumValues().getEnumClass());
        assertNull(serializer._serializeAsIndex);
    }

    // Test createContextual method
    @org.junit.Test
    public void testCreateContextual() throws JsonMappingException {
        BeanDescription beanDescription = mock(BeanDescription.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(beanDescription.getType()).thenReturn(MyEnum.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<?> serializedSerializer = SERIALIZER.createContextual(objectMapper.getSerializerProvider(), beanDescription);
        assertSame(SERIALIZER, serializedSerializer);
        assertNull(serializedSerializer._serializeAsIndex);
    }

    // Test serialize method with String format
    @org.junit.Test
    public void testSerializeWithStringFormat() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        when(jsonGenerator.writeString(anyString())).thenReturn(this);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        MyEnum myEnum = MyEnum.VALUE_1;
        SERIALIZER.serialize(myEnum, jsonGenerator, serializerProvider);
        verify(jsonGenerator, times(1)).writeString(myEnum.toString());
    }

    // Test serialize method with Index format
    @org.junit.Test
    public void testSerializeWithIndexFormat() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        when(jsonGenerator.writeNumber(anyInt())).thenReturn(this);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        MyEnum myEnum = MyEnum.VALUE_1;
        SERIALIZER.serialize(myEnum, jsonGenerator, serializerProvider);
        verify(jsonGenerator, times(1)).writeNumber(myEnum.ordinal());
    }

    // Test serialize method with null format
    @org.junit.Test
    public void testSerializeWithNullFormat() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        MyEnum myEnum = MyEnum.VALUE_1;
        SERIALIZER.serialize(myEnum, jsonGenerator, serializerProvider);
        verify(jsonGenerator, times(1)).writeString(_values.serializedValueFor(myEnum));
    }

    // Test getSchema method
    @org.junit.Test
    public void testGetSchema() {
        ObjectNode schemaNode = SERIALIZER.getSchema(mock(SerializerProvider.class), null);
        ArrayNode enumNode = schemaNode.get("enum");
        assertEquals(3, enumNode.size());
        assertSame(MyEnum.VALUE_1.name(), enumNode.get(0).asText());
        assertSame(MyEnum.VALUE_2.name(), enumNode.get(1).asText());
        assertSame(MyEnum.VALUE_3.name(), enumNode.get(2).asText());
    }

    // Test acceptJsonFormatVisitor method with String format
    @org.junit.Test
    public void testAcceptJsonFormatVisitorWithStringFormat() {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        Type typeHint = MyEnum.class;
        JsonStringFormatVisitor jsonStringFormatVisitor = jsonFormatVisitorWrapper.expectStringFormat(typeHint);
        Set<String> enums = new LinkedHashSet<>();
        enums.add(MyEnum.VALUE_1.name());
        enums.add(MyEnum.VALUE_2.name());
        enums.add(MyEnum.VALUE_3.name());
        jsonStringFormatVisitor.enumTypes(enums);
        SERIALIZER.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);
    }

    // Test acceptJsonFormatVisitor method with Index format
    @org.junit.Test
    public void testAcceptJsonFormatVisitorWithIndexFormat() {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        Type typeHint = MyEnum.class;
        JsonFormat.Shape shape = JsonFormat.Shape.ARRAY;
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(shape);
        JsonFormatVisitor jsonFormatVisitor = jsonFormatVisitorWrapper.expectArrayFormat(typeHint);
        jsonFormatVisitor.acceptEnum(MyEnum.VALUE_1);
        jsonFormatVisitor.acceptEnum(MyEnum.VALUE_2);
        jsonFormatVisitor.acceptEnum(MyEnum.VALUE_3);
        SERIALIZER.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);
    }

    // Test handleMissingSchema method
    @org.junit.Test
    public void testHandleMissingSchema() {
        JsonSerializerProvider serializerProvider = mock(JsonSerializerProvider.class);
        when(serializerProvider.findValueSerializer(MyEnum.class)).thenReturn(SERIALIZER);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<?> serializedSerializer = objectMapper.findValueSerializer(MyEnum.class, null);
        assertEquals(SERIALIZER, serializedSerializer);
    }

    // Test findFormatOverrides method
    @org.junit.Test
    public void testFindFormatOverrides() {
        BeanDescription beanDescription = mock(BeanDescription.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(beanDescription.getType()).thenReturn(MyEnum.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(objectMapper.findValueSerializer(MyEnum.class, beanDescription, null)).thenReturn(SERIALIZER);
        JsonSerializer<?> serializedSerializer = SERIALIZER.createContextual(objectMapper.getSerializerProvider(), beanDescription);
        assertSame(SERIALIZER, serializedSerializer);
    }

    
}