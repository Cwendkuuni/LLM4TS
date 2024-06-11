package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class EnumSerializerTest {

    @Test
    public void testSerialize() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(
                null, TestEnum.class), false);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider serializers = new DefaultSerializerProvider.Impl();
        serializer.serialize(TestEnum.FOO, gen, serializers);
        gen.flush();
    }

    @Test
    public void testCreateContextual() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(
                null, TestEnum.class), false);
        SerializerProvider serializers = new DefaultSerializerProvider.Impl();
        assertEquals(serializer, serializer.createContextual(serializers, null));
    }

    @Test
    public void testGetEnumValues() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(
                null, TestEnum.class), false);
        assertEquals(EnumValues.constructFromName(null, TestEnum.class),
                serializer.getEnumValues());
    }

    @Test
    public void testGetSchema() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(
                null, TestEnum.class), false);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        ObjectNode ObjectNode = new ObjectNode(JsonNodeFactory.instance);
        ArrayNode enumNode = ObjectNode.putArray("enum");
        enumNode.add("A");
        enumNode.add("B");
        assertEquals(ObjectNode, serializer.getSchema(provider, TestEnum.class));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(
                null, TestEnum.class), false);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        JsonFormatVisitorWrapper visitor = provider.getSerializerFactory()
                .visitorProvider().getProvider(TestEnum.class, null);
        seralizer.acceptJsonFormatVisitor(visitor, TestEnum.class);
    }

    @Test
    public void testSerializeAsIndex() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(
                null, TestEnum.class), false);
        SerializerProvider serializers = new DefaultSerializerProvider.Impl();
        assertFalse(serializer._serializeAsIndex(serializers));
    }

    @Test
    public void test_isShapeWrittenUsingIndex() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(
                null, TestEnum.class), false);
        assertEquals(Boolean.FALSE, serializer._isShapeWrittenUsingIndex(
                TestEnum.class, new JsonFormat.Value(), true));
    }

    public static enum TestEnum {
        FOO,
        BAR
    }

}