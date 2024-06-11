import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectStatus objectStatus = mock(ObjectStatus.class);
        when(objectStatus.getType()).thenReturn("Type1");
        when(objectStatus.getStatus()).thenReturn(ObjectStatus.Status.SUCCESS);
        when(objectStatus.getName()).thenReturn("ObjectName");
        when(objectStatus.getSchema()).thenReturn("SchemaName");

        ObjectResult objectResult = new ObjectResult(objectStatus);
        assertEquals("SchemaName", objectResult.getSchema());
        assertEquals("ObjectName", objectResult.getName());
        assertEquals("Type1", objectResult.getType());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("Type1");
        objectStatus.setStatus(ObjectStatus.Status.SUCCESS);
        objectStatus.setName("ObjectName");
        objectStatus.setSchema("SchemaName");
        objectStatus.setCount(new AtomicLong(1));
        objectStatus.setTotal(new AtomicLong(10));

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("SchemaName", objectResult.getSchema());
        assertEquals("ObjectName", objectResult.getName());
        assertEquals("Type1", objectResult.getType());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(10, objectResult.getTotal().get());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        when(objectResult.getSchema()).thenReturn("TestSchema");
        when(objectResult.getName()).thenReturn("TestObject");
        when(objectResult.getType()).thenReturn("TestType");

        String summary = objectResult.getSummary();
        assertEquals("TestSchema.TestObject[TestType]", summary);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryNullSchema() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema(null);
        objectResult.getSummary();
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryNullName() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setName(null);
        objectResult.getSummary();
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryNullType() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setType(null);
        objectResult.getSummary();
    }

    @Test
    public void testJsonIgnoreGetExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testSetExportPathsAndGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        when(objectResult.getSchema()).thenReturn("TestSchema");
        when(objectResult.getName()).thenReturn("TestObject");
        when(objectResult.getType()).thenReturn("TestType");
        List<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);
        String summary = objectResult.getSummary();
        assertNull(summary);
    }

}