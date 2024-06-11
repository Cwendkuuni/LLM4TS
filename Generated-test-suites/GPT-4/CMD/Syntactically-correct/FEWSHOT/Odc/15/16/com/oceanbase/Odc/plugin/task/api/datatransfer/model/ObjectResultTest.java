
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
    @Test
    public void testObjectResultConstructor() {
        ObjectResult result = new ObjectResult("schemaX", "nameY", "typeZ");
        assertEquals("schemaX", result.getSchema());
        assertEquals("nameY", result.getName());
        assertEquals("typeZ", result.getType());
        assertNull(result.getExportPaths());
    }

    @Test
    public void testOf() {
        ObjectStatus status = new ObjectStatus("name", new AtomicLong(2), new AtomicLong(4), "statusX");
        status.setType("typeX");
        status.setSchema("schemaX");
        ObjectResult result = ObjectResult.of(status);

        assertEquals("typeX", result.getType());
        assertEquals("statusX", result.getStatus());
        assertEquals("name", result.getName());
        assertEquals("schemaX", result.getSchema());
        assertEquals(2, result.getCount().get());
        assertEquals(4, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("schemaX", "nameY", "typeZ");
        assertEquals("schemaX.nameY[typeZ]", result.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult result = new ObjectResult();
        List<URL> urls = new ArrayList<>();
        urls.add(new URL("http://example.com"));
        
        result.setExportPaths(urls);
        assertEquals(urls, result.getExportPaths());
    }
}
