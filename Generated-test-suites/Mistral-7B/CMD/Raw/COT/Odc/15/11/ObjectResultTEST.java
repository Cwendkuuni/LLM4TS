import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

/**
 * Test class for ObjectResult.
 */
public class ObjectResultTest {

    /**
     * Test constructor ObjectResult(String schema, String name, String type).
     */
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");
        assertEquals("test_schema", objectResult.getSchema());
        assertEquals("test_name", objectResult.getName());
        assertEquals("test_type", objectResult.getType());
        assertNull(objectResult.getExportPaths());
    }

    /**
     * Test method of(ObjectStatus that).
     */
    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("test_name", new AtomicLong(1), new AtomicLong(1), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(new AtomicLong(1), objectResult.getCount());
        assertEquals(new AtomicLong(1), objectResult.getTotal());
        assertNull(objectResult.getExportPaths());
    }

    /**
     * Test method getSummary().
     */
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");
        assertEquals("test_schema.test_name[test_type]", objectResult.getSummary());
    }

    /**
     * Test constructor ObjectResult().
     */
    @Test
    public void testObjectResultDefaultConstructor() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getName());
        assertNull(objectResult.getType());
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getExportPaths());
        assertNull(objectResult.getCount());
        assertNull(objectResult.getTotal());
        assertNull(objectResult.getStatus());
    }

    /**
     * Test method of with null ObjectStatus.
     */
    @Test(expected = NullPointerException.class)
    public void testOfWithNullObjectStatus() {
        ObjectResult.of(null);
    }

    /**
     * Test method getSummary with null schema.
     */
    @Test
    public void testGetSummaryWithNullSchema() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getSummary());
    }

    /**
     * Test method getSummary with null name.
     */
    @Test
    public void testGetSummaryWithNullName() {
        ObjectResult objectResult = new ObjectResult("test_schema", null, "test_type");
        assertNull(objectResult.getSummary());
    }

    /**
     * Test method getSummary with null type.
     */
    @Test
    public void testGetSummaryWithNullType() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", null);
        assertNull(objectResult.getSummary());
    }

    /**
     * Test method getSummary with empty schema.
     */
    @Test
    public void testGetSummaryWithEmptySchema() {
        ObjectResult objectResult = new ObjectResult("", "test_name", "test_type");
        assertNull(objectResult.getSummary());
    }

    /**
     * Test method getSummary with empty name.
     */
    @Test
    public void testGetSummaryWithEmptyName() {
        ObjectResult objectResult = new ObjectResult("test_schema", "", "test_type");
        assertNull(objectResult.getSummary());
    }

    /**
     * Test method getSummary with empty type.
     */
    @Test
    public void testGetSummaryWithEmptyType() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "");
        assertNull(objectResult.getSummary());
    }

    /**
     * Test method getExportPaths with null value.
     */
    @Test
    public void testGetExportPathsWithNullValue() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");
        assertNull(objectResult.getExportPaths());
    }

    /**
     * Test method getExportPaths with empty list.
     */
    @Test
    public void testGetExportPathsWithEmptyList() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");
        List<URL> emptyList = new ArrayList<>();
        objectResult.setExportPaths(emptyList);
        assertEquals(emptyList, objectResult.getExportPaths());
    }

    /**
     * Test method getExportPaths with non-empty list.
     */
    @Test
    public void testGetExportPathsWithNonEmptyList() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");
        List<URL> nonEmptyList = Arrays.asList(new URL("test_url1"), new URL("test_url2"));
        objectResult.setExportPaths(nonEmptyList);
        assertEquals(nonEmptyList, objectResult.getExportPaths());
    }
}