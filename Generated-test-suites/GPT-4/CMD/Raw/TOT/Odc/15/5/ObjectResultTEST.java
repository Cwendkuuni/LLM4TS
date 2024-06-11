
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.atomic.AtomicLong;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class ObjectResultTest {
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("Schema", "Name", "Type");
        assertEquals("Schema", objectResult.getSchema());
        assertEquals("Name", objectResult.getName());
        assertEquals("Type", objectResult.getType());
    }

    @Test
    public void testOf() {
        AtomicLong count = new AtomicLong(5);
        AtomicLong total = new AtomicLong(10);
        ObjectStatus objectStatus = new ObjectStatus("Test status", count, total, null);

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals(objectResult.getCount().get(), objectStatus.getCount().get());
        assertEquals(objectResult.getTotal().get(), objectStatus.getTotal().get());
        assertEquals(objectResult.getName(), objectStatus.getName());
        assertEquals(objectResult.getSchema(), objectStatus.getSchema());
        assertEquals(objectResult.getStatus(), objectStatus.getStatus());
        assertEquals(objectResult.getType(), objectStatus.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("Schema", "Name", "Type");
        String expectedSummary = "Schema.Name[Type]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult("Schema", "Name", "Type");
        List<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
