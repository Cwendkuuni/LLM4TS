import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertNull(objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("name2", new AtomicLong(10), new AtomicLong(20), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testGetSummaryMethod() {
        ObjectResult objectResult = new ObjectResult("schema3", "name3", "type3");
        assertEquals("schema3.name3[type3]", objectResult.getSummary());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithNullSchema() {
        new ObjectResult(null, "name4", "type4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithNullName() {
        new ObjectResult("schema5", null, "type5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithNullType() {
        new ObjectResult("schema6", "name6", null);
    }

}