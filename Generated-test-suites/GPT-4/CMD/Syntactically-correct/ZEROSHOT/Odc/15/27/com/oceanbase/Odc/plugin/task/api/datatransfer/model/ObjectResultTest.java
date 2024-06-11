package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class ObjectResultTest {
    
    private ObjectResult objectResult;

    @Before
    public void setup() {
        objectResult = new ObjectResult("Test_Schema", "Test_Name", "Type_String");
    }

    @Test
    public void testConstructor() {
        assertNotNull(objectResult);
        assertEquals("Test_Schema", objectResult.getSchema());
        assertEquals("Test_Name", objectResult.getName());
        assertEquals("Type_String", objectResult.getType());
    }

    @Test
    public void testOfMethod() {
        ObjectResult result = ObjectResult.of(objectResult);
        assertNotNull(result);
        assertEquals(objectResult.getSchema(), result.getSchema());
        assertEquals(objectResult.getName(), result.getName());
        assertEquals(objectResult.getType(), result.getType());
        assertEquals(objectResult.getCount().get(), result.getCount().get());
        assertEquals(objectResult.getTotal().get(), result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String expectedSummary = objectResult.getSchema() + "." + objectResult.getName() + "[" + objectResult.getType() + "]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testSetAndGetExportPaths() {
        try {
            URL url1 = new URL("http://example.com");
            URL url2 = new URL("http://test.com");
            objectResult.setExportPaths(Arrays.asList(url1, url2));
            assertTrue(objectResult.getExportPaths().contains(url1));
            assertTrue(objectResult.getExportPaths().contains(url2));

        } catch (MalformedURLException e) {
            log.error("Malformed URL Exception : ", e);
        }
    }
}
