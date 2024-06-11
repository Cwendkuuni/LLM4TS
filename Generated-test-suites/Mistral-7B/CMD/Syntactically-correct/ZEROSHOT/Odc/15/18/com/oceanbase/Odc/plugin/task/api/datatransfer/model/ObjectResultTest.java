package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    private ObjectResult objectResult;
    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        objectStatus = new ObjectStatus("schema1", "table1", ObjectStatus.Status.SUCCESS, new AtomicLong(1), new AtomicLong(1));
        objectResult = new ObjectResult(objectStatus.getSchema(), objectStatus.getName(), objectStatus.getType());
    }

    @Test
    public void testConstructor() {
        ObjectResult result = new ObjectResult("schema", "table", "type");
        assertEquals("schema", result.getSchema());
        assertEquals("table", result.getName());
        assertEquals("type", result.getType());
    }

    @Test
    public void testOf() {
        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getSchema(), result.getSchema());
        assertEquals(objectStatus.getName(), result.getName());
        assertEquals(objectStatus.getType(), result.getType());
        assertEquals(objectStatus.getStatus(), result.getStatus());
        assertEquals(objectStatus.getCount().get(), result.getCount().get());
        assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        String expectedSummary = String.format("%s.%s[%s]", "schema1", "table1", "type");
        assertEquals(expectedSummary, summary);
    }

    @Test
    public void testSetExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://localhost:8081/export/path1"));
        exportPaths.add(new URL("http://localhost:8081/export/path2"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testGetType() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"type\":\"type1\"}";
        try {
            ObjectResult result = objectMapper.readValue(json, ObjectResult.class);
            assertEquals("type1", result.getType());
        } catch (Exception e) {
            fail("Failed to deserialize ObjectResult from JSON: " + e.getMessage());
        }
    }

    @Test
    public void testSetType() {
        objectResult.setType("type1");
        assertEquals("type1", objectResult.getType());
    }

    @Test
    public void testGetStatus() {
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
    }

    @Test
    public void testSetStatus() {
        ObjectStatus status = new ObjectStatus("schema1", "table1", ObjectStatus.Status.FAILED, new AtomicLong(1), new AtomicLong(1));
        objectResult.setStatus(status);
        assertEquals(status.getStatus(), objectResult.getStatus());
    }

    @Test
    public void testGetSchema() {
        assertEquals("schema1", objectResult.getSchema());
    }

    @Test
    public void testSetSchema() {
        objectResult.setSchema("schema2");
        assertEquals("schema2", objectResult.getSchema());
    }

    @Test
    public void testgetName() {
        assertEquals("table1", objectResult.getName());
    }

    @Test
    public void testSetName() {
        objectResult.setName("table2");
        assertEquals("table2", objectResult.getName());
    }

    @Test
    public void testGetCount() {
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
    }

    @Test
    public void testSetCount() {
        AtomicLong count = new AtomicLong(2);
        objectResult.setCount(count);
        assertEquals(count, objectResult.getCount());
    }

    @Test
    public void testGetTotal() {
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testSetTotal() {
        AtomicLong total = new AtomicLong(3);
        objectResult.setTotal(total);
        assertEquals(total, objectResult.getTotal());
    }
}