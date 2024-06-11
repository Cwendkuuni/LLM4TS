
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectResultTest {

    private ObjectStatus objectStatus;

    @Before
    public void setupTestEnvironment() {
        objectStatus = mock(ObjectStatus.class);
        when(objectStatus.getType()).thenReturn("testType");
        when(objectStatus.getStatus()).thenReturn("Success");
        when(objectStatus.getName()).thenReturn("testName");
        when(objectStatus.getSchema()).thenReturn("testSchema");
        when(objectStatus.getCount()).thenReturn(new AtomicLong(10));
        when(objectStatus.getTotal()).thenReturn(new AtomicLong(20));
    }

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertNotNull(objectResult);
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals("testSchema", objectResult.getSchema());
    }

    @Test
    public void testOf() {
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals(10L, objectResult.getCount().get());
        assertEquals(20L, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        String summary = objectResult.getSummary();
        assertNotNull(summary);
        assertEquals("testSchema.testName[testType]", summary);
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(Arrays.asList(mock(URL.class)));
        assertNotNull(objectResult.getExportPaths());
        assertEquals(1, objectResult.getExportPaths().size());
    }
}
