
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import java.util.concurrent.atomic.AtomicLong;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {
    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("testSchema", "testName", "testType");
    }
    
    @Test
    public void testSetName() {
        objectResult.setName("newName");
        Assert.assertEquals("newName", objectResult.getName());
    }

    @Test
    public void testSetSchema() {
        objectResult.setSchema("newSchema");
        Assert.assertEquals("newSchema", objectResult.getSchema());
    }

    @Test
    public void testSetType() {
        objectResult.setType("newType");
        Assert.assertEquals("newType", objectResult.getType());
    }

    @Test
    public void testSetCountAndTotal() {
        objectResult.setCount(new AtomicLong(5));
        objectResult.setTotal(new AtomicLong(10));
        Assert.assertEquals(5, objectResult.getCount().get());
        Assert.assertEquals(10, objectResult.getTotal().get());
    }

    @Test
    public void factoryMethod_CopyObjectStatus() {
        ObjectStatus objStatus = new ObjectStatus();
        objStatus.setCount(new AtomicLong(5));
        objStatus.setTotal(new AtomicLong(10));
        ObjResult = ObjectResult.of(objStatus);
        Assert.assertEquals(objStatus.getCount().get(), objectResult.getCount().get());
        Assert.assertEquals(objStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String expectSummary = "testSchema.testName[testType]";
        Assert.assertEquals(expectSummary, objectResult.getSummary());
    }
}
