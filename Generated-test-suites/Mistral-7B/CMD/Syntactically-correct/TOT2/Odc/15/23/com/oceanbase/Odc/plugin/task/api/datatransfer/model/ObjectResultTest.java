// Your complete JUnit 4 test file should start with this comment
package com.example.junit4test;

import static org.junit.Assert.*;
import org.junit.Test;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1.table1[type1]", objectResult.getSummary());
    }

    @Test
    public void testConstructorWithNullValues() {
        ObjectResult objectResult = new ObjectResult(null, null, null);
        assertEquals("Error: schema is null", objectResult.getStatus().getMessage());
    }

    @Test
    public void testConstructorWithNullName() {
        ObjectResult objectResult = new ObjectResult("schema1", null, "type1");
        assertEquals("Error: name is null", objectResult.getStatus().getMessage());
    }

    @Test
    public void testConstructorWithNullType() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", null);
        assertEquals("Error: type is null", objectResult.getStatus().getMessage());
    }

    @Test
    public void testSetStatusWithErrorStatus() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        ObjectStatus errorStatus = new ObjectStatus("Error");
        objectResult.setStatus(errorStatus);
        assertEquals("Error", objectResult.getStatus().getName());
    }

    @Test
    public void testGetNameWhenIsNull() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        objectResult.setName(null);
        assertEquals(null, objectResult.getName());
    }

    @Test
    public void testgetTypeWhenIsNull() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        objectResult.setType(null);
        assertEquals(null, objectResult.getType());
    }

    @Test
    public void testEdgeCasesAndExceptionHandling() {
        ObjectResult objectResult = new ObjectResult(null, "table1", "type1");
        assertEquals("Error: schema is null", objectResult.getStatus().getMessage());

        ObjectResult objectResult1 = new ObjectResult("schema1", null, "type1");
        assertEquals("Error: name is null", objectResult1.getStatus().getMessage());

        ObjectResult objectResult2 = new ObjectResult("schema1", "table1", null);
        assertEquals("Error: type is null", objectResult2.getStatus().getMessage());

        ObjectResult objectResult3 = new ObjectResult("schema1", "table1", "type1");
        objectResult3.setStatus(ObjectStatus.ERROR);
        assertEquals("Error", objectResult3.getStatus().getName());

        ObjectResult objectResult4 = new ObjectResult("schema1", "table1", "type1");
        objectResult4.setName(null);
        assertEquals(null, objectResult4.getName());

        ObjectResult objectResult5 = new ObjectResult("schema1", "table1", "type1");
        objectResult5.setType(null);
        assertEquals(null, objectResult5.getType());
    }
}

// Your complete JUnit 4 test file should end with this comment