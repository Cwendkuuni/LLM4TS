import org.junit.Assert;
import org.junit.Test;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import java.util.Arrays;
import java.util.AtomicLong;
import java.util.List;

public class ObjectResultTest {

 @Test
 public void testObjectResultConstruction() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 Assert.assertEquals("schema1", objectResult.getSchema());
 Assert.assertEquals("table1", objectResult.getName());
 Assert.assertEquals("type1", objectResult.getType());
 Assert.assertNull(objectResult.getExportPaths());
 Assert.assertNull(objectResult.getStatus());
 }

 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(10), new AtomicLong(20), null);
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 Assert.assertEquals(objectStatus.getName(), objectResult.getName());
 Assert.assertEquals(objectStatus.getType(), objectResult.getType());
 Assert.assertEquals(objectStatus.getStatus(), objectResult.getStatus());
 Assert.assertNull(objectResult.getExportPaths());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 Assert.assertEquals("schema1.table1[type1]", objectResult.getSummary());
 }

 @Test
 public void testSettersAndGetters() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export1"), new URL("http://localhost:8080/export2"));
 objectResult.setExportPaths(exportPaths);
 Assert.assertEquals(exportPaths, objectResult.getExportPaths());
 ObjectStatus status = new ObjectStatus("status1", new AtomicLong(1), new AtomicLong(2), null);
 objectResult.setStatus(status);
 Assert.assertEquals(status, objectResult.getStatus());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullStatus() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 objectResult.setStatus(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullExportPaths() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 objectResult.setExportPaths(null);
 }

 @Test
 public void testEqualsAndHashCode() {
 ObjectResult objectResult1 = new ObjectResult("schema1", "table1", "type1");
 ObjectResult objectResult2 = new ObjectResult("schema1", "table1", "type1");
 ObjectResult objectResult3 = new ObjectResult("schema2", "table2", "type2");
 Assert.assertEquals(objectResult1, objectResult1);
 Assert.assertEquals(objectResult1, objectResult2);
 Assert.assertNotEquals(objectResult1, objectResult3);
 }

 @Test
 public void testToString() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 String expected = "ObjectResult{schema='schema1', name='table1', type='type1', status=null, count=0, total=0, exportPaths=null}";
 Assert.assertEquals(expected, objectResult.toString());
 }

 @Test
 public void testClone() {
 ObjectResult original = new ObjectResult("schema1", "table1", "type1");
 ObjectResult clone = original.clone();
 Assert.assertEquals(original, clone);
 }

 @Test
 public void testCloneWithModifiedFields() {
 ObjectResult original = new ObjectResult("schema1", "table1", "type1");
 ObjectResult clone = original.clone();
 clone.setExportPaths(Arrays.asList(new URL("http://localhost:8080/export1")));
 Assert.assertNotEquals(original, clone);
 }

 @Test
 public void testCloneWithSameFields() {
 ObjectResult original = new ObjectResult("schema1", "table1", "type1");
 ObjectResult clone = original.clone();
 clone.setStatus(original.getStatus());
 Assert.assertEquals(original, clone);
 }
}