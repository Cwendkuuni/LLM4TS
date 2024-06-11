package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {
    private ObjectResult testObject;
    private ObjectStatus statusObj;

    @Before
    public void setUp() {
        testObject = new ObjectResult("Schema1", "Object1", "Type1");
        statusObj = new ObjectStatus("Object1", new AtomicLong(0), new AtomicLong(0), null);
        statusObj.setSchema("Schema1");
        statusObj.setType("Type1");
    }

    @Test
    public void testConstructor() {
        assertEquals("Schema1", testObject.getSchema());
        assertEquals("Object1", testObject.getName());
        assertEquals("Type1", testObject.getType());
    }

    @Test
    public void testOfMethod() {
        ObjectResult copiedObject = ObjectResult.of(statusObj);
        assertEquals(statusObj.getSchema(), copiedObject.getSchema());
        assertEquals(statusObj.getName(), copiedObject.getName());
        assertEquals(statusObj.getType(), copiedObject.getType());
        assertEquals(statusObj.getCount().get(), copiedObject.getCount().get());
        assertEquals(statusObj.getTotal().get(), copiedObject.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String expectedSummary = "Schema1.Object1[Type1]";
        assertEquals(expectedSummary, testObject.getSummary());
    }
}