import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;

public class EnumSerializerTest {

    private ObjectMapper objectMapper;
    private EnumSerializer enumSerializer;

    @Before
    public void setUp() throws JsonProcessingException {
        objectMapper = new ObjectMapper();
        EnumSerializer.construct(MyEnum.class, objectMapper.getSerializationConfig(), null, null);
        enumSerializer = (EnumSerializer) objectMapper.findValueSerializer(MyEnum.class);
    }

    @Test
    public void testSerializeToJson() throws JsonGenerationException, JsonMappingException, IOException {
        MyEnum value = MyEnum.VALUE_1;
        String expectedJson = _getExpectedJsonForValue(value);

        String actualJson = serializeToJson(value);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeToJsonWithIndex() throws JsonGenerationException, JsonMappingException, IOException {
        MyEnum value = MyEnum.VALUE_1;
        ObjectMapper objectMapperWithIndex = new ObjectMapper();
        objectMapperWithIndex.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);

        String actualJson = serializeToJsonWithIndex(value, objectMapperWithIndex);
        int expectedIndex = value.ordinal();
        Assert.assertEquals(Integer.toString(expectedIndex), actualJson);
    }

    @Test
    public void testSerializeToJsonWithToString() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapperWithToString = new ObjectMapper();
        objectMapperWithToString.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

        String actualJson = serializeToJsonWithToString(MyEnum.VALUE_1, objectMapperWithToString);
        String expectedToString = MyEnum.VALUE_1.toString();
        Assert.assertEquals(expectedToString, actualJson);
    }

    @Test
    public void testDeserializeFromJson() throws JsonProcessingException {
        String json = _getExpectedJsonForValue(MyEnum.VALUE_1);
        MyEnum expectedValue = MyEnum.VALUE_1;
        MyEnum actualValue = deserializeFromJson(json);

        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testDeserializeFromJsonWithIndex() throws JsonProcessingException {
        String jsonWithIndex = Integer.toString(MyEnum.VALUE_1.ordinal());
        MyEnum expectedValue = MyEnum.VALUE_1;
        MyEnum actualValue = deserializeFromJsonWithIndex(jsonWithIndex);

        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeFromJsonWithInvalidValue() throws JsonProcessingException {
        String jsonWithInvalidValue = "INVALID_VALUE";
        deserializeFromJson(jsonWithInvalidValue);
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        JsonNode schema = getSchema();

        JsonNode expectedSchema = getExpectedSchema();
        Assert.assertEquals(expectedSchema, schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper objectMapperForAcceptJsonFormatVisitor = new ObjectMapper();
        ObjectNode expectedNode = getExpectedAcceptJsonFormatVisitorNode();

        ObjectNode actualNode = acceptJsonFormatVisitor(objectMapperForAcceptJsonFormatVisitor);
        Assert.assertEquals(expectedNode, actualNode);
    }

    private String _getExpectedJsonForValue(MyEnum value) {
        return _valuesToJson(_valuesForEnum(value));
    }

    private String serializeToJson(MyEnum value) throws JsonGenerationException, JsonMappingException, IOException {
        return objectMapper.writeValueAsString(value);
    }

    private String serializeToJsonWithIndex(MyEnum value, ObjectMapper objectMapper)
            throws JsonGenerationException, JsonMappingException, IOException {
        return objectMapper.writeValueAsString(value.ordinal());
    }

    private String serializeToJsonWithToString(MyEnum value, ObjectMapper objectMapper)
            throws JsonGenerationException, JsonMappingException, IOException {
        return objectMapper.writeValueAsString(value.toString());
    }

    private MyEnum deserializeFromJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, MyEnum.class);
    }

    private MyEnum deserializeFromJsonWithIndex(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, int.class);
        // assuming the deserialization config is set up correctly
    }

    private JsonNode getSchema() throws JsonProcessingException {
        return enumSerializer.getSchema(objectMapper.getSerializationConfig(), MyEnum.class);
    }

    private ObjectNode acceptJsonFormatVisitor(ObjectMapper objectMapper) throws JsonMappingException {
        ObjectNode node = objectMapper.createObjectNode();
        enumSerializer.acceptJsonFormatVisitor(objectMapper.getSerializerProvider().getJsonFormatVisitor(
                JsonFormat.Feature.FAIL_ON_EMPTY_Beans), MyEnum.class);
        return node;
    }

    private JsonNode getExpectedSchema() {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        arrayNode.add("string");
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.putArray("enum");
        ArrayNode enumNode = objectMapper.createArrayNode();
        enumNode.add("VALUE_0");
        enumNode.add("VALUE_1");
        objectNode.set("properties", objectMapper.valueToTree(EnumSet.allOf(MyEnum.class).stream()
                .map(e -> new SimpleBeanPropertyDefinition(e.name(), ObjectMapper.ValueType.ENUM))
                .collect(Collectors.toList())));
        arrayNode.add(objectNode);
        return arrayNode;
    }

    private ObjectNode getExpectedAcceptJsonFormatVisitorNode() {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("type", "STRING");
        ArrayNode enumNode = node.putArray("enum");
        for (MyEnum value : MyEnum.values()) {
            enumNode.add(value.name());
        }
        return node;
    }

    private Map<MyEnum, SerializableString> _valuesForEnum(MyEnum value) {
        return Arrays.stream(MyEnum.values())
                .collect(Collectors.toMap(Function.identity(), e -> new SimpleSerializableString(e.name())));
    }

    private String _valuesToJson(Map<MyEnum, SerializableString> values) {
        ObjectNode node = objectMapper.createObjectNode();
        ArrayNode arrayNode = node.putArray("enum");
        values.forEach((k, v) -> arrayNode.add(v.getValue()));
        return node.toString();
    }

    private static class SimpleSerializableString implements SerializableString {
        private final String value;

        SimpleSerializableString(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private static class SimpleBeanPropertyDefinition extends BeanPropertyDefinition {
        SimpleBeanPropertyDefinition(String name, ObjectMapper.ValueType valueType) {
            super(name, Object.class, valueType, null);
        }
    }

    enum MyEnum {
        VALUE_0,
        VALUE_1
    }
}