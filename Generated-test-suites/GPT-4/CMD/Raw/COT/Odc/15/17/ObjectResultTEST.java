import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Test;
import org.junit.Assert;

public class ObjectResultTest {
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        Assert.assertEquals("schema1", objectResult.getSchema());
        Assert.assertEquals("name1", objectResult.getName());
        Assert.assertEquals("type1", objectResult.getType());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("name2", new AtomicLong(1), new AtomicLong(2), "status2");
        objectStatus.setType("type2");
        objectStatus.setSchema("schema2");

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        Assert.assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        Assert.assertEquals(objectStatus.getName(), objectResult.getName());
        Assert.assertEquals(objectStatus.getType(), objectResult.getType());
        Assert.assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        Assert.assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummaryMethod() {
        ObjectResult objectResult = new ObjectResult("schema3", "name3", "type3");
        Assert.assertEquals("schema3.name3[type3]", objectResult.getSummary());
    }
}