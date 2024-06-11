package com.oceanbase.odc.plugin.task.api.datatransfer.model.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    // Test constructor
    @Test
    public void testObjectResultConstruction() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertNull(result.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCESS, result.getStatus());
        assertEquals(0L, result.getCount().get());
        assertEquals(0L, result.getTotal().get());
    }

    // Test of method: of
    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus();
        status.setName("name1");
        status.setType("type1");
        status.setSchema("schema1");
        status.setCount(new AtomicLong(10));
        status.setTotal(new AtomicLong(20));
        status.setStatus(ObjectStatus.Status.SUCCESS);

        ObjectResult result = ObjectResult.of(status);

        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertNull(result.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCESS, result.getStatus());
        assertEquals(10L, result.getCount().get());
        assertEquals(20L, result.getTotal().get());
    }

    // Test getSummary method
    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        String expected = "schema1.name1[type1]";
        String actual = result.getSummary();
        assertEquals(expected, actual);
    }

    // Test empty constructor
    @Test
    public void testEmptyConstructor() {
        ObjectResult result = new ObjectResult();
        assertNull(result.getSchema());
        assertNull(result.getName());
        assertNull(result.getType());
        assertNull(result.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCESS, result.getStatus());
        assertEquals(0L, result.getCount().get());
        assertEquals(0L, result.getTotal().get());
    }

    // Test setter methods
    @Test
    public void testSetters() {
        ObjectResult result = new ObjectResult();
        result.setType("type1");
        result.setStatus(ObjectStatus.Status.FAILED);
        result.setName("name1");
        result.setSchema("schema1");
        result.setCount(new AtomicLong(10));
        result.setTotal(new AtomicLong(20));
        result.setExportPaths(Collections.singletonList(new URL("http://test.com")));

        assertEquals("type1", result.getType());
        assertEquals(ObjectStatus.Status.FAILED, result.getStatus());
        assertEquals("name1", result.getName());
        assertEquals("schema1", result.getSchema());
        assertEquals(10L, result.getCount().get());
        assertEquals(20L, result.getTotal().get());
        assertEquals(1, result.getExportPaths().size());
        assertTrue(result.getExportPaths().contains(new URL("http://test.com")));
    }

    // Test empty exportPaths list
    @Test
    public void testEmptyExportPaths() {
        ObjectResult result = new ObjectResult();
        result.setExportPaths(Collections.emptyList());
        assertTrue(result.getExportPaths().isEmpty());
    }

    // Test null exportPaths
    @Test(expected = NullPointerException.class)
    public void testNullExportPaths() {
        ObjectResult result = new ObjectResult();
        result.setExportPaths(null);
    }

    // Test size of exportPaths list
    @Test
    public void testSizeOfExportPaths() {
        ObjectResult result = new ObjectResult();
        result.setExportPaths(Arrays.asList(new URL("http://test1.com"), new URL("http://test2.com")));
        assertEquals(2, result.getExportPaths().size());
    }

    // Test add exportPath to empty list
    @Test
    public void testAddExportPathToEmptyList() {
        ObjectResult result = new ObjectResult();
        result.setExportPaths(Collections.emptyList());
        result.getExportPaths().add(new URL("http://test.com"));
        assertTrue(result.getExportPaths().contains(new URL("http://test.com")));
        assertEquals(1, result.getExportPaths().size());
    }

    // Test add exportPath to non-empty list
    @Test
    public void testAddExportPathToNonEmptyList() {
        ObjectResult result = new ObjectResult();
        result.setExportPaths(Arrays.asList(new URL("http://test1.com"), new URL("http://test2.com")));
        result.getExportPaths().add(new URL("http://test3.com"));
        assertTrue(result.getExportPaths().contains(new URL("http://test3.com")));
        assertEquals(3, result.getExportPaths().size());
    }

    // Test remove exportPath from list
    @Test
    public void testRemoveExportPathFromList() {
        ObjectResult result = new ObjectResult();
        result.setExportPaths(Arrays.asList(new URL("http://test1.com"), new URL("http://test2.com"), new URL("http://test3.com")));
        result.getExportPaths().remove(1);
        assertFalse(result.getExportPaths().contains(new URL("http://test2.com")));
        assertEquals(2, result.getExportPaths().size());
    }

}