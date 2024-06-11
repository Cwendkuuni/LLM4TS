import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import java.util.Arrays;
import java.util.ArrayList;

public class ObjectResultTest {

 @Test
 public void testObjectResultConstructor() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 assertEquals("schema1", objectResult.getSchema());
 assertEquals("name1", objectResult.getName());
 assertEquals("type1", objectResult.getType());
 }

 @Test
 public void testOfMethod() {
 ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(2), null);
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 assertEquals("name1", objectResult.getName());
 assertEquals(1, objectResult.getCount().get());
 assertEquals(2, objectResult.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 assertEquals("schema1.name1[type1]", objectResult.getSummary());
 }

 // Test for edge cases and exceptions

 @Test(expected = IllegalArgumentException.class)
 public void testObjectResultConstructorWithNullSchema() {
 ObjectResult objectResult = new ObjectResult(null, "name1", "type1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectResultConstructorWithNullName() {
 ObjectResult objectResult = new ObjectResult("schema1", null, "type1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectResultConstructorWithNullType() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", null);
 }

 @Test
 public void testSetStatus() {
 ObjectResult objectResult = new ObjectResult();
 ObjectStatus status = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(2), null);
 objectResult.setStatus(status);
 assertEquals(status, objectResult.getStatus());
 }

 @Test
 public void testSetType() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setType("type1");
 assertEquals("type1", objectResult.getType());
 }

 @Test
 public void testSetName() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setName("name1");
 assertEquals("name1", objectResult.getName());
 }

 @Test
 public void testSetSchema() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setSchema("schema1");
 assertEquals("schema1", objectResult.getSchema());
 }

 @Test
 public void testSetCount() {
 ObjectResult objectResult = new ObjectResult();
 AtomicLong count = new AtomicLong(1);
 objectResult.setCount(count);
 assertEquals(count, objectResult.getCount());
 }

 @Test
 public void testSetTotal() {
 ObjectResult objectResult = new ObjectResult();
 AtomicLong total = new AtomicLong(2);
 objectResult.setTotal(total);
 assertEquals(total, objectResult.getTotal());
 }

 @Test
 public void testGetExportPaths() {
 ObjectResult objectResult = new ObjectResult();
 List<URL> exportPaths = Arrays.asList(new URL("path1"), new URL("path2"));
 objectResult.setExportPaths(exportPaths);
 assertEquals(exportPaths, objectResult.getExportPaths());
 }

 @Test
 public void testToString() {
 ObjectResult objectResult = new ObjectResult();
 assertEquals("ObjectResult(schema=null, name=null, type=null, status=ObjectStatus(name=null, count=0, total=0, createTime=null, updateTime=null), count=0, total=0)", objectResult.toString());
 }

 // Test for methods that return lists
 @Test
 public void testGetListOfURLs() {
 ObjectResult objectResult = new ObjectResult();
 List<URL> exportPaths = new ArrayList<>();
 exportPaths.add(new URL("path1"));
 objectResult.setExportPaths(exportPaths);
 assertEquals(exportPaths, objectResult.getExportPaths());
 }
}