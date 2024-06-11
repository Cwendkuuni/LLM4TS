import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setup() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";
        objectResult = new ObjectResult(schema, name, type);
    }

    @Test
    public void testObjectResultConstructor() {
        // Test constructor with valid arguments
        ObjectResult expectedObjectResult = new ObjectResult(schema, name, type);
        assertEquals(expectedObjectResult.getSchema(), objectResult.getSchema());
        assertEquals(expectedObjectResult.getName(), objectResult.getName());
        assertEquals(expectedObjectResult.getType(), objectResult.getType());
        assertEquals(expectedObjectResult.getStatus(), objectResult.getStatus());
        assertEquals(expectedObjectResult.getCount().get(), objectResult.getCount().get());
        assertEquals(expectedObjectResult.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("testName");
        objectStatus.setType("testType");
        objectStatus.setStatus(Status.SUCCESS);
        objectStatus.setSchema("testSchema");
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(20));

        ObjectResult expectedObjectResult = ObjectResult.of(objectStatus);
        ObjectResult actualObjectResult = new ObjectResult();
        actualObjectResult.setType(objectStatus.getType());
        actualObjectResult.setStatus(objectStatus.getStatus());
        actualObjectResult.setName(objectStatus.getName());
        actualObjectResult.setSchema(objectStatus.getSchema());
        actualObjectResult.setCount(new AtomicLong(objectStatus.getCount().get()));
        actualObjectResult.setTotal(new AtomicLong(objectStatus.getTotal().get()));

        assertEquals(expectedObjectResult.getSchema(), actualObjectResult.getSchema());
        assertEquals(expectedObjectResult.getName(), actualObjectResult.getName());
        assertEquals(expectedObjectResult.getType(), actualObjectResult.getType());
        assertEquals(expectedObjectResult.getStatus(), actualObjectResult.getStatus());
        assertEquals(expectedObjectResult.getCount().get(), actualObjectResult.getCount().get());
        assertEquals(expectedObjectResult.getTotal().get(), actualObjectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String expectedSummary = "testSchema.testName[testType]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testJsonIgnore() {
        assertNull(objectResult.getExportPaths());
    }
}