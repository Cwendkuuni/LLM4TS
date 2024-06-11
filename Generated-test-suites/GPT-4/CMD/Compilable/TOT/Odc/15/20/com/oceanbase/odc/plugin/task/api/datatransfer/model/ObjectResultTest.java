package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
    @Test
    public void objectConstructorTest() {
        String schema = "TestSchema";
        String name = "TestName";
        String type = "TestType";
        
        ObjectResult objectResult = new ObjectResult(schema, name, type);
        Assert.assertEquals("Object name should be "+name, name, objectResult.getName());
        Assert.assertEquals("Object type should be "+type, type, objectResult.getType());
        Assert.assertEquals("Object schema should be "+schema, schema, objectResult.getSchema());
    }
    
    @Test
    public void objectOfTest() {
        String schema = "TestSchema2";
        String name = "TestName2";
        String type = "TestType2";
        AtomicLong count = new AtomicLong(0);
        AtomicLong total = new AtomicLong(0);
        
        ObjectStatus objectStatus = new ObjectStatus(name, count, total, null);
        objectStatus.setSchema(schema);
        objectStatus.setType(type);
        
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        Assert.assertEquals("Object name should be "+name, name, objectResult.getName());
        Assert.assertEquals("Object type should be "+type, type, objectResult.getType());
        Assert.assertEquals("Object schema should be "+schema, schema, objectResult.getSchema());
        Assert.assertEquals("Object count should be 0", 0, objectResult.getCount().get());
        Assert.assertEquals("Object total should be 0", 0, objectResult.getTotal().get());
    }

    @Test
    public void getSummaryTest() {
        String schema = "TestSchema3";
        String name = "TestName3";
        String type = "TestType3";

        ObjectResult objectResult = new ObjectResult(schema, name, type);
        String expectedSummary = schema + "." + name + "[" + type + "]";
        Assert.assertEquals("The summary doesn't match the expectation.", expectedSummary, objectResult.getSummary());
    }
}