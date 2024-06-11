package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(5), new AtomicLong(10), null);
        objectStatus.setSchema("schema");
        objectStatus.setStatus("status");
        objectStatus.setType("type");
        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals("status", objectResult.getStatus());
        assertEquals(5, objectResult.getCount().get());
        assertEquals(10, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    public void testGetExportPaths() {
        String strUrl = "http://example.com";
        URL url = new URL(strUrl);
        ArrayList<URL> urlList = new ArrayList<>();
        urlList.add(url);
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(urlList);

        assertEquals(url, objectResult.getExportPaths().get(0));
    }
}
