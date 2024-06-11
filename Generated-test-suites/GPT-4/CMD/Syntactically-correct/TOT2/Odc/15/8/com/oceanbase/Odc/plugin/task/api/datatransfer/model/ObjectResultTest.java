package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import static org.junit.Assert.*;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;

public class ObjectResultTest {
    @Test
    public void constructorTest() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        //... continue assertions
    }

    @Test
    public void ofTest() {
        ObjectStatus status = new ObjectStatus("name1", new AtomicLong(0), new AtomicLong(0), null);
        status.setSchema("schema1");
        status.setType("type1");
        ObjectResult result = ObjectResult.of(status);
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        //... continue assertions
    }

    @Test
    public void getSummaryTest() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        String expected = "schema1.name1[type1]";
        assertEquals(expected, result.getSummary());
    }

    // Here, also add tests for edge cases and maybe invalid parameters. It's hard to propose more without any info about the class    
}