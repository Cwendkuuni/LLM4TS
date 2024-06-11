package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testWrapWithPath() {
        Exception sourceException = new IllegalArgumentException("Source exception");
        Object refFrom = new Object();
        String refFieldName = "fieldName";

        JsonMappingException jme1 = JsonMappingException.wrapWithPath(sourceException, refFrom, refFieldName);
        Reference reference = jme1.getPath().get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(refFieldName, reference.getFieldName());

        assertEquals("Source exception (through reference chain: " + refFrom.getClass().getName() + "." + refFieldName + ")", jme1.getMessage());

        Object refFromIndexed = new Object()[0];
        int index = 0;
        JsonMappingException jme2 = JsonMappingException.wrapWithPath(sourceException, refFromIndexed, index);
        reference = jme2.getPath().get(0);
        assertEquals(refFromIndexed, reference.getFrom());
        assertEquals(index, reference.getIndex());

        assertEquals("Source exception (through reference chain: " + refFromIndexed.getClass().getName() + "[" + index + "])", jme2.getMessage());
    }

    @Test
    public void testGetPathReference() {
        Exception sourceException = new IllegalArgumentException("Source exception");
        Object refFrom = new Object();
        String refFieldName = "fieldName";

        JsonMappingException jme = JsonMappingException.wrapWithPath(sourceException, refFrom, refFieldName);
        assertEquals("Source exception (through reference chain: " + refFrom.getClass().getName() + "." + refFieldName + ")", jme.getPathReference());

        Object refFromIndexed = new Object()[0];
        int index = 0;
        jme = JsonMappingException.wrapWithPath(sourceException, refFromIndexed, index);
        assertEquals("Source exception (through reference chain: " + refFromIndexed.getClass().getName() + "[" + index + "])", jme.getPathReference());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapWithPathNullReference() {
        Exception sourceException = new IllegalArgumentException("Source exception");
        JsonMappingException.wrapWithPath(sourceException, null, "fieldName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapWithPathNullRefFieldName() {
        Exception sourceException = new IllegalArgumentException("Source exception");
        Object refFrom = new Object();
        JsonMappingException.wrapWithPath(sourceException, refFrom, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapWithPathNegativeIndex() {
        Exception sourceException = new IllegalArgumentException("Source exception");
        Object refFrom = new Object()[0];
        JsonMappingException.wrapWithPath(sourceException, refFrom, -1);
    }

    @Test
    public void testGetPathEmpty() {
        JsonMappingException jme = new JsonMappingException("Message");
        List<Reference> path = jme.getPath();
        assertEquals(Collections.emptyList(), path);
    }

    @Test
    public void testGetPathNotEmpty() {
        Exception sourceException = new IllegalArgumentException("Source exception");
        Object refFrom = new Object();
        String refFieldName = "fieldName";

        JsonMappingException jme = JsonMappingException.wrapWithPath(sourceException, refFrom, refFieldName);
        List<Reference> path = jme.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(refFieldName, reference.getFieldName());

        assertEquals("Source exception (through reference chain: " + refFrom.getClass().getName() + "." + refFieldName + ")", jme.getMessage());
    }

    @Test
    public void testPrependPath() {
        Exception sourceException = new IllegalArgumentException("Source exception");
        Object refFrom1 = new Object();
        String refFieldName1 = "fieldName1";
        Object refFrom2 = new Object();
        String refFieldName2 = "fieldName2";

        JsonMappingException jme = JsonMappingException.wrapWithPath(sourceException, refFrom1, refFieldName1);
        jme.prependPath(refFrom2, refFieldName2);

        List<Reference> path = jme.getPath();
        assertEquals(2, path.size());
        Reference reference1 = path.get(0);
        assertEquals(refFrom2, reference1.getFrom());
        assertEquals(refFieldName2, reference1.getFieldName());

        Reference reference2 = path.get(1);
        assertEquals(refFrom1, reference2.getFrom());
        assertEquals(refFieldName1, reference2.getFieldName());

        assertEquals("Source exception (through reference chain: " + refFrom2.getClass().getName() + "." + refFieldName2 + "->" + refFrom1.getClass().getName() + "." + refFieldName1 + ")", jme.getMessage());
    }

    @Test
    public void testToString() {
        Exception sourceException = new IllegalArgumentException("Source exception");
        Object refFrom = new Object();
        String refFieldName = "fieldName";

        JsonMappingException jme = JsonMappingException.wrapWithPath(sourceException, refFrom, refFieldName);
        assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Source exception (through reference chain: " + refFrom.getClass().getName() + "." + refFieldName + ")", jme.toString());
    }

    @Test
    public void testFromJsonParser() {
        String input = "{\"name\":\"John Doe\",\"age\":18}";
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(new StringReader(input));

        JsonMappingException jme = JsonMappingException.from(jsonParser, "Error message");
        List<Reference> path = jme.getPath();
        assertEquals(0, path.size());
        assertEquals("Error message", jme.getMessage());
    }

    @Test
    public void testFromJsonParserWithPath() {
        String input = "[1,2,3,null]";
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(new StringReader(input));

        Object refFrom = new Object() {
            int value = 1;
        };
        int index = 2;
        JsonMappingException jme = JsonMappingException.from(jsonParser, "Error message", refFrom, index);
        List<Reference> path = jme.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(index, reference.getIndex());

        assertEquals("Error message (through reference chain: com.example.MyObject[1])\n  Path: [class com.example.MyObject, list[0]]", jme.getMessage());
    }

    @Test
    public void testFromJsonGenerator() {
        ObjectWriter objectWriter = new ObjectMapper().writer();
        StringWriter stringWriter = new StringWriter();

        JsonMappingException jme = JsonMappingException.from(objectWriter, "Error message");
        List<Reference> path = jme.getPath();
        assertEquals(0, path.size());
        assertEquals("Error message", jme.getMessage());
    }

    @Test
    public void testFromJsonGeneratorWithPath() {
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        ObjectWriter objectWriter = objectMapper.writer();

        JsonMappingException jme = JsonMappingException.from(objectWriter, "Error message", new Reference(new Object(), "fieldName"));
        List<Reference> path = jme.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(new Object(), reference.getFrom());
        assertEquals("fieldName", reference.getFieldName());

        assertEquals("Error message (through reference chain: \"fieldName\")", jme.getMessage());
    }

    @Test
    public void testFromDeserializationContext() {
        JsonFactory jsonFactory = new JsonFactory();
        DeserializationContext deserializationContext = jsonFactory.getDeserializationContext();

        String input = "{\"name\":\"John Doe\",\"age\":18}";
        JsonParser jsonParser = jsonFactory.createParser(new StringReader(input));
        JsonMappingException jme = JsonMappingException.from(deserializationContext, "Error message");
        List<Reference> path = jme.getPath();
        assertEquals(0, path.size());
        assertEquals("Error message", jme.getMessage());
    }

    @Test
    public void testFromDeserializationContextWithPath() {
        JsonFactory jsonFactory = new JsonFactory();
        DeserializationContext deserializationContext = jsonFactory.getDeserializationContext();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory2 = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory2.createParser(new StringReader("[1,2,3,null]"));

        Object refFrom = new Object() {
            int value = 1;
        };
        int index = 2;
        JsonMappingException jme = JsonMappingException.from(deserializationContext, "Error message", refFrom, index);
        List<Reference> path = jme.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals(index, reference.getIndex());

        assertEquals("Error message (through reference chain: com.example.MyObject[1])\n  Path: [class com.example.MyObject, list[0]]", jme.getMessage());
    }

    @Test
    public void testFromSerializerProvider() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        StringWriter stringWriter = new StringWriter();
        ObjectWriter objectWriter = jsonMapper.writer();

        JsonMappingException jme = JsonMappingException.from(objectWriter, "Error message");
        List<Reference> path = jme.getPath();
        assertEquals(0, path.size());
        assertEquals("Error message", jme.getMessage());
    }

    @Test
    public void testFromSerializerProviderWithPath() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        StringWriter stringWriter = new StringWriter();
        ObjectWriter objectWriter = jsonMapper.writer();

        Object refFrom = new Object() {
            int value = 1;
        };
        int index = 2;
        JsonMappingException jme = JsonMappingException.from(objectWriter, "Error message", new Reference(refFrom, "fieldName"));
        List<Reference> path = jme.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(refFrom, reference.getFrom());
        assertEquals("fieldName", reference.getFieldName());

        assertEquals("Error message (through reference chain: \"fieldName\")", jme.getMessage());
    }

    @Test
    public void testFromUnexpectedIOE() {
        IOException ioException = new IOException("IO exception");
        JsonMappingException jme = JsonMappingException.fromUnexpectedIOE(ioException);
        List<Reference> path = jme.getPath();
        assertEquals(0, path.size());
        assertEquals("Unexpected IOException (of type java.io.IOException): IO exception", jme.getMessage());
    }
}