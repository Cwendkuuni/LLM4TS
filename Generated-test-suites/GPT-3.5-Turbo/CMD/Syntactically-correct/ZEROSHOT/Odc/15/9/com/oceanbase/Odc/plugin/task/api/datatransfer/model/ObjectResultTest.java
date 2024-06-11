import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("testSchema", "testName", "testType");
    }

    @Test
    public void testObjectResultConstructor() {
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("testName2", new AtomicLong(1), new AtomicLong(2), "testType2");
        ObjectResult result = ObjectResult.of(objectStatus);

        assertEquals("testType2", result.getType());
        assertEquals("testName2", result.getName());
        assertNotNull(result.getCount());
        assertNotNull(result.getTotal());
        assertNull(result.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals("testSchema.testName[testType]", summary);
    }

    @Test
    public void testSetAndGetExportPaths() {
        List<URL> urls = new ArrayList<>();
        URL testUrl = new URL("http://www.test.com");
        urls.add(testUrl);

        objectResult.setExportPaths(urls);

        List<URL> exportedUrls = objectResult.getExportPaths();
        assertEquals(1, exportedUrls.size());
        assertEquals(testUrl, exportedUrls.get(0));
    }

}