package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.Serializers.Base;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {
    @Test
    public void testConstruct() {
        Class<?> enumClass = EnumClass.class;
        SerializationConfig serializationConfig = Mockito.mock(SerializationConfig.class);
        Boolean serializeAsIndex = Boolean.TRUE;

        EnumSerializer enumSerializer = EnumSerializer.construct(enumClass, serializationConfig, null, null);

        assertEquals(EnumValues.constructFromName(serializationConfig, (Class<Enum<?>>) enumClass), enumSerializer.getEnumValues());
        assertTrue(enumSerializer._serializeAsIndex);
    }

    @Test
    public void testConstructWithoutSerializeAsIndex() {
        Class<?> enumClass = EnumClass.class;
        SerializationConfig serializationConfig = Mockito.mock(SerializationConfig.class);
        EnumValues enumValues = EnumValues.constructFromName(serializationConfig, (Class<Enum<?>>) enumClass);

        EnumSerializer enumSerializer = new EnumSerializer(enumValues, null);

        assertFalse(enumSerializer._serializeAsIndex);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanProperty beanProperty = Mockito.mock(BeanProperty.class);

        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(serializerProvider.getConfig(),
                EnumClass.class), false);

        Mockito.when(beanProperty.getType().getRawClass()).thenReturn(EnumClass.class);

        EnumSerializer newEnumSerializer = (EnumSerializer) enumSerializer.createContextual(serializerProvider,
                beanProperty);

        assertEquals(enumSerializer, newEnumSerializer);
    }

    @Test
    public void testSerializeAsIndex() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(Mockito.mock(SerializationConfig.class),
                EnumClass.class), true);
        EnumClass enumClass = EnumClass.ONE;
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        enumSerializer.serialize(enumClass, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(enumClass.ordinal());
    }

    @Test
    public void testSerializeAsString() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(Mockito.mock(SerializationConfig.class),
                EnumClass.class), false);
        EnumClass enumClass = EnumClass.ONE;
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        Mockito.when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);

        enumSerializer.serialize(enumClass, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeString(enumClass.toString());
    }

    @Test
    public void testSerialize() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(Mockito.mock(SerializationConfig.class),
                EnumClass.class), false);
        EnumClass enumClass = EnumClass.ONE;
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        enumSerializer.serialize(enumClass, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeString(enumClass.name());
    }

    @Test
    public void testGetSchema() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(Mockito.mock(SerializationConfig.class),
                EnumClass.class), false);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        Type typeHint = EnumClass.class;

        ObjectNode expectedNode = new ObjectNode(JsonNodeFactory.instance);
        expectedNode.put("type", "string");
        ArrayNode enumNode = expectedNode.putArray("enum");
        enumNode.add("ONE");
        enumNode.add("TWO");
        enumNode.add("THREE");

        ObjectNode node = (ObjectNode) enumSerializer.getSchema(serializerProvider, typeHint);

        assertEquals(expectedNode, node);
    }

    @Test
    public void testGetSchemaWithSerializeAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(Mockito.mock(SerializationConfig.class),
                EnumClass.class), true);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        Type typeHint = EnumClass.class;

        ObjectNode expectedNode = new ObjectNode(JsonNodeFactory.instance);
        expectedNode.put("type", "integer");

        ObjectNode node = (ObjectNode) enumSerializer.getSchema(serializerProvider, typeHint);

        assertEquals(expectedNode, node);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithStringVisitor() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(Mockito.mock(SerializationConfig.class),
                EnumClass.class), false);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        JsonStringFormatVisitor stringVisitor = Mockito.mock(JsonStringFormatVisitor.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);

        Mockito.when(visitor.expectStringFormat(Mockito.any(JavaType.class))).thenReturn(stringVisitor);

        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        Set<String> expectedEnums = new LinkedHashSet<String>();
        expectedEnums.add("ONE");
        expectedEnums.add("TWO");
        expectedEnums.add("THREE");

        Mockito.verify(stringVisitor).enumTypes(expectedEnums);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithNullStringVisitor() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(Mockito.mock(SerializationConfig.class),
                EnumClass.class), false);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);

        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(visitor).getProvider();
        Mockito.verify(serializers).isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsShapeWrittenUsingIndexWithInvalidShape() {
        EnumSerializer._isShapeWrittenUsingIndex(EnumClass.class, new Value(), true);
    }
}

enum EnumClass {
    ONE, TWO, THREE
}