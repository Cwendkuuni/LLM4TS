package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getName());
        assertNull(objectResult.getType());
        assertEquals(ObjectStatus.UNINITIALIZED, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());

        ObjectResult objectResultWithValues = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1", objectResultWithValues.getSchema());
        assertEquals("table1", objectResultWithValues.getName());
        assertEquals("type1", objectResultWithValues.getType());
        assertEquals(ObjectStatus.UNINITIALIZED, objectResultWithValues.getStatus());
        assertEquals(0, objectResultWithValues.getCount().get());
        assertEquals(0, objectResultWithValues.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("type1");
        objectStatus.setStatus(ObjectStatus.SUCCESS);
        objectStatus.setName("table1");
        objectStatus.setSchema("schema1");
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(20));

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(ObjectStatus.SUCCESS, objectResult.getStatus());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema1");
        objectResult.setName("table1");
        objectResult.setType("type1");

        assertEquals("schema1.table1[type1]", objectResult.getSummary());
    }

    @Test
    public void testJsonIgnore() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(new ArrayList<>());

        try {
            String json = mapper.writeValueAsString(objectResult);
            assertTrue(json.contains("\"exportPaths\": null"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}