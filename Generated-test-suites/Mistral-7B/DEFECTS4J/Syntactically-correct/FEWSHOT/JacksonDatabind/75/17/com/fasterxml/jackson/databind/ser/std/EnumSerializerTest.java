package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {

    @Test
    public void testSerializeAndDeserialize() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.class), null);

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = writer.writeValueAsString( Arrays.asList(MyEnum.VALUE1, MyEnum.VALUE2) );
        ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
        List<MyEnum> deserialized = mapper.readValue(node, new TypeReference<List<MyEnum>>(){});

        assertEquals(Arrays.asList(MyEnum.VALUE1, MyEnum.VALUE2), deserialized);
    }

    @Test
    public void testSerializeAsIndex() throws Exception {
        enum MyEnumWithIndex {
            VALUE1(1),
            VALUE2(2),
            VALUE3(3);

            private final int index;

            MyEnumWithIndex(int index) {
                this.index = index;
            }

            @JsonValue
            public int asInt() {
                return index;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromValues(MyEnumWithIndex.class), true);

        String json = writer.writeValueAsString(Arrays.asList(MyEnumWithIndex.VALUE1, MyEnumWithIndex.VALUE2));
        ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);

        List<MyEnumWithIndex> deserialized = mapper.readValue(node, new TypeReference<List<MyEnumWithIndex>>(){});

        assertEquals(Arrays.asList(MyEnumWithIndex.VALUE1, MyEnumWithIndex.VALUE2), deserialized);

        assertEquals(1, deserialized.get(0).asInt());
        assertEquals(2, deserialized.get(1).asInt());
    }

    @Test
    public void testSerializeAsString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.class), null);

        String json = writer.writeValueAsString(Arrays.asList(MyEnum.VALUE1, MyEnum.VALUE2));
        ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);

        List<MyEnum> deserialized = mapper.readValue(node, new TypeReference<List<MyEnum>>(){});

        assertEquals(Arrays.asList(MyEnum.VALUE1, MyEnum.VALUE2), deserialized);

        assertEquals(MyEnum.VALUE1.name(), deserialized.get(0).name());
        assertEquals(MyEnum.VALUE2.name(), deserialized.get(1).name());
    }

    @Test
    public void testSerializeWithCustomValues() throws Exception {
        enum MyEnumWithCustomValues {
            VALUE1("CUSTOM_VALUE_1"),
            VALUE2("CUSTOM_VALUE_2");

            private final String customValue;

            MyEnumWithCustomValues(String customValue) {
                this.customValue = customValue;
            }

            @JsonValue
            public String getCustomValue() {
                return customValue;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromValues(MyEnumWithCustomValues.class), null);

        String json = writer.writeValueAsString(Arrays.asList(MyEnumWithCustomValues.VALUE1, MyEnumWithCustomValues.VALUE2));
        ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);

        List<MyEnumWithCustomValues> deserialized = mapper.readValue(node, new TypeReference<List<MyEnumWithCustomValues>>(){});

        assertEquals(Arrays.asList(MyEnumWithCustomValues.VALUE1, MyEnumWithCustomValues.VALUE2), deserialized);

        assertEquals(MyEnumWithCustomValues.VALUE1.getCustomValue(), deserialized.get(0).getCustomValue());
        assertEquals(MyEnumWithCustomValues.VALUE2.getCustomValue(), deserialized.get(1).getCustomValue());
    }

    @Test
    public void testCreateContextual() throws Exception {
        enum MyEnum {
            VALUE1,
            VALUE2,
            @JsonFormat(shape = JsonFormat.Shape.STRING) VALUE3;

            @JsonValue
            public String toJsonString() {
                return name();
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        EnumSerializer serializer1 = new EnumSerializer(EnumValues.constructFromValues(MyEnum.class), null);
        EnumSerializer serializer2 = serializer1.createContextual(serializers, null);

        assertEquals(serializer1, serializer2);

        MyEnum value3 = MyEnum.VALUE3;
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

        String json = writer.writeValueAsString(value3);
        ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);

        assertEquals("VALUE3", node.getTextValue());

        List<MyEnum> deserialized = mapper.readValue(node, new TypeReference<List<MyEnum>>(){});
        assertEquals(MyEnum.VALUE3, deserialized.get(0));
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.class), null);

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), new TypeReference<Class<Enum< ? >> >(){});

        assertEquals("string", schema.get("type").asText());
        assertEquals("enum", schema.get("properties").get("enum").asText());

        ObjectNode enumNode = (ObjectNode) schema.get("properties").get("enum");
        List<String> enumValues = new ObjectMapper().treeAsValue(enumNode, new TypeReference<List<String>>(){});

        assertEquals(Arrays.asList(MyEnum.VALUE1.name(), MyEnum.VALUE2.name()), enumValues);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.class), null);

        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(SerializerProvider.StandardSerializers.OBJECT);
        serializer.acceptJsonFormatVisitor(visitor, new TypeReference<Class<Enum< ? >> >(){});

        JsonNode schema = visitor.getSchema();
        assertEquals("string", schema.get("type").asText());
        assertEquals("enum", schema.get("properties").get("enum").asText());

        ObjectNode enumNode = (ObjectNode) schema.get("properties").get("enum");
        List<String> enumValues = new ObjectMapper().treeAsValue(enumNode, new TypeReference<List<String>>(){});

        assertEquals(Arrays.asList(MyEnum.VALUE1.name(), MyEnum.VALUE2.name()), enumValues);
    }

    enum MyEnum {
        VALUE1,
        VALUE2
    }
}