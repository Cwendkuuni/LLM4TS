package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonMappingExceptionTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSimpleException() throws Exception {
        JsonMappingException e = JsonMappingException.from(mapper, "Unexpected token (EOF) at line 1 column 1 path $");
        assertEquals("Unexpected token (EOF) at line 1 column 1 path $", e.getMessage());
        assertNull(e.getPath());
    }

    @Test
    public void testExceptionWithPath() throws Exception {
        JsonNode node = mapper.readTree("{\"a\": [1, 2]}");
        JsonMappingException e = JsonMappingException.from(mapper, "Unexpected token (EOF) at line 1 column 1 path /a/0/2", node.get(0).get(1));
        assertEquals("Unexpected token (EOF) at line 1 column 1 path /a/0/2", e.getMessage());
        assertEquals(1, e.getPath().size());
        Reference ref = e.getPath().get(0);
        assertEquals(JsonNode.class, ref.getFrom().getClass());
        assertEquals("a", ref.getFieldName());
        assertEquals(-1, ref.getIndex());
    }

    @Test
    public void testExceptionWithPathAndProcessor() throws Exception {
        JsonNode node = mapper.readTree("{\"a\": [1, 2]}");
        JsonMappingException e = JsonMappingException.from(mapper.getFactory().createParser(new ByteArrayInputStream(mapper.writeValueAsBytes(node))), "Unexpected token (EOF) at line 1 column 1 path /a/0/2", node.get(0).get(1));
        assertEquals("Unexpected token (EOF) at line 1 column 1 path /a/0/2", e.getMessage());
        assertEquals(1, e.getPath().size());
        Reference ref = e.getPath().get(0);
        assertNotNull(e.getProcessor());
        String desc = ref.getDescription();
        assertTrue(desc.startsWith("JsonParser"));
        assertTrue(desc.contains("JsonMappingExceptionTest"));
    }

    @Test(expected = JsonMappingException.class)
    public void testExceptionWithPathAndProcessorThrows() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException e = JsonMappingException.from(mapper.getFactory().createParser(new ByteArrayInputStream(mapper.writeValueAsBytes("{\"a\": [1, 2]}"))), "Unexpected token (EOF) at line 1 column 1 path /a/0/3", null);
    }
}