package com.oceanbase.odc.plugin.task.api.datatransfer.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectStatus status = mock(ObjectStatus.class);
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        ObjectResult expectedObjectResult = new ObjectResult();
        expectedObjectResult.setType("type1");
        expectedObjectResult.setStatus(status);
        expectedObjectResult.setName("name1");
        expectedObjectResult.setSchema("schema1");
        expectedObjectResult.setCount(new AtomicLong(0));
        expectedObjectResult.setTotal(new AtomicLong(0));

        assertEquals(expectedObjectResult, objectResult);
    }

    @Test
    public void testOfMethod() {
        ObjectStatus status = mock(ObjectStatus.class);
        ObjectResult expectedObjectResult = new ObjectResult();
        expectedObjectResult.setType("type1");
        expectedObjectResult.setStatus(status);
        expectedObjectResult.setName("name1");
        expectedObjectResult.setSchema("schema1");
        expectedObjectResult.setCount(new AtomicLong(10));
        expectedObjectResult.setTotal(new AtomicLong(20));

        ObjectResult objectResult = ObjectResult.of(status);

        assertSame(expectedObjectResult, objectResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithNullSchema() {
        ObjectStatus status = mock(ObjectStatus.class);
        String nullSchema = null;
        ObjectResult objectResult = new ObjectResult(nullSchema, "name1", "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithNullName() {
        ObjectStatus status = mock(ObjectStatus.class);
        String nullName = null;
        ObjectResult objectResult = new ObjectResult("schema1", nullName, "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithNullType() {
        ObjectStatus status = mock(ObjectStatus.class);
        String nullType = null;
        ObjectResult objectResult = new ObjectResult("schema1", "name1", nullType);
    }

    @Test
    public void testGetSummaryMethod() {
        ObjectResult objectResult = new ObjectResult();
        String expectedSummary = "schema1.name1[type1]";
        when(objectResult.getSchema()).thenReturn("schema1");
        when(objectResult.getName()).thenReturn("name1");
        when(objectResult.getType()).thenReturn("type1");

        String actualSummary = objectResult.getSummary();

        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void testGetExportPathsMethod() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        when(objectResult.getExportPaths()).thenReturn(exportPaths);

        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testSetExportPathsMethod() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();

        objectResult.setExportPaths(exportPaths);

        assertEquals(exportPaths, objectResult.getExportPaths());
    }

}