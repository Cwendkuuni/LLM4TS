package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testEmptySchema() {
        ObjectResult objectResult = new ObjectResult(null, "table1", "type1");
        assertNull(objectResult.getSummary());
    }

    @Test
    public void testEmptyName() {
        ObjectResult objectResult = new ObjectResult("schema1", null, "type1");
        assertNull(objectResult.getSummary());
    }

    @Test
    public void testEmptyType() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", null);
        assertNull(objectResult.getSummary());
    }

    @Test(expected = NullPointerException.class)
    public void testNullExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        objectResult.setExportPaths(null);
        objectResult.getSummary();
    }

    @Test
    public void testLargeExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        List<URL> largeExportPaths = Arrays.asList(new URL("http://localhost:8080/exportPath1"), new URL("http://localhost:8080/exportPath2"), new URL("http://localhost:8080/exportPath3"), new URL("http://localhost:8080/exportPath4"), new URL("http://localhost:8080/exportPath5"), new URL("http://localhost:8080/exportPath6"), new URL("http://localhost:8080/exportPath7"), new URL("http://localhost:8080/exportPath8"), new URL("http://localhost:8080/exportPath9"), new URL("http://localhost:8080/exportPath10"), new URL("http://localhost:8080/exportPath11"), new URL("http://localhost:8080/exportPath12"), new URL("http://localhost:8080/exportPath13"), new URL("http://localhost:8080/exportPath14"), new URL("http://localhost:8080/exportPath15"), new URL("http://localhost:8080/exportPath16"), new URL("http://localhost:8080/exportPath17"), new URL("http://localhost:8080/exportPath18"), new URL("http://localhost:8080/exportPath19"), new URL("http://localhost:8080/exportPath20"));
        objectResult.setExportPaths(largeExportPaths);
        assertEquals("schema1.table1[type1]", objectResult.getSummary());
    }

    @Test
    public void testEmptyGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", null, "type1");
        assertNull(objectResult.getSummary());
    }

    @Test
    public void testNullGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        objectResult.setSummary(null);
        assertNull(objectResult.getSummary());
    }

    @Test
    public void testGetSummaryWithNullExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        objectResult.setExportPaths(null);
        assertNull(objectResult.getSummary());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1.table1[type1]", objectResult.getSummary());
    }
}