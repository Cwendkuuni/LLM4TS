package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCEEDED, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = mock(ObjectStatus.class);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1.table1[type1]", objectResult.getSummary());
    }

    @Test
    public void testSetAndGetExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://localhost:8080/export1"));
        exportPaths.add(new URL("http://localhost:8080/export2"));

        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        objectResult.setExportPaths(exportPaths);

        List<URL> exportPathsResult = objectResult.getExportPaths();
        assertEquals(exportPaths, exportPathsResult);
        assertEquals(2, exportPathsResult.size());
    }

    @Test
    public void testEmptyExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultEquality() {
        ObjectResult objectResult1 = new ObjectResult("schema1", "table1", "type1");
        ObjectResult objectResult2 = new ObjectResult("schema1", "table1", "type1");

        assertEquals(objectResult1, objectResult2);

        ObjectResult objectResult3 = new ObjectResult("schema1", "table2", "type1");
        assertNotEquals(objectResult1, objectResult3);
    }
}