package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ObjectResultTest {
    private ObjectResult objectResult;
    private String schema;
    private String name;
    private String type;
    private ObjectStatus objectStatus;
    
    @Before
    public void setUp() {
        schema = "testSchema";
        name = "testName";
        type = "testType";
        objectStatus = new ObjectStatus(name, new AtomicLong(1), new AtomicLong(2), null);
        objectResult = new ObjectResult(schema, name, type);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
    }
    
    @Test
    public void testOfMethod() {
        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals(name, result.getName());
        assertEquals(1, result.getCount().get());
        assertEquals(2, result.getTotal().get());
    }
    
    @Test
    public void testGetSummary() {
        String expectedSummary = String.format("%s.%s[%s]", schema, name, type);
        assertEquals(expectedSummary, objectResult.getSummary());
    }
    
    @Test
    public void testExportPaths() {
        ArrayList<URL> testUrls = new ArrayList<>();
        try {
            testUrls.add(new URL("http://www.example.com"));
        } catch(Exception e) {
            System.out.println("Error creating URL");
        }
        objectResult.setExportPaths(testUrls);
        assertEquals(testUrls, objectResult.getExportPaths());
    }
}