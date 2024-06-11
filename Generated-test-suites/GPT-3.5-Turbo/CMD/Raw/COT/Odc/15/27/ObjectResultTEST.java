
import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(1), new AtomicLong(2), "testType");

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("testName", objectResult.getName());
        assertEquals(new AtomicLong(1), objectResult.getCount());
        assertEquals(new AtomicLong(2), objectResult.getTotal());
        assertEquals("testType", objectResult.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("testSchema");
        objectResult.setName("testName");
        objectResult.setType("testType");

        String expectedResult = "testSchema.testName[testType]";
        assertEquals(expectedResult, objectResult.getSummary());
    }
}
