package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Test;

import static org.junit.Assert.*;

@JacksonStdImpl
public class EnumSerializerTest {

    @Test
    public void testSerialize() {
        EnumValues enumValues = EnumValues.constructFromName(null, SomeEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        SomeEnum someEnum = SomeEnum.FIRST;
        ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance);

        try {
            enumSerializer.serialize(someEnum, Mockito.mock(JsonGenerator.class), Mockito.mock(SerializerProvider.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertFalse(enumSerializer._serializeAsIndex(Mockito.mock(SerializerProvider.class)));
    }

    @Test
    public void testGetSchema() {
        EnumValues enumValues = EnumValues.constructFromName(null, SomeEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        JsonSerializer provider = Mockito.mock(JsonSerializer.class);
        Type typeHint = SomeEnum.class;

        JsonNode jsonNode = enumSerializer.getSchema(Mockito.mock(SerializerProvider.class), typeHint);

        assertEquals("integer", jsonNode.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        EnumValues enumValues = EnumValues.constructFromName(null, SomeEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        try {
            enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }

        Mockito.verify(visitor, Mockito.times(1)).getProvider();
    }
}