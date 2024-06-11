
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {
    private ObjectResult objectResult;
    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        objectStatus = new ObjectStatus("TestObject", new AtomicLong(100), new AtomicLong(200), ObjectStatus.GENERATING);
        objectResult = new ObjectResult("Schema1", "TestObject1", "Type1");
    }

    @Test
    public void testSetAndGetExportPaths() {
        List<URL> mockExportPaths = Arrays.asList(new URL("http://example.com"));
        objectResult.setExportPaths(mockExportPaths);

        assertEquals(mockExportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testConstructorWithParameters() {
        assertEquals("TestObject1", objectResult.getName());
        assertEquals("Type1", objectResult.getType());
        assertEquals("Schema1", objectResult.getSchema());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
        assertNull(objectResult.getStatus());
    }

    @Test
    public void testObjectResultOf() {
        ObjectResult convertedObjectResult = ObjectResult.of(objectStatus);

        assertEquals(objectStatus.getType(), convertedObjectResult.getType());
        assertEquals(objectStatus.getStatus(), convertedObjectResult.getStatus());
        assertEquals(objectStatus.getName(), convertedObjectResult.getName());
        assertEquals(objectStatus.getSchema(), convertedObjectResult.getSchema());
        assertEquals(objectStatus.getCount().get(), convertedObjectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), convertedObjectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String expectedSummary = "Schema1.TestObject1[Type1]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }
}
