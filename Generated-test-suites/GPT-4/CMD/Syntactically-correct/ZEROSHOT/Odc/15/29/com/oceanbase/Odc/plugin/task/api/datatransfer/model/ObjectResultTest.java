package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testConstructorWithParameters() {
        ObjectResult objectResult = new ObjectResult("schema1","name1","type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
        assertNull(objectResult.getStatus());
    }
    
    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(2), "status1");
        objectStatus.setType("type1");
        objectStatus.setSchema("schema1");

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(2, objectResult.getTotal().get());
        assertEquals("status1", objectResult.getStatus());
    }
    
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1","name1","type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    public void testSetAndGetExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> list = new ArrayList<>();
        objectResult.setExportPaths(list);
        assertTrue(objectResult.getExportPaths() instanceof List);
    }
}
