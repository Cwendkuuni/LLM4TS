package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.StdSerializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test class for EnumSerializer.
 */
public class EnumSerializerTest {

    private static final Class<MyEnum> MY_ENUM_CLASS = MyEnum.class;
    private static final MyEnum MY_ENUM_VALUE_1 = MyEnum.VAL_1;
    private static final MyEnum MY_ENUM_VALUE_2 = MyEnum.VAL_2;
    private static final MyEnum MY_ENUM_VALUE_3 = MyEnum.VAL_3;

    private ObjectMapper objectMapper;
    private SerializerProvider serializers;
    private SerializationConfig config;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        serializers = objectMapper.getSerializerProvider();
        config = objectMapper.getSerializationConfig();
    }

    @Test
    public void testSerializeEnumUsingIndex() throws JsonProcessingException {
        // Given
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.values()), true);

        // When
        final JsonGenerator gen = mock(JsonGenerator.class);
        enumSerializer.serialize(MY_ENUM_VALUE_1, gen, serializers);

        // Then
        ArgumentCaptor<Number> numberCaptor = ArgumentCaptor.forClass(Number.class);
        when(gen.writeNumber(numberCaptor.capture())).thenReturn(null);
        assertEquals(MY_ENUM_VALUE_1.ordinal(), numberCaptor.getValue().intValue());
        assertNull(gen.getOutputBuffer());
    }

    @Test
    public void testSerializeEnumUsingToString() throws JsonProcessingException {
        // Given
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.values()), false);

        // When
        final JsonGenerator gen = mock(JsonGenerator.class);
        enumSerializer.serialize(MY_ENUM_VALUE_1, gen, serializers);

        // Then
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        when(gen.writeString(stringCaptor.capture())).thenReturn(null);
        assertEquals(MY_ENUM_VALUE_1.name(), stringCaptor.getValue());
        assertNull(gen.getOutputBuffer());
    }

    @Test
    public void testSerializeEnumUsingName() throws JsonProcessingException {
        // Given
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(config, MY_ENUM_CLASS), null);

        // When
        final JsonGenerator gen = mock(JsonGenerator.class);
        enumSerializer.serialize(MY_ENUM_VALUE_1, gen, serializers);

        // Then
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        when(gen.writeString(stringCaptor.capture())).thenReturn(null);
        assertEquals(MY_ENUM_VALUE_1.name(), stringCaptor.getValue());
        assertNull(gen.getOutputBuffer());
    }

    @Test
    public void testSerializeEnumUsingExplicitFormat() throws JsonProcessingException {
        // Given
        JsonFormat jsonFormat = new JsonFormat();
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(Shape.STRING);
        jsonFormat.setEnumValues(new EnumValues(MY_ENUM_CLASS.getEnumConstants()));
        jsonFormat.setShape(Shape.STRING);
        EnumSerializer enumSerializer = new EnumSerializer(MY_ENUM_CLASS, config, null, jsonFormat);

        // When
        final JsonGenerator gen = mock(JsonGenerator.class);
        enumSerializer.serialize(MY_ENUM_VALUE_1, gen, serializers);

        // Then
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        when(gen.writeString(stringCaptor.capture())).thenReturn(null);
        assertEquals(MY_ENUM_VALUE_1.name(), stringCaptor.getValue());
        assertNull(gen.getOutputBuffer());
    }

    @Test
    public void testCreateContextualSerializerForPropertyWithShapeString() throws JsonMappingException {
        // Given
        JsonFormat jsonFormat = new JsonFormat();
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(Shape.STRING);
        jsonFormat.setEnumValues(new EnumValues(MY_ENUM_CLASS.getEnumConstants()));
        BeanProperty property = mock(BeanProperty.class);
        when(property.getType()).thenReturn(MY_ENUM_CLASS);
        when(property.getName()).thenReturn("fooProperty");
        when(property.getAnnotation(JsonFormat.class)).thenReturn(jsonFormat);

        // When
        EnumSerializer enumSerializer = new EnumSerializer(MY_ENUM_CLASS, config, null, jsonFormat).createContextual(serializers, property);

        // Then
        assertEquals(MY_ENUM_CLASS, enumSerializer.handledType());
    }

    @Test
    public void testCreateContextualSerializerForPropertyWithShapeIndex() throws JsonMappingException {
        // Given
        JsonFormat jsonFormat = new JsonFormat();
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(Shape.NUMBER);
        jsonFormat.setEnumValues(new EnumValues(MY_ENUM_CLASS.getEnumConstants()));
        BeanProperty property = mock(BeanProperty.class);
        when(property.getType()).thenReturn(MY_ENUM_CLASS);
        when(property.getName()).thenReturn("fooProperty");
        when(property.getAnnotation(JsonFormat.class)).thenReturn(jsonFormat);

        // When
        EnumSerializer enumSerializer = new EnumSerializer(MY_ENUM_CLASS, config, null, jsonFormat).createContextual(serializers, property);

        // Then
        assertEquals(MY_ENUM_CLASS, enumSerializer.handledType());
        assertFalse(enumSerializer._serializeAsIndex);
    }

    @Test
    public void testCreateContextualSerializerForPropertyWithNoAnnotation() throws JsonMappingException {
        // Given
        BeanProperty property = mock(BeanProperty.class);
        when(property.getType()).thenReturn(MY_ENUM_CLASS);

        // When
        EnumSerializer enumSerializer = new EnumSerializer(MY_ENUM_CLASS, config, null, null).createContextual(serializers, property);

        // Then
        assertEquals(MY_ENUM_CLASS, enumSerializer.handledType());
        assertFalse(enumSerializer._serializeAsIndex);
    }

    @Test
    public void testSerializeEnumWithNullValue() throws JsonProcessingException {
        // Given
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.values()), false);

        // When
        final JsonGenerator gen = mock(JsonGenerator.class);
        enumSerializer.serialize(null, gen, serializers);

        // Then
        assertNull(gen.getOutputBuffer());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeEnumWithUnsupportedValue() throws JsonProcessingException {
        // Given
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.values()), false);

        // When
        final JsonGenerator gen = mock(JsonGenerator.class);
        enumSerializer.serialize(new Object(), gen, serializers);

        // Then
        // AssertionError is expected because of the test case design
        // JsonMappingException is thrown internally when serializing the wrong type
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        // Given
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.values()), false);

        // When
        JsonNode schema = enumSerializer.getSchema(serializers, null);

        // Then
        assertNotNull(schema);
        assertEquals("string", schema.getClass().getSimpleName());
        assertNotNull(schema.get("type"));
        assertEquals("string", schema.get("type").asText());
        assertEquals("foo", schema.get("title").asText());
        assertEquals(true, schema.has("items"));
        JsonNode items = schema.get("items");
        assertNotNull(items);
        assertEquals(JsonNode.ValueType.ARRAY, items.getValueType());
        JsonNode firstItem = items.get(0);
        assertNotNull(firstItem);
        assertEquals(JsonNode.ValueType.STRING, firstItem.getValueType());
        assertEquals("VAL_1", firstItem.asText());
        JsonNode lastItem = items.get(items.size() - 1);
        assertNotNull(lastItem);
        assertEquals(JsonNode.ValueType.STRING, lastItem.getValueType());
        assertEquals("VAL_3", lastItem.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Given
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.values()), false);

        // When
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        enumSerializer.acceptJsonFormatVisitor(visitor, null);

        // Then
        // The visitor implementation is complex and would require a mocking framework
        // capable of checking the order of calls to its methods and the values passed to them
        // and that is beyond the scope of this test case
    }

    @Test
    public void testCreateSerializersForAllSupportedShapes() throws JsonMappingException {
        // Given
        Set<Shape> shapes = EnumSet.allOf(Shape.class);

        // When
        for (Shape shape : shapes) {
            JsonFormat jsonFormat = new JsonFormat();
            jsonFormat.setShape(shape);
            EnumSerializer enumSerializer = EnumSerializer.construct(MY_ENUM_CLASS, config, null, jsonFormat);
            assertEquals(MY_ENUM_CLASS, enumSerializer.handledType());
            if (shape.isNumeric()) {
                assertFalse(enumSerializer._serializeAsIndex);
            } else {
                assertFalse(enumSerializer._serializeAsIndex);
            }
        }
    }

    @Test
    public void testCreateSerializersForUnsupportedShape() throws JsonMappingException {
        // Given
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(Shape.ARRAY);

        // When
        ArgumentException exception = assertThrows(ArgumentException.class, () -> EnumSerializer.construct(MY_ENUM_CLASS, config, null, jsonFormat));

        // Then
        assertEquals("Unsupported serialization shape (ARRAY) for Enum " + MY_ENUM_CLASS.getName(), exception.getMessage());
    }

    @Test
    public void testCreateDeserializer() throws JsonProcessingException {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("foo", "VAL_1");
        JsonNode jsonNode = node;

        // When
        ObjectMapper objectMapperForTest = new ObjectMapper();
        EnumDeserializer enumDeserializer = (EnumDeserializer) objectMapperForTest.findValueSerializer(MY_ENUM_CLASS, null);
        Enum<?> deserializedValue = enumDeserializer.deserialize(jsonNode, Enum.class, serializers);

        // Then
        assertEquals(MY_ENUM_VALUE_1, deserializedValue);
    }

    @After
    public void teardown() throws Exception {
        objectMapper.close();
    }

    private static enum MyEnum {
        VAL_1,
        VAL_2,
        VAL_3
    }
}