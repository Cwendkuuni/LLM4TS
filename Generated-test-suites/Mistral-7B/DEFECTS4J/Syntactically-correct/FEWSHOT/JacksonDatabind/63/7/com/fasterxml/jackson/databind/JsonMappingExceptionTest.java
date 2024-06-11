package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JsonMappingException.Reference;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testWrapWithPath() {
        Throwable exception = new IOException("Unexpected IO exception");
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(exception, "MyClass", "fieldName");
        assertEquals("Unexpected IO exception (through reference chain: MyClass->fieldName)", jsonMappingException.getMessage());

        Reference reference = new Reference("MyClass", "fieldName");
        jsonMappingException = JsonMappingException.wrapWithPath(exception, reference);
        assertEquals("Unexpected IO exception (through reference chain: MyClass->fieldName)", jsonMappingException.getMessage());

        List<Reference> path = jsonMappingException.getPath();
        assertEquals(1, path.size());
        assertEquals("MyClass", path.get(0).getFrom().getClass().getName());
        assertEquals("fieldName", path.get(0).getFieldName());

        jsonMappingException = JsonMappingException.wrapWithPath(exception, new Object[]{"list", 0}, "MyList");
        assertEquals("Unexpected IO exception (through reference chain: list[0]->MyList)", jsonMappingException.getMessage());

        path = jsonMappingException.getPath();
        assertEquals(1, path.size());
        assertEquals("list", path.get(0).getFrom().getClass().getSimpleName());
        assertEquals("MyList", path.get(0).getFieldName());
        assertEquals(0, path.get(0).getIndex());
    }

    @Test
    public void testFrom() {
        JsonParser parser = null;
        try {
            parser = new JsonParser();
            parser.nextToken();
        } catch (IOException e) {
            JsonMappingException jsonMappingException = JsonMappingException.from(parser, "Unexpected token: " + parser.getCurrentToken().getClass().getName());
            assertEquals("Unexpected token: ID_NULL (through reference chain: com.fasterxml.jackson.core.JsonParser->currentLocation)", jsonMappingException.getMessage());
            List<Reference> path = jsonMappingException.getPath();
            assertEquals(1, path.size());
            assertEquals("com.fasterxml.jackson.core.JsonParser", path.get(0).getFrom().getClass().getName());
        }

        JsonGenerator generator = null;
        try {
            generator = new JsonGenerator();
            generator.close();
        } catch (IOException e) {
            JsonMappingException jsonMappingException = JsonMappingException.from(generator, "Failed to close generator");
            assertEquals("Failed to close generator (through reference chain: com.fasterxml.jackson.core.JsonGenerator->)", jsonMappingException.getMessage());
            path = jsonMappingException.getPath();
            assertEquals(0, path.size());
        }

        DeserializationContext ctxt = new Mapper().getDeserializationContext();
        try {
            ctxt.readValueAs(String.class, null);
        } catch (JsonMappingException e) {
            JsonMappingException jsonMappingException = JsonMappingException.from(ctxt, "Failed to read value");
            assertEquals("Failed to read value (through reference chain: [root] (to be filled))", jsonMappingException.getMessage());
            path = jsonMappingException.getPath();
            assertEquals(1, path.size());
            assertEquals("[root]", path.get(0).getDescription());
        }

        SerializerProvider provider = new Mapper().getSerializerProvider();
        try {
            provider.getSerializerFor(String.class).serializeValues(provider.getOutputFactory(), null, null);
        } catch (JsonMappingException e) {
            JsonMappingException jsonMappingException = JsonMappingException.from(provider, "Failed to serialize values");
            assertEquals("Failed to serialize values (through reference chain: com.fasterxml.jackson.core.SerializerProvider->)", jsonMappingException.getMessage());
            path = jsonMappingException.getPath();
            assertEquals(0, path.size());
        }
    }

    @Test
    public void testFromUnexpectedIOE() {
        IOException ioException = new IOException("Unexpected IO exception");
        JsonMappingException jsonMappingException = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type IOException): Unexpected IO exception", jsonMappingException.getMessage());
    }
}