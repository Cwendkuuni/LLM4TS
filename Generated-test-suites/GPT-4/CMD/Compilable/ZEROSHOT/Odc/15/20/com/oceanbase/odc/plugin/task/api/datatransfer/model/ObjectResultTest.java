package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("Schema1", "Name1", "Type1");
        assertEquals("Schema1", objectResult.getSchema());
        assertEquals("Name1", objectResult.getName());
        assertEquals("Type1", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setSchema("Schema1");
        objectStatus.setName("Name1");
        objectStatus.setType("Type1");
        AtomicLong count = new AtomicLong(5);
        objectStatus.setCount(count);
        AtomicLong total = new AtomicLong(10);
        objectStatus.setTotal(total);
        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("Schema1", objectResult.getSchema());
        assertEquals("Name1", objectResult.getName());
        assertEquals("Type1", objectResult.getType());
        assertEquals(count.get(), objectResult.getCount().get());
        assertEquals(total.get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("Schema1", "Name1", "Type1");
        assertEquals("Schema1.Name1[Type1]", objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
