import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setup() {
        objectResult = new ObjectResult("schema", "name", "type");
    }

    @Test
    public void testObjectResultConstructor() {
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = mock(ObjectStatus.class);
        ObjectResult result = ObjectResult.of(objectStatus);
        assertNotNull(result);
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertNotNull(summary);
    }

    @Test
    public void testExportPaths() {
        ArrayList<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultSettersGetters() {
        objectResult.setExportPaths(new ArrayList<>());
        assertEquals(new ArrayList<>(), objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultToString() {
        assertNotNull(objectResult.toString());
    }

}