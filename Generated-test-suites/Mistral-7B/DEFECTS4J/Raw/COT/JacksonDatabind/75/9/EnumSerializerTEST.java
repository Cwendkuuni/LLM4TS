package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Test;

@JacksonStdImpl
public class EnumSerializerTest {

    

    @Test
    public void testSerializeNormal() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(TestEnum.class), null);
        ObjectNode node = new ObjectMapper().writeValueAsObject(TestEnum. values()[0]);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(TestEnum.values()[0], gen, new SerializerProvider());
        JsonParser parser = new ObjectMapper().getFactory().createParser(node.getSerializableValue().toString());
        assertEquals(parser.nextValue().asInt(), gen.getOutputValue().asInt());
    }

    @Test
    public void testSerializeAsString() throws Exception {
        SerializerProvider serializers = new SerializerProvider();
        serializers.setConfig(new ObjectMapper().getSerializationConfig());
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(TestEnum.class), null);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), serializers);
        ObjectNode node = new ObjectMapper().writeValueAsObject(TestEnum. values()[0]);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(node.toString());
        parser.nextToken();
        assertTrue(parser.nextValue().isTextual());
        assertEquals(parser.nextTextValue().getText(), gen.getOutputValue().asText());
    }

    @Test
    public void testSerializeAsIndex() throws Exception {
        SerializerProvider serializers = new SerializerProvider();
        serializers.setConfig(new ObjectMapper().getSerializationConfig());
        serializers.setEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(TestEnum.class), null);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), serializers);
        ObjectNode node = new ObjectMapper().writeValueAsObject(TestEnum. values()[0]);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(node.toString());
        parser.nextToken();
        assertTrue(parser.nextValue().isNumber());
        assertEquals(parser.nextIntValue(), gen.getOutputValue().asInt());
    }

    @Test
    public void testSerializeWithCustomValue() throws Exception {
        SerializerProvider serializers = new SerializerProvider();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFrom(TestEnum.class, "CUSTOM"), null);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), serializers);
        serializer.serialize(TestEnum.values()[2], gen, serializers);
        JsonParser parser = new ObjectMapper().getFactory().createParser(gen.getOutputValue().toString());
        assertEquals("CUSTOM", parser.nextTextValue().getText());
    }

    @Test
    public void testSerializeWithJsonFormat() throws Exception {
        JsonFormat format = new JsonFormat();
        format.setShape(Shape.STRING);
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(TestEnum.class), null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("format", format);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new SerializerProvider());
        serializer.serialize(TestEnum.values()[0], gen, new SerializerProvider(), "TestEnum", node);
        JsonParser parser = new ObjectMapper().getFactory().createParser(gen.getOutputValue().toString());
        parser.nextToken();
        assertTrue(parser.nextValue().isTextual());
        assertEquals(TestEnum.values()[0].name(), parser.nextTextValue().getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithInvalidJsonFormat() throws Exception {
        JsonFormat format = new JsonFormat();
        format.setShape(Shape.INVALID_SHAPE);
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(TestEnum.class), null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("format", format);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new SerializerProvider());
        serializer.serialize(TestEnum.values()[0], gen, new SerializerProvider(), "TestEnum", node);
    }

    

    enum TestEnum {
        @JsonFormat(shape = Shape.STRING)
        A,
        @JsonFormat(shape = Shape.STRING, values = {"B1", "B2"})
        B,
        C,
        D,
        E;

        public static EnumValues allValues(Class<TestEnum> clazz) {
            return new EnumValues(clazz);
        }

        public static EnumValues constructFrom(Class<TestEnum> clazz, String customValue) {
            return new EnumValues(clazz, Arrays.asList(new EnumValue[]{new EnumValue(TestEnum.class, "CUSTOM")}));
        }

        public static EnumValues constructFromName(SerializationConfig config, Class<TestEnum> clazz) {
            return new EnumValues(clazz, null);
        }

        public static EnumValues constructFrom(Class<TestEnum> clazz, String[] customValues) {
            List<EnumValue> values = new ArrayList<>();
            for (String customValue : customValues) {
                values.add(new EnumValue(TestEnum.class, customValue));
            }
            return new EnumValues(clazz, values);
        }

        public static EnumValues constructFrom(Class<TestEnum> clazz, EnumSet<TestEnum> customValues) {
            List<EnumValue> values = new ArrayList<>();
            for (TestEnum customValue : customValues) {
                values.add(new EnumValue(TestEnum.class, customValue.name()));
            }
            return new EnumValues(clazz, values);
        }

        public static void main(String[] args) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(TestEnum.class), null);
            ObjectWriter writer = mapper.writer(new DefaultSerializerProvider());
            ObjectNode node = mapper.createObjectNode();
            node.put("enum", Arrays.asList("A", "B", "C", "D", "E"));
            ArrayNode enumNode = node.getPOJO("enum");
            for (EnumValue value : EnumValues.values()) {
                enumNode.add(value.getValue());
            }
            String json = writer.writeValueAsString(node);
            System.out.println(json);
            JsonParser parser = mapper.getFactory().createParser(json);
            parser.nextToken();
            JsonNode enumNode2 = parser.readValueAsTree("/enum");
            for (JsonNode node : enumNode2) {
                System.out.println(node.asText());
            }
        }
    }

    static class EnumValue {
        private final Class<?> _enumClass;
        private final String _value;

        EnumValue(Class<?> enumClass, String value) {
            _enumClass = enumClass;
            _value = value;
        }

        String getValue() {
            return _value;
        }
    }
}