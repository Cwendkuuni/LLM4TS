
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Arrays;
import java.net.URL;
import java.net.MalformedURLException;

public class ObjectResultTest {
    @Test
    public void testObjectResultConstructor() throws MalformedURLException {
        ObjectResult objResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals(objResult.getSchema(), "schema1");
        assertEquals(objResult.getName(), "name1");
        assertEquals(objResult.getType(), "type1");
        assertEquals(objResult.getCount().get(), 0);
        assertEquals(objResult.getTotal().get(), 0);
        assertNull(objResult.getExportPaths());
    }

    @Test
    public void testObjectResultOfStatus() throws MalformedURLException {
        ObjectStatus objStatus = new ObjectStatus("name1", new AtomicLong(12), new AtomicLong(13), null);
        objStatus.setSchema("schema2");
        ObjectResult objResult = ObjectResult.of(objStatus);
        assertEquals(objResult.getName(), "name1");
        assertEquals(objResult.getSchema(), "schema2");
        assertEquals(objResult.getCount().get(), 12);
        assertEquals(objResult.getTotal().get(), 13);
        assertNull(objResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objResult = new ObjectResult("schema3", "name3", "type3");
        String expected = "schema3.name3[type3]";
        assertEquals(expected, objResult.getSummary());
    }
    
    @Test
    public void testExportPaths() throws MalformedURLException {
        ObjectResult objResult = new ObjectResult("schema4", "name4", "type4");
        URL url1 = new URL("http://localhost");
        URL url2 = new URL("http://google.com");
        objResult.setExportPaths(Arrays.asList(url1, url2));
        assertEquals(2, objResult.getExportPaths().size());
        assertEquals(url1, objResult.getExportPaths().get(0));
        assertEquals(url2, objResult.getExportPaths().get(1));
    }
}
