package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult result = new ObjectResult("schema1", "table1", "IntType");
        assertEquals("schema1", result.getSchema());
        assertEquals("table1", result.getName());
        assertEquals("IntType", result.getType());
        assertEquals(ObjectStatus.Status.INIT, result.getStatus());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus("type1", ObjectStatus.Status.SUCCESS, "name1", "schema1", 1L, 10L, null);
        ObjectResult result = ObjectResult.of(status);
        assertEquals("type1", result.getType());
        assertEquals(ObjectStatus.Status.SUCCESS, result.getStatus());
        assertEquals("name1", result.getName());
        assertEquals("schema1", result.getSchema());
        assertEquals(1L, result.getCount().get());
        assertEquals(10L, result.getTotal().get());
        assertNull(result.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("schema1", "table1", "IntType");
        assertEquals("schema1.table1[IntType]", result.getSummary());
    }

    @Test
    public void testSetAndGetExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("file:/path1/export1.csv"));
        exportPaths.add(new URL("file:/path2/export2.csv"));

        ObjectResult result = new ObjectResult("schema1", "table1", "IntType");
        result.setExportPaths(exportPaths);

        List<URL> actualExportPaths = result.getExportPaths();
        assertEquals(exportPaths, actualExportPaths);
        assertEquals(2, actualExportPaths.size());
        assertTrue(actualExportPaths.get(0).toString().equals("file:/path1/export1.csv"));
        assertTrue(actualExportPaths.get(1).toString().equals("file:/path2/export2.csv"));
    }

    @Test
    public void testEmptyExportPaths() {
        ObjectResult result = new ObjectResult("schema1", "table1", "IntType");
        assertNull(result.getExportPaths());
    }
}