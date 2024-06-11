package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(2), null);
        objectStatus.setSchema("schema1");
        objectStatus.setType("type1");

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(2, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setCount(new AtomicLong(1));
        objectResult.setTotal(new AtomicLong(2));
        String expected = "schema1.name1[type1]";
        String actual = objectResult.getSummary();
        assertEquals(expected, actual);
    }
	
	@Test
	public void testExportPaths() {
		URL url1 = new URL("http://example.com");
		URL url2 = new URL("http://example2.com");
		ObjectResult objectResult = new ObjectResult();
		objectResult.setExportPaths(Arrays.asList(url1, url2));

		assertTrue(objectResult.getExportPaths().contains(url1));
		assertTrue(objectResult.getExportPaths().contains(url2));
		assertEquals(2, objectResult.getExportPaths().size());
	}
}
