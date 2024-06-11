package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicLong;

import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {

    private ObjectResult objectResult;
    private String schema = "schema_test";
    private String name = "name_test";
    private String type = "type_test";

    @Before
    public void setup() {
        objectResult = new ObjectResult(schema, name, type);
    }

    //Expert 1
    @Test
    public void testObjectResultConstructor() {
        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertEquals(new AtomicLong(0).longValue(), objectResult.getCount().longValue());
        assertEquals(new AtomicLong(0).longValue(), objectResult.getTotal().longValue());
    }

    //Expert 2
    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus(name, new AtomicLong(1), new AtomicLong(2), type);
        ObjectResult result = ObjectResult.of(objectStatus);

        assertEquals(name, result.getName());
        assertEquals(type, result.getType());
        assertEquals(new AtomicLong(1).longValue(), result.getCount().longValue());
        assertEquals(new AtomicLong(2).longValue(), result.getTotal().longValue());
    }

    //Expert 3
    @Test
    public void testGetSummary() {
        String expectedSummary = schema + "." + name + "[" + type + "]";
        String actualSummary = objectResult.getSummary();

        assertEquals(expectedSummary, actualSummary);
    }
}