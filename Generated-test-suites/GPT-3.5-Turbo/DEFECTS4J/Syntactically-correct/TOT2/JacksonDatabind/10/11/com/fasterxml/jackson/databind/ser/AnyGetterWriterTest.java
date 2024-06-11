package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize() {
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, new MapSerializer());
        JsonGenerator gen = new JsonGenerator();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        try {
            anyGetterWriter.getAndSerialize(map, gen, new SerializerProvider());
        } catch (Exception e) {
            fail("Exception thrown when it shouldn't have.");
        }
    }

    @Test
    public void testGetAndFilter() {
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, new MapSerializer());
        JsonGenerator gen = new JsonGenerator();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        try {
            anyGetterWriter.getAndFilter(map, gen, new SerializerProvider(), null);
        } catch (Exception e) {
            fail("Exception thrown when it shouldn't have.");
        }
    }

    @Test
    public void testResolve() {
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);
        try {
            anyGetterWriter.resolve(new SerializerProvider());
        } catch (JsonMappingException e) {
            fail("Exception thrown when it shouldn't have.");
        }
    }

    @Test
    public void testGetAndSerializeWithNullValue() {
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, new MapSerializer());
        JsonGenerator gen = new JsonGenerator();
        Map<String, String> map = null;
        try {
            anyGetterWriter.getAndSerialize(map, gen, new SerializerProvider());
        } catch (Exception e) {
            fail("Exception thrown when it shouldn't have.");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNonMapValue() throws Exception {
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, new MapSerializer());
        JsonGenerator gen = new JsonGenerator();
        String notAMap = "Not a Map";
        anyGetterWriter.getAndSerialize(notAMap, gen, new SerializerProvider());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithNonMapValue() throws Exception {
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, new MapSerializer());
        JsonGenerator gen = new JsonGenerator();
        String notAMap = "Not a Map";
        anyGetterWriter.getAndFilter(notAMap, gen, new SerializerProvider(), null);
    }
}