import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.ObjectStreamConstantPool;

public class StdDelegatingSerializerTest {

    private ObjectMapper mapper;
    private StdDelegatingSerializer<String> stringSerializer;
    private StdDelegatingSerializer<Integer> integerSerializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        Converter<String, Integer> converter = new StringToIntConverter();
        stringSerializer = new StdDelegatingSerializer<String>(converter);
        integerSerializer = new StdDelegatingSerializer<Integer>(Integer.class, converter);
    }

    @Test
    public void testSerialization_StringToInteger() throws JsonGenerationException, JsonMappingException, IOException {
        String input = "42";
        ObjectNode expectedOutput = mapper.createObjectNode();
        expectedOutput.put(0, "number");
        expectedOutput.put(1, 42);

        ObjectNode actualOutput = mapper.writeValueAsTree(stringSerializer.serialize(input));
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSerialization_NullInput() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectNode expectedOutput = mapper.createObjectNode();
        expectedOutput.putNull("number");

        ObjectNode actualOutput = mapper.writeValueAsTree(stringSerializer.serialize(null));
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSerialization_InvalidInput() throws JsonGenerationException, JsonMappingException, IOException {
        String input = "invalid";
        ObjectNode expectedOutput = mapper.createObjectNode();
        expectedOutput.put("message", "Failed to convert value of type java.lang.String to value of type class java.lang.Integer (for input \"invalid\"): com.fasterxml.jackson.databind.JsonMappingException: Failed to instantiate value of type [simple type, class java.lang.Integer] from JSON String [invalid]");

        try {
            stringSerializer.serialize(input);
        } catch (JsonMappingException e) {
            ObjectNode actualOutput = mapper.writeValueAsTree(e.asJson());
            Assert.assertEquals(expectedOutput, actualOutput);
        }
    }

    @Test
    public void testSerialization_IntegerSerializerIsCalled() throws JsonGenerationException, JsonMappingException, IOException {
        Integer input = 42;
        ObjectNode expectedOutput = mapper.createObjectNode();
        expectedOutput.put(0, "number");
        expectedOutput.put(1, 42);

        ObjectNode actualOutput = mapper.writeValueAsTree(integerSerializer.serialize(input));
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSerialization_NullDelegateValue() throws JsonGenerationException, JsonMappingException, IOException {
        String input = null;
        ObjectNode expectedOutput = mapper.createObjectNode();
        expectedOutput.putNull("number");

        ObjectNode actualOutput = mapper.writeValueAsTree(stringSerializer.serialize(input));
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDeserialization_IntegerToString() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper reverseMapper = new ObjectMapper();
        ObjectNode input = mapper.createObjectNode();
        input.put(0, "number");
        input.put(1, 42);

        String actualOutput = reverseMapper.readValue(input, String.class);
        Assert.assertEquals("42", actualOutput);
    }

    @Test
    public void testDeserialization_InvalidInput() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectNode input = mapper.createObjectNode();
        input.put("message", "Failed to convert value of type com.fasterxml.jackson.core.tree.ObjectNode to value of type class java.lang.String");

        try {
            mapper.readValue(input, String.class);
        } catch (JsonMappingException e) {
            Assert.assertEquals(input.toString(), e.asJson().toString());
        }
    }

    @Test
    public void testDeserialization_NullInput() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectNode input = mapper.createObjectNode();
        input.putNull("number");

        String actualOutput = mapper.readValue(input, String.class);
        Assert.assertNull(actualOutput);
    }

    @Test
    public void testDeserialization_EmptyInput() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectNode input = mapper.createObjectNode();

        String actualOutput = mapper.readValue(input, String.class);
        Assert.assertEquals("", actualOutput);
    }

    @Test
    public void testDeserialization_InvalidType() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectNode input = mapper.createObjectNode();
        input.put(0, "string");
        input.put(1, "test");

        try {
            mapper.readValue(input, Integer.class);
        } catch (JsonMappingException e) {
            Assert.assertEquals("Cannot deserialize value of type java.lang.Integer from Object value (number=\"string\")", e.getMessage());
        }
    }

    @Test
    public void testSchema() throws JsonMappingException {
        SchemaFactory schemaFactory = new JsonSchemaFactory();
        JsonSchema schema = mapper.getSerializationConfig().getDefaultSerializerProvider().getDefaultContextualSerializerProvider()
                .findValueSerializer(String.class).getSchema(mapper.getSerializationConfig(), String.class);
        JsonSchema expectedSchema = schemaFactory.getSchema(mapper.getSerializationConfig().findValueSerializer(Integer.class).getClass());

        JsonNode actualSchema = stringSerializer.getSchema(mapper.getSerializationConfig(), String.class);
        JsonNode expectedSchemaNode = schema.getSchemaProvider().getSchema(schema);

        Assert.assertEquals(expectedSchemaNode.toString(), actualSchema.toString());
        Assert.assertEquals(expectedSchema, stringSerializer.getDelegateSerializer().getSchema(mapper.getSerializationConfig(), Integer.class));
    }

    @Test
    public void testEmptyIsEmpty() throws JsonProcessingException {
        StdDelegatingSerializer<String> emptyStringSerializer = new StdDelegatingSerializer<String>(new StringConverter(), String.class, null);

        Assert.assertTrue(emptyStringSerializer.isEmpty(mapper.getSerializationConfig(), null));
    }

    @Test
    public void testEmptyIsNotEmpty() {
        StdDelegatingSerializer<String> nonEmptySerializer = new StdDelegatingSerializer<String>(new StringConverter(), String.class, null);

        Assert.assertFalse(nonEmptySerializer.isEmpty(mapper.getSerializationConfig(), new Object()));
    }
}