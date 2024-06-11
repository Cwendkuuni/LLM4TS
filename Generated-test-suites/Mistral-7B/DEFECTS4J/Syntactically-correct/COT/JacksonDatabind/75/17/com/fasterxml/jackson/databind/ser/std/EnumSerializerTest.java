package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.databind.util.TestBase;

public class EnumSerializerTest extends TestBase {
    // Test helper class
    private static class MyEnum extends Enum<MyEnum> {
        MyEnum(String name, int ordinal) {
            super(name);
        }

        public static MyEnum[] values() {
            return EnumSet.allOf(MyEnum.class);
        }

        @Override
        public String toString() {
            return "MyEnum_" + name();
        }
    }

    private static final Class<EnumSerializer> CLASS = EnumSerializer.class;
    private static final EnumSerializer SERIALIZER = new EnumSerializer(new EnumValues(MyEnum.class), null);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @org.junit.BeforeClass
    public static void setup() throws Exception {
        MAPPER.registerSerializer(MyEnum.class, SERIALIZER);
    }

    

    @org.junit.Test
    public void testSerialize() throws JsonProcessingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.getActiveContext()).thenReturn(new Object());

        Enum<?> enumValue = MyEnum.VAL1;
        SERIALIZER.serialize(enumValue, gen, serializers);

        verify(gen).writeString(_SERIALIZER._values.serializedValueFor(enumValue));
    }

    @org.junit.Test
    public void testSerializeAsIndex() throws JsonProcessingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        when(serializers.getActiveContext()).thenReturn(new Object());

        Enum<?> enumValue = MyEnum.VAL1;
        SERIALIZER.serialize(enumValue, gen, serializers);

        verify(gen).writeNumber(enumValue.ordinal());
    }

    @org.junit.Test
    public void testSerializeToString() throws JsonProcessingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        when(serializers.getActiveContext()).thenReturn(new Object());

        Enum<?> enumValue = MyEnum.VAL1;
        SERIALIZER.serialize(enumValue, gen, serializers);

        verify(gen).writeString(enumValue.toString());
    }

    @org.junit.Test
    public void testConstruct() throws JsonMappingException, IOException {
        Class<?> enumClass = MyEnum.class;
        BeanDescription beanDesc = mock(BeanDescription.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        JsonSerializer<Enum<?>> serializer = EnumSerializer.construct(enumClass, null, beanDesc, format);
        assertEquals(SERIALIZER, serializer);
    }

    @org.junit.Test
    public void testCreateContextual() throws JsonMappingException, IOException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(Shape.STRING);

        JsonSerializer<Enum<?>> serialized = new EnumSerializer(new EnumValues(MyEnum.class), null);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

        JsonSerializer<Enum<?>> contextual = serialized.createContextual(serializers, property);
        assertEquals(SERIALIZER, contextual);

        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        contextual = serialized.createContextual(serializers, property);
        assertNull(contextual);
    }

    @org.junit.Test
    public void testGetSchema() throws JsonMappingException, IOException {
        SerializerProvider serializers = mock(SerializerProvider.class);

        JsonNode schema = SERIALIZER.getSchema(serializers, null);
        assertEquals("string", schema.getClass().getSimpleName());

        JsonNode schemaWithValues = SERIALIZER.getSchema(serializers, MyEnum.class);
        assertEquals("array", schemaWithValues.get(0).getClass().getSimpleName());
        assertArrayEquals(MyEnum.values(), schemaWithValues.get(0).get(0).asTextArray());
    }

    @org.junit.Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = MyEnum.class;

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JsonSerializer<Enum<?>> serializer = SERIALIZER;

        visitor.expectStringFormat(typeHint).andReturn(mock(JsonStringFormatVisitor.class));

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).enumTypes(Arrays.asList("MyEnum_VAL0", "MyEnum_VAL1", "MyEnum_VAL2"));
    }

    @org.junit.Test
    public void testIsShapeWrittenUsingIndex() {
        Boolean result = EnumSerializer._isShapeWrittenUsingIndex(MyEnum.class, null, false);
        assertNull(result);

        result = EnumSerializer._isShapeWrittenUsingIndex(MyEnum.class, null, true);
        assertNull(result);

        result = EnumSerializer._isShapeWrittenUsingIndex(MyEnum.class, mock(JsonFormat.Value.class), false);
        assertNull(result);

        result = EnumSerializer._isShapeWrittenUsingIndex(MyEnum.class, mock(JsonFormat.Value.class), true);
        assertTrue(result);
    }

    
}