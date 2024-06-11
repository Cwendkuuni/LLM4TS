import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
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
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(1), new AtomicLong(2), "type");
        ObjectResult result = ObjectResult.of(objectStatus);
        assertNotNull(result);
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
        assertEquals(1, result.getCount().get());
        assertEquals(2, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String expected = "schema.name[type]";
        assertEquals(expected, objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        objectResult.setExportPaths(null);
        assertNull(objectResult.getExportPaths());
    }

}