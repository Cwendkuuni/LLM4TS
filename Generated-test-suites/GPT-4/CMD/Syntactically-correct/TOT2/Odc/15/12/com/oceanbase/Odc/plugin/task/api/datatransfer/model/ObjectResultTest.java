
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    private ObjectResult objectResult;
    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        objectStatus = new ObjectStatus("Name", new AtomicLong(10), new AtomicLong(20), "status");
        objectResult = new ObjectResult("Schema", "Name", "Type");
    }

    @Test
    public void testOf() {
        ObjectResult newResult = ObjectResult.of(objectStatus);
        assertEquals("Name", newResult.getName());
        assertEquals("Type", newResult.getType());
        assertEquals("Schema", newResult.getSchema());
        assertEquals(10, newResult.getCount().get());
        assertEquals(20, newResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals("Schema.Name[Type]", summary);
    }

    @Test(expected=NullPointerException.class)
    public void testOf_NullObjectStatus() {
        ObjectResult newResult = ObjectResult.of(null);
    }
    
    @Test(expected=NullPointerException.class)
    public void testGetSummary_NullFields() {
        objectResult = new ObjectResult();
        String summary = objectResult.getSummary();
    }
    
}
