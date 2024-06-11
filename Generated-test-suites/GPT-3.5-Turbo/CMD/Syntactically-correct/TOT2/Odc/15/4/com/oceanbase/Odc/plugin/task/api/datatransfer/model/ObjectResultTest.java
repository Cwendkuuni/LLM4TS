import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");

        assertEquals("testName", objectResult.getName());
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testType", objectResult.getType());
        assertNotNull(objectResult.getStatus());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testStaticOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(5), new AtomicLong(10), "testType");
        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("testName", objectResult.getName());
        assertEquals("default", objectResult.getSchema());
        assertEquals("testType", objectResult.getType());
        assertEquals(5, objectResult.getCount().get());
        assertEquals(10, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummaryMethod() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");

        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

    @Test
    public void testExportPathsField() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(Collections.singletonList(new URL("http://www.example.com")));

        assertNotNull(objectResult.getExportPaths());
        assertEquals("http://www.example.com", objectResult.getExportPaths().get(0).toString());
    }
}