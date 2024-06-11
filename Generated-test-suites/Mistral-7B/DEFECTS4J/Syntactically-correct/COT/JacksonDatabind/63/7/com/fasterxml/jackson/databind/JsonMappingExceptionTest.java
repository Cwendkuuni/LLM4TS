package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.DefaultPreprendableCharArray;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.ReferenceNameTransformer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class JsonMappingExceptionTest {

    @BeforeClass
    public static void setup() {
        // Initialize ObjectMapper for serialization and deserialization tests
        JsonMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    // Basic test cases for constructors

    @Test
    public void testJsonMappingExceptionNoArgs() {
        try {
            new JsonMappingException("");
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testJsonMappingExceptionString() {
        String message = "An error occurred.";
        JsonMappingException e = new JsonMappingException(message);
        assertEquals(message, e.getMessage());
    }

    @Test
    public void testJsonMappingExceptionStringThrowable() {
        String message = "An error occurred.";
        Throwable cause = new IOException("Caused by an I/O error.");
        JsonMappingException e = new JsonMappingException(message, cause);
        assertEquals(message, e.getMessage());
        assertEquals(cause, e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionStringJsonLocation() {
        String message = "An error occurred.";
        JsonLocation location = new JsonLocation(1, 1, "source.json", "path", 1234);
        JsonMappingException e = new JsonMappingException(message, location);
        assertEquals(message, e.getMessage());
        assertEquals(location, e.getLocation());
    }

    @Test
    public void testJsonMappingExceptionStringJsonLocationThrowable() {
        String message = "An error occurred.";
        JsonLocation location = new JsonLocation(1, 1, "source.json", "path", 1234);
        Throwable cause = new IOException("Caused by an I/O error.");
        JsonMappingException e = new JsonMappingException(message, location, cause);
        assertEquals(message, e.getMessage());
        assertEquals(location, e.getLocation());
        assertEquals(cause, e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionCloseableString() {
        Closeable processor = new ObjectMapper().getFactory().createParser("source.json");
        String message = "An error occurred.";
        JsonMappingException e = new JsonMappingException(processor, message);
        assertNotNull(e.getProcessor());
        assertEquals(message, e.getMessage());
    }

    @Test
    public void testJsonMappingExceptionCloseableStringThrowable() {
        Closeable processor = new ObjectMapper().getFactory().createParser("source.json");
        String message = "An error occurred.";
        Throwable cause = new IOException("Caused by an I/O error.");
        JsonMappingException e = new JsonMappingException(processor, message, cause);
        assertNotNull(e.getProcessor());
        assertEquals(message, e.getMessage());
        assertEquals(cause, e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionCloseableStringJsonLocation() {
        Closeable processor = new ObjectMapper().getFactory().createParser("source.json");
        String message = "An error occurred.";
        JsonLocation location = new JsonLocation(1, 1, "source.json", "path", 1234);
        JsonMappingException e = new JsonMappingException(processor, message, location);
        assertNotNull(e.getProcessor());
        assertEquals(message, e.getMessage());
        assertEquals(location, e.getLocation());
    }

    @Test
    public void testJsonMappingExceptionCloseableStringJsonLocationThrowable() {
        Closeable processor = new ObjectMapper().getFactory().createParser("source.json");
        String message = "An error occurred.";
        JsonLocation location = new JsonLocation(1, 1, "source.json", "path", 1234);
        Throwable cause = new IOException("Caused by an I/O error.");
        JsonMappingException e = new JsonMappingException(processor, message, location, cause);
        assertNotNull(e.getProcessor());
        assertEquals(message, e.getMessage());
        assertEquals(location, e.getLocation());
        assertEquals(cause, e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionParserString() {
        JsonParser parser = new ObjectMapper().getFactory().createParser("source.json");
        String message = "An error occurred.";
        JsonMappingException e = JsonMappingException.from(parser, message);
        assertNotNull(e.getProcessor());
        assertEquals(message, e.getMessage());
    }

    @Test
    public void testJsonMappingExceptionParserStringThrowable() {
        JsonParser parser = new ObjectMapper().getFactory().createParser("source.json");
        String message = "An error occurred.";
        Throwable cause = new IOException("Caused by an I/O error.");
        JsonMappingException e = JsonMappingException.from(parser, message, cause);
        assertNotNull(e.getProcessor());
        assertEquals(message, e.getMessage());
        assertEquals(cause, e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionGeneratorString() {
        JsonGenerator generator = mapper.getFactory().createGenerator(new DefaultPreprendableCharArray(1024), JsonGenerator.Feature.AUTO_CLOSE);
        String message = "An error occurred.";
        JsonMappingException e = JsonMappingException.from(generator, message);
        assertNotNull(e.getProcessor());
        assertEquals(message, e.getMessage());
    }

    @Test
    public void testJsonMappingExceptionGeneratorStringThrowable() {
        JsonGenerator generator = mapper.getFactory().createGenerator(new DefaultPreprendableCharArray(1024), JsonGenerator.Feature.AUTO_CLOSE);
        String message = "An error occurred.";
        Throwable cause = new IOException("Caused by an I/O error.");
        JsonMappingException e = JsonMappingException.from(generator, message, cause);
        assertNotNull(e.getProcessor());
        assertEquals(message, e.getMessage());
        assertEquals(cause, e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionContextString() {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String message = "An error occurred.";
        JsonMappingException e = JsonMappingException.from(ctxt, message);
        assertNotNull(e.getProcessor());
        assertEquals(message, e.getMessage());
    }

    @Test
    public void testJsonMappingExceptionContextStringThrowable() {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String message = "An error occurred.";
        Throwable cause = new IOException("Caused by an I/O error.");
        JsonMappingException e = JsonMappingException.from(ctxt, message, cause);
        assertNotNull(e.getProcessor());
        assertEquals(message, e.getMessage());
        assertEquals(cause, e.getRootCause());
    }

    @Test
    public void testJsonMappingExceptionProviderString() {
        SerializerProvider ctxt = mapper.getSerializerProvider();
        String message = "An error occurred.";
        JsonMappingException e = JsonMappingException.from(ctxt, message);
        assertNotNull(e.getProcessor());
        assertEquals(message, e.getMessage());
    }

    @Test
    public void testJsonMappingExceptionProviderStringThrowable() {
        SerializerProvider ctxt = mapper.getSerializerProvider();
        String message = "An error occurred.";
        Throwable cause = new IOException("Caused by an I/O error.");
        JsonMappingException e = JsonMappingException.from(ctxt, message, cause);
        assertNotNull(e.getProcessor());
        assertEquals(message, e.getMessage());
        assertEquals(cause, e.getRootCause());
    }

    // Test cases for static factory methods

    @Test
    public void testFromUnexpectedIOE() {
        IOException ioe = new IOException("Unexpected IO Exception: Unexpected EOF");
        JsonMappingException jme = JsonMappingException.fromUnexpectedIOE(ioe);
        assertEquals("Unexpected IOException (of type IOException): Unexpected EOF", jme.getMessage());
    }

    @Test
    public void testWrapWithPathObjectString() {
        Object referrer = new Object();
        String fieldName = "field1";
        Throwable cause = new IOException("Caused by an I/O error.");
        JsonMappingException e = JsonMappingException.wrapWithPath(cause, referrer, fieldName);
        assertNotNull(e.getProcessor());
        assertEquals("An error occurred. (through reference chain: <Object>->" + fieldName + ")", e.getMessage());
        assertEquals(cause, e.getRootCause());
    }

    @Test
    public void testWrapWithPathObjectInt() {
        Object referrer = new Object();
        int index = 0;
        Throwable cause = new IOException("Caused by an I/O error.");
        JsonMappingException e = JsonMappingException.wrapWithPath(cause, referrer, index);
        assertNotNull(e.getProcessor());
        assertEquals("An error occurred. (through reference chain: <Object>[0])", e.getMessage());
        assertEquals(cause, e.getRootCause());
    }

    // Test cases for accessors and mutators

    @Test
    public void testGetPath() {
        Closeable processor = new ObjectMapper().getFactory().createParser("source.json");
        String message = "An error occurred.";
        JsonMappingException e = new JsonMappingException(processor, message);
        e.prependPath(new Object(), "field1");
        e.prependPath(new Object(), "field2");
        List<Reference> path = e.getPath();
        assertEquals(2, path.size());
        Reference reference1 = path.get(0);
        assertEquals(JsonMappingException.Reference.class, reference1.getClass());
        assertEquals(new Object(), reference1.getFrom());
        assertEquals("field1", reference1.getFieldName());
        Reference reference2 = path.get(1);
        assertEquals(JsonMappingException.Reference.class, reference2.getClass());
        assertEquals(new Object(), reference2.getFrom());
        assertEquals("field2", reference2.getFieldName());
    }

    @Test
    public void testGetPathReference() {
        Closeable processor = new ObjectMapper().getFactory().createParser("source.json");
        String message = "An error occurred.";
        JsonMappingException e = new JsonMappingException(processor, message);
        e.prependPath(new Object(), "field1");
        e.prependPath(new Object(), "field2");
        StringBuilder sb = new StringBuilder();
        e.getPathReference(sb);
        assertEquals("An error occurred. (through reference chain: <Object>->field1->field2)", sb.toString());
    }

    @Test
    public void testPrependPath() {
        Closeable processor = new ObjectMapper().getFactory().createParser("source.json");
        String message = "An error occurred.";
        JsonMappingException e = new JsonMappingException(processor, message);
        Object referrer = new Object();
        String fieldName = "field1";
        e.prependPath(referrer, fieldName);
        assertEquals(1, e.getPath().size());
        Reference reference = e.getPath().get(0);
        assertEquals(JsonMappingException.Reference.class, reference.getClass());
        assertEquals(referrer, reference.getFrom());
        assertEquals(fieldName, reference.getFieldName());
    }

    @Test(expected = IllegalStateException.class)
    public void testPrependPathWithMaxReferences() {
        Closeable processor = new ObjectMapper().getFactory().createParser("source.json");
        String message = "An error occurred.";
        JsonMappingException e = new JsonMappingException(processor, message);
        for (int i = 0; i < JsonMappingException.MAX_REFS_TO_LIST + 1; i++) {
            Object referrer = new Object();
            String fieldName = "field1";
            e.prependPath(referrer, fieldName);
        }
    }
}