package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schema", "name", "type");
    }

    @Test
    public void testObjectResult() {
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());

        objectResult.setExportPaths(Arrays.asList(new URL("http://example.com")));
        assertNotNull(objectResult.getExportPaths());
        assertEquals(1, objectResult.getExportPaths().size());
    }

    @Test
    public void testSummary() {
        String summary = objectResult.getSummary();
        assertNotNull(summary);
        assertEquals("schema.name[type]", summary);
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("newName", new AtomicLong(0), 
            new AtomicLong(0), null);
        objectStatus.setType("newType");
        objectStatus.setSchema("newSchema");
        
        ObjectResult result = ObjectResult.of(objectStatus);

        assertNotNull(result);
        assertEquals("newType", result.getType());
        assertEquals("newName", result.getName());
        assertEquals("newSchema", result.getSchema());
    }
}
