import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertNull(result.getExportPaths());
        assertEquals(ObjectStatus.Status.NORMAL, result.getStatus());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus that = new ObjectStatus();
        that.setSchema("schema2");
        that.setName("name2");
        that.setType("type2");
        that.setCount(new AtomicLong(10));
        that.setTotal(new AtomicLong(20));
        ObjectStatus status = new ObjectStatus();
        status.setSchema("schema3");
        status.setName("name3");
        status.setType("type3");

        ObjectResult result = ObjectResult.of(that);
        assertEquals("schema2", result.getSchema());
        assertEquals("name2", result.getName());
        assertEquals("type2", result.getType());
        assertNull(result.getExportPaths());
        assertEquals(ObjectStatus.Status.NORMAL, result.getStatus());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());

        result = ObjectResult.of(status);
        assertEquals("schema3", result.getSchema());
        assertEquals("name3", result.getName());
        assertEquals("type3", result.getType());
        assertNull(result.getExportPaths());
        assertEquals(ObjectStatus.Status.NORMAL, result.getStatus());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", result.getSummary());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructionWithNullSchema() {
        new ObjectResult(null, "name1", "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructionWithNullName() {
        new ObjectResult("schema1", null, "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructionWithNullType() {
        new ObjectResult("schema1", "name1", null);
    }

    @Test
    public void testObjectResultSetStatus() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        result.setStatus(ObjectStatus.Status.ERROR);
        assertEquals(ObjectStatus.Status.ERROR, result.getStatus());
    }

    @Test
    public void testObjectResultGetExportPathsWhenNull() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertNull(result.getExportPaths());
    }

    @Test
    public void testObjectResultGetExportPathsWhenListNotEmpty() {
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/test1"), new URL("http://localhost:8080/test2"));
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        result.setExportPaths(exportPaths);
        assertEquals(exportPaths, result.getExportPaths());
    }

    @Test
    public void testObjectResultGetExportPathsWhenListEmpty() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = Collections.emptyList();
        result.setExportPaths(exportPaths);
        assertEquals(exportPaths, result.getExportPaths());
    }

}