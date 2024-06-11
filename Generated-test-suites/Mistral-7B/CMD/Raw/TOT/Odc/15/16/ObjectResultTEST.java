package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@SuppressWarnings("unchecked")
public class ObjectResultTest {

    private ObjectStatus objectStatusMock;
    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectStatusMock = mock(ObjectStatus.class);
        objectResult = new ObjectResult();
    }

    @Test
    public void testObjectResultConstruction() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertEquals(ObjectStatus.Status.SUCCESS, result.getStatus());
        assertNull(result.getExportPaths());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    @Test
    public void testStaticFactory() {
        ObjectStatus objectStatus = new ObjectStatus();
        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getType(), result.getType());
        assertEquals(objectStatus.getStatus(), result.getStatus());
        assertEquals(objectStatus.getName(), result.getName());
        assertEquals(objectStatus.getSchema(), result.getSchema());
        assertEquals(objectStatus.getCount().get(), result.getCount().get());
        assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String expectedSummary = "%s.%s[%s]";
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema1");
        objectResult.setName("name1");
        objectResult.setType("type1");
        String actualSummary = objectResult.getSummary();
        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void testConstructor_WithObjectStatus() {
        String schema = "schema1";
        String name = "name1";
        String type = "type1";
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType(type);
        objectStatus.setStatus(ObjectStatus.Status.SUCCESS);
        objectStatus.setName(name);
        objectStatus.setSchema(schema);
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(20));

        ObjectResult result = new ObjectResult(objectStatus);
        assertEquals(schema, result.getSchema());
        assertEquals(name, result.getName());
        assertEquals(type, result.getType());
        assertEquals(ObjectStatus.Status.SUCCESS, result.getStatus());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());
        assertNull(result.getExportPaths());
    }

    @Test
    public void testSetExportPaths_withNull() {
        ObjectResult result = new ObjectResult();
        result.setExportPaths(null);
        assertNull(result.getExportPaths());
    }

    @Test
    public void testSetExportPaths_withEmptyList() {
        ObjectResult result = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        result.setExportPaths(exportPaths);
        assertSame(exportPaths, result.getExportPaths());
        assertEquals(0, result.getExportPaths().size());
    }

    @Test
    public void testSetExportPaths_withList() {
        ObjectResult result = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("path1"));
        exportPaths.add(new URL("path2"));
        result.setExportPaths(exportPaths);
        assertSame(exportPaths, result.getExportPaths());
        assertEquals(2, result.getExportPaths().size());
    }
}