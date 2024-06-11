import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

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
        assertNotNull(objectResult);
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName1", new AtomicLong(1), new AtomicLong(1), "testType1");
        ObjectResult result = ObjectResult.of(objectStatus);

        assertNotNull(result);
        assertEquals("testName1", result.getName());
        assertEquals("testType1", result.getType());
        assertEquals(1, result.getCount().get());
        assertEquals(1, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();

        assertNotNull(summary);
        assertEquals("testSchema.testName[testType]", summary);
    }

    @Test
    public void testExportPaths() {
        ArrayList<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://example.com"));

        objectResult.setExportPaths(exportPaths);

        assertNotNull(objectResult.getExportPaths());
        assertEquals(1, objectResult.getExportPaths().size());
        assertEquals("http://example.com", objectResult.getExportPaths().get(0).toString());
    }

}