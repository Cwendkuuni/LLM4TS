package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Test cases for EnumSerializer.
 */
public class EnumSerializerTest {

    @Test
    public void testSerializeAsIndex() throws IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.construct(Integer.class), true);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        serializer.serialize(Enum.valueOf(Integer.class, "ONE"), gen, null);
        gen.flush();

        assertEquals(1, gen.getOutputContent().readValueAs(Integer.TYPE));
    }

    @Test
    public void testSerializeUsingToString() throws IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.construct(MyEnum.class), false);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        serializer.serialize(MyEnum.VALUE_1, gen, null);
        gen.flush();

        assertEquals("VALUE_1", gen.getOutputContent().readValueAs(String.class));
    }

    @Test
    public void testSerializeUsingName() throws IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.construct(MyEnum.class), false);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        ObjectNode node = mapper.createObjectNode();
        node.put("findValuesByAnnotation", serializer);
        ObjectNode enumNode = node.putArray("enum");
        enumNode.add(node.objectNode().putPOJO("value", MyEnum.VALUE_1));
        mapper.writeValueAsString(node);
        String json = "{\"findValuesByAnnotation\":{\"class\":\"com.example.MyEnum\",\"enum\":[{\"value\":\"VALUE_1\"}]}}";

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode rootNode = parser.readValueAsTree();
        ObjectNode nodeFromJson = (ObjectNode) rootNode.path("findValuesByAnnotation");
        EnumSerializer serializedSerializer = (EnumSerializer) nodeFromJson.get("findValuesByAnnotation");
        assertEquals("VALUE_1", serializedSerializer.serialize(MyEnum.VALUE_1, null, null).getValueAsString());
    }

    @Test
    public void testSerializeUsingAnnotation() throws IOException {
        JsonFormat annotation = new JsonFormat();
        annotation.setShape(JsonFormat.Shape.INT);
        EnumSerializer serializer = new EnumSerializer(EnumValues.construct(MyEnum.class), false).withValueHandler(new EnumValueHandler());

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("findValuesByAnnotation", serializer);
        JsonSerializer<ObjectNode> serializerForNode = mapper.findValueSerializer(ObjectNode.class);
        node = serializerForNode.serializeValue(node, null, null);
        mapper.registerModule(new SimpleModule().addSerializer(ObjectNode.class, new JsonNodeSerializer()));
        ObjectNode nodeWithAnnotation = mapper.createObjectNode();
        nodeWithAnnotation.set("findValuesByAnnotation", annotation);
        nodeWithAnnotation.set("value", MyEnum.VALUE_1);

        JsonNode jsonNode = mapper.readTree(mapper.writeValueAsString(nodeWithAnnotation));
        JsonNode serializedNode = jsonNode.path("findValuesByAnnotation");
        EnumSerializer serializedSerializer = (EnumSerializer) serializedNode.get("findValuesByAnnotation");

        assertEquals(0, serializedSerializer.serialize(MyEnum.VALUE_1, null, null).getIntegerValueExact());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedShape() throws IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.construct(MyEnum.class), false);

        ObjectMapper mapper = new ObjectMapper();
        JsonFormat annotation = new JsonFormat();
        annotation.setShape(JsonFormat.Shape.OBJECT);
        ObjectNode node = mapper.createObjectNode();
        node.set("findValuesByAnnotation", annotation);
        node.set("value", MyEnum.VALUE_1);

        JsonNode jsonNode = mapper.readTree(mapper.writeValueAsString(node));
        JsonNode serializedNode = jsonNode.path("findValuesByAnnotation");
        EnumSerializer serializedSerializer = (EnumSerializer) serializedNode.get("findValuesByAnnotation");

        serializedSerializer.serialize(MyEnum.VALUE_1, null, null);
    }

    private static class MyEnum {
        public static final MyEnum VALUE_1 = new MyEnum();
        public static final MyEnum VALUE_2 = new MyEnum();

        private MyEnum() {
        }
    }

    private static class EnumValueHandler extends JsonValueHandler {
        @Override
        public void handleValue(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.getValueAsInt() != null) {
                ctxt.findEnumValue(ctxt.getCurrentValueProcessor().getEnumClass(), p.getValueAsInt());
            } else {
                ctxt.handleUnknownValue(p.getValueAsString());
            }
        }
    }
}