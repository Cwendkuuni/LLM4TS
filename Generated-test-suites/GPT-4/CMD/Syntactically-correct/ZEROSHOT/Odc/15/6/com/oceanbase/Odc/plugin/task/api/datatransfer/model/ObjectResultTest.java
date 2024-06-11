
package com.oceanbase.odc.plugin.task.api.datatransfer.model.test;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectResultTest {

    private static final String TEST_SCHEMA = "testSchema";
    private static final String TEST_NAME = "testName";
    private static final String TEST_TYPE = "testType";

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult(TEST_SCHEMA, TEST_NAME, TEST_TYPE);

        assertEquals(TEST_SCHEMA, objectResult.getSchema());
        assertEquals(TEST_NAME, objectResult.getName());
        assertEquals(TEST_TYPE, objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus(TEST_NAME, new AtomicLong(10), new AtomicLong(20), null);
        objectStatus.setType(TEST_TYPE);
        objectStatus.setSchema(TEST_SCHEMA);

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals(TEST_SCHEMA, objectResult.getSchema());
        assertEquals(TEST_NAME, objectResult.getName());
        assertEquals(TEST_TYPE, objectResult.getType());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult(TEST_SCHEMA, TEST_NAME, TEST_TYPE);
        String summary = objectResult.getSummary();
        assertNotNull(summary);
        assertEquals(TEST_SCHEMA + "." + TEST_NAME + "[" + TEST_TYPE + "]", summary);
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> urls = new ArrayList<>();
        objectResult.setExportPaths(urls);
        assertEquals(urls, objectResult.getExportPaths());
    }
}
