import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

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
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName1", new AtomicLong(10), new AtomicLong(20), "testType1");
        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals("testType1", result.getType());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());
        assertEquals("testName1", result.getName());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals("testSchema.testName[testType]", summary);
    }

    @Test
    public void testExportPaths() {
        ArrayList<URL> exportList = new ArrayList<>();
        exportList.add(new URL("http://example.com"));
        objectResult.setExportPaths(exportList);
        assertNotNull(objectResult.getExportPaths());
        assertEquals(1, objectResult.getExportPaths().size());
    }
}