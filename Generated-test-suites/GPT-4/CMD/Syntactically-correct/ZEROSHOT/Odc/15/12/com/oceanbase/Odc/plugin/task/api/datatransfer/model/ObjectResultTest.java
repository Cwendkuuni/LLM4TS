package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schema", "name", "type");
    }

    @Test
    public void testGetSummary() {
        String expected = "schema.name[type]";
        String actual = objectResult.getSummary();
        assertEquals(expected, actual);
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(1), new AtomicLong(2), "type");
        ObjectResult expected = ObjectResult.of(objectStatus);

        assertEquals(objectStatus.getName(), expected.getName());
        assertEquals(objectStatus.getSchema(), expected.getSchema());
        assertEquals(objectStatus.getType(), expected.getType());
        assertEquals(objectStatus.getStatus(), expected.getStatus());
        assertEquals(objectStatus.getCount().get(), expected.getCount().get());
        assertEquals(objectStatus.getTotal().get(), expected.getTotal().get());
    }
}
