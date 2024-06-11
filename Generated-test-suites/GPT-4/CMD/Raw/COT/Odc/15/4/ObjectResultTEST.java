package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Test;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testObjectResult() {
        ObjectResult objRes = new ObjectResult("Schema1", "Name1", "Type1");
        assertEquals("Schema1", objRes.getSchema());
        assertEquals("Name1", objRes.getName());
        assertEquals("Type1", objRes.getType());
    }

    @Test
    public void testOf() {
        AtomicLong count = new AtomicLong(10);
        AtomicLong total = new AtomicLong(20);
        ObjectStatus objStatus = new ObjectStatus("Name2", count, total, "Status2");
        objStatus.setSchema("Schema2");
        objStatus.setType("Type2");
        ObjectResult objRes = ObjectResult.of(objStatus);
        assertEquals("Schema2", objRes.getSchema());
        assertEquals("Name2", objRes.getName());
        assertEquals("Type2", objRes.getType());
        assertEquals(10, objRes.getCount().get());
        assertEquals(20, objRes.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objRes = new ObjectResult("Schema1", "Name1", "Type1");
        String expectedSummary = "Schema1.Name1[Type1]";
        assertEquals(expectedSummary, objRes.getSummary());
    }
}