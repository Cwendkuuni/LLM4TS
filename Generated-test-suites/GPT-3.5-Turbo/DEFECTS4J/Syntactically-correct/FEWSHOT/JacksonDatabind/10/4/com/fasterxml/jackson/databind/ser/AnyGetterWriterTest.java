package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {
    
    @Test
    public void testGetAndSerialize() throws Exception {
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);

        // Test case when value is null
        anyGetterWriter.getAndSerialize(null, null, null);

        // Test case when value is not null and not instance of Map
        Object notMapValue = new Object();
        try {
            anyGetterWriter.getAndSerialize(notMapValue, null, null);
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (null()) not java.util.Map but java.lang.Object", e.getMessage());
        }

        // Test case when value is Map and _mapSerializer is null
        Map<String, String> mapValue = new HashMap<String, String>();
        anyGetterWriter.getAndSerialize(mapValue, null, null);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);

        // Test case when value is null
        anyGetterWriter.getAndFilter(null, null, null, null);

        // Test case when value is not null and not instance of Map
        Object notMapValue = new Object();
        try {
            anyGetterWriter.getAndFilter(notMapValue, null, null, null);
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (null()) not java.util.Map but java.lang.Object", e.getMessage());
        }

        // Test case when value is Map and _mapSerializer is null
        Map<String, String> mapValue = new HashMap<String, String>();
        anyGetterWriter.getAndFilter(mapValue, null, null, null);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);

        // Test case when _mapSerializer is null
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        anyGetterWriter.resolve(provider);
        assertNull(anyGetterWriter._mapSerializer);

        // Test case when _mapSerializer is not null
        MapSerializer mapSerializer = new MapSerializer(null, null, null, null, null, null);
        anyGetterWriter._mapSerializer = mapSerializer;
        anyGetterWriter.resolve(provider);
        assertEquals(mapSerializer, anyGetterWriter._mapSerializer);
    }
}