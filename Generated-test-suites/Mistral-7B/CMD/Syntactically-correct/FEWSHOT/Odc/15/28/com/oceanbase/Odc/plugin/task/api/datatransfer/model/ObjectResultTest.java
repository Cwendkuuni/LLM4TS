package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "int");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("int", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.STATUS_SUCCESS, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("int");
        objectStatus.setStatus(ObjectStatus.STATUS_SUCCESS);
        objectStatus.setName("table1");
        objectStatus.setSchema("schema1");
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(20));

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("int", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.STATUS_SUCCESS, objectResult.getStatus());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "int");
        assertEquals("schema1.table1[int]", objectResult.getSummary());
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "int");
        List<URL> exportPaths = java.util.Arrays.asList(new URL("file:/path1"), new URL("file:/path2"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testJsonSerialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "int");
        String jsonString = objectMapper.writeValueAsString(objectResult);
        ObjectResult deserializedObjectResult = objectMapper.readValue(jsonString, ObjectResult.class);
        assertEquals("schema1", deserializedObjectResult.getSchema());
        assertEquals("table1", deserializedObjectResult.getName());
        assertEquals("int", deserializedObjectResult.getType());
        assertNull(deserializedObjectResult.getExportPaths());
        assertEquals(ObjectStatus.STATUS_SUCCESS, deserializedObjectResult.getStatus());
        assertEquals(0, deserializedObjectResult.getCount().get());
        assertEquals(0, deserializedObjectResult.getTotal().get());
    }
}