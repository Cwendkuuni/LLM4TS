package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.parser.ParserRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonMappingExceptionTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.registerModule(new SimpleModule().addSerializer(Integer.class, new IntSerializer()));
        MAPper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void testSimpleSerializationException() throws JsonProcessingException {
        try {
            MAPper.writeValueAsString(null);
        } catch (JsonMappingException e) {
            assertEquals("Null value (of type class [null])", e.getMessage());
            assertEquals("jsonMappingException", e.getDescription());
            assertEquals(JsonMappingException.class, e.getClass());
            assertEquals(Mapper.class, e.getProcessor());
            assertEquals(0, e.getPath().size());
        }
    }

    @Test
    public void testComplexSerializationException() throws JsonProcessingException {
        ObjectNode root = MAPper.createObjectNode();
        root.set("a", MAPper.valueToTree(1));
        root.set("b.c", MAPper.valueToTree(new ComplexType()));

        try {
            String json = MAPper.writeValueAsString(root);
        } catch (JsonMappingException e) {
            assertEquals("Root element 'root' must contain exactly one element of type com.fasterxml.jackson.databind.test.ComplexType", e.getMessage());
            assertEquals("jsonMappingException", e.getDescription());
            assertEquals(JsonMappingException.class, e.getClass());
            assertEquals(ObjectMapper.class, e.getProcessor());
            List<Reference> path = e.getPath();
            assertEquals(2, path.size());
            Reference last = path.get(path.size() - 1);
            assertEquals("b.c", last.getFieldName());
            assertEquals(1, last.getIndex());
            Reference first = path.get(0);
            assertEquals("root", first.getFieldName());
            assertEquals(-1, first.getIndex());
        }
    }

    @Test
    public void testDeserializationException() throws IOException {
        ParserRegistry registry = MAPper.getFactory().getParserRegistry();
        StandardParser parser = registry.getPrimaryParser();
        parser.setCodec(MAPper);

        String json = "{\"a\":1,\"b\":null,\"c\":[1,2,3]}";
        try {
            ObjectNode tree = parser.readValueAsTree(json);
        } catch (JsonMappingException e) {
            assertEquals("Can not deserialize instance of com.fasterxml.jackson.databind.test.ComplexType out of START_OBJECT token", e.getMessage());
            assertEquals("jsonMappingException", e.getDescription());
            assertEquals(JsonMappingException.class, e.getClass());
            assertEquals(parser, e.getProcessor());
            List<Reference> path = e.getPath();
            assertEquals(1, path.size());
            Reference ref = path.get(0);
            assertEquals("a", ref.getFieldName());
            assertEquals(-1, ref.getIndex());
        }
    }

    @Test
    public void testComplexDeserializationException() throws IOException {
        ParserRegistry registry = MAPper.getFactory().getParserRegistry();
        StandardParser parser = registry.getPrimaryParser();
        parser.setCodec(MAPper);

        String json = "{\"a\":{\"b\":null,\"c\":[1,2,3]}}";
        try {
            ObjectNode tree = parser.readValueAsTree(json);
        } catch (JsonMappingException e) {
            assertEquals("Can not deserialize instance of com.fasterxml.jackson.databind.test.ComplexType from ValueContainer (need to use JsonDeserializer)", e.getMessage());
            assertEquals("jsonMappingException", e.getDescription());
            assertEquals(JsonMappingException.class, e.getClass());
            assertEquals(parser, e.getProcessor());
            List<Reference> path = e.getPath();
            assertEquals(2, path.size());
            Reference last = path.get(path.size() - 1);
            assertEquals("b", last.getFieldName());
            assertEquals(-1, last.getIndex());
            Reference first = path.get(0);
            assertEquals("a", first.getFieldName());
            assertEquals(-1, first.getIndex());
        }
    }

    private static class ComplexType {
        private int i;
    }

    private static class IntSerializer extends JsonSerializer<Integer> {
        @Override
        public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(value.intValue());
        }
    }
}