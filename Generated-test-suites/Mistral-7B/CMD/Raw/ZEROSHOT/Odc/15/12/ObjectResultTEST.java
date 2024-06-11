package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

import java.util.Arrays;
import java.util.AtomicLong;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "Type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("Type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.INIT, objectResult.getStatus());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("Type1", ObjectStatus.Status.SUCCESS, "table1", "schema1", new AtomicLong(5), new AtomicLong(10));
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("Type1", objectResult.getType());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals(new AtomicLong(5), objectResult.getCount());
        assertEquals(new AtomicLong(10), objectResult.getTotal());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "Type1");
        assertEquals("schema1.table1[Type1]", objectResult.getSummary());
    }

    @Test
    public void testJsonSerialization() throws Exception {
        ObjectStatus objectStatus = new ObjectStatus("Type1", ObjectStatus.Status.SUCCESS, "table1", "schema1", new AtomicLong(5), new AtomicLong(10));
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(objectResult);
        ObjectResult deserializedObjectResult = objectMapper.readValue(jsonString, ObjectResult.class);
        assertEquals(objectResult.getSchema(), deserializedObjectResult.getSchema());
        assertEquals(objectResult.getName(), deserializedObjectResult.getName());
        assertEquals(objectResult.getType(), deserializedObjectResult.getType());
        assertEquals(objectResult.getStatus(), deserializedObjectResult.getStatus());
        assertEquals(objectResult.getCount(), deserializedObjectResult.getCount());
        assertEquals(objectResult.getTotal(), deserializedObjectResult.getTotal());
    }

    @Test
    public void testEqualsAndHashcode() {
        ObjectResult objectResult1 = new ObjectResult("schema1", "table1", "Type1");
        ObjectResult objectResult2 = new ObjectResult("schema1", "table1", "Type1");
        ObjectResult objectResult3 = new ObjectResult("schema1", "table2", "Type1");
        ObjectResult objectResult4 = new ObjectResult("schema2", "table1", "Type1");

        assertEquals(objectResult1, objectResult1);
        assertEquals(objectResult1, objectResult2);
        assertNotEquals(objectResult1, objectResult3);
        assertNotEquals(objectResult1, objectResult4);

        assertEquals(objectResult1.hashCode(), objectResult2.hashCode());
        assertNotEquals(objectResult1.hashCode(), objectResult3.hashCode());
        assertNotEquals(objectResult1.hashCode(), objectResult4.hashCode());
    }

    @Test
    public void testAddExportPath() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "Type1");
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export1.csv"), new URL("http://localhost:8080/export2.csv"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testAddNullExportPath() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "Type1");
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export1.csv"), null);
        try {
            objectResult.setExportPaths(exportPaths);
            fail("ExpectedIllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddEmptyExportPath() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "Type1");
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export1.csv"), new URL(""));
        try {
            objectResult.setExportPaths(exportPaths);
            fail("ExpectedIllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEmptyConstructor() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getName());
        assertNull(objectResult.getType());
        assertNull(objectResult.getStatus());
        assertNull(objectResult.getCount());
        assertNull(objectResult.getTotal());
        assertNull(objectResult.getExportPaths());
    }
}