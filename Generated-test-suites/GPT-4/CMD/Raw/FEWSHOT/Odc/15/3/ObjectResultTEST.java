package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;

public class ObjectResultTest {
    @Test
    public void testObjectResultConstuctor() {
        ObjectResult objRes = new ObjectResult("schema1", "name1", "type1");
        assertEquals(objRes.getName(), "name1");
        assertEquals(objRes.getSchema(), "schema1");
        assertEquals(objRes.getType(), "type1");
        assertEquals(objRes.getCount().get(), 0);
        assertEquals(objRes.getTotal().get(), 0);
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objStat = new ObjectStatus("name2", new AtomicLong(5), new AtomicLong(10), null);
        objStat.setSchema("schema2");
        objStat.setType("type2");

        ObjectResult objRes = ObjectResult.of(objStat);
        assertEquals(objRes.getName(), objStat.getName());
        assertEquals(objRes.getSchema(), objStat.getSchema());
        assertEquals(objRes.getType(), objStat.getType());
        assertEquals(objRes.getCount().get(), objStat.getCount().get());
        assertEquals(objRes.getTotal().get(), objStat.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objRes = new ObjectResult("schema3", "name3", "type3");
        String summary = objRes.getSummary();
        assertEquals(summary, "schema3.name3[type3]");
    }

    @Test
    public void testExportPaths() {
        ObjectResult objRes = new ObjectResult();
        assertNull(objRes.getExportPaths());
        
        List<URL> urls = new ArrayList<URL>();
        urls.add(new URL("http://example.org"));
        objRes.setExportPaths(urls);
        
        assertEquals(urls, objRes.getExportPaths());
    }
}