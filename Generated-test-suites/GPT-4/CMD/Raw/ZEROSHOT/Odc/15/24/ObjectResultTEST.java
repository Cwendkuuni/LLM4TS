
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schema", "name", "type");
    }

    @Test
    public void testConstructor() {
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testStaticMethodOf() {
        ObjectResult newObject = ObjectResult.of(objectResult);
        assertEquals(newObject.getSchema(), objectResult.getSchema());
        assertEquals(newObject.getName(), objectResult.getName());
        assertEquals(newObject.getType(), objectResult.getType());
        assertEquals(newObject.getCount().get(), objectResult.getCount().get());
        assertEquals(newObject.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String expectedSummary = "schema.name[type]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        ArrayList<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);

        assertTrue(objectResult.getExportPaths().isEmpty());
    }
}
