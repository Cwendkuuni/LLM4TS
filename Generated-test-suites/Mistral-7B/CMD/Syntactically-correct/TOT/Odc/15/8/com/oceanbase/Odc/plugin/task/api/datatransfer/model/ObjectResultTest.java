import org.junit.Assert;
import org.junit.Test;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 @Test
 public void testObjectResultConstruction() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 Assert.assertEquals("schema1", objectResult.getSchema());
 Assert.assertEquals("name1", objectResult.getName());
 Assert.assertEquals("type1", objectResult.getType());
 Assert.assertNull(objectResult.getExportPaths());
 Assert.assertEquals(ObjectStatus.Status.UNPROCESSED, objectResult.getStatus());
 Assert.assertEquals(0, objectResult.getCount().get());
 Assert.assertEquals(0, objectResult.getTotal().get());
 }

 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(5), new AtomicLong(10), null);
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 Assert.assertEquals("name1", objectResult.getName());
 Assert.assertEquals(ObjectStatus.Status.UNPROCESSED, objectResult.getStatus());
 Assert.assertNull(objectResult.getExportPaths());
 Assert.assertEquals(5, objectResult.getCount().get());
 Assert.assertEquals(10, objectResult.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 Assert.assertEquals("schema1.name1[type1]", objectResult.getSummary());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetExportPathsWithNull() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 objectResult.setExportPaths(null);
 }

 @Test
 public void testSetExportPaths() {
 List<URL> exportPaths = List.of(new URL("http://localhost:8080/export1"), new URL("http://localhost:8080/export2"));
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 objectResult.setExportPaths(exportPaths);
 Assert.assertEquals(exportPaths, objectResult.getExportPaths());
 }
}