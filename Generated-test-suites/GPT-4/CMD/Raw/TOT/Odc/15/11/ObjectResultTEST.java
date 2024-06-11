import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    ObjectResult objectResult;
    
    @Before
    public void setUp() throws Exception {
        objectResult = new ObjectResult("schema", "name", "type");
    }

    @Test
    public void testObjectResultConstructor() {
        assertNotNull(objectResult);
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("other_name", new AtomicLong(123), new AtomicLong(456), null);
        objectStatus.setSchema("other_schema");
        objectStatus.setType("other_type");

        ObjectResult result = ObjectResult.of(objectStatus);

        assertNotNull(result);
        assertEquals("other_schema", result.getSchema());
        assertEquals("other_name", result.getName());
        assertEquals("other_type", result.getType());
    }

    @Test
    public void testGetSummary() {
        String expectedSummary = "schema.name[type]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }
    
    @Test
    public void testGetExportPaths() { // assuming presence of getter and setter for exportPaths
        List<URL> paths = new ArrayList<>();
        objectResult.setExportPaths(paths);

        assertEquals(paths, objectResult.getExportPaths());
    }

    @Test(expected = NullPointerException.class) // assuming that it handles the NPE
    public void testNullObjectStatus() {
        ObjectResult result = ObjectResult.of(null);
    }
}