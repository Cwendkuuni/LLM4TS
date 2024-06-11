package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {

    @Test
    public void testEnumSerializerConstruction() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.enumValuesOf(MyEnum.class), null);
        assertNotNull(serializer);
        assertNotNull(serializer.getEnumValues());
    }

    @Test
    public void testEnumSerializerSerialize() throws IOException, JsonProcessingException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.enumValuesOf(MyEnum.class), null);
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        serializer.serialize(MyEnum.ONE, new JsonGenerator(node), new SerializerProvider());
        assertEquals("ONE", node.get("0").asText());

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(MyEnum.TWO, gen, new SerializerProvider());
        assertEquals("TWO", gen.getOutputStructure().toString());
    }

    @Test
    public void testEnumSerializerSerializeAsIndex() throws IOException, JsonProcessingException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.enumValuesOf(MyEnumWithIndex.class), true);
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        serializer.serialize(MyEnumWithIndex.ONE, new JsonGenerator(node), new SerializerProvider());
        assertEquals("0", node.get("0").asText());

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(MyEnumWithIndex.TWO, gen, new SerializerProvider());
        assertEquals("1", gen.getOutputStructure().toString());
    }

    @Test
    public void testEnumSerializerJsonFormatVisitorWrapper() throws JsonMappingException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.enumValuesOf(MyEnum.class), null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper().getSerializationConfig(), null);
        visitor.acceptSerializable(serializer, null);

        ObjectNode expectedNode = ObjectMapper.getDefaultInstance().createObjectNode();
        expectedNode.put("enum", new ArrayNode().add("ONE").add("TWO"));
        assertEquals(expectedNode, visitor.getSchemaProvider().getSchema(new TypeReference<ObjectNode>() {}));
    }

    @Test
    public void testEnumSerializerJsonFormatVisitorWrapperWithToString() throws JsonMappingException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.enumValuesOf(MyEnumWithToString.class), null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper().getSerializationConfig(), null);
        visitor.acceptSerializable(serializer, null);

        ObjectNode expectedNode = ObjectMapper.getDefaultInstance().createObjectNode();
        expectedNode.put("enum", new ArrayNode().add("ONE_ONE").add("TWO_TWO"));
        assertEquals(expectedNode, visitor.getSchemaProvider().getSchema(new TypeReference<ObjectNode>() {}));
    }

    @Test
    public void testEnumSerializerJsonFormat() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JsonFormat<Enum<?>> format = new JsonFormat<>();
        format.setShape(Shape.STRING);

        ObjectNode node = mapper.readTree("{\"enum\":[\"ONE\",\"TWO\"]}");
        MyEnumSerializer serializer = new MyEnumSerializer();
        serializer.setSerializationConfig(mapper.getSerializationConfig());
        serializer.configure(JsonSerializer.Feature.WRITE_ENUMS_USING_TO_STRING_AS_NAMES, true);
        JsonSerializer<?> contextualSerializer = serializer.createContextual(new SerializerProvider(), null);

        assertEquals(MyEnum.ONE, contextualSerializer.deserialize("ONE", null));
        assertEquals(MyEnum.TWO, contextualSerializer.deserialize("TWO", null));

        ObjectMapper mapperWithFormat = new ObjectMapper();
        mapperWithFormat.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapperWithFormat.setSerializationConfig(mapper.getSerializationConfig());
        mapperWithFormat.addHandler(new JsonFormatProvider() {
            @Override
            public JsonFormat<Enum<?>> findFormat(SerializationConfig config, Type type) {
                return format;
            }
        });
        assertEquals(MyEnum.ONE, mapperWithFormat.readValue(node, MyEnum[].class)[0]);
        assertEquals(MyEnum.TWO, mapperWithFormat.readValue(node, MyEnum[].class)[1]);
    }

    @Test
    public void testEnumSerializerContextualSerializer() throws JsonMappingException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.enumValuesOf(MyEnum.class), null);
        ContextualSerializer contextualSerializer = serializer.createContextual(new SerializerProvider(), null);
        JsonSerializer<?> contextualSerializerWithToString = serializer.createContextual(new SerializerProvider(), null);

        assertEquals(serializer, contextualSerializer);
        assertEquals(serializer, contextualSerializerWithToString);

        assertEquals(MyEnum.ONE, contextualSerializer.deserialize("ONE", null));
        assertEquals(MyEnum.TWO, contextualSerializer.deserialize("TWO", null));

        assertEquals(MyEnum.ONE_ONE, contextualSerializerWithToString.deserialize("ONE_ONE", null));
        assertEquals(MyEnum.TWO_TWO, contextualSerializerWithToString.deserialize("TWO_TWO", null));
    }

    @Test
    public void testEnumSerializerCreateContextualWithJsonFormat() throws JsonMappingException {
        JsonFormat<Enum<?>> format = new JsonFormat<>();
        format.setShape(Shape.STRING);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationConfig(mapper.getSerializationConfig());
        mapper.addHandler(new JsonFormatProvider() {
            @Override
            public JsonFormat<Enum<?>> findFormat(SerializationConfig config, Type type) {
                return format;
            }
        });

        EnumSerializer serializer = new EnumSerializer(EnumValues.enumValuesOf(MyEnum.class), null);
        ContextualSerializer contextualSerializer = serializer.createContextual(new SerializerProvider(), null);

        assertEquals(MyEnum.ONE, contextualSerializer.deserialize("ONE", null));
        assertEquals(MyEnum.TWO, contextualSerializer.deserialize("TWO", null));

        ContextualSerializer contextualSerializerWithFormat = serializer.createContextual(new SerializerProvider(), null);
        assertEquals(MyEnum.ONE_ONE, contextualSerializerWithFormat.deserialize("ONE_ONE", null));
        assertEquals(MyEnum.TWO_TWO, contextualSerializerWithFormat.deserialize("TWO_TWO", null));

        JsonFormat<Enum<?>> formatWithJsonFormat = new JsonFormat<>();
        formatWithJsonFormat.setShape(Shape.STRING);
        formatWithJsonFormat.setEnumValues(EnumValues.enumValuesOf(MyEnum.class));

        ContextualSerializer contextualSerializerWithJsonFormat = serializer.createContextual(new SerializerProvider(), null);
        assertEquals(MyEnum.ONE, contextualSerializerWithJsonFormat.deserialize("ONE", null));
        assertEquals(MyEnum.TWO, contextualSerializerWithJsonFormat.deserialize("TWO", null));
    }

    @Test
    public void testEnumSerializerSerializationFeature() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        EnumSerializer serializer = new EnumSerializer(EnumValues.enumValuesOf(MyEnum.class), null);
        ContextualSerializer contextualSerializer = serializer.createContextual(new SerializerProvider(), null);

        assertEquals(MyEnum.ONE, contextualSerializer.deserialize("ONE", null));
        assertEquals(MyEnum.TWO, contextualSerializer.deserialize("TWO", null));

        mapper.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING_AS_NAMES);
        ContextualSerializer contextualSerializerWithFlags = serializer.createContextual(new SerializerProvider(), null);

        assertEquals(MyEnum.ONE.name(), contextualSerializerWithFlags.serializeValueAsString(MyEnum.ONE));
        assertEquals(MyEnum.TWO.name(), contextualSerializerWithFlags.serializeValueAsString(MyEnum.TWO));

        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING_AS_NAMES);
        ContextualSerializer contextualSerializerWithToString = serializer.createContextual(new SerializerProvider(), null);

        assertEquals(MyEnum.ONE_ONE, contextualSerializerWithToString.serializeValueAsString(MyEnum.ONE));
        assertEquals(MyEnum.TWO_TWO, contextualSerializerWithToString.serializeValueAsString(MyEnum.TWO));
    }

    @Test
    public void testEnumSerializerSerializationFeatureWithJsonFormat() throws JsonMappingException {
        JsonFormat<Enum<?>> format = new JsonFormat<>();
        format.setShape(Shape.STRING);
        format.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationConfig(mapper.getSerializationConfig());
        mapper.addHandler(new JsonFormatProvider() {
            @Override
            public JsonFormat<Enum<?>> findFormat(SerializationConfig config, Type type) {
                return format;
            }
        });

        EnumSerializer serializer = new EnumSerializer(EnumValues.enumValuesOf(MyEnum.class), null);
        ContextualSerializer contextualSerializer = serializer.createContextual(new SerializerProvider(), null);

        assertEquals(MyEnum.ONE, contextualSerializer.deserialize("ONE", null));
        assertEquals(MyEnum.TWO, contextualSerializer.deserialize("TWO", null));

        mapper.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING_AS_NAMES);
        ContextualSerializer contextualSerializerWithFlags = serializer.createContextual(new SerializerProvider(), null);

        assertEquals(MyEnum.ONE.name(), contextualSerializerWithFlags.serializeValueAsString(MyEnum.ONE));
        assertEquals(MyEnum.TWO.name(), contextualSerializerWithFlags.serializeValueAsString(MyEnum.TWO));

        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING_AS_NAMES);
        ContextualSerializer contextualSerializerWithToString = serializer.createContextual(new SerializerProvider(), null);

        assertEquals(MyEnum.ONE_ONE, contextualSerializerWithToString.serializeValueAsString(MyEnum.ONE));
        assertEquals(MyEnum.TWO_TWO, contextualSerializerWithToString.serializeValueAsString(MyEnum.TWO));
    }

    @Test
    public void testEnumSerializerJsonFormatAndSerializationFeature() throws JsonMappingException {
        JsonFormat<Enum<?>> format = new JsonFormat<>();
        format.setShape(Shape.STRING);
        format.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationConfig(mapper.getSerializationConfig());
        mapper.addHandler(new JsonFormatProvider() {
            @Override
            public JsonFormat<Enum<?>> findFormat(SerializationConfig config, Type type) {
                return format;
            }
        });

        EnumSerializer serializer = new EnumSerializer(EnumValues.enumValuesOf(MyEnum.class), null);
        ContextualSerializer contextualSerializer = serializer.createContextual(new SerializerProvider(), null);

        assertEquals(MyEnum.ONE, contextualSerializer.deserialize("ONE", null));
        assertEquals(MyEnum.TWO, contextualSerializer.deserialize("TWO", null));

        mapper.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING_AS_NAMES);
        ContextualSerializer contextualSerializerWithFlags = serializer.createContextual(new SerializerProvider(), null);

        assertEquals(MyEnum.ONE.name(), contextualSerializerWithFlags.serializeValueAsString(MyEnum.ONE));
        assertEquals(MyEnum.TWO.name(), contextualSerializerWithFlags.serializeValueAsString(MyEnum.TWO));

        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING_AS_NAMES);
        ContextualSerializer contextualSerializerWithToString = serializer.createContextual(new SerializerProvider(), null);

        assertEquals(MyEnum.ONE_ONE, contextualSerializerWithToString.serializeValueAsString(MyEnum.ONE));
        assertEquals(MyEnum.TWO_TWO, contextualSerializerWithToString.serializeValueAsString(MyEnum.TWO));

        ObjectMapper mapperWithFormat = new ObjectMapper();
        mapperWithFormat.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapperWithFormat.setSerializationConfig(mapper.getSerializationConfig());
        mapperWithFormat.addHandler(new JsonFormatProvider() {
            @Override
            public JsonFormat<Enum<?>> findFormat(SerializationConfig config, Type type) {
                return format;
            }
        });

        assertEquals(MyEnum.ONE, mapperWithFormat.readValue(new Object[]{MyEnum.ONE}, MyEnum.class)[0]);
        assertEquals(MyEnum.TWO, mapperWithFormat.readValue(new Object[]{MyEnum.TWO}, MyEnum.class)[0]);

        assertEquals(MyEnum.ONE_ONE, mapperWithFormat.readValue(new Object[]{MyEnum.ONE_ONE}, MyEnum.class)[0]);
        assertEquals(MyEnum.TWO_TWO, mapperWithFormat.readValue(new Object[]{MyEnum.TWO_TWO}, MyEnum.class)[0]);
    }

    @JsonFormat(shape = Shape.STRING)
    enum MyEnum {
        ONE, TWO
    }

    @JsonFormat(shape = Shape.STRING, value = "ONE_ONE, TWO_TWO")
    enum MyEnumWithToString {
        ONE_ONE, TWO_TWO
    }

    @JsonFormat(shape = Shape.STRING, value = "{name}")
    enum MyEnumWithName {
        ONE, TWO
    }

    @JsonFormat(shape = Shape.STRING, value = "@JsonValue(\"ONE\")")
    enum MyEnumWithAnnotation {
        ONE, TWO
    }

    static class MyEnumSerializer extends EnumSerializer {
        @Override
        protected Boolean _isShapeWrittenUsingIndex(Class<?> enumClass, JsonFormat.Value format, boolean fromClass) {
            return false;
        }
    }
}