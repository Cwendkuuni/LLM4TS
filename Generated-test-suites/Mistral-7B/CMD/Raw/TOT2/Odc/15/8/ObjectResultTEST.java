package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertNull(objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("name1");
        objectStatus.setType("type1");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertNull(objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema1");
        objectResult.setName("name1");
        objectResult.setType("type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    public void testConstructorWithStatus() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1", ObjectStatus.OK);
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(ObjectStatus.OK, objectResult.getStatus());
        assertNull(objectResult.getExportPaths());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSchema() {
        new ObjectResult(null, "name1", "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        new ObjectResult("schema1", null, "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullType() {
        new ObjectResult("schema1", "name1", null);
    }

    @Test
    public void testJsonIgnoreMethod() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = Arrays.asList(new URL("http://example.com/export1"), new URL("http://example.com/export2"));
        objectResult.setExportPaths(exportPaths);
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testSetAndGetCount() {
        ObjectResult objectResult = new ObjectResult();
        AtomicLong count = new AtomicLong(10);
        objectResult.setCount(count);
        assertEquals(10, objectResult.getCount().get());
        count.set(20);
        assertEquals(20, objectResult.getCount().get());
    }

    @Test
    public void testSetAndGetTotal() {
        ObjectResult objectResult = new ObjectResult();
        AtomicLong total = new AtomicLong(50);
        objectResult.setTotal(total);
        assertEquals(50, objectResult.getTotal().get());
        total.set(30);
        assertEquals(30, objectResult.getTotal().get());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullStatus() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setStatus(null);
    }
}