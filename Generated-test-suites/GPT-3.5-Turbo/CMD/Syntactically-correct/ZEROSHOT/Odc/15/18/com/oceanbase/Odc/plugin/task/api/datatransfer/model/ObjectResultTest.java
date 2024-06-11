import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertNotNull(objectResult.getExportPaths());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(0), new AtomicLong(0), null);
        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getType(), result.getType());
        assertEquals(objectStatus.getStatus(), result.getStatus());
        assertEquals(objectStatus.getName(), result.getName());
        assertEquals(objectStatus.getSchema(), result.getSchema());
        assertEquals(objectStatus.getCount().get(), result.getCount().get());
        assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals("testSchema.testName[testType]", summary);
    }

    @Test
    public void testExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        URL testURL = getClass().getResource("test.txt");
        exportPaths.add(testURL);

        objectResult.setExportPaths(exportPaths);
        assertEquals(1, objectResult.getExportPaths().size());
        assertEquals(testURL, objectResult.getExportPaths().get(0));
    }

}