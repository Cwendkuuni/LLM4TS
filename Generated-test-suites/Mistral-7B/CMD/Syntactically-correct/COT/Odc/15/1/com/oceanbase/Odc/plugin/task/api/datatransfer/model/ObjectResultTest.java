import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "int");
        assertNotNull(objectResult);
        assertEquals("schema1.table1[int]", objectResult.getSummary());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("table1", new AtomicLong(1), new AtomicLong(1), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "int");
        assertEquals("schema1.table1[int]", objectResult.getSummary());
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryNullSchema() {
        ObjectResult objectResult = new ObjectResult(null, "table1", "int");
        assertNull(objectResult.getSummary());
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryNullName() {
        ObjectResult objectResult = new ObjectResult("schema1", null, "int");
        assertNull(objectResult.getSummary());
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryNullType() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", null);
        assertNull(objectResult.getSummary());
    }

    @Test
    public void testJsonIgnoreMethod() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "int");
        List<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);
        assertNotNull(objectResult.getExportPaths());
        assertNull(objectResult.getExportPaths().size());
    }

}