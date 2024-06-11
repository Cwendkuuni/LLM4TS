import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {

    // Test case for serialize method with Date object
    @Test
    public void testSerializeWithDate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(date, generator, mapper.getSerializationConfig());

        String expected = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);
        assertEquals(expected, generator.getOutputContentAsString());
    }

    // Test case for serialize method with String object
    @Test
    public void testSerializeWithString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String str = "Hello World";
        StdKeySerializer serializer = new StdKeySerializer();

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(str, generator, mapper.getSerializationConfig());

        String expected = "\"Hello World\"";
        assertEquals(expected, generator.getOutputContentAsString());
    }

    // Test case for getSchema method
    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode schema = serializer.getSchema(mapper.getSerializationConfig(), Object.class);

        ObjectNode expected = mapper.createObjectNode();
        expected.put("type", "string");
        assertEquals(expected, schema);
    }

    // Test case for acceptJsonFormatVisitor method
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        ObjectNode rootNode = mapper.createObjectNode();
        JsonFormatVisitorWrapper visitor = new JsonSerializer.StdJsonFormatVisitor(rootNode, mapper.getSerializationConfig());

        serializer.acceptJsonFormatVisitor(visitor, Object.class);

        String expected = "{\"type\":\"string\"}";
        assertEquals(expected, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));
    }

    // Edge case test for null value
    @Test(expected = JsonProcessingException.class)
    public void testSerializeWithNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(null, generator, mapper.getSerializationConfig());

        // This test should throw JsonProcessingException
    }

    // Edge case test for empty String
    @Test
    public void testSerializeWithEmptyString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        String emptyString = "";
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(emptyString, generator, mapper.getSerializationConfig());

        String expected = "\"\"";
        assertEquals(expected, generator.getOutputContentAsString());
    }

    // Exception handling test for ClassCastException
    @Test(expected = ClassCastException.class)
    public void testSerializeWithNonStringOrDate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        Integer number = 123;
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(number, generator, mapper.getSerializationConfig());

        // This test should throw ClassCastException
    }
}