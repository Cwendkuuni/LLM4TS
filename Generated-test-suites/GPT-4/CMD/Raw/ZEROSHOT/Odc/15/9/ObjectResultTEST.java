
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schemaTest", "nameTest", "typeTest");
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("nameTest", new AtomicLong(10), new AtomicLong(100), "statusTest");
        objectStatus.setType("typeTest");
        objectStatus.setSchema("schemaTest");

        ObjectResult result = ObjectResult.of(objectStatus);

        assertNotNull(result);
        assertEquals("nameTest", result.getName());
        assertEquals("typeTest", result.getType());
        assertEquals("schemaTest", result.getSchema());
        assertEquals(10, result.getCount().get());
        assertEquals(100, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();

        assertEquals("schemaTest.nameTest[typeTest]", summary);
    }

    @Test
    public void testExportPaths() {
        URL url1;
        URL url2;
        try {
            url1 = new URL("http://example.com");
            url2 = new URL("http://example2.com");
            objectResult.setExportPaths(Arrays.asList(url1, url2));

            assertEquals(Arrays.asList(url1, url2), objectResult.getExportPaths());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        objectResult = null;
    }
}
