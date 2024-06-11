
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {

    private static final String SCHEMA = "TestSchema";
    private static final String NAME = "TestName";
    private static final String TYPE = "TestType";
    private static final String STATUS = "TestStatus";

    private ObjectResult objectResult;

    @Before
    public void setUp() throws MalformedURLException {
        objectResult = new ObjectResult(SCHEMA, NAME, TYPE);
        objectResult.setStatus(STATUS);
        objectResult.setExportPaths(Arrays.asList(new URL("http://localhost")));
    }

    @Test
    public void testConstructor() {
        assertEquals(NAME, objectResult.getName());
        assertEquals(TYPE, objectResult.getType());
        assertEquals(SCHEMA, objectResult.getSchema());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus(NAME, new AtomicLong(100), new AtomicLong(200), STATUS);
        objectStatus.setSchema(SCHEMA);
        objectStatus.setType(TYPE);

        ObjectResult result = ObjectResult.of(objectStatus);

        assertEquals(objectStatus.getName(), result.getName());
        assertEquals(objectStatus.getType(), result.getType());
        assertEquals(objectStatus.getSchema(), result.getSchema());
        assertEquals(objectStatus.getCount().get(), result.getCount().get());
        assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
        assertEquals(objectStatus.getStatus(), result.getStatus());
    }

    @Test
    public void testExportPaths() throws MalformedURLException {
        assertEquals(Arrays.asList(new URL("http://localhost")), objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        String expectedSummary = String.format("%s.%s[%s]", SCHEMA, NAME, TYPE);
        assertEquals(expectedSummary, objectResult.getSummary());
    }
}
