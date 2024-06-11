
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;


public class ObjectResultTest {

    private ObjectResult objectResult;
    private ObjectStatus objectStatus;

    @Before
    public void setUp() throws Exception {
        objectResult = new ObjectResult("testSchema", "testName", "testType");
        objectStatus = new ObjectStatus("statusName", new AtomicLong(10), new AtomicLong(20), "testStatus");
    }

    @Test
    public void testConstructor() {
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getSchema(), result.getSchema());
        assertEquals(objectStatus.getName(), result.getName());
        assertEquals(objectStatus.getType(), result.getType());
        assertEquals(objectStatus.getCount().get(), result.getCount().get());
        assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals("testSchema.testName[testType]", summary);
    }

    @Test
    public void testSetAndGetExportPaths() {
        ArrayList<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);
        assertTrue(objectResult.getExportPaths() == exportPaths);
    }
}
