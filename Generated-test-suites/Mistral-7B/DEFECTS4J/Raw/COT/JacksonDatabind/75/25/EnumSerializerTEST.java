import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class EnumSerializerTest {

    private EnumSerializer enumSerializer;
    private EnumValues enumValues;

    @Before
    public void setUp() {
        Class<MyEnum> enumClass = MyEnum.class;
        Object enumInstance = MyEnum.VALUE1;
        enumValues = new EnumValues(enumClass);
        enumSerializer = new EnumSerializer(enumValues, null);
    }

    @Test
    public void testConstruct() {
        EnumSerializer instance = EnumSerializer.construct(enumClass, null, null, null);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testCreateContextual() {
        EnumSerializer serializer1 = new EnumSerializer(enumValues, null);
        EnumSerializer serializer2 = serializer1.createContextual(null, null);
        Assert.assertNotSame(serializer1, serializer2);
    }

    @Test
    public void testSerializeAsIndex() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        enumSerializer.serialize(enumInstance, gen, null);
        ObjectNode node = new ObjectMapper().readValue(System.out, ObjectNode.class);
        Assert.assertEquals(Integer.toString(enumInstance.ordinal()), node.get("0").asText());
    }

    @Test
    public void testSerializeToString() throws IOException {
        SerializationConfig config = new SerializationConfig();
        config.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        ObjectMapper mapper = new ObjectMapper(config);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        enumSerializer.serialize(enumInstance, gen, null);
        ObjectNode node = mapper.readValue(System.out, ObjectNode.class);
        Assert.assertEquals(enumInstance.toString(), node.get("0").asText());
    }

    @Test
    public void testSerializeNamedValue() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        enumSerializer.serialize(enumInstance, gen, null);
        ObjectNode node = new ObjectMapper().readValue(System.out, ObjectNode.class);
        Assert.assertEquals(_values.serializedValueFor(enumInstance), node.get("0").asText());
    }

    @Test
    public void testGetSchema() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode schema = enumSerializer.getSchema(null, getType(enumClass));
        Assert.assertNotNull(schema);
        Assert.assertEquals("string", schema.get("type").asText());

        ObjectNode objectNode = (ObjectNode) schema;
        ArrayNode enumNode = objectNode.get("enum");
        for (Enum<?> e : enumValues.enums()) {
            Assert.assertNotNull(enumNode);
            Assert.assertEquals(e.toString(), enumNode.get(enumNode.size()).asText());
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormat.Value format = new JsonFormat.Value();
        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(mapper.getTypeFactory().constructType(Map.class), null);
        enumSerializer.acceptJsonFormatVisitor(visitor, getType(enumClass));
        JsonStringFormatVisitor stringVisitor = visitor.expectStringFormat(getType(enumClass));
        Assert.assertNotNull(stringVisitor);
        Set<String> enums = stringVisitor.getEnumValues();
        for (Enum<?> e : enumValues.enums()) {
            Assert.assertTrue(enums.contains(e.toString()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedSerializationShape() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(Shape.DATE_TIME);
        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(mapper.getTypeFactory().constructType(Map.class), null);
        enumSerializer.acceptJsonFormatVisitor(visitor, getType(enumClass));
    }

    @Test
    public void testSerializeNull() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        ObjectNode node = new ObjectMapper().createObjectNode();
        enumSerializer.serialize(null, gen, null);
        node.put("null", "null");
        Assert.assertEquals(node.toString(), gen.getOutputStream().toString());
    }

    private Type getType(Class<?> clazz) {
        return new ObjectMapper().getTypeFactory().constructType(clazz);
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, value = MyEnum.VALUE1.name())
    enum MyEnum {
        VALUE1, VALUE2, VALUE3;
    }
}