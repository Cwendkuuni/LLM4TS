
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("SchemaTest", "NameTest", "TypeTest");
    }

    @Test
    public void testObjectResult_ConstructorWithParams() {
        assertEquals("SchemaTest", objectResult.getSchema());
        assertEquals("NameTest", objectResult.getName());
        assertEquals("TypeTest", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResult_CopyingConstructor() {
        ObjectStatus that = new ObjectStatus("NameTest2", new AtomicLong(0), new AtomicLong(0), null);
        that.setType("TypeTest2");
        that.setSchema("SchemaTest2");

        ObjectResult result = ObjectResult.of(that);
        assertEquals("SchemaTest2", result.getSchema());
        assertEquals("NameTest2", result.getName());
        assertEquals("TypeTest2", result.getType());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals("SchemaTest.NameTest[TypeTest]", summary);
    }

    // Further methods will be required to test edge cases and error scenarios, for example when AtomicLong value changes, or when constructed with null values
}
