import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

import org.junit.Test;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getExportPaths());
        assertNull(objectResult.getName());
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getType());
        assertEquals(ObjectStatus.SUCCESS, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultObjectStatusConstructor() {
        ObjectStatus objectStatus = new ObjectStatus();
        ObjectResult objectResult = new ObjectResult(objectStatus);
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setName("testName");
        objectResult.setSchema("testSchema");
        objectResult.setType("testType");
        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

    @Test
    public void testObjectResultWithEmptyStringSchema() {
        ObjectResult objectResult = new ObjectResult("", "", "");
        assertEquals("", objectResult.getSchema());
        assertEquals("", objectResult.getName());
        assertEquals("", objectResult.getType());
    }

    @Test
    public void testObjectResultWithEmptyStringName() {
        ObjectResult objectResult = new ObjectResult("schema", "", "");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("", objectResult.getName());
        assertEquals("", objectResult.getType());
    }

    @Test
    public void testObjectResultWithEmptyStringType() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("", objectResult.getType());
    }

    @Test
    public void testGetSchemaWithNullValue() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getSchema());
    }

    @Test
    public void testGetNameWithNullValue() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getName());
    }

    @Test
    public void testGetTypeWithNullValue() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultObjectStatusConstructorWithNullObjectStatus() {
        ObjectStatus objectStatus = null;
        new ObjectResult(objectStatus);
    }
}