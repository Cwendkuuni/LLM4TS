
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Before;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.*;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() throws Exception {
        objectResult = new ObjectResult("TEST", "ObjectTest", "TypeTest");
    }

    @Test
    public void testSchemaNotNull() throws Exception {
        assertNotNull(objectResult.getSchema());
    }

    @Test
    public void testNameNotNull() throws Exception {
        assertNotNull(objectResult.getName());
    }

    @Test
    public void testTypeNotNull() throws Exception {
        assertNotNull(objectResult.getType());
    }

    @Test
    public void testObjectResultOf() throws Exception {
        ObjectStatus status = new ObjectStatus();
        status.setName("StatusNameTest");
        status.setSchema("StatusSchemaTest");
        status.setType("StatusTypeTest");
        status.setCount(new AtomicLong(100));
        status.setTotal(new AtomicLong(200));

        ObjectResult result = ObjectResult.of(status);
        assertEquals("StatusNameTest", result.getName());
        assertEquals("StatusSchemaTest", result.getSchema());
        assertEquals("StatusTypeTest", result.getType());
        assertEquals(100, result.getCount().get());
        assertEquals(200, result.getTotal().get());
    }

    @Test
    public void testSummaryNotNull() {
        assertNotNull(objectResult.getSummary());
    }
    
    @Test
    public void testExportPathsEmpty(){
        List<URL> urlList = new ArrayList<>();
        objectResult.setExportPaths(urlList);
        assertTrue(objectResult.getExportPaths().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionThrownWhenExportPathsIsNull() {
        objectResult.setExportPaths(null);
    }
}
