package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

@SuppressWarnings({ "unchecked", "static-access" })
public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertEquals(Status.SUCCESS, result.getStatus());
        assertNull(result.getExportPaths());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus("name2", new AtomicLong(10), new AtomicLong(20), null);
        status.setType("type2");
        status.setStatus(Status.FAILED);
        ObjectResult result = ObjectResult.of(status);
        assertEquals("name2", result.getName());
        assertEquals("type2", result.getType());
        assertEquals(Status.FAILED, result.getStatus());
        assertNull(result.getExportPaths());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("schema3", "name3", "type3");
        assertEquals("schema3.name3[type3]", result.getSummary());
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult result = new ObjectResult("schema4", "name4", "type4");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://localhost:8080/export1"));
        exportPaths.add(new URL("http://localhost:8080/export2"));
        result.setExportPaths(exportPaths);
        assertEquals(exportPaths, result.getExportPaths());
    }

    @Test
    public void testJsonSerializationAndDeserialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectResult result = new ObjectResult("schema5", "name5", "type5");
        String jsonString = objectMapper.writeValueAsString(result);
        ObjectResult deserializedResult = objectMapper.readValue(jsonString, ObjectResult.class);
        assertEquals(result.getSchema(), deserializedResult.getSchema());
        assertEquals(result.getName(), deserializedResult.getName());
        assertEquals(result.getType(), deserializedResult.getType());
        assertEquals(result.getStatus(), deserializedResult.getStatus());
        assertEquals(result.getCount(), deserializedResult.getCount());
        assertEquals(result.getTotal(), deserializedResult.getTotal());
        assertNull(result.getExportPaths());
        assertNull(deserializedResult.getExportPaths());
    }

    @Test
    public void testIsSuccess() {
        ObjectResult result1 = new ObjectResult("schema6", "name6", "type6");
        assertTrue(result1.isSuccess());
        ObjectResult result2 = new ObjectResult("schema7", "name7", "type7");
        result2.setStatus(Status.FAILED);
        assertFalse(result2.isSuccess());
    }
}