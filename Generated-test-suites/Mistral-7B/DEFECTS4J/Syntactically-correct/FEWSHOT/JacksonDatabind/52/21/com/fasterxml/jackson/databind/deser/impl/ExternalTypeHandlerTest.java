package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.util.*;

import java.io.*;
import java.util.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ExternalTypeHandler startedHandler = handler.start();
        assertNotNull(startedHandler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String json = "{\"typeId\":\"StringType\",\"myProperty\":\"test\"}";
        JsonFactory factory = new JsonFactory();
        JsonParser p = factory.createParser(new ByteArrayInputStream(json.getBytes()));
        DeserializationContext ctxt = new DeserializationContext(factory, null, null);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object result = handler.handleTypePropertyValue(p, ctxt, "typeId", null);
        assertEquals("StringType", result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String json = "{\"myProperty\":\"test\"}";
        JsonFactory factory = new JsonFactory();
        JsonParser p = factory.createParser(new ByteArrayInputStream(json.getBytes()));
        DeserializationContext ctxt = new DeserializationContext(factory, null, null);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object result = handler.handlePropertyValue(p, ctxt, "myProperty", null);
        assertNull(result);
    }

    @Test
    public void testComplete() throws IOException {
        String json = "{\"myProperty\":\"test\",\"typeId\":\"StringType\"}";
        JsonFactory factory = new JsonFactory();
        JsonParser p = factory.createParser(new ByteArrayInputStream(json.getBytes()));
        DeserializationContext ctxt = new DeserializationContext(factory, null, null);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object result = handler.complete(p, ctxt, null);
        assertEquals("test", result);
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        String json = "{\"myProperty1\":\"test1\",\"myProperty2\":\"test2\",\"typeId\":\"StringType\"}";
        JsonFactory factory = new JsonFactory();
        PropertyValueBuffer buffer = new PropertyValueBuffer(new JsonFactory(), null);
        JsonParser p = factory.createParser(new ByteArrayInputStream(json.getBytes()));
        DeserializationContext ctxt = new DeserializationContext(factory, null, null);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object result = handler.complete(p, ctxt, buffer, null);
        assertEquals(2, result.getClass().getDeclaredFields().length);
        Field field1 = result.getClass().getDeclaredField("myProperty1");
        field1.setAccessible(true);
        assertEquals("test1", field1.get(result));
        Field field2 = result.getClass().getDeclaredField("myProperty2");
        field2.setAccessible(true);
        assertEquals("test2", field2.get(result));
    }

    @Test(expected = IOException.class)
    public void testMissingTypeId() throws IOException {
        String json = "{\"myProperty\":\"test\"}";
        JsonFactory factory = new JsonFactory();
        JsonParser p = factory.createParser(new ByteArrayInputStream(json.getBytes()));
        DeserializationContext ctxt = new DeserializationContext(factory, null, null);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object result = handler.complete(p, ctxt, null);
        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testMissingProperty() throws IOException {
        String json = "{\"typeId\":\"StringType\"}";
        JsonFactory factory = new JsonFactory();
        JsonParser p = factory.createParser(new ByteArrayInputStream(json.getBytes()));
        DeserializationContext ctxt = new DeserializationContext(factory, null, null);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object result = handler.complete(p, ctxt, null);
        fail("Expected IOException");
    }
}