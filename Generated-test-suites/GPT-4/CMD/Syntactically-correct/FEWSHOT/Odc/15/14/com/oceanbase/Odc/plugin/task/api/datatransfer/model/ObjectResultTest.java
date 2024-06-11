
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor(){
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");

        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertEquals(0L, result.getCount().get());
        assertEquals(0L, result.getTotal().get());
        assertNull(result.getAllErrs());
        assertNull(result.getExportPaths());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus status = new ObjectStatus("test_name", new AtomicLong(1), new AtomicLong(2), Arrays.asList("Err1"));
        status.setType("test_type");
        status.setSchema("test_schema");

        ObjectResult result = ObjectResult.of(status);

        assertEquals("test_type", result.getType());
        assertEquals("test_schema", result.getSchema());
        assertEquals("test_name", result.getName());
        assertEquals(1L, result.getCount().get());
        assertEquals(2L, result.getTotal().get());
        assertEquals(Arrays.asList("Err1"), result.getAllErrs());
        assertNull(result.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult();

        result.setName("test_name");
        result.setType("test_type");
        result.setSchema("test_schema");

        assertEquals("test_schema.test_name[test_type]", result.getSummary());
    }
}
