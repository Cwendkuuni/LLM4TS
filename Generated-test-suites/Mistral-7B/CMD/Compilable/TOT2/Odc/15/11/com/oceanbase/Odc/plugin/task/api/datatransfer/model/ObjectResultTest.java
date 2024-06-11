package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema1");
        objectResult.setName("objectName1");
        objectResult.setType("type1");

        String expectedSummary = "schema1.objectName1[type1]";
        String actualSummary = objectResult.getSummary();

        assertEquals(expectedSummary, actualSummary);
    }
}