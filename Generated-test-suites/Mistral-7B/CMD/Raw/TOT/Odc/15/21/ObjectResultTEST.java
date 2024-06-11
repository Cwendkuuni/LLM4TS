package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.AVAILABLE, objectResult.getStatus());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setSchema("testSchema");
        objectStatus.setName("testName");
        objectStatus.setType("testType");
        objectStatus.setStatus(ObjectStatus.Status.AVAILABLE);
        objectStatus.setCount(new AtomicLong(1));
        objectStatus.setTotal(new AtomicLong(1));

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.AVAILABLE, objectResult.getStatus());
        assertEquals(new AtomicLong(1), objectResult.getCount());
        assertEquals(new AtomicLong(1), objectResult.getTotal());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

    @Test
    public void testJsonSerializationAndDeserialization() throws Exception {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(objectResult);
        ObjectResult deserializedObjectResult = mapper.readValue(json, ObjectResult.class);
        assertEquals(objectResult, deserializedObjectResult);
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("testUrl"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testGetStatusWithNullStatus() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        ObjectStatus status = null;
        ObjectResult result = ObjectResult.of(status);
        assertEquals(ObjectStatus.Status.AVAILABLE, result.getStatus());
    }

    @Test
    public void testGetStatusWithInvalidStatus() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        ObjectStatus status = new ObjectStatus();
        status.setStatus(ObjectStatus.Status.values()[2]); //Invalid status value
        ObjectResult result = ObjectResult.of(status);
        assertEquals(ObjectStatus.Status.AVAILABLE, result.getStatus());
    }
}