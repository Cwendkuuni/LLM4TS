import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(1), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("name1", objectResult.getName());
        assertEquals("name1", objectStatus.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals("type1", objectStatus.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    // Edge cases testing

    @Test(expected = NullPointerException.class)
    public void testObjectResultConstructorWithNullSchema() {
        new ObjectResult(null, "name1", "type1");
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultConstructorWithNullName() {
        new ObjectResult("schema1", null, "type1");
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultConstructorWithNullType() {
        new ObjectResult("schema1", "name1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithEmptySchema() {
        new ObjectResult("", "name1", "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithEmptyName() {
        new ObjectResult("schema1", "", "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithEmptyType() {
        new ObjectResult("schema1", "name1", "");
    }

    @Test(expected = NullPointerException.class)
    public void testOfWithNullObjectStatus() {
        ObjectResult.of(null);
    }

    // Merge all test cases above
}