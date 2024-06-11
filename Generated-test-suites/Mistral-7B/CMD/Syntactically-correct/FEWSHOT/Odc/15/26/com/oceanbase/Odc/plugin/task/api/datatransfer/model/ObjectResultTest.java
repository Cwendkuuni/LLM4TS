package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schema", "name", "type");
    }

    @Test
    public void testConstructor() {
        assertNotNull(objectResult.getSchema());
        assertNotNull(objectResult.getName());
        assertNotNull(objectResult.getType());
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertNotNull(objectResult.getStatus());
        assertEquals(ObjectStatus.Status.OK, objectResult.getStatus());
        assertNotNull(objectResult.getCount());
        assertEquals(0, objectResult.getCount().get());
        assertNotNull(objectResult.getTotal());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        ObjectResult result = ObjectResult.of(objectStatus);
        assertNotNull(result.getSchema());
        assertNotNull(result.getName());
        assertNotNull(result.getType());
        assertNotNull(result.getCount());
        assertNotNull(result.getTotal());
        assertEquals(objectStatus.getSchema(), result.getSchema());
        assertEquals(objectStatus.getName(), result.getName());
        assertEquals(objectStatus.getType(), result.getType());
        assertEquals(objectStatus.getStatus(), result.getStatus());
        assertEquals(objectStatus.getCount().get(), result.getCount().get());
        assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        assertEquals("schema.name[type]", objectResult.getSummary());
    }
}