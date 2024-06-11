import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.EnumSet;

import static org.junit.Assert.*;

/**
 * Tests for {@link EnumSerializer}.
 */
public class EnumSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeEnumUsingIndex() throws JsonGenerationException, IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.valueOf(MyEnumClass.values()), true);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new StringWriter());
        serializer.serialize(MyEnumClass.VALUE_1, gen, objectMapper.getSerializationConfig());
        String result = gen.getOutputFile().toString();
        assertEquals("0", result);
    }

    @Test
    public void testSerializeEnumUsingToString() throws JsonGenerationException, IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.valueOf(MyEnumClass.values()), false);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new StringWriter());
        serializer.serialize(MyEnumClass.VALUE_1, gen, objectMapper.getSerializationConfig());
        String result = gen.getOutputFile().toString();
        assertEquals("VALUE_1", result);
    }

    @Test
    public void testSerializeEnumUsingName() throws JsonGenerationException, IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.valueOf(MyEnumClass.values()), false);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new StringWriter());
        ObjectNode node = objectMapper.createObjectNode();
        node.put("foo", MyEnumClass.VALUE_1);
        serializer.serialize(node, gen, objectMapper.getSerializationConfig());
        String result = gen.getOutputFile().toString();
        assertEquals("\"VALUE_1\"", result);
    }

    @Test
    public void testSerializeEnumUsingCustomFormat() throws JsonGenerationException, IOException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setSerializationInclusion(JsonFormat.SerializedName.ALWAYS);
        EnumSerializer serializer = new EnumSerializer(EnumValues.valueOf(MyEnumClass.values()), false);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new StringWriter());
        ObjectNode node = objectMapper.createObjectNode();
        node.put("foo", MyEnumClass.VALUE_1);
        JsonSerializer<ObjectNode> serializerWrapper = new JsonSerializer<ObjectNode>() {
            @Override
            public void serialize(ObjectNode value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                serializer.serialize(value.get("foo"), gen, serializers);
            }
        };
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectWriter writer = objectMapper.writerWithView(StandardSerializerProvider.BaseSerializerProvider.instance());
        JsonSerializer<?> formattedSerializer = serializerWrapper.withResolved(serializers -> {
            serializers.findValueSerializer(ObjectNode.class);
            serializers.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
            serializers.addSerializer(ObjectNode.class, new JsonNodeSerializer(writer));
            JsonSerializer<ObjectNode> formatter = serializers.findValueSerializer(ObjectNode.class);
            formatter.acceptJsonFormatVisitor(format, null);
            return formatter;
        });
        formattedSerializer.serialize(node, gen, serializers);
        String result = gen.getOutputFile().toString();
        assertEquals("\"VALUE_1\"", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeEnumUsingNullValue() throws JsonGenerationException, IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.valueOf(MyEnumClass.values()), false);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new StringWriter());
        serializer.serialize(null, gen, objectMapper.getSerializationConfig());
        String result = gen.getOutputFile().toString();
        fail("Expected JsonMappingException but got: " + result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeEnumWithInvalidShape() throws JsonGenerationException, IOException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(Shape.CUSTOM);
        EnumSerializer serializer = new EnumSerializer(EnumValues.valueOf(MyEnumClass.values()), false);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new StringWriter());
        ObjectNode node = objectMapper.createObjectNode();
        node.put("foo", MyEnumClass.VALUE_1);
        JsonSerializer<ObjectNode> serializerWrapper = new JsonSerializer<ObjectNode>() {
            @Override
            public void serialize(ObjectNode value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                serializer.serialize(value.get("foo"), gen, serializers);
            }
        };
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectWriter writer = objectMapper.writerWithView(StandardSerializerProvider.BaseSerializerProvider.instance());
        JsonSerializer<?> formattedSerializer = serializerWrapper.withResolved(serializers -> {
            serializers.findValueSerializer(ObjectNode.class);
            serializers.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
            serializers.addSerializer(ObjectNode.class, new JsonNodeSerializer(writer));
            JsonSerializer<ObjectNode> formatter = serializers.findValueSerializer(ObjectNode.class);
            formatter.acceptJsonFormatVisitor(format, null);
            return formatter;
        });
        try {
            formattedSerializer.serialize(node, gen, serializers);
            fail("Expected IllegalArgumentException but got: " + gen.getOutputFile().toString());
        } catch (JsonMappingException e) {
            // Expected
        }
        assertEquals(JsonMappingException.Class, e.getClass());
    }

    @Test
    public void testDeserializeEnumUsingIndex() throws IOException {
        JsonNode node = new ObjectMapper().readTree("0");
        Enum<?> deserialized = objectMapper.readValue(node.toString(), Enum.class);
        assertEquals(MyEnumClass.VALUE_1, deserialized);
    }

    @Test
    public void testDeserializeEnumUsingToString() throws IOException {
        JsonNode node = new ObjectMapper().readTree("\"VALUE_1\"");
        Enum<?> deserialized = objectMapper.readValue(node.toString(), Enum.class);
        assertEquals(MyEnumClass.VALUE_1, deserialized);
    }

    @Test
    public void testDeserializeEnumUsingName() throws IOException {
        JsonNode node = new ObjectMapper().readTree("{\"foo\": \"VALUE_1\"}");
        Enum<?> deserialized = objectMapper.readValue(node.toString(), Enum.class);
        assertEquals(MyEnumClass.VALUE_1, deserialized);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEnumUsingNullValue() throws IOException {
        JsonNode node = new ObjectMapper().readTree(null);
        try {
            Enum<?> deserialized = objectMapper.readValue(node.toString(), Enum.class);
            fail("Expected JsonMappingException but got: " + deserialized);
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEnumWithInvalidShape() throws IOException {
        JsonNode node = new ObjectMapper().readTree("{\"foo\": 123}");
        try {
            Enum<?> deserialized = objectMapper.readValue(node.toString(), Enum.class);
            fail("Expected JsonMappingException but got: " + deserialized);
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeEnumWithEnumSet() throws IOException {
        JsonNode node = new ObjectMapper().readTree("[\"VALUE_1\", \"VALUE_2\", \"VALUE_3\"]");
        EnumSet<MyEnumClass> deserialized = objectMapper.readValue(node.toString(), EnumSet.class);
        assertEquals(EnumSet.allOf(MyEnumClass.class), deserialized);
    }

    @After
    public void tearDown() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(stringWriter);
        gen.close();
    }

    private enum MyEnumClass {
        VALUE_1,
        VALUE_2,
        VALUE_3
    }
}