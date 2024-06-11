import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;

public class ObjectResultTest {

    @Test
    public void testDefaultConstructor() {
        ObjectResult objResult = new ObjectResult();
        assertNull(objResult.getSchema());
        assertNull(objResult.getName());
        assertNull(objResult.getType());
        assertNull(objResult.getExportPaths());
        assertNull(objResult.getStatus());
        assertEquals(ObjectStatus.UNINITIALIZED, objResult.getStatus());
        assertEquals(0, objResult.getCount().get());
        assertEquals(0, objResult.getTotal().get());
    }

    @Test
    public void testParameterizedConstructor() {
        ObjectResult objResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema", objResult.getSchema());
        assertEquals("testName", objResult.getName());
        assertEquals("testType", objResult.getType());
        assertNull(objResult.getExportPaths());
        assertNull(objResult.getStatus());
        assertEquals(ObjectStatus.UNINITIALIZED, objResult.getStatus());
        assertEquals(0, objResult.getCount().get());
        assertEquals(0, objResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema.testName[testType]", objResult.getSummary());
    }

    @Test
    public void testOf() {
        ObjectStatus that = new ObjectStatus("testSchema", "testName", ObjectStatus.SUCCEEDED, 1L, 10L);
        ObjectResult objResult = ObjectResult.of(that);
        assertEquals("testSchema", objResult.getSchema());
        assertEquals("testName", objResult.getName());
        assertEquals("SUCCESSFUL", objResult.getType());
        assertNotNull(objResult.getStatus());
        assertEquals(ObjectStatus.SUCCEEDED, objResult.getStatus());
        assertEquals(1, objResult.getCount().get());
        assertEquals(10, objResult.getTotal().get());
    }
}