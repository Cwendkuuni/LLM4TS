package com.example.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.type.ClassIdType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;

public class EnumSerializerTest extends BaseMapTest {

    @Test
    public void testEnumSerializerConstruction() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.anonymousEnumValues(MyEnumClass.class));
        Assert.assertNotNull(serializer);
    }

    @Test
    public void testEnumSerializerCreateContextual() throws Exception {
        EnumSerializer baseSerializer = new EnumSerializer(EnumValues.anonymousEnumValues(MyEnumClass.class));
        ContextualSerializer contextualSerializer = baseSerializer.createContextual(null, null);
        Assert.assertSame(baseSerializer, contextualSerializer);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("test", MyEnum.VALUE_1);
        BeanDescription beanDescription = new BeanDescription(MyEnum.class, node);
        ContextualSerializer contextualSerializer2 = baseSerializer.createContextual(null, beanDescription);
        Assert.assertNotSame(baseSerializer, contextualSerializer2);
    }

    @Test
    public void testSerializeAsIndex() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        EnumSerializer serializer1 = new EnumSerializer(EnumValues.anonymousEnumValues(MyEnumClass.class), true);
        EnumSerializer serializer2 = new EnumSerializer(EnumValues.anonymousEnumValues(MyEnumClass.class), false);

        JsonGenerator gen = createJsonGenerator();

        serialize(serializer1, gen, serializerProvider, MyEnum.VALUE_1);
        Assert.assertEquals("0", gen.getOutputBuffer().toString());

        gen.close();

        serialize(serializer2, gen, serializerProvider, MyEnum.VALUE_1);
        Assert.assertEquals("VALUE_1", gen.getOutputBuffer().toString());

        gen.close();
    }

    @Test
    public void testSerializeToString() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        EnumSerializer serializer = new EnumSerializer(EnumValues.anonymousEnumValues(MyEnumClass.class), false);

        JsonGenerator gen = createJsonGenerator();

        ObjectMapper objectMapper2 = new ObjectMapper();
        ObjectMapper.DefaultSerializerProvider serializerProvider2 = objectMapper2.getDefaultSerializerProvider();
        JsonGenerator gen2 = createJsonGenerator();

        JsonFormat format = new JsonFormat(Shape.STRING, null);

        ObjectNode node = objectMapper2.createObjectNode();
        node.put("test", MyEnum.VALUE_1);
        BeanDescription beanDescription = new BeanDescription(MyEnum.class, node);
        EnumSerializer serializer2 = serializer.createContextual(null, beanDescription);

        // Test serialization with default settings
        serialize(serializer, gen, serializerProvider, MyEnum.VALUE_1);
        Assert.assertEquals("VALUE_1", gen.getOutputBuffer().toString());

        gen.close();

        // Test serialization with custom JsonFormat
        serialize(serializer2, gen2, serializerProvider2, MyEnum.VALUE_1, format);
        Assert.assertEquals("\"VALUE_1\"", gen2.getOutputBuffer().toString());

        gen2.close();
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.anonymousEnumValues(MyEnumClass.class), false);

        JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), MyEnum.class);
        Assert.assertEquals("string", schema.getNodeType().getClass().getName());
        ObjectNode schemaObject = (ObjectNode) schema;
        ArrayNode enumNode = schemaObject.get("enum");
        Assert.assertEquals(2, enumNode.size());
        Assert.assertEquals("VALUE_1", enumNode.get(0).asText());
        Assert.assertEquals("VALUE_2", enumNode.get(1).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.anonymousEnumValues(MyEnumClass.class), false);

        SimpleModule module = new SimpleModule();
        module.addSerializer(MyEnum.class, serializer);
        ObjectMapper objectMapper2 = objectMapper.copyBuilder().registerModule(module).build();

        JsonFormatVisitorWrapper visitor = objectMapper2.schemaFactory().getSchemaProvider()
                .findSchemaProvider(MyEnum.class)
                .acceptJsonFormatVisitor("schema", new ClassIdType(MyEnum.class, JsonType.NAME));

        ArrayNode enumNode = visitor.getSchema().get("enum");
        Assert.assertEquals(2, enumNode.size());
        Assert.assertEquals("VALUE_1", enumNode.get(0).asText());
        Assert.assertEquals("VALUE_2", enumNode.get(1).asText());
    }

    @Test
    public void testSerializeNull() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        EnumSerializer serializer = new EnumSerializer(EnumValues.anonymousEnumValues(MyEnumClass.class), false);

        JsonGenerator gen = createJsonGenerator();

        serialize(serializer, gen, serializerProvider, null);
        Assert.assertEquals("null", gen.getOutputBuffer().toString());

        gen.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeInvalidEnumValue() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        EnumSerializer serializer = new EnumSerializer(EnumValues.anonymousEnumValues(MyEnumClass.class), false);

        JsonGenerator gen = createJsonGenerator();

        // Test serialization with valid enum value
        serialize(serializer, gen, serializerProvider, MyEnum.VALUE_1);
        gen.close();

        // Test serialization with invalid enum value
        MyEnum invalidEnumValue = new MyEnum("INVALID_VALUE") {
            @Override
            public String toString() {
                return "INVALID_VALUE_STRING";
            }
        };

        try {
            serialize(serializer, gen, serializerProvider, invalidEnumValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        gen.close();
    }

    private void serialize(EnumSerializer serializer, JsonGenerator gen, SerializerProvider serializerProvider,
                          Enum<?> enumValue, JsonFormat format) throws IOException {
        serializer.serialize(enumValue, gen, serializerProvider);
        if (format != null) {
            serializer.acceptJsonFormatVisitor(gen, new ClassIdType(enumValue.getClass(), JsonType.NAME), format);
        }
    }

    @JsonFormat(shape = Shape.STRING)
    private static enum MyEnum {
        VALUE_1,
        VALUE_2
    }
}