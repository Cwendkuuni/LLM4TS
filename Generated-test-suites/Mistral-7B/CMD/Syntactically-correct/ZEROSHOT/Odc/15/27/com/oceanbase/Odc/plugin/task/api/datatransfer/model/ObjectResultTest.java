import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

 @Test
 public void testObjectResultConstruction() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 assertNotNull(objectResult);
 assertEquals("schema1", objectResult.getSchema());
 assertEquals("table1", objectResult.getName());
 assertEquals("type1", objectResult.getType());
 }

 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(1), null);
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 assertNotNull(objectResult);
 assertEquals(objectStatus.getName(), objectResult.getName());
 assertEquals(objectStatus.getType(), objectResult.getType());
 assertEquals(objectStatus.getStatus(), objectResult.getStatus());
 assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
 assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 assertEquals("schema1.table1[type1]", objectResult.getSummary());
 }

}