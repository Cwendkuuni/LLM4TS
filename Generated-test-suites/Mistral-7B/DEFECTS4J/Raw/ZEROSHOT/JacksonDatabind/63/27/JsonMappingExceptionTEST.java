package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test default constructor
        JsonMappingException e1 = new JsonMappingException("Message 1");
        assertEquals("Message 1", e1.getMessage());
        assertNull(e1.getPath());
        assertNull(e1.getProcessor());

        // Test constructor with message and location
        JsonParser parser = mapper.getFactory().createParser("[]");
        JsonMappingException e2 = new JsonMappingException(parser, "Message 2", new TextLocation(1, 2));
        assertEquals("Message 2", e2.getMessage());
        assertEquals(parser, e2.getProcessor());
        assertNotNull(e2.getLocalizedMessage());

        // Test constructor with message, location, and root cause
        JsonMappingException e3 = new JsonMappingException(parser, "Message 3", new TextLocation(1, 2), new IOException());
        assertEquals("Message 3", e3.getMessage());
        assertEquals(parser, e3.getProcessor());
        assertNotNull(e3.getLocalizedMessage());
        assertSame(e3.getCause(), new IOException());

        // Test construction from JsonParser
        JsonMappingException e4 = JsonMappingException.from(parser, "Message 4");
        assertEquals("Message 4", e4.getMessage());
        assertEquals(parser, e4.getProcessor());

        // Test construction from JsonGenerator
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        JsonMappingException e5 = JsonMappingException.from(generator, "Message 5");
        assertEquals("Message 5", e5.getMessage());
        assertNull(e5.getProcessor());

        // Test construction from DeserializationContext
        ObjectNode node = mapper.createObjectNode();
        JsonDeserializer<ObjectNode> deserializer = mapper.findValueDeserializer(ObjectNode.class, node.getClass());
        DeserializationContext context = mapper.getDeserializationContext();
        context.setRootValue(node);
        context.setCurrentValue(node);
        context.setCurrentLocation(parser.getCurrentLocation());
        JsonMappingException e6 = JsonMappingException.from(context, "Message 6");
        assertEquals("Message 6", e6.getMessage());
        assertEquals(parser, e6.getProcessor());

        // Test construction from SerializerProvider
        ObjectNode node2 = mapper.createObjectNode();
        JsonSerializer<ObjectNode> serializer = mapper.findValueSerializer(ObjectNode.class, node2.getClass());
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonMappingException e7 = JsonMappingException.from(provider, "Message 7");
        assertEquals("Message 7", e7.getMessage());
        assertNull(e7.getProcessor());

        // Test factory method fromUnexpectedIOE
        JsonProcessingException ioException = new JsonProcessingException("Unexpected IO Exception");
        JsonMappingException e8 = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type java.io.IOException): Unexpected IO Exception", e8.getMessage());
        assertNull(e8.getProcessor());

        // Test method wrapWithPath
        ObjectMapper mapper2 = new ObjectMapper();
        JsonParser parser2 = mapper2.getFactory().createParser("[]");
        Object obj = new Object();
        JsonMappingException e9 = new JsonMappingException(parser2, "Message 9");
        JsonMappingException e10 = JsonMappingException.wrapWithPath(e9, obj, "field1");
        assertEquals("Message 9 (through reference chain: field1->[])", e10.getMessage());
        List<Reference> path = e10.getPath();
        assertEquals(1, path.size());
        Reference ref = path.get(0);
        assertEquals(obj, ref.getFrom());
        assertEquals("field1", ref.getFieldName());

        JsonMappingException e11 = JsonMappingException.wrapWithPath(e9, obj, 1);
        assertEquals("Message 9 (through reference chain: [0]->[])", e11.getMessage());
        path = e11.getPath();
        assertEquals(1, path.size());
        ref = path.get(0);
        assertEquals(obj, ref.getFrom());
        assertEquals(1, ref.getIndex());
    }

    @Test
    public void testReference() {
        Object obj = new Object();
        Reference ref1 = new Reference(obj, "field1");
        assertEquals(obj, ref1.getFrom());
        assertEquals("field1", ref1.getFieldName());
        assertEquals("field1", ref1.toString());

        Reference ref2 = new Reference(obj, 0);
        assertEquals(obj, ref2.getFrom());
        assertEquals(0, ref2.getIndex());
        assertEquals("[0]", ref2.toString());
    }
}