package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

@SuppressWarnings("deprecation")
public class ObjectResultTest {

    private ObjectStatus mockObjectStatus;
    private ObjectResult objectResult;

    @Before
    public void setup() {
        mockObjectStatus = Mockito.mock(ObjectStatus.class);
        objectResult = new ObjectResult();
    }

    @Test
    public void testObjectResultConstruction() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertEquals(ObjectStatus.STATUS_SUCCESS, result.getStatus());
        assertEquals(0L, result.getCount().get());
        assertEquals(0L, result.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        when(mockObjectStatus.getType()).thenReturn("type1");
        when(mockObjectStatus.getStatus()).thenReturn(ObjectStatus.STATUS_SUCCESS);
        when(mockObjectStatus.getName()).thenReturn("name1");
        when(mockObjectStatus.getSchema()).thenReturn("schema1");
        when(mockObjectStatus.getCount()).thenReturn(new AtomicLong(10));
        when(mockObjectStatus.getTotal()).thenReturn(new AtomicLong(20));

        ObjectResult result = ObjectResult.of(mockObjectStatus);

        assertEquals(ObjectStatus.STATUS_SUCCESS, result.getStatus());
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertEquals(10L, result.getCount().get());
        assertEquals(20L, result.getTotal().get());

        verify(mockObjectStatus, times(1)).getType();
        verify(mockObjectStatus, times(1)).getStatus();
        verify(mockObjectStatus, times(1)).getName();
        verify(mockObjectStatus, times(1)).getSchema();
        verify(mockObjectStatus, times(1)).getCount();
        verify(mockObjectStatus, times(1)).getTotal();
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult();
        result.setSchema("schema1");
        result.setName("name1");
        result.setType("type1");

        String expected = "schema1.name1[type1]";
        assertEquals(expected, result.getSummary());
    }

    @Test
    public void testExportPathsIsJsonIgnore() {
        ObjectResult result = new ObjectResult();
        result.setExportPaths(new ArrayList<>());

        assertNull(result.getExportPaths());
    }
}