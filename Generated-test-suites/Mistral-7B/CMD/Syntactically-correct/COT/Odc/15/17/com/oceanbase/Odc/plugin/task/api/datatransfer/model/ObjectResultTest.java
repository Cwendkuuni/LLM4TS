import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(1), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertSame(objectStatus.getType(), objectResult.getType());
        assertSame(objectStatus.getStatus(), objectResult.getStatus());
        assertSame(objectStatus.getName(), objectResult.getName());
        assertSame(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryNullName() {
        ObjectResult objectResult = new ObjectResult("schema1", null, "type1");
        objectResult.setName(null);
        assertNull(objectResult.getSummary());
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryNullType() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", null);
        objectResult.setType(null);
        assertNull(objectResult.getSummary());
    }

    @Test
    public void testJsonIgnoreGetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertNull(objectResult.getExportPaths());
    }
}