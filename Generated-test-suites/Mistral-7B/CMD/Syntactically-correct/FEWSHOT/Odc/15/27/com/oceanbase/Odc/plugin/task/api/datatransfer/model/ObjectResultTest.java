package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objResult = new ObjectResult("schema1", "tableName", "type1");
        assertEquals("schema1", objResult.getSchema());
        assertEquals("tableName", objResult.getName());
        assertEquals("type1", objResult.getType());
        assertEquals(ObjectStatus.Status.NOT_STARTED, objResult.getStatus());
        assertEquals(0, objResult.getCount().get());
        assertEquals(0, objResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus that = new ObjectStatus();
        that.setType("type1");
        that.setStatus(ObjectStatus.Status.FINISHED);
        that.setName("tableName");
        that.setSchema("schema1");
        that.setCount(new AtomicLong(10));
        that.setTotal(new AtomicLong(15));

        ObjectResult result = ObjectResult.of(that);

        assertSame(that.getType(), result.getType());
        assertSame(that.getStatus(), result.getStatus());
        assertSame(that.getName(), result.getName());
        assertSame(that.getSchema(), result.getSchema());
        assertEquals(10, result.getCount().get());
        assertEquals(15, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objResult = new ObjectResult("schema1", "tableName", "type1");
        assertEquals("schema1.tableName[type1]", objResult.getSummary());
    }

    @Test
    public void testSettersAndGetters() {
        ObjectResult objResult = new ObjectResult();

        objResult.setType("type1");
        assertEquals("type1", objResult.getType());

        objResult.setStatus(ObjectStatus.Status.FAILED);
        assertEquals(ObjectStatus.Status.FAILED, objResult.getStatus());

        objResult.setName("tableName");
        assertEquals("tableName", objResult.getName());

        objResult.setSchema("schema1");
        assertEquals("schema1", objResult.getSchema());

        objResult.setCount(new AtomicLong(10));
        assertEquals(10, objResult.getCount().get());

        objResult.setTotal(new AtomicLong(15));
        assertEquals(15, objResult.getTotal().get());
    }

    @Test
    public void testJsonIgnore() {
        ObjectResult objResult = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        objResult.setExportPaths(exportPaths);
        assertNull(objResult.getExportPaths());
    }
}