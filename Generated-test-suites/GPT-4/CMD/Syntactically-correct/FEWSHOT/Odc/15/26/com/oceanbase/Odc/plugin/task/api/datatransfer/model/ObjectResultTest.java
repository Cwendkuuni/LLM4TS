package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicLong;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.tools.loaddump.common.model.ObjectStatus;


public class ObjectResultTest {
    
    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        objectStatus = new ObjectStatus("name", new AtomicLong(1), new AtomicLong(2), "test");
    }

    @Test
    public void testObjectResultWithStringParametersConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertNull(objectResult.getStatus());
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
        assertNull(objectResult.getFailureCause());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultOf() {
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("test", objectResult.getStatus());
        assertEquals("name", objectResult.getName());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(2, objectResult.getTotal().get());
        assertNull(objectResult.getType());
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getExportPaths());
        assertNull(objectResult.getFailureCause());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test
    public void testGetSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        objectResult.setExportPaths(Collections.singletonList(null));
        assertNotNull(objectResult.getExportPaths());
        assertEquals(1, objectResult.getExportPaths().size());
    }

}