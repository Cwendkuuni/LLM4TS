import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnumSerializerTest {

    private EnumSerializer serializer;
    private Enum<?> enumValue;

    @BeforeAll
    void setup() {
        Class<MyEnum> enumClass = MyEnum.class;
        EnumValues enumValues = EnumValues.constructFromName(new ObjectMapper(), enumClass);
        serializer = new EnumSerializer(enumValues, false);
    }

    @Test
    void testSerializerNormalCase() throws JsonProcessingException {
        enumValue = MyEnum.VALUE_1;
        String expected = MyEnum.VALUE_1.name();

        ObjectNode result = mapper.createObjectNode();
        serializer.serialize(enumValue, result, new JsonSerializer.DefaultSerializers.BaseJsonSerializer.Instance());
        String actual = result.get(0).asText();

        assertEquals(expected, actual);
    }

    @Test
    void testSerializerSerializeAsIndex() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        ObjectNode result = mapper.createObjectNode();

        _serializeAsIndex(serializer, result) = true;
        serializer.serialize(enumValue, result, new JsonSerializer.DefaultSerializers.BaseJsonSerializer.Instance());

        int actual = result.get(0).asInt();
        int expected = enumValue.ordinal();

        assertEquals(expected, actual);
    }

    @Test
    void testSerializerToString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        ObjectNode result = mapper.createObjectNode();
        _serializeAsIndex(serializer, result) = false;
        serializer.serialize(enumValue, result, new JsonSerializer.DefaultSerializers.BaseJsonSerializer.Instance());

        String actual = result.get(0).asText();
        String expected = enumValue.toString();

        assertEquals(expected, actual);
    }

    @Test
    void testSerializerArrayFormat() throws JsonProcessingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.ARRAY);
        ObjectNode result = mapper.createObjectNode();

        serializer = new EnumSerializer(new EnumValues(MyEnum.class), null).createContextual(null, null);
        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(null, new ArrayNode(mapper.createObjectNode())) {
            @Override
            public void acceptStringFormat(JsonFormatVisitorWrapper v) throws JsonMappingException {
                fail("Should not be reached");
            }

            @Override
            public void acceptNumberFormat(JsonFormatVisitorWrapper v, JavaType typeHint, JsonParser.NumberType numberType)
                    throws JsonMappingException {
                v.visitArrayFormat(null, typeHint, null, null, null);
            }
        }, null);

        ObjectNode enumNode = result.path("enum");
        int size = enumNode.size();

        assertEquals(MyEnum.values().length, size);
    }

    @Test
    void testSerializerSchema() throws JsonProcessingException {
        ObjectNode schema = serializer.getSchema(new JsonSerializer.DefaultSerializers.BaseJsonSerializer.Instance(), null);

        ArrayNode enumNode = schema.path("type");
        String type = enumNode.get(0).asText();

        assertEquals("array", type);

        ArrayNode itemsNode = enumNode.path("items");
        String itemsType = itemsNode.get(0).asText();

        assertEquals("string", itemsType);
    }

    private boolean _serializeAsIndex(EnumSerializer serializer, ObjectNode result) {
        ObjectWriter objectWriter = mapper.writerWithRootValue(result);
        JsonSerializer<Enum<?>> ser = serializer;
        boolean actual = ser.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX);

        try {
            objectWriter.writeValueAsString(MyEnum.VALUE_1);
        } catch (JsonProcessingException e) {
            actual = false;
        }

        return actual;
    }

    enum MyEnum {
        VALUE_1,
        VALUE_2,
        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        VALUE_3
    }

    private ObjectMapper mapper = new ObjectMapper();
}