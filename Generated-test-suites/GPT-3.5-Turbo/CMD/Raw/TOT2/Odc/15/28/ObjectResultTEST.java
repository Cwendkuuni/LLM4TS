import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

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
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testStaticOfMethod() {
        ObjectStatus status = new ObjectStatus("test_name", new AtomicLong(10), new AtomicLong(20), "test_type");
        ObjectResult result = ObjectResult.of(status);

        assertEquals("test_name", result.getName());
        assertEquals("test_type", result.getType());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals("test_schema.test_name[test_type]", summary);
    }

    // Edge Cases and Exceptions Testing
    @Test
    public void testObjectResultEmptyConstructor() {
        ObjectResult emptyObjectResult = new ObjectResult();
        assertNotNull(emptyObjectResult.getSchema());
        assertNotNull(emptyObjectResult.getName());
        assertNotNull(emptyObjectResult.getType());
        assertNotNull(emptyObjectResult.getCount());
        assertNotNull(emptyObjectResult.getTotal());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultNullSchema() {
        ObjectResult nullSchemaObjectResult = new ObjectResult(null, "test_name", "test_type");
    }

    @Test
    public void testGetSummaryNullSchema() {
        objectResult.setSchema(null);
        String summary = objectResult.getSummary();
        assertEquals("null.test_name[test_type]", summary);
    }

    @Test
    public void testExportPaths() {
        objectResult.setExportPaths(new ArrayList<>());
        assertEquals(0, objectResult.getExportPaths().size());
    }

}