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
    public void testObjectResultConstuction() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";

        ObjectResult objectResult = new ObjectResult(schema, name, type);

        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(Status.VALID, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";
        ObjectStatus objectStatus = new ObjectStatus(name, new AtomicLong(1), new AtomicLong(1), null);
        objectStatus.setType(type);
        objectStatus.setStatus(Status.VALID);

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(Status.VALID, objectResult.getStatus());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(1, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultGetSummary() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";

        ObjectResult objectResult = new ObjectResult(schema, name, type);

        String expectedSummary = String.format("%s.%s[%s]", schema, name, type);

        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testJsonIgnore() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";

        ObjectResult objectResult = new ObjectResult(schema, name, type);
        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(objectResult);

        ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);

        assertEquals(schema, deserializedObjectResult.getSchema());
        assertEquals(name, deserializedObjectResult.getName());
        assertEquals(type, deserializedObjectResult.getType());
        assertNull(deserializedObjectResult.getExportPaths());
    }

    @Test
    public void testSetStatus() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";

        ObjectResult objectResult = new ObjectResult(schema, name, type);

        Status expectedStatus = Status.ERROR;
        objectResult.setStatus(expectedStatus);

        assertEquals(expectedStatus, objectResult.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStatusNull() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";

        ObjectResult objectResult = new ObjectResult(schema, name, type);

        objectResult.setStatus(null);
    }

    @Test
    public void testSetExportPaths() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";

        ObjectResult objectResult = new ObjectResult(schema, name, type);

        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://test.com/export1"));
        exportPaths.add(new URL("http://test.com/export2"));

        objectResult.setExportPaths(exportPaths);

        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testGetCountAndTotal() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";

        ObjectResult objectResult = new ObjectResult(schema, name, type);

        AtomicLong count = new AtomicLong(5);
        AtomicLong total = new AtomicLong(10);

        objectResult.setCount(count);
        objectResult.setTotal(total);

        assertEquals(count, objectResult.getCount());
        assertEquals(total, objectResult.getTotal());
    }

    @Test
    public void testObjectResultOfWithCountAndTotal() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";
        ObjectStatus objectStatus = new ObjectStatus(name, new AtomicLong(1), new AtomicLong(1), null);
        objectStatus.setType(type);
        objectStatus.setStatus(Status.VALID);

        AtomicLong count = new AtomicLong(5);
        AtomicLong total = new AtomicLong(10);
        objectStatus.setCount(count);
        objectStatus.setTotal(total);

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(Status.VALID, objectResult.getStatus());
        assertEquals(count, objectResult.getCount());
        assertEquals(total, objectResult.getTotal());
    }

    @Test
    public void testSetTypeNull() {
        String schema = "test_schema";
        String name = "test_name";

        ObjectResult objectResult = new ObjectResult(schema, name, null);

        assertNull(objectResult.getType());
    }

    @Test
    public void testSetSchemaNull() {
        String name = "test_name";
        String type = "test_type";

        ObjectResult objectResult = new ObjectResult(null, name, type);

        assertNull(objectResult.getSchema());
    }

    @Test
    public void testSetNameNull() {
        String schema = "test_schema";
        String type = "test_type";

        ObjectResult objectResult = new ObjectResult(schema, null, type);

        assertNull(objectResult.getName());
    }

}