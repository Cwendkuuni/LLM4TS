package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import java.util.ArrayList;
import java.util.List;

public class ObjectResultTest {

    @Test
    public void testObjectResult() {
        String schema = "schema";
        String name = "name";
        String type = "type";
        ObjectResult objectResult = new ObjectResult(schema, name, type);
        assertEquals(objectResult.getSchema(), schema);
        assertEquals(objectResult.getName(), name);
        assertEquals(objectResult.getType(), type);
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        String type = "type";
        objectStatus.setType(type);
        String status = "status";
        objectStatus.setStatus(status);
        String name = "name";
        objectStatus.setName(name);
        String schema = "schema";
        objectStatus.setSchema(schema);
        AtomicLong count = new AtomicLong(10);
        objectStatus.setCount(count);
        AtomicLong total = new AtomicLong(100);
        objectStatus.setTotal(total);

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectResult.getType(), objectStatus.getType());
        assertEquals(objectResult.getStatus(), objectStatus.getStatus());
        assertEquals(objectResult.getName(), objectStatus.getName());
        assertEquals(objectResult.getSchema(), objectStatus.getSchema());
        assertEquals(objectResult.getCount().get(), objectStatus.getCount().get());
        assertEquals(objectResult.getTotal().get(), objectStatus.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String schema = "mySchema";
        String name = "myName";
        String type = "myType";

        ObjectResult objectResult = new ObjectResult(schema, name, type);
        String expectedSummary = schema + "." + name + "[" + type + "]";
        
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);
        
        assertNotNull(objectResult.getExportPaths());
    }
}
