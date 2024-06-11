package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.AtomicLong;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class ObjectResultTest {

    private ObjectResult objectResult;
    private List<URL> exportPaths;

    @Before
    public void setup() {
        objectResult = new ObjectResult("schema", "name", "type");
        exportPaths = new ArrayList<>();
    }

    @Test
    public void testObjectResultConstruction() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertNull(result.getStatus());
        assertEquals(0, (long) result.getCount().get());
        assertEquals(0, (long) result.getTotal().get());

        ObjectResult result2 = new ObjectResult("schema2", "name2", "type2");
        result2.setStatus(ObjectStatus.Status.FAILED);
        result2.setCount(new AtomicLong(10));
        result2.setTotal(new AtomicLong(20));
        assertEquals(ObjectStatus.Status.FAILED, result2.getStatus());
        assertEquals(10, (long) result2.getCount().get());
        assertEquals(20, (long) result2.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectStatus status = mock(ObjectStatus.class);
        when(status.getName()).thenReturn("name");
        when(status.getSchema()).thenReturn("schema");
        when(status.getType()).thenReturn("type");
        when(status.getCount()).thenReturn(new AtomicLong(10));
        when(status.getTotal()).thenReturn(new AtomicLong(20));
        when(status.getStatus()).thenReturn(ObjectStatus.Status.FAILED);

        ObjectResult result = ObjectResult.of(status);
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
        assertEquals(ObjectStatus.Status.FAILED, result.getStatus());
        assertEquals(10, (long) result.getCount().get());
        assertEquals(20, (long) result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", result.getSummary());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExportPathsNull() {
        objectResult.setExportPaths(null);
    }

    @Test
    public void testSetExportPaths() {
        URL url1 = mock(URL.class);
        URL url2 = mock(URL.class);
        exportPaths.add(url1);
        exportPaths.add(url2);
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testToString() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        String expected = String.format("%s ObjectResult{schema='%s', name='%s', type='%s', status=%s, count=%d, total=%d}",
                getClass().getName(), "schema", "name", "type", ObjectStatus.Status.SUCCESS, 10, 20);
        assertEquals(expected, result.toString());
    }
}