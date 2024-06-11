import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";

        ObjectResult objectResult = new ObjectResult(schema, name, type);

        Assert.assertEquals(schema, objectResult.getSchema());
        Assert.assertEquals(name, objectResult.getName());
        Assert.assertEquals(type, objectResult.getType());
        Assert.assertNull(objectResult.getExportPaths());
        Assert.assertEquals(ObjectStatus.SUCCESS, objectResult.getStatus());
        Assert.assertEquals(0, objectResult.getCount().get());
        Assert.assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() throws Exception {
        String schema = "testSchema";
        String name = "testName";
        ObjectStatus objectStatus = new ObjectStatus(name, new AtomicLong(10), new AtomicLong(20), null);
        objectStatus.setType("testType");
        objectStatus.setStatus(ObjectStatus.SUCCESS);

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        Assert.assertEquals(schema, objectResult.getSchema());
        Assert.assertEquals(name, objectResult.getName());
        Assert.assertEquals("testType", objectResult.getType());
        Assert.assertNull(objectResult.getExportPaths());
        Assert.assertEquals(ObjectStatus.SUCCESS, objectResult.getStatus());
        Assert.assertEquals(10, objectResult.getCount().get());
        Assert.assertEquals(20, objectResult.getTotal().get());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectStatus that = objectMapper.readValue(objectMapper.writeValueAsString(objectResult), ObjectStatus.class);
        Assert.assertEquals(objectStatus, that);
    }

    @Test
    public void testGetSummary() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";

        ObjectResult objectResult = new ObjectResult(schema, name, type);

        String expectedSummary = String.format("%s.%s[%s]", schema, name, type);
        String actualSummary = objectResult.getSummary();

        Assert.assertEquals(expectedSummary, actualSummary);
    }
}