package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Before;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.*;

public class ObjectResultTest {
    private ObjectResult objectResult;
    private ObjectStatus objectStatus;
    private static final String SCHEMA = "SCHEMA";
    private static final String NAME = "NAME";
    private static final String TYPE = "TYPE";
    private static final long COUNT = 10L;
    private static final long TOTAL = 20L;

    @Before
    public void setup() {
        objectStatus = new ObjectStatus(NAME, new AtomicLong(COUNT), new AtomicLong(TOTAL), null);
        objectStatus.setType(TYPE);
        objectStatus.setSchema(SCHEMA);
        objectResult = ObjectResult.of(objectStatus);
    }

    @Test
    public void testOf() {
        assertEquals(NAME, objectResult.getName());
        assertEquals(TYPE, objectResult.getType());
        assertEquals(SCHEMA, objectResult.getSchema());
        assertEquals(COUNT, objectResult.getCount().get());
        assertEquals(TOTAL, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals(SCHEMA+"."+NAME+"["+TYPE+"]", summary);
    }

    @Test
    public void testExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://test.com"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testConstructor() {
        objectResult = new ObjectResult(SCHEMA, NAME, TYPE);
        assertEquals(NAME, objectResult.getName());
        assertEquals(TYPE, objectResult.getType());
        assertEquals(SCHEMA, objectResult.getSchema());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }
}
