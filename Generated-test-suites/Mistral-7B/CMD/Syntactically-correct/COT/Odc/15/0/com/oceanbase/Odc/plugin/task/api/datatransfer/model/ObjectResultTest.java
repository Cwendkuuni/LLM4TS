package com.oceanbase.odc.plugin.task.api.datatransfer.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    // Test constructor
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.STATUS_INIT, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    // Test of method: public static ObjectResult of(ObjectStatus that)
    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name2", new AtomicLong(1), new AtomicLong(2), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(2, objectResult.getTotal().get());
    }

    // Test getter for private field: @JsonIgnore List<URL> exportPaths
    @Test
    public void testGetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    // Test method: public String getSummary()
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    // Test edge case: empty constructor and getSummary()
    @Test
    public void testEdgeCaseEmptyConstructorAndGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        assertEquals("", objectResult.getSummary());
    }

    // Test exception handling: null schema
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionNullSchema() {
        new ObjectResult(null, "name1", "type1");
    }

    // Test exception handling: null name
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionNullName() {
        new ObjectResult("schema1", null, "type1");
    }

    // Test exception handling: null type
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionNullType() {
        new ObjectResult("schema1", "name1", null);
    }
}