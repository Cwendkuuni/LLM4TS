package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for JsonMappingException
 */
public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() throws IOException {
        JsonFactory factory = new JsonFactory();

        // Test default constructor
        JsonMappingException e1 = new JsonMappingException("Message 1");
        assertEquals("Message 1", e1.getMessage());
        assertNull(e1.getPath());
        assertNull(e1.getProcessor());

        // Test constructor with message
        JsonMappingException e2 = new JsonMappingException(factory, "Message 2");
        assertEquals("Message 2", e2.getMessage());
        assertNull(e2.getPath());
        assertNotNull(e2.getProcessor());

        // Test constructor with message and location
        JsonMappingException e3 = new JsonMappingException(factory, "Message 3", null);
        assertEquals("Message 3", e3.getMessage());
        assertNull(e3.getPath());
        assertNotNull(e3.getProcessor());

        // Test constructor with message, location, and root cause
        JsonMappingException e4 = new JsonMappingException(factory, "Message 4", null, new IOException());
        assertEquals("Message 4", e4.getMessage());
        assertNull(e4.getPath());
        assertNotNull(e4.getProcessor());
        assertNotNull(e4.getCause());

        // Test constructor with processor
        Closeable processor = factory.getCarrier();
        JsonMappingException e5 = new JsonMappingException(processor, "Message 5");
        assertEquals("Message 5", e5.getMessage());
        assertNull(e5.getPath());
        assertNotNull(e5.getProcessor());

        // Test factory methods
        JsonParser p = factory.createParser(new ByteArrayInputStream(new byte[0]));
        JsonMappingException e6 = JsonMappingException.from(p, "Message 6");
        assertEquals("Message 6", e6.getMessage());
        assertNull(e6.getPath());
        assertNotNull(e6.getProcessor());

        JsonGenerator g = factory.createGenerator(new ByteArrayOutputStream());
        JsonMappingException e7 = JsonMappingException.from(g, "Message 7");
        assertEquals("Message 7", e7.getMessage());
        assertNull(e7.getPath());
        assertNotNull(e7.getProcessor());

        DeserializationContext ctxt = factory.createContext();
        JsonMappingException e8 = JsonMappingException.from(ctxt, "Message 8");
        assertEquals("Message 8", e8.getMessage());
        assertNull(e8.getPath());
        assertNotNull(e8.getProcessor());

        SerializerProvider sp = factory.createSerializerProvider();
        JsonMappingException e9 = JsonMappingException.from(sp, "Message 9");
        assertEquals("Message 9", e9.getMessage());
        assertNull(e9.getPath());
        assertNotNull(e9.getProcessor());

        // Test upgrade from IOException
        IOException ioe = new IOException("IOException message");
        JsonMappingException e10 = JsonMappingException.fromUnexpectedIOE(ioe);
        assertEquals("Unexpected IOException (of type IOException): IOException message", e10.getMessage());

        // Test wrapWithPath
        JsonParser p2 = factory.createParser(new ByteArrayInputStream(new byte[0]));
        Object refFrom = new Object();
        String refFieldName = "field1";
        JsonMappingException e11 = JsonMappingException.wrapWithPath(p2, refFrom, refFieldName);
        List<Reference> path = e11.getPath();
        assertEquals(1, path.size());
        Reference ref = path.get(0);
        assertEquals(refFrom, ref.getFrom());
        assertEquals(refFieldName, ref.getFieldName());

        Object refFrom2 = new Object[1];
        int index = 0;
        JsonMappingException e12 = JsonMappingException.wrapWithPath(p2, refFrom2, index);
        path = e12.getPath();
        assertEquals(1, path.size());
        ref = path.get(0);
        assertEquals(refFrom2, ref.getFrom());
        assertEquals(index, ref.getIndex());
    }
}