package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objResult = new ObjectResult("schema1", "table1", "int");
        assertEquals("schema1", objResult.getSchema());
        assertEquals("table1", objResult.getName());
        assertEquals("int", objResult.getType());
        assertEquals(new AtomicLong(0), objResult.getCount());
        assertEquals(new AtomicLong(0), objResult.getTotal());
        assertEquals(ObjectStatus.Status.SUCCESS, objResult.getStatus());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objStatus = mock(ObjectStatus.class);
        ObjectResult objResult = ObjectResult.of(objStatus);
        assertSame(objStatus.getType(), objResult.getType());
        assertSame(objStatus.getStatus(), objResult.getStatus());
        assertSame(objStatus.getName(), objResult.getName());
        assertSame(objStatus.getSchema(), objResult.getSchema());
        assertSame(objStatus.getCount(), objResult.getCount());
        assertSame(objStatus.getTotal(), objResult.getTotal());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objResult = new ObjectResult("schema1", "table1", "int");
        assertEquals("schema1.table1[int]", objResult.getSummary());
    }

    @Test
    public void testEmptyConstructor() {
        ObjectResult objResult = new ObjectResult();
        assertNull(objResult.getType());
        assertNull(objResult.getStatus());
        assertNull(objResult.getName());
        assertNull(objResult.getSchema());
        assertNull(objResult.getCount());
        assertNull(objResult.getTotal());
    }
}