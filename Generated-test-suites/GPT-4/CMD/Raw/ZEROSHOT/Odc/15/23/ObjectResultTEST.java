
import static org.junit.Assert.*;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testObjectResultWithParams() {
        ObjectResult result = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema", result.getSchema());
        assertEquals("testName", result.getName());
        assertEquals("testType", result.getType());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("testType");
        objectStatus.setStatus("testStatus");
        objectStatus.setName("testName");
        objectStatus.setSchema("testSchema");
        objectStatus.setCount(new AtomicLong(5));
        objectStatus.setTotal(new AtomicLong(10));

        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals("testSchema", result.getSchema());
        assertEquals("testName", result.getName());
        assertEquals("testType", result.getType());
        assertEquals(5, result.getCount().get());
        assertEquals(10, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("testSchema", "testName", "testType");
        String summary = result.getSummary();
        assertEquals("testSchema.testName[testType]", summary);
    }

    @Test
    public void testExportPaths() {
        ObjectResult result = new ObjectResult();
        ArrayList<URL> urls = new ArrayList<>();
        result.setExportPaths(urls); 
        assertEquals(urls, result.getExportPaths());
    }
}
