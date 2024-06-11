package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.*;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionBasic() {
        String msg = "Test JsonMappingException";
        JsonMappingException e = new JsonMappingException(msg);
        assertEquals(msg, e.getMessage());
        assertNull(e.getPath());
        assertNull(e.getFrom());
        assertNull(e.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionWithProcessor() {
        JsonParser jsonParser = createMock(JsonParser.class);
        String msg = "Test JsonMappingException with Processor";
        JsonMappingException e = new JsonMappingException(jsonParser, msg);
        assertEquals(msg, e.getMessage());
        assertEquals(jsonParser, e.getProcessor());
        assertNull(e.getPath());
        assertNull(e.getFrom());
    }

    @Test
    public void testJsonMappingExceptionWithLocation() {
        JsonParser jsonParser = createMock(JsonParser.class);
        LocationLocation location = createMock(LocationLocation.class);
        String msg = "Test JsonMappingException with Location";
        JsonMappingException e = new JsonMappingException(jsonParser, msg, location);
        assertEquals(msg, e.getMessage());
        assertEquals(jsonParser, e.getProcessor());
        assertEquals(location, e.getLocation());
        assertNull(e.getPath());
        assertNull(e.getFrom());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonProcessingException jsonProcessingException = objectMapper.getDeserializationConfig().withoutFeatures(JsonParser.Feature.FAIL_ON_EMPTY_BEANS).readValue(new ByteArrayInputStream(new byte[0]), String.class);
        JsonMappingException e = JsonMappingException.from(jsonProcessingException);
        List<JsonMappingException.Reference> references = e.getPath();
        assertTrue(references.isEmpty());

        String json = "{\"name\":\"John\",\"age\":30,\"cars\":[{\"color\":\"red\",\"model\":\"Corolla\"},{\"color\":\"blue\",\"model\":\"Camry\"}]}";
        JsonNode node = new ObjectMapper().readTree(json);
        JsonParser jsonParser = new JsonParser(new ByteArrayInputStream(json.getBytes()));
        JsonMappingException e2 = JsonMappingException.from(jsonParser, "Test JsonMappingException with Path");
        references = e2.getPath();
        assertEquals(1, references.size());
        JsonMappingException.Reference reference = references.get(0);
        assertEquals("cars", reference.getFieldName());
        assertEquals(-1, reference.getIndex());
        assertNull(reference.getFrom());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndFrom() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonProcessingException jsonProcessingException = objectMapper.getDeserializationConfig().withoutFeatures(JsonParser.Feature.FAIL_ON_EMPTY_BEANS).readValue(new ByteArrayInputStream(new byte[0]), String.class);
        Object referrer = new Object();
        String json = "{\"name\":\"John\",\"age\":30,\"cars\":[{\"color\":\"red\",\"model\":\"Corolla\"},{\"color\":\"blue\",\"model\":\"Camry\"}]}";
        JsonNode node = new ObjectMapper().readTree(json);
        JsonParser jsonParser = new JsonParser(new ByteArrayInputStream(json.getBytes()));
        JsonMappingException e = JsonMappingException.wrapWithPath(jsonProcessingException, referrer, "cars");
        List<JsonMappingException.Reference> references = e.getPath();
        assertEquals(1, references.size());
        JsonMappingException.Reference reference = references.get(0);
        assertEquals("cars", reference.getFieldName());
        assertEquals(referrer, reference.getFrom());
        assertNull(reference.getIndex());
    }

    @Test
    public void testJsonMappingExceptionWithPathAndFromAndIndex() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonProcessingException jsonProcessingException = objectMapper.getDeserializationConfig().withoutFeatures(JsonParser.Feature.FAIL_ON_EMPTY_BEANS).readValue(new ByteArrayInputStream(new byte[0]), String.class);
        Object referrer = new Object();
        JsonNode node = new ObjectMapper().readTree("{\"name\":\"John\",\"age\":30,\"cars\":[{\"color\":\"red\",\"model\":\"Corolla\",\"passengers\":[{\"name\":\"Alice\",\"age\":25},{\"name\":\"Bob\",\"age\":30}]}]}");
        JsonParser jsonParser = new JsonParser(new ByteArrayInputStream(node.toString().getBytes()));
        JsonMappingException e = JsonMappingException.wrapWithPath(jsonProcessingException, referrer, "cars", 1);
        List<JsonMappingException.Reference> references = e.getPath();
        assertEquals(2, references.size());
        JsonMappingException.Reference reference1 = references.get(0);
        assertEquals("cars", reference1.getFieldName());
        assertEquals(referrer, reference1.getFrom());
        assertEquals(1, reference1.getIndex());
        JsonMappingException.Reference reference2 = references.get(1);
        assertEquals("passengers", reference2.getFieldName());
        assertEquals(null, reference2.getFrom());
        assertEquals(-1, reference2.getIndex());
    }

    @Test
    public void testJsonMappingExceptionWithClass() {
        String msg = "Test JsonMappingException with Class";
        JsonMappingException e = JsonMappingException.fromUnexpectedIOE(new IOException(msg));
        assertEquals(msg + " (of type IOException)", e.getMessage());
        assertNull(e.getPath());
        assertNull(e.getFrom());
        assertNull(e.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionWithClassAndPath() {
        String msg = "Test JsonMappingException with Class and Path";
        IOException ioException = new IOException(msg);
        Object referrer = new Object();
        String json = "{\"name\":\"John\",\"age\":30,\"cars\":[{\"color\":\"red\",\"model\":\"Corolla\"},{\"color\":\"blue\",\"model\":\"Camry\"}]}";
        JsonNode node = new ObjectMapper().readTree(json);
        JsonParser jsonParser = new JsonParser(new ByteArrayInputStream(json.getBytes()));
        JsonMappingException e = JsonMappingException.wrapWithPath(ioException, referrer, "cars");
        assertEquals(msg + " (of type IOException)", e.getMessage());
        List<JsonMappingException.Reference> references = e.getPath();
        assertEquals(1, references.size());
        JsonMappingException.Reference reference = references.get(0);
        assertEquals("cars", reference.getFieldName());
        assertEquals(referrer, reference.getFrom());
        assertNull(reference.getIndex());
    }
}