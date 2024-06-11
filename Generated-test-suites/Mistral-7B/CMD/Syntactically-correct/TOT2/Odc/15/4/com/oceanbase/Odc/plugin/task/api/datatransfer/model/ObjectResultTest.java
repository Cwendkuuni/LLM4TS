import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResult() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultWithObjectStatus() {
        ObjectStatus objectStatus = new ObjectStatus();
        ObjectResult objectResult = new ObjectResult(objectStatus);
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummaryNullObjectResult() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getSummary());
    }

    @Test
    public void testGetSummaryEmptyObjectResult() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("testSchema");
        objectResult.setName("testName");
        objectResult.setType("testType");
        String result = objectResult.getSummary();
        assertEquals("testSchema.testName[testType]", result);
    }

    @Test
    public void testGetSummaryInvalidCharacters() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("testSchema");
        objectResult.setName("testName%");
        objectResult.setType("testType");
        String result = objectResult.getSummary();
        assertEquals("testSchema.testName[testType]", result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetExportPathsNullObjectResult() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(null);
        objectResult.getExportPaths();
    }

    @Test
    public void testGetExportPathsEmptyObjectResult() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> emptyList = null;
        objectResult.setExportPaths(emptyList);
        assertNotNull(objectResult.getExportPaths());
        assertTrue(objectResult.getExportPaths().isEmpty());
    }
}

//