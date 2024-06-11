package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "INT");
        assertNotNull(objectResult);
        assertEquals("schema1.table1[INT]", objectResult.getSummary());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("schema1", "table1", ObjectStatus.Status.SUCCESS, 1L, 1L);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals("schema1.table1[INT]", objectResult.getSummary());
    }

    @Test
    public void testGetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "INT");
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "INT");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("file1.csv"));
        objectResult.setExportPaths(exportPaths);
        assertNotNull(objectResult.getExportPaths());
        assertEquals(1, objectResult.getExportPaths().size());
    }
}