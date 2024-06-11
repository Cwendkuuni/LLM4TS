
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {
    @Test
    public void testConstructor() {
        ObjectResult or = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", or.getSchema());
        assertEquals("name1", or.getName());
        assertEquals("type1", or.getType());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus os = new ObjectStatus("osName", new AtomicLong(10), new AtomicLong(20), ObjectStatus.Status.FAILED);
        os.setSchema("osSchema");
        os.setType("osType");
        
        ObjectResult or = ObjectResult.of(os);
        
        assertEquals("osName", or.getName());
        assertEquals("osSchema", or.getSchema());
        assertEquals("osType", or.getType());
        assertEquals(os.getCount().get(), or.getCount().get());
        assertEquals(os.getTotal().get(), or.getTotal().get());
        assertEquals(os.getStatus(), or.getStatus());
    }

    @Test
    public void testGetSummary() {
        ObjectResult or = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", or.getSummary());
    }

    @Test
    public void testExportPaths(){
        ObjectResult or = new ObjectResult();
        List<URL> dummyList = new LinkedList<URL>();
        assertNull(or.getExportPaths());

        or.setExportPaths(dummyList);
        assertEquals(dummyList, or.getExportPaths());
    }
}
