package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {

    private ObjectStatus objectStatus;
    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectStatus = mock(ObjectStatus.class);
        objectResult = ObjectResult.of(objectStatus);
    }

    @Test
    public void testObjectResultConstructor() {
        assertEquals("", objectResult.getSchema());
        assertNull(objectResult.getName());
        assertNull(objectResult.getCount());
        assertNull(objectResult.getTotal());
        assertEquals(ObjectStatus.STATUS_SUCCESS, objectResult.getStatus());
        assertNull(objectResult.getExportPaths());
        assertSame(ObjectStatus.class, objectResult.getClass());
    }

    @Test
    public void testSetType() {
        objectResult.setType("integer");
        assertEquals("integer", objectResult.getType());
    }

    @Test
    public void testSetStatus() {
        objectResult.setStatus(ObjectStatus.STATUS_FAILED);
        assertEquals(ObjectStatus.STATUS_FAILED, objectResult.getStatus());
    }

    @Test
    public void testSetName() {
        objectResult.setName("test_table");
        assertEquals("test_table", objectResult.getName());
    }

    @Test
    public void testSetSchema() {
        objectResult.setSchema("test_schema");
        assertEquals("test_schema", objectResult.getSchema());
    }

    @Test
    public void testSetCount() {
        AtomicLong count = new AtomicLong(10);
        objectResult.setCount(count);
        assertEquals(count, objectResult.getCount());
    }

    @Test
    public void testSetTotal() {
        AtomicLong total = new AtomicLong(20);
        objectResult.setTotal(total);
        assertEquals(total, objectResult.getTotal());
    }

    @Test
    public void testGetSummary() {
        String schema = "test_schema";
        String name = "test_table";
        String type = "integer";
        when(objectStatus.getSchema()).thenReturn(schema);
        when(objectStatus.getName()).thenReturn(name);
        when(objectStatus.getType()).thenReturn(type);

        assertEquals(String.format("%s.%s[%s]", schema, name, type), objectResult.getSummary());
    }

    @Test
    public void testOfWithNullObjectStatus() {
        assertNull(ObjectResult.of(null));
    }

    @Test
    public void testOfWithValidObjectStatus() {
        when(objectStatus.getName()).thenReturn("test_table");
        when(objectStatus.getSchema()).thenReturn("test_schema");
        when(objectStatus.getType()).thenReturn("integer");
        when(objectStatus.getCount()).thenReturn(new AtomicLong(10));
        when(objectStatus.getTotal()).thenReturn(new AtomicLong(20));
        when(objectStatus.getStatus()).thenReturn(ObjectStatus.STATUS_SUCCESS);

        ObjectResult result = ObjectResult.of(objectStatus);

        assertEquals("test_schema", result.getSchema());
        assertEquals("test_table", result.getName());
        assertEquals("integer", result.getType());
        assertEquals(ObjectStatus.STATUS_SUCCESS, result.getStatus());
        assertNull(result.getCount());
        assertNull(result.getTotal());
        assertNull(result.getExportPaths());
    }

    @Test
    public void testGetExportPathsNull() {
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testSetExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        URL url1 = mock(URL.class);
        URL url2 = mock(URL.class);
        exportPaths.add(url1);
        exportPaths.add(url2);

        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

}