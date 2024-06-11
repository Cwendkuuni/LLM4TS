package com.example.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test file for {@link EnumSerializer} class.
 */
public class EnumSerializerTest {

    private EnumSerializer serializer;
    private ObjectMapper mapper;
    private EnumValues enumValues;

    @Before
    public void setup() {
        enumValues = EnumValues.allValues(TestEnum.class);
        mapper = new ObjectMapper();
        serializer = new EnumSerializer(enumValues, true);
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);

        // serialize using index
        Mockito.when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        serializer.serialize(TestEnum.ONE, gen, serializers);
        Mockito.verify(gen).writeNumber(0);

        // serialize using name
        Mockito.when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        Mockito.when(_serializer -> _serializeAsIndex(serializers)).thenReturn(false);
        serializer.serialize(TestEnum.ONE, gen, serializers);
        Mockito.verify(gen).writeString("ONE");

        // serialize using custom name
        Mockito.when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        Mockito.when(_serializer -> _values.serializedValueFor(TestEnum.ONE)).thenReturn("ONE_CUSTOM");
        serializer.serialize(TestEnum.ONE, gen, serializers);
        Mockito.verify(gen).writeString("ONE_CUSTOM");
    }

    @Test
    public void testCreateContextual() {
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);

        EnumSerializer serializedSerializer = serializer.createContextual(serializers, property);
        assertTrue(serializedSerializer instanceof EnumSerializer);
        assertEquals(serializer.getEnumValues(), serializedSerializer.getEnumValues());
        assertEquals(serializer._serializeAsIndex, serializedSerializer._serializeAsIndex);

        // check when using different serializeAsIndex
        EnumSerializer otherSerializer = new EnumSerializer(enumValues, false);
        serializedSerializer = otherSerializer.createContextual(serializers, property);
        assertFalse(serializedSerializer._serializeAsIndex.booleanValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JsonParser.NumberType numberType = JsonParser.NumberType.INT;

        // visit int format
        Mockito.when(visitor.expectNumberFormat(null)).thenReturn(null);
        Mockito.when(visitor.expectIntFormat(null, numberType)).thenReturn(null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        Mockito.verify(visitor, Mockito.never()).expectStringFormat(null);

        // visit string format
        ObjectNode objectNode = Mockito.mock(ObjectNode.class);
        Mockito.when(visitor.expectStringFormat(null)).thenReturn(Mockito.argThat(node -> node instanceof JsonStringFormatVisitor));
        JsonStringFormatVisitor stringVisitor = Mockito.argumentCaptor(JsonStringFormatVisitor.class).getValue();
        Mockito.when(stringVisitor.enumTypes(Mockito.anySet())).thenReturn(null);

        serializer.acceptJsonFormatVisitor(visitor, null);
        Mockito.verify(objectNode).put("enum", Mockito.never());

        // visit int format with enumValues
        Arrays.stream(TestEnum.values()).forEach(value -> {
            Mockito.when(stringVisitor.enumTypes(Mockito.anySet())).thenReturn(Arrays.asList(value.toString()));
            ObjectNode serializedNode = Mockito.mock(ObjectNode.class);
            Mockito.when(visitor.expectObjectNode(Mockito.any())).thenReturn(serializedNode);
            serializer.acceptJsonFormatVisitor(visitor, TestEnum.class.getType());
            Mockito.verify(serializedNode, Mockito.times(TestEnum.values().length)).put("enum", Mockito.any());
        });

        // visit int format with custom toString
        Arrays.stream(TestEnum.values()).forEach(value -> {
            Mockito.when(_serializer -> _values.serializedValueFor(value)).thenReturn(value.getName());
            Mockito.when(stringVisitor.enumTypes(Mockito.anySet())).thenReturn(Arrays.asList(value.getName()));
            ObjectNode serializedNode = Mockito.mock(ObjectNode.class);
            Mockito.when(visitor.expectObjectNode(Mockito.any())).thenReturn(serializedNode);
            serializer.acceptJsonFormatVisitor(visitor, TestEnum.class.getType());
            Mockito.verify(serializedNode, Mockito.times(TestEnum.values().length)).put("enum", Mockito.any());
        });
    }

    @Test
    public void testGetSchema() {
        ObjectNode schemaNode = Mockito.mock(ObjectNode.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);

        // serialize as index
        Mockito.when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        ObjectNode schema = (ObjectNode) serializer.getSchema(serializers, null);
        assertEquals("integer", schema.get("type").asText());

        // serialize using name
        Mockito.when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        Mockito.when(_serializer -> _serializeAsIndex(serializers)).thenReturn(false);
        ObjectNode schema2 = (ObjectNode) serializer.getSchema(serializers, null);
        assertEquals("string", schema2.get("type").asText());
        assertEquals("TestEnum", schema2.get("items").get(0).asText());

        // serialize using custom name
        Mockito.when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        Mockito.when(_serializer -> _values.serializedValueFor(TestEnum.ONE)).thenReturn("ONE_CUSTOM");
        ObjectNode schema3 = (ObjectNode) serializer.getSchema(serializers, null);
        assertEquals("string", schema3.get("type").asText());
        assertEquals("ONE_CUSTOM", schema3.get("items").get(0).asText());
    }

    private static class _serializer {
        static Boolean _isShapeWrittenUsingIndex(Class<?> enumClass, JsonFormat.Value format, boolean fromClass) {
            return EnumSerializerTest.serializer._isShapeWrittenUsingIndex(enumClass, format, fromClass);
        }
    }

    enum TestEnum {
        ONE("ONE"),
        TWO("TWO"),
        THREE("THREE"),
        FOUR("FOUR");

        private final String name;

        TestEnum(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }

        public static EnumSet<TestEnum> values() {
            return EnumSet.allOf(TestEnum.class);
        }
    }
}