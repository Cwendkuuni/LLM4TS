
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Test;

public class ObjectResultTest {
    
    @Test
    public void testObjectResultConstructorWithParameters() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
    }
    
    @Test
    public void testObjectResultConstructorWithAnotherObjectStatus() {
        ObjectStatus objectStatus = new ObjectStatus("testName2", new AtomicLong(1), new AtomicLong(2), null);
        objectStatus.setSchema("testSchema2");
        objectStatus.setType("testType2");
        
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        
        assertEquals("testSchema2", objectResult.getSchema());
        assertEquals("testName2", objectResult.getName());
        assertEquals("testType2", objectResult.getType());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(2, objectResult.getTotal().get());
    }
    
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        String summary = objectResult.getSummary();
        assertEquals("testSchema.testName[testType]", summary);
    }
    
    @Test
    public void testGetExportPaths() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        objectResult.setExportPaths(new ArrayList<URL>());
        assertNull(objectResult.getExportPaths());
    }

}
