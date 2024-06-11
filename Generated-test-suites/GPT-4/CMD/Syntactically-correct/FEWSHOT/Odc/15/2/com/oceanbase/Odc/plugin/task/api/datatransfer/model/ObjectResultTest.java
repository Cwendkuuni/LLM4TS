
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema_test", "name_test", "type_test");
        assertEquals("schema_test", objectResult.getSchema());
        assertEquals("name_test", objectResult.getName());
        assertEquals("type_test", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
        assertNull(objectResult.getStatus());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("name_test", new AtomicLong(10), new AtomicLong(20), "status_test");
        objectStatus.setSchema("schema_test");
        objectStatus.setType("type_test");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema_test", "name_test", "type_test");
        String summary = objectResult.getSummary();
        assertEquals("schema_test.name_test[type_test]", summary);
    }

    @Test
    public void testGetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema_test", "name_test", "type_test");
        objectResult.setExportPaths(Arrays.asList());
        assertNotNull(objectResult.getExportPaths());
        assertEquals(0, objectResult.getExportPaths().size());
    }
}
