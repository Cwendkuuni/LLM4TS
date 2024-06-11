import org.junit.Test;
import static org.junit.Assert.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("test", "testObject", "testType");
        assertNotNull(objectResult);
        assertEquals("test", objectResult.getSchema());
        assertEquals("testObject", objectResult.getName());
        assertEquals("testType", objectResult.getType());
    }
    
    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("testObject", new AtomicLong(10), new AtomicLong(20), "testType");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals("testObject", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals(10L, objectResult.getCount().get());
        assertEquals(20L, objectResult.getTotal().get());
    }
    
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testObject", "testType");
        String summary = objectResult.getSummary();
        assertEquals("testSchema.testObject[testType]", summary);
    }
    
    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("https://www.example.com"));
        
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

}