package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {

    private ObjectResult objectResult;
    private String name = "testName";
    private String schema = "testSchema";
    private String type = "testType";
    private AtomicLong count = new AtomicLong(1);
    private AtomicLong total = new AtomicLong(5);

    @Before
    public void setUp() {
        objectResult = new ObjectResult(schema, name, type);
        objectResult.setCount(count);
        objectResult.setTotal(total);
    }

    @Test
    public void testGetExportPaths() {
        List<URL> urlList = new ArrayList<>();
        objectResult.setExportPaths(urlList);
        assertEquals(urlList, objectResult.getExportPaths());
    }

    @Test
    public void testSummary() {
        String expectedSummary = schema + "." + name + "[" + type + "]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testOf() {
        ObjectStatus that = new ObjectStatus();
        that.setName(name);
        that.setType(type);
        that.setSchema(schema);
        that.setCount(count);
        that.setTotal(total);

        ObjectResult result = ObjectResult.of(that);
        assertEquals(name, result.getName());
        assertEquals(schema, result.getSchema());
        assertEquals(type, result.getType());
        assertEquals(count.get(), result.getCount().get());
        assertEquals(total.get(), result.getTotal().get());
    }
}
