package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("status_name");
        objectStatus.setSchema("status_schema");
        objectStatus.setType("status_type");
        objectStatus.setCount(new AtomicLong(30));
        objectStatus.setTotal(new AtomicLong(60));
        
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema_summary", "name_summary", "type_summary");
        
        String expectedSummary = "schema_summary.name_summary[type_summary]";
        
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        
        List<URL> urlList = new ArrayList<URL>();
        objectResult.setExportPaths(urlList);

        assertEquals(urlList, objectResult.getExportPaths());
    }
}