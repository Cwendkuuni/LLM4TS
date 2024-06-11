import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ObjectResultTest {

 @Test
 public void testObjectResultConstruction() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 assertEquals("schema1", objectResult.getSchema());
 assertEquals("name1", objectResult.getName());
 assertEquals("type1", objectResult.getType());
 assertNull(objectResult.getExportPaths());
 assertNull(objectResult.getStatus());
 assertEquals(0, objectResult.getCount().get());
 assertEquals(0, objectResult.getTotal().get());
 }

 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = new ObjectStatus("type1", ObjectStatus.Status.SUCCESS, "name1", "schema1");
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 assertEquals("schema1", objectResult.getSchema());
 assertEquals("name1", objectResult.getName());
 assertEquals("type1", objectResult.getType());
 assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
 assertEquals(1, objectResult.getCount().get());
 assertEquals(1, objectResult.getTotal().get());
 }

 @Test
 public void testObjectResultGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 assertEquals("schema1.name1[type1]", objectResult.getSummary());
 }

 @Test
 public void testObjectResultSettersAndGetters() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 List<URL> exportPaths = Arrays.asList(new URL("path1"), new URL("path2"));
 objectResult.setExportPaths(exportPaths);
 assertEquals(exportPaths, objectResult.getExportPaths());

 ObjectStatus objectStatus = new ObjectStatus("type1", ObjectStatus.Status.SUCCESS, "name1", "schema1");
 objectResult.setStatus(objectStatus);
 assertEquals(objectStatus, objectResult.getStatus());

 AtomicLong count = new AtomicLong(10);
 objectResult.setCount(count);
 assertEquals(count, objectResult.getCount());

 AtomicLong total = new AtomicLong(20);
 objectResult.setTotal(total);
 assertEquals(total, objectResult.getTotal());
 }

 @Test
 public void testObjectResultEqualsAndHashCode() {
 ObjectResult objectResult1 = new ObjectResult("schema1", "name1", "type1");
 ObjectResult objectResult2 = new ObjectResult("schema1", "name1", "type1");
 assertTrue(objectResult1.equals(objectResult2));
 assertTrue(objectResult1.hashCode() == objectResult2.hashCode());
 }

 @Test
 public void testObjectResultNotEquals() {
 ObjectResult objectResult1 = new ObjectResult("schema1", "name1", "type1");
 ObjectResult objectResult2 = new ObjectResult("schema2", "name1", "type1");
 assertFalse(objectResult1.equals(objectResult2));
 assertNotSame(objectResult1.hashCode(), objectResult2.hashCode());
 }

 @Test
 public void testObjectResultWithNullFields() {
 ObjectResult objectResult = new ObjectResult("schema1", null, "type1");
 assertNull(objectResult.getName());

 objectResult = new ObjectResult(null, "name1", "type1");
 assertNull(objectResult.getSchema());

 objectResult = new ObjectResult("schema1", "name1", null);
 assertNull(objectResult.getType());
 }

 @Test
 public void testObjectResultWithEmptyList() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 objectResult.setExportPaths(Collections.emptyList());
 assertFalse(objectResult.getExportPaths().isEmpty());
 assertEquals(0, objectResult.getExportPaths().size());
 }
}