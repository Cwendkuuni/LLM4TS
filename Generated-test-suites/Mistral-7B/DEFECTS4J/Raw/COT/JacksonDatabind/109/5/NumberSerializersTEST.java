import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(11, allDeserializers.size());
    }

    @Test
    public void testIntSerializer_serialize() throws Exception {
        JsonParser parser = new ObjectMapper().getFactory().createParser("[1]");
        ObjectReader reader = new ObjectMapper().readerForUpdating(Integer.class);
        Integer result = reader.readValue(parser);
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        NumberSerializers.IntegerSerializer intSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
        intSerializer.serialize(1, gen, null);
        gen.flush();
        assertEquals(1, result.intValue());
    }

    @Test
    public void testIntSerializer_serializeWithType() throws Exception {
        JsonParser parser = new ObjectMapper().getFactory().createParser("[1]");
        ObjectReader reader = new ObjectMapper().readerForUpdating(Integer.class);
        Integer result = reader.readValue(parser);
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Integer> intSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonNode serializedNode = mapper.treeToNode(intSerializer, new Object() {}, null, 0, null, null, null);
        JsonNode expectedNode = mapper.valueToTree(1);
        assertEquals(expectedNode, serializedNode);
    }

    @Test(expected = JsonProcessingException.class)
    public void testIntSerializer_serializeWithInvalidType() throws Exception {
        JsonParser parser = new ObjectMapper().getFactory().createParser("[1.1]");
        ObjectReader reader = new ObjectMapper().readerForUpdating(Integer.class);
        reader.readValue(parser);
    }

    @Test
    public void testShortSerializer_serialize() throws Exception {
        JsonParser parser = new ObjectMapper().getFactory().createParser("[1]");
        ObjectReader reader = new ObjectMapper().readerForUpdating(Short.class);
        Short result = reader.readValue(parser);
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        shortSerializer.serialize((short) 1, gen, null);
        gen.flush();
        assertEquals((short) 1, result.shortValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testShortSerializer_serializeWithInvalidType() throws Exception {
        JsonParser parser = new ObjectMapper().getFactory().createParser("[1.1]");
        ObjectReader reader = new ObjectMapper().readerForUpdating(Short.class);
        reader.readValue(parser);
    }

    // Add test cases for LongSerializer, FloatSerializer, DoubleSerializer, and IntLikeSerializer

    @Test
    public void testBaseClass_getSchema() throws Exception {
        JsonParser parser = new ObjectMapper().getFactory().createParser("[1]");
        ObjectReader reader = new ObjectMapper().readerForUpdating(Number.class);
        JsonNode schemaNode = reader.readValue(parser).get("schema$");
        JsonNode expectedSchemaNode = ObjectMapper.getDefaultInstance().getFactory().createObjectNode();
        expectedSchemaNode.put("type", "number");
        assertEquals(expectedSchemaNode, schemaNode);
    }

    @Test
    public void testBaseClass_acceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Number> intSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonFormatVisitorWrapper formatVisitor = mapper.schemaFactory().getFormatVisitor(ObjectMapper.DefaultTyping.JAVA_LANG_NUMBER, null);
        intSerializer.acceptJsonFormatVisitor(formatVisitor, null);
        JsonNode expectedNode = mapper.valueToTree(Number.class);
        JsonNode actualNode = formatVisitor.getSchemaNode();
        assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testBaseClass_createContextual() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormat format = new JsonFormat();
        JsonSerializer<Object> intSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonSerializer<Object> intSerializerWithFormat = intSerializer.createContextual(mapper.getSerializerProvider(), new StdTestBean());
        JsonNode expectedNode = mapper.valueToTree(Number.class);
        JsonNode actualNode = mapper.treeToNode(intSerializerWithFormat, new Object(), null, 0, null, null, null);
        assertEquals(expectedNode, actualNode);
    }
}