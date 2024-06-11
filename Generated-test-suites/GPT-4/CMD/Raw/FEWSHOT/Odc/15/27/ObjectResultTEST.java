
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Test;

public class ObjectResultTest {
    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("TestSchema", "TestName", "TestType");
        assertEquals("TestSchema", objectResult.getSchema());
        assertEquals("TestName", objectResult.getName());
        assertEquals("TestType", objectResult.getType());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
        assertNull(objectResult.getStatus());
    }
    
    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus("TestName", new AtomicLong(1), new AtomicLong(1), "TestStatus");
        status.setType("TestType");
        status.setSchema("TestSchema");

        ObjectResult objectResult = ObjectResult.of(status);

        assertEquals("TestSchema", objectResult.getSchema());
        assertEquals("TestName", objectResult.getName());
        assertEquals("TestType", objectResult.getType());
        assertEquals(new AtomicLong(1), objectResult.getCount());
        assertEquals(new AtomicLong(1), objectResult.getTotal());
        assertEquals("TestStatus", objectResult.getStatus());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("TestSchema", "TestName", "TestType");
        String expected = "TestSchema.TestName[TestType]";
        assertEquals(expected, objectResult.getSummary());
    }
}
