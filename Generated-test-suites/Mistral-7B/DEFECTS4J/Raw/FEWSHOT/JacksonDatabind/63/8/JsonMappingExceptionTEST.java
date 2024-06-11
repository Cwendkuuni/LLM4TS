package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        JsonMappingException e = new JsonMappingException("Message");
        assertEquals("Message", e.getMessage());
        assertNull(e.getPath());

        JsonMappingException e1 = new JsonMappingException("Message", new JsonLocation(1, 1, "test.json")); {
            @JsonIgnore
            Closeable processor = null;
        };
        assertEquals("Message", e1.getMessage());
        assertEquals(new JsonLocation(1, 1, "test.json"), e1.getOriginalLocation());
        assertNull(e1.getProcessor());

        JsonMappingException e2 = new JsonMappingException("Message", new JsonLocation(1, 1, "test.json"), new IOException("IO Error")); {
            @JsonIgnore
            Closeable processor = null;
        };
        assertEquals("Message", e2.getMessage());
        assertEquals(new JsonLocation(1, 1, "test.json"), e2.getOriginalLocation());
        assertNull(e2.getProcessor());

        JsonParser parser = null;
        JsonMappingException e3 = JsonMappingException.from(parser, "Message");
        assertEquals("Message", e3.getMessage());
        assertNull(e3.getPath());

        JsonParser parser1 = null;
        JsonMappingException e4 = JsonMappingException.from(parser1, "Message", new IOException("IO Error"));
        assertEquals("Message", e4.getMessage());
        assertNull(e4.getPath());
        assertEquals("IO Error", e4.getCausedBy(0).getMessage());

        JsonGenerator generator = null;
        JsonMappingException e5 = JsonMappingException.from(generator, "Message");
        assertEquals("Message", e5.getMessage());
        assertNull(e5.getPath());

        JsonGenerator generator1 = null;
        JsonMappingException e6 = JsonMappingException.from(generator1, "Message", new IOException("IO Error"));
        assertEquals("Message", e6.getMessage());
        assertNull(e6.getPath());
        assertEquals("IO Error", e6.getCausedBy(0).getMessage());

        DeserializationContext context = null;
        JsonMappingException e7 = JsonMappingException.from(context, "Message");
        assertEquals("Message", e7.getMessage());
        assertNull(e7.getPath());

        DeserializationContext context1 = null;
        JsonMappingException e8 = JsonMappingException.from(context1, "Message", new IOException("IO Error"));
        assertEquals("Message", e8.getMessage());
        assertNull(e8.getPath());
        assertEquals("IO Error", e8.getCausedBy(0).getMessage());

        SerializerProvider provider = null;
        JsonMappingException e9 = JsonMappingException.from(provider, "Message");
        assertEquals("Message", e9.getMessage());
        assertNull(e9.getPath());

        SerializerProvider provider1 = null;
        JsonMappingException e10 = JsonMappingException.from(provider1, "Message", new IOException("IO Error"));
        assertEquals("Message", e10.getMessage());
        assertNull(e10.getPath());
        assertEquals("IO Error", e10.getCausedBy(0).getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        JsonParser parser = null;
        Object referrer = new Object();
        String fieldName = "fieldName";
        JsonMappingException e = JsonMappingException.wrapWithPath(new IOException("IO Error"), referrer, fieldName);
        assertEquals("IO Error", e.getCausedBy(0).getMessage());
        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals(referrer, reference.getFrom());
        assertEquals(fieldName, reference.getFieldName());

        JsonParser parser1 = null;
        Object referrer1 = new Object();
        int index = 0;
        JsonMappingException e1 = JsonMappingException.wrapWithPath(new IOException("IO Error"), referrer1, index);
        assertEquals("IO Error", e1.getCausedBy(0).getMessage());
        List<Reference> path1 = e1.getPath();
        assertEquals(1, path1.size());
        Reference reference1 = path1.get(0);
        assertEquals(referrer1, reference1.getFrom());
        assertEquals(index, reference1.getIndex());

        JsonMappingException e2 = JsonMappingException.wrapWithPath(new IOException("IO Error"), new Object()[1], 1);
        assertEquals("IO Error", e2.getCausedBy(0).getMessage());
        List<Reference> path2 = e2.getPath();
        assertEquals(1, path2.size());
        Reference reference2 = path2.get(0);
        assertNotNull(reference2.getFrom());
        assertEquals(1, reference2.getIndex());
    }
}