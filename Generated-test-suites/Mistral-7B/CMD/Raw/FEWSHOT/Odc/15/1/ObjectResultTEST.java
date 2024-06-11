package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {

    private static final String SCHEMA = "schema1";
    private static final String NAME = "name1";
    private static final String TYPE = "type1";

    private ObjectResult objectResult;

    @Before
    public void setup() {
        objectResult = new ObjectResult(SCHEMA, NAME, TYPE);
    }

    @Test
    public void testObjectResultConstruction() {
        assertEquals(SCHEMA, objectResult.getSchema());
        assertEquals(NAME, objectResult.getName());
        assertEquals(TYPE, objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setSchema(SCHEMA);
        objectStatus.setName(NAME);
        objectStatus.setType(TYPE);
        objectStatus.setCount(new AtomicLong(1));
        objectStatus.setTotal(new AtomicLong(1));

        ObjectResult result = ObjectResult.of(objectStatus);

        assertEquals(SCHEMA, result.getSchema());
        assertEquals(NAME, result.getName());
        assertEquals(TYPE, result.getType());
        assertEquals(1, result.getCount().get());
        assertEquals(1, result.getTotal().get());
        assertNull(result.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        String expected = String.format("%s.%s[%s]", SCHEMA, NAME, TYPE);
        assertEquals(expected, objectResult.getSummary());
    }

    @Test
    public void testJsonSerialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(objectResult);

        ObjectResult deserialized = objectMapper.readValue(json, ObjectResult.class);

        assertEquals(SCHEMA, deserialized.getSchema());
        assertEquals(NAME, deserialized.getName());
        assertEquals(TYPE, deserialized.getType());
        assertEquals(0, deserialized.getCount().get());
        assertEquals(0, deserialized.getTotal().get());
        assertNull(deserialized.getExportPaths());
    }

    @Test
    public void testSetExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        URL exportPath = new URL("http://localhost:8080/export");
        exportPaths.add(exportPath);

        objectResult.setExportPaths(exportPaths);

        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}