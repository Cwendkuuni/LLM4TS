import org.junit.Assert;
import org.junit.Test;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 @Test
 public void testObjectResultConstruction() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "INT");
 Assert.assertEquals("schema1", objectResult.getSchema());
 Assert.assertEquals("table1", objectResult.getName());
 Assert.assertEquals("INT", objectResult.getType());
 Assert.assertNull(objectResult.getExportPaths());
 Assert.assertEquals(ObjectStatus.Status.UNPROCESSED, objectResult.getStatus());
 Assert.assertEquals(0L, objectResult.getCount().get());
 Assert.assertEquals(0L, objectResult.getTotal().get());
 }

 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = new ObjectStatus("table1", ObjectStatus.Status.COMPLETED, new AtomicLong(100), new AtomicLong(1000));
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 Assert.assertEquals("table1", objectResult.getSchema());
 Assert.assertEquals("table1", objectResult.getName());
 Assert.assertEquals(ObjectStatus.Status.COMPLETED, objectResult.getStatus());
 Assert.assertEquals(new AtomicLong(100), objectResult.getCount());
 Assert.assertEquals(new AtomicLong(1000), objectResult.getTotal());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "INT");
 Assert.assertEquals("schema1.table1[INT]", objectResult.getSummary());
 }
}