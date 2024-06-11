package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {
    ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schemaA", "nameA", "typeA");
    }

    // Expert 1 proposes a test case for the constructor.
    @Test
    public void testConstructor() {
        assertNotNull(objectResult);
        assertEquals("schemaA", objectResult.getSchema());
        assertEquals("nameA", objectResult.getName());
        assertEquals("typeA", objectResult.getType());
    }

    // Expert 2 proposes a test case for the static "of" method.
    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setSchema("schemaB");
        objectStatus.setName("nameB");
        objectStatus.setType("typeB");
        objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getType(), objectResult.getType());
    }

    // Expert 3 proposes a test case for the "getSummary" method.
    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertNotNull(summary);
        assertEquals("schemaA.nameA[typeA]", summary);
    }
}
