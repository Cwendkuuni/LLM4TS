import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.STATUS_NORMAL, objectResult.getStatus());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(1), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertSame(objectStatus.getType(), objectResult.getType());
        assertSame(objectStatus.getStatus(), objectResult.getStatus());
        assertSame(objectStatus.getName(), objectResult.getName());
        assertSame(objectStatus.getSchema(), objectResult.getSchema());
        assertSame(objectStatus.getCount().get(), objectResult.getCount().get());
        assertSame(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test(expected = IllegalStateException.class)
    public void testObjectResultWithNullSchema() {
        new ObjectResult(null, "name1", "type1");
    }

    @Test(expected = IllegalStateException.class)
    public void testObjectResultWithNullName() {
        new ObjectResult("schema1", null, "type1");
    }

    @Test(expected = IllegalStateException.class)
    public void testObjectResultWithNullType() {
        new ObjectResult("schema1", "name1", null);
    }

    @Test
    public void testSetTypeWithNullType() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertSame("type1", objectResult.getType());
        objectResult.setType(null);
        assertNull(objectResult.getType());
    }

    @Test
    public void testSetStatusWithNullStatus() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals(ObjectStatus.STATUS_NORMAL, objectResult.getStatus());
        ObjectStatus status = null;
        objectResult.setStatus(status);
        assertSame(ObjectStatus.STATUS_NORMAL, objectResult.getStatus());
    }

    @Test
    public void testSetNameWithEmptyString() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("name1", objectResult.getName());
        objectResult.setName("");
        assertSame("", objectResult.getName());
    }

    @Test
    public void testSetNameWithNullName() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("name1", objectResult.getName());
        objectResult.setName(null);
        assertNull(objectResult.getName());
    }

    @Test
    public void testSetSchemaWithEmptyString() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        objectResult.setSchema("");
        assertSame("", objectResult.getSchema());
    }

    @Test
    public void testSetSchemaWithNullSchema() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        objectResult.setSchema(null);
        assertNull(objectResult.getSchema());
    }

    @Test
    public void testObjectResultWithEmptyExportPathsList() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertNull(objectResult.getExportPaths());
        objectResult.setExportPaths(Collections.emptyList());
        assertSame(Collections.emptyList(), objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultWithNullExportPathsList() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertNull(objectResult.getExportPaths());
        List<URL> exportPaths = null;
        objectResult.setExportPaths(exportPaths);
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultWithEmptyStringExportPath() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertNull(objectResult.getExportPaths());
        List<URL> exportPaths = Arrays.asList(new URL(""));
        objectResult.setExportPaths(exportPaths);
        assertSame(Collections.singletonList(new URL("")), objectResult.getExportPaths());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultWithNullExportPath() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertNull(objectResult.getExportPaths());
        List<URL> exportPaths = Arrays.asList(null);
        objectResult.setExportPaths(exportPaths);
    }
}