
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import java.net.URL;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testConstructorWithParameters() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
    }

    @Test
    public void testOf() {
        ObjectStatus status = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(2), "SCHEMAS");
        status.setSchema("schema1");
        ObjectResult objectResult = ObjectResult.of(status);

        assertEquals("name1", objectResult.getName());
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("SCHEMAS", objectResult.getType());
        assertEquals(1L, objectResult.getCount().get());
        assertEquals(2L, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(Collections.singletonList(new URL("http://localhost:1234")));

        assertNotNull(objectResult.getExportPaths());
        assertEquals(1, objectResult.getExportPaths().size());
        assertEquals(new URL("http://localhost:1234"), objectResult.getExportPaths().get(0));
    }
}
