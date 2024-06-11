package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1", objResult.getSchema());
        assertEquals("table1", objResult.getName());
        assertEquals("type1", objResult.getType());
        assertEquals(0, objResult.getCount().get());
        assertEquals(0, objResult.getTotal().get());
        assertEquals(ObjectStatus.Status.UNPROCESSED, objResult.getStatus());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus that = new ObjectStatus();
        that.setType("type1");
        that.setStatus(ObjectStatus.Status.PROCESSING);
        that.setName("table1");
        that.setSchema("schema1");
        that.setCount(new AtomicLong(5));
        that.setTotal(new AtomicLong(10));

        ObjectResult result = ObjectResult.of(that);

        assertEquals("schema1", result.getSchema());
        assertEquals("table1", result.getName());
        assertEquals("type1", result.getType());
        assertEquals(5, result.getCount().get());
        assertEquals(10, result.getTotal().get());
        assertEquals(ObjectStatus.Status.PROCESSING, result.getStatus());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1.table1[type1]", objResult.getSummary());
    }

    @Test
    public void testExportPathsGettersAndSetters() {
        ObjectResult objResult = new ObjectResult("schema1", "table1", "type1");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("file:///exportPath1/"));
        objResult.setExportPaths(exportPaths);

        List<URL> resultExportPaths = objResult.getExportPaths();
        assertEquals(1, resultExportPaths.size());
        assertEquals("file:///exportPath1/", resultExportPaths.get(0).toString());

        ObjectResult objResult2 = new ObjectResult("schema1", "table1", "type1");
        assertNull(objResult2.getExportPaths());
    }
}