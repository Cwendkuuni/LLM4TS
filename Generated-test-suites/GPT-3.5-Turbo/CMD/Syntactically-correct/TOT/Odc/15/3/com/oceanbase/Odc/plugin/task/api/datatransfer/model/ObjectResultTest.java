import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("test_schema", "test_name", "test_type");
    }

    @Test
    public void testObjectResultConstructor() {
        assertEquals("test_schema", objectResult.getSchema());
        assertEquals("test_name", objectResult.getName());
        assertEquals("test_type", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("test_name", new AtomicLong(10), new AtomicLong(20), "test_type");
        ObjectResult result = ObjectResult.of(objectStatus);

        assertEquals("test_name", result.getName());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());
        assertEquals("test_type", result.getType());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals("test_schema.test_name[test_type]", summary);
    }

    @Test
    public void testExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://example.com"));

        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

}