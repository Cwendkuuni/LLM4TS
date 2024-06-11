
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {
    
    @Test
    public void testObjectResultConstructor() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertEquals(new AtomicLong(0), result.getCount());
        assertEquals(new AtomicLong(0), result.getTotal());
        assertNotNull(result.getExportPaths());
    }
    
    @Test
    public void testOf_MustReturnProperObject() {
        ObjectStatus status = new ObjectStatus();
        status.setSchema("schema");
        status.setName("name");
        status.setType("type");
        status.setCount(new AtomicLong(10));
        status.setTotal(new AtomicLong(20));
        ObjectResult result = ObjectResult.of(status);
        assertNotNull(result);
        assertEquals(status.getSchema(), result.getSchema());
        assertEquals(status.getName(), result.getName());
        assertEquals(status.getType(), result.getType());
        assertEquals(status.getCount().get(), result.getCount().get());
        assertEquals(status.getTotal().get(), result.getTotal().get());
    }
    
    @Test
    public void testGetSummary_MustReturnProperSummary(){
        ObjectResult result = new ObjectResult("schema2", "name2", "type2");
        String summary = result.getSummary();
        assertNotNull(summary);
        assertEquals("schema2.name2[type2]", summary);
    }
    
    @Test(expected = NullPointerException.class)
    public void testOf_NullObjectStatus_ShouldThrowNullPointerException(){
        ObjectResult.of(null);
    }

}
