
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import java.util.*;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objresult = new ObjectResult("TestSchema", "TestName", "TestType");
        assertEquals("TestSchema", objresult.getSchema());
        assertEquals("TestName", objresult.getName());
        assertEquals("TestType", objresult.getType());
    }

    @Test
    public void testOf() {
        ObjectStatus objstatus = new ObjectStatus("TestName2", new AtomicLong(1), new AtomicLong(1), null);
        objstatus.setSchema("TestSchema2");
        objstatus.setType("TestType2");

        ObjectResult result = ObjectResult.of(objstatus);

        assertEquals("TestName2", result.getName());
        assertEquals("TestSchema2", result.getSchema());
        assertEquals("TestType2", result.getType());
        assertEquals(1, result.getCount().get());
        assertEquals(1, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objresult = new ObjectResult("TestSchema3", "TestName3", "TestType3");

        String summary = objresult.getSummary();

        assertEquals("TestSchema3.TestName3[TestType3]", summary);
    }

    @Test
    public void testConstructor_NullValues() {
        // This test is asserting that the class handles null parameters in its constructor
        ObjectResult objResult = new ObjectResult(null, "TestName", "TestType");
        assertNull(objResult.getSchema());

        objResult = new ObjectResult("TestSchema", null, "TestType");
        assertNull(objResult.getName());

        objResult = new ObjectResult("TestSchema", "TestName", null);
        assertNull(objResult.getType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOf_NullObjectStatus() {
        // This test is asserting that the static of method throws an exception if provided with a null ObjectStatus
        ObjectResult result = ObjectResult.of(null);
    }
  
    @Test(expected = NullPointerException.class)
    public void testGetSummary_NullSchema() {
        // This test is asserting that the getSummary metod throws an exception if the schema is null
        ObjectResult objResult = new ObjectResult(null, "TestName", "TestType");
        String summary = objresult.getSummary();
    }
}
