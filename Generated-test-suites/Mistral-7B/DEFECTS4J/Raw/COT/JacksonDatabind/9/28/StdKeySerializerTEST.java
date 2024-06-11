import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {

    // Test case for serialize method with null value
    @Test
    public void testSerializeWithNullValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(null, generator, null);
        generator.flush();

        String actual = generator.toString();
        String expected = "null";

        assertEquals(expected, actual);
    }

    // Test case for serialize method with Date value
    @Test
    public void testSerializeWithDateValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("key", serializer);

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        mapper.writeValue(generator, rootNode);

        generator.flush();
        generator.close();

        String json = generator.toString();
        JsonParser parser = mapper.getFactory().createParser(json);
        ObjectNode parsedNode = mapper.readValue(parser, ObjectNode.class);

        String actual = parsedNode.get("key").asText();
        String expected = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);

        assertEquals(expected, actual);
    }

    // Test case for serialize method with String value
    @Test
    public void testSerializeWithStringValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        String value = "TestString";

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("key", serializer);

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        mapper.writeValue(generator, rootNode);

        generator.flush();
        generator.close();

        String json = generator.toString();
        JsonParser parser = mapper.getFactory().createParser(json);
        ObjectNode parsedNode = mapper.readValue(parser, ObjectNode.class);

        String actual = parsedNode.get("key").asText();
        String expected = value;

        assertEquals(expected, actual);
    }

    // Test case for getSchema method
    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        ObjectNode schema = serializer.getSchema(null, null);

        String expected = "string";
        String actual = schema.getClass().getSimpleName();

        assertEquals(expected, actual);
    }

    // Test case for acceptJsonFormatVisitor method with null typeHint
    @Test
    public void testAcceptJsonFormatVisitorWithNullTypeHint() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        try {
            serializer.acceptJsonFormatVisitor(null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    // Test case for acceptJsonFormatVisitor method with valid typeHint
    @Test
    public void testAcceptJsonFormatVisitorWithValidTypeHint() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JavaType typeHint = mapper.getTypeFactory().constructType(String.class);
        serializer.acceptJsonFormatVisitor(null, typeHint);
    }
}