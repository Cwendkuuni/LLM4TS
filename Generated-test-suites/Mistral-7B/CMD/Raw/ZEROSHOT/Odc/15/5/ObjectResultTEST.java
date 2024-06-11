package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.AtomicLong;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

@SuppressWarnings("deprecation")
public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(Status.SUCCESS, objectResult.getStatus());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("table1", new AtomicLong(10), new AtomicLong(20), Status.SUCCESS);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("table1", objectResult.getName());
        assertEquals(Status.SUCCESS, objectResult.getStatus());
        assertEquals("type1", objectResult.getType()); // Assuming that ObjectStatus has a getType() method
        assertEquals(new AtomicLong(10), objectResult.getCount());
        assertEquals(new AtomicLong(20), objectResult.getTotal());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1.table1[type1]", objectResult.getSummary());
    }

    @Test
    public void testJsonIgnore() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        List<URL> exportPaths = Arrays.asList(new URL("file1.csv"), new URL("file2.csv"));
        objectResult.setExportPaths(exportPaths);
        assertNull(objectResult.getExportPaths()); // Since @JsonIgnore is used, JUnit should not be able to access it
    }

    @Test
    public void testObjectStatusGetters() {
        ObjectStatus objectStatus = new ObjectStatus("table1", new AtomicLong(10), new AtomicLong(20), Status.SUCCESS);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("table1", objectResult.getName());
        assertEquals(Status.SUCCESS, objectResult.getStatus());
        assertEquals("type1", objectResult.getType()); // Assuming that ObjectStatus has a getType() method
        assertEquals(new AtomicLong(10), objectResult.getCount());
        assertEquals(new AtomicLong(20), objectResult.getTotal());
    }

    @Test
    public void testEmptyObjectResult() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getName());
        assertNull(objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(Status.SUCCESS, objectResult.getStatus());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testObjectResultEquality() {
        ObjectResult objectResult1 = new ObjectResult("schema1", "table1", "type1");
        ObjectResult objectResult2 = new ObjectResult("schema1", "table1", "type1");
        ObjectResult objectResult3 = new ObjectResult("schema1", "table2", "type1");

        assertEquals(objectResult1, objectResult1);
        assertEquals(objectResult1, objectResult2);
        assertNotEquals(objectResult1, objectResult3);
    }

    @Test
    public void testObjectResultHashCode() {
        ObjectResult objectResult1 = new ObjectResult("schema1", "table1", "type1");
        ObjectResult objectResult2 = new ObjectResult("schema1", "table1", "type1");
        ObjectResult objectResult3 = new ObjectResult("schema1", "table2", "type1");

        assertEquals(objectResult1.hashCode(), objectResult1.hashCode());
        assertEquals(objectResult1.hashCode(), objectResult2.hashCode());
        assertNotEquals(objectResult1.hashCode(), objectResult3.hashCode());
    }

    @Test
    public void testObjectResultToString() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("ObjectResult(schema=schema1, name=table1, type=type1, status=SUCCESS, count=0, total=0)", objectResult.toString());
    }

    @Test
    public void testObjectResultGetters() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(Status.SUCCESS, objectResult.getStatus());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testObjectResultSetters() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = Arrays.asList(new URL("file1.csv"), new URL("file2.csv"));
        objectResult.setSchema("schema1");
        objectResult.setName("table1");
        objectResult.setType("type1");
        objectResult.setExportPaths(exportPaths);
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(exportPaths, objectResult.getExportPaths());
        assertNull(objectResult.getStatus());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testObjectResultCountAndTotal() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        objectResult.setCount(new AtomicLong(10));
        objectResult.setTotal(new AtomicLong(20));
        assertEquals(new AtomicLong(10), objectResult.getCount());
        assertEquals(new AtomicLong(20), objectResult.getTotal());
    }

    @Test
    public void testObjectResultEmptyCountAndTotal() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        ObjectResult emptyObjectResult = new ObjectResult();
        assertEquals(emptyObjectResult.getCount(), objectResult.getCount());
        assertEquals(emptyObjectResult.getTotal(), objectResult.getTotal());
    }

    @Test
    public void testObjectResultStatus() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        Status status = Status.FAILED;
        objectResult.setStatus(status);
        assertEquals(status, objectResult.getStatus());
    }

    @Test
    public void testObjectResultEmptyStatus() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getStatus());
    }

    @Test
    public void testObjectResultExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        List<URL> exportPaths = Arrays.asList(new URL("file1.csv"), new URL("file2.csv"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultEmptyExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getExportPaths());
    }
}