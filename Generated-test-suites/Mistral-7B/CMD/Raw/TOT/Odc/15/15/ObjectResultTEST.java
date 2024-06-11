package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

@SuppressWarnings("deprecation")
public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testObjectName", "testType");
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testObjectName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(Status.NONE, objectResult.getStatus());
        assertEquals(0L, objectResult.getCount().get());
        assertEquals(0L, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("testObjectName");
        objectStatus.setStatus(Status.ERROR);
        objectStatus.setType("testType");
        objectStatus.setSchema("testSchema");
        objectStatus.setCount(1L);
        objectStatus.setTotal(1L);

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testObjectName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(Status.ERROR, objectResult.getStatus());
        assertEquals(1L, objectResult.getCount().get());
        assertEquals(1L, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setType("testType");
        objectResult.setStatus(Status.ERROR);
        objectResult.setName("testObjectName");
        objectResult.setSchema("testSchema");
        objectResult.setCount(new AtomicLong(1));
        objectResult.setTotal(new AtomicLong(1));

        String expectedSummary = "testSchema.testObjectName[testType]";
        String actualSummary = objectResult.getSummary();

        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void testJsonIgnore() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("file://testPath1"));
        exportPaths.add(new URL("file://testPath2"));
        objectResult.setExportPaths(exportPaths);

        String jsonString = objectMapper.writeValueAsString(objectResult);

        ObjectResult deserializedObjectResult = objectMapper.readValue(jsonString, ObjectResult.class);

        assertEquals(exportPaths, deserializedObjectResult.getExportPaths());
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        new ObjectResult(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullType() {
        new ObjectResult("testSchema", null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSchema() {
        new ObjectResult(null, "testObjectName", "testType");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyName() {
        new ObjectResult("testSchema", "", "testType");
    }

    @Test
    public void testSetType() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setType(null);
        objectResult.setType("testType");
        assertEquals("testType", objectResult.getType());
    }

    @Test
    public void testSetStatus() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setStatus(null);
        objectResult.setStatus(Status.ERROR);
        assertEquals(Status.ERROR, objectResult.getStatus());
    }

    @Test
    public void testSetName() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setName(null);
        objectResult.setName("testObjectName");
        assertEquals("testObjectName", objectResult.getName());
    }

    @Test
    public void testSetSchema() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema(null);
        objectResult.setSchema("testSchema");
        assertEquals("testSchema", objectResult.getSchema());
    }

    @Test
    public void testSetCount() {
        ObjectResult objectResult = new ObjectResult();
        AtomicLong count = new AtomicLong(-1);
        objectResult.setCount(count);
        assertEquals(count, objectResult.getCount());
    }

    @Test
    public void testSetTotal() {
        ObjectResult objectResult = new ObjectResult();
        AtomicLong total = new AtomicLong(-1);
        objectResult.setTotal(total);
        assertEquals(total, objectResult.getTotal());
    }
}