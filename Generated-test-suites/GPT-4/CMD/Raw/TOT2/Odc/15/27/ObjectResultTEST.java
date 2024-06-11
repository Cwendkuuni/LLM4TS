
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicLong;
import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {

    ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("Schema", "Name", "Type");
    }

    @Test
    public void testGetObjectResultFromObjectStatus() {
        // Given
        ObjectStatus objectStatus = new ObjectStatus("Name", new AtomicLong(0), new AtomicLong(0), null);
        objectStatus.setType("Type");
        objectStatus.setSchema("Schema");

        // When
        ObjectResult result = ObjectResult.of(objectStatus);

        // Then
        assertEquals("Schema", result.getSchema());
        assertEquals("Name", result.getName());
        assertEquals("Type", result.getType());
        assertNotNull(result.getCount());
        assertNotNull(result.getTotal());
    }

    @Test
    public void testGetSummary() {
        // Given
        // When
        String summary = objectResult.getSummary();

        // Then
        assertEquals("Schema.Name[Type]", summary);
    }

    @Test(expected = Exception.class)
    public void testGetObjectResultFromNullPointerException() {
        // Given
        ObjectStatus objectStatus = null;

        // When
        ObjectResult result = ObjectResult.of(objectStatus);

        // Then 
        // Exception is expected
    }
}
