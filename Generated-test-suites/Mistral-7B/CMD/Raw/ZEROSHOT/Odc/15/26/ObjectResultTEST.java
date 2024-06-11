import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 @Test
 public void testObjectResultConstruction() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "TYPE1");
 Assertions.assertEquals("schema1", objectResult.getSchema());
 Assertions.assertEquals("table1", objectResult.getName());
 Assertions.assertEquals("TYPE1", objectResult.getType());
 Assertions.assertNull(objectResult.getExportPaths());
 Assertions.assertEquals(ObjectStatus.STATUS_OK, objectResult.getStatus());
 Assertions.assertEquals(0, objectResult.getCount().get());
 Assertions.assertEquals(0, objectResult.getTotal().get());
 }

 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = new ObjectStatus("NAME1", "SCHEMA1", ObjectStatus.STATUS_OK, new AtomicLong(10), new AtomicLong(20));
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 Assertions.assertEquals("SCHEMA1", objectResult.getSchema());
 Assertions.assertEquals("NAME1", objectResult.getName());
 Assertions.assertEquals(ObjectStatus.STATUS_OK, objectResult.getStatus());
 Assertions.assertEquals(new AtomicLong(10), objectResult.getCount());
 Assertions.assertEquals(new AtomicLong(20), objectResult.getTotal());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "TYPE1");
 Assertions.assertEquals("schema1.table1[TYPE1]", objectResult.getSummary());
 }
}