package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus that = new ObjectStatus("name", new AtomicLong(5), new AtomicLong(10), "status");
        that.setType("type");
        that.setSchema("schema");
        ObjectResult objectResult = ObjectResult.of(that);

        assertEquals("name", objectResult.getName());
        assertEquals(new Long(5), new Long(objectResult.getCount().get()));
        assertEquals(new Long(10), new Long(objectResult.getTotal().get()));
        assertEquals("status", objectResult.getStatus());
        assertEquals("type", objectResult.getType());
        assertEquals("schema", objectResult.getSchema());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String expectedSummary = "schema.name[type]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        List<URL> urls = new ArrayList<>();
        try {
            urls.add(new URL("http://some.url.com"));
            urls.add(new URL("http://some.other.url.com"));
        } catch (Exception e) {
            //handle exception
        }
        objectResult.setExportPaths(urls);
        assertEquals(urls, objectResult.getExportPaths());
    }
}
