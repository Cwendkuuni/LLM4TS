import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObjectResultTest {

 @Test
 public void testObjectResultConstruction() {
 ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
 assertEquals("testSchema", objectResult.getSchema());
 assertEquals("testName", objectResult.getName());
 assertEquals("testType", objectResult.getType());
 }

 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(1), new AtomicLong(10), null);
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 assertEquals(objectStatus.getName(), objectResult.getName());
 assertEquals(objectStatus.getType(), objectResult.getType());
 assertEquals(objectStatus.getStatus(), objectResult.getStatus());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
 assertEquals("testSchema.testName[testType]", objectResult.getSummary());
 }

 @Test
 public void testJsonIgnore() {
 ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
 assertNull(objectResult.getExportPaths());
 }
}