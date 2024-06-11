package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class EnumSerializerTest {

    @Test
    public void testSerializationWithIndex() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);

        EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), true);

        String json = mapper.writeValueAsString(MyEnum.VALUE1);
        assertEquals("0", json);
    }

    @Test
    public void testSerializationWithToString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

        EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), false);

        String json = mapper.writeValueAsString(MyEnum.VALUE1);
        assertEquals("VALUE1", json);
    }

    @Test
    public void testSerializationWithName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), false);

        String json = mapper.writeValueAsString(MyEnum.VALUE1);
        assertEquals("VALUE_VALUE1", json);
    }

    @Test
    public void testSerializationWithCustomValues() throws IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFrom("CUSTOM_VALUE_", MyEnum.class), false);

        String json = serializer.serialize(MyEnum.VALUE1, null, null);
        assertEquals("CUSTOM_VALUE_VALUE1", json);
    }

    @Test
    public void testSerializationWithCustomValuesAndIndex() throws IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFrom("CUSTOM_VALUE_", MyEnum.class), true);

        String json = serializer.serialize(MyEnum.VALUE1, null, null);
        assertEquals("0", json);
    }

    @Test
    public void testSerializationWithFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);

        EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), false);

        String json = mapper.writerWithView(new StandardSerializerSetup(null, null, null) {
            @Override
            public JsonSerializer<?> createSerializer(SerializerProvider serializers, BeanProperty property) {
                return serializer.createContextual(serializers, property);
            }
        }).writeValueAsString(MyEnum.VALUE1, new ObjectNode(JsonNodeFactory.instance)
                .putPOJO("format", format));
        assertEquals("VALUE_VALUE1", json);
    }

    @Test
    public void testSerializationWithFormatIndex() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);

        EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), false);

        String json = mapper.writerWithView(new StandardSerializerSetup(null, null, null) {
            @Override
            public JsonSerializer<?> createSerializer(SerializerProvider serializers, BeanProperty property) {
                return serializer.createContextual(serializers, property);
            }
        }).writeValueAsString(MyEnum.VALUE1, new ObjectNode(JsonNodeFactory.instance)
                .putPOJO("format", format));
        assertEquals("0", json);
    }

    @Test
    public void testDeserializationWithIndex() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("value", 0);

        MyEnum enumValue = mapper.readValue(JsonNode.class.getDeclaredField("value"), MyEnum.class, null);
        assertEquals(MyEnum.VALUE1, enumValue);
    }

    @Test
    public void testDeserializationWithToString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("value", "VALUE1");

        MyEnum enumValue = mapper.readValue(JsonNode.class.getDeclaredField("value"), MyEnum.class, null);
        assertEquals(MyEnum.VALUE1, enumValue);
    }

    @Test
    public void testDeserializationWithName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("value", "VALUE_VALUE1");

        MyEnum enumValue = mapper.readValue(JsonNode.class.getDeclaredField("value"), MyEnum.class, null);
        assertEquals(MyEnum.VALUE1, enumValue);
    }

    @Test
    public void testDeserializationWithCustomValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("value", "CUSTOM_VALUE_VALUE1");

        MyEnum enumValue = mapper.readValue(JsonNode.class.getDeclaredField("value"), MyEnum.class, null);
        assertEquals(MyEnum.VALUE1, enumValue);
    }

    @Test
    public void testDeserializationWithFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.putPOJO("value", new ObjectNode(JsonNodeFactory.instance)
                .putPOJO("format", new ObjectNode(JsonNodeFactory.instance)
                        .put("shape", JsonFormat.Shape.STRING.toString())));

        MyEnum enumValue = mapper.readValue(node, MyEnum.class);
        assertEquals(MyEnum.VALUE1, enumValue);
    }

    @Test
    public void testDeserializationWithFormatIndex() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.putPOJO("value", new ObjectNode(JsonNodeFactory.instance)
                .putPOJO("format", new ObjectNode(JsonNodeFactory.instance)
                        .put("shape", JsonFormat.Shape.NUMBER.toString())));

        MyEnum enumValue = mapper.readValue(node, MyEnum.class);
        assertEquals(MyEnum.VALUE1, enumValue);
    }

    @Test
    public void testSerializationSchema() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        JsonNode schema = new EnumSerializer(EnumValues.values(MyEnum.class), false).getSchema(serializers, null);

        assertEquals(JsonNodeType.OBJECT, schema.getType());
        ArrayNode enumNode = schema.path("enum");
        assertEquals(2, enumNode.size());
        assertEquals(MyEnum.VALUE1.name(), enumNode.get(0).asText());
        assertEquals(MyEnum.VALUE2.name(), enumNode.get(1).asText());
    }

    @Test
    public void testSerializationSchemaWithIndex() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        JsonNode schema = new EnumSerializer(EnumValues.values(MyEnum.class), true).getSchema(serializers, null);

        assertEquals(JsonNodeType.OBJECT, schema.getType());
        ArrayNode enumNode = schema.path("enum");
        assertEquals(2, enumNode.size());
        assertEquals("0", enumNode.get(0).asText());
        assertEquals("1", enumNode.get(1).asText());
    }

    @Test
    public void testSerializationSchemaWithCustomValues() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        JsonNode schema = new EnumSerializer(EnumValues.constructFrom("CUSTOM_VALUE_", MyEnum.class), false).getSchema(serializers, null);

        assertEquals(JsonNodeType.OBJECT, schema.getType());
        ArrayNode enumNode = schema.path("enum");
        assertEquals(2, enumNode.size());
        assertEquals("CUSTOM_VALUE_VALUE1", enumNode.get(0).asText());
        assertEquals("CUSTOM_VALUE_VALUE2", enumNode.get(1).asText());
    }

    private static class StandardSerializerSetup extends JsonSerializer.StdSerializer<Object> {
        final Class<?> _class;

        public StandardSerializerSetup(Class<?> cls) {
            this(_class, null, null);
        }

        public StandardSerializerSetup(Class<?> cls, Class<?> vc, JsonSerializer<?> cs) {
            this._class = cls;
            valueClass = vc;
            _valueSerializers = new JsonSerializer<?>[] {cs};
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) {
            return new EnumSerializer(EnumValues.values(property.getType().getEnumConstants()[0].getDeclaringClass()), false);
        }

        @Override
        public JsonSerializer<?> createSerializer(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
            return new EnumSerializer(EnumValues.values(property.getType().getEnumConstants()[0].getDeclaringClass()), false);
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (_valueSerializers != null) {
                for (JsonSerializer<?> serializer : _valueSerializers) {
                    serializer.serialize(value, gen, serializers);
                    break;
                }
            }
        }

        @Override
        public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
            serialize(value, gen, serializers);
            if (valueClass != null) {
                typeSer.writeTypePrefixForValue(gen, valueClass);
                typeSer.writeTypeSuffixForValue(gen, valueClass);
            }
        }
    }

    enum MyEnum {
        VALUE1,
        VALUE2
    }
}