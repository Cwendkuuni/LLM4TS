package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.StatusType.IN_PROGRESS, objectResult.getStatus());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("name1");
        objectStatus.setType("type1");
        objectStatus.setStatus(ObjectStatus.StatusType.COMPLETED);
        objectStatus.setCount(new AtomicLong(5));
        objectStatus.setTotal(new AtomicLong(10));

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(ObjectStatus.StatusType.COMPLETED, objectResult.getStatus());
        assertEquals(new AtomicLong(5), objectResult.getCount());
        assertEquals(new AtomicLong(10), objectResult.getTotal());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://localhost:8080/export1.csv"));
        exportPaths.add(new URL("http://localhost:8080/export2.csv"));
        objectResult.setExportPaths(exportPaths);

        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}