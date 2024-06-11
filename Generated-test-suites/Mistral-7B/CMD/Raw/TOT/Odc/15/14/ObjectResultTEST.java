import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 // Test constructor
 @Test
 public void testObjectResultConstructor() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 assertEquals("schema1", objectResult.getSchema());
 assertEquals("name1", objectResult.getName());
 assertEquals("type1", objectResult.getType());
 }

 // Test of method getType
 @Test
 public void testGetType() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 ObjectStatus objectStatus = new ObjectStatus();
 ObjectResult objectResultFromStatus = ObjectResult.of(objectStatus);
 assertEquals("type1", objectResult.getType());
 assertEquals("type1", objectResultFromStatus.getType());
 }

 // Test of method getSummary
 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 assertEquals("schema1.name1[type1]", objectResult.getSummary());
 }

 // Test of method setType
 @Test
 public void testSetType() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 objectResult.setType("type2");
 assertEquals("type2", objectResult.getType());
 }

 // Test of method equals
 @Test
 public void testEquals() {
 ObjectResult objectResult1 = new ObjectResult("schema1", "name1", "type1");
 ObjectResult objectResult2 = new ObjectResult("schema1", "name1", "type1");
 assertTrue(objectResult1.equals(objectResult2));
 }

 // Test of method hashCode
 @Test
 public void testHashCode() {
 ObjectResult objectResult1 = new ObjectResult("schema1", "name1", "type1");
 ObjectResult objectResult2 = new ObjectResult("schema1", "name1", "type1");
 assertEquals(objectResult1.hashCode(), objectResult2.hashCode());
 }

 // Test of method toString
 @Test
 public void testToString() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 assertEquals("ObjectResult(schema=schema1, name=name1, type=type1, status=null, count=0, total=0)", objectResult.toString());
 }

 // Test of method getStatus
 @Test(expected = UnsupportedOperationException.class)
 public void testGetStatus() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 objectResult.getStatus();
 }

 // Test of method getExportPaths
 @Test(expected = UnsupportedOperationException.class)
 public void testGetExportPaths() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 objectResult.getExportPaths();
 }

 // Test of method setStatus
 @Test(expected = UnsupportedOperationException.class)
 public void testSetStatus() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 objectResult.setStatus(ObjectStatus.FAILED);
 }

 // Test of method setCount
 @Test(expected = UnsupportedOperationException.class)
 public void testSetCount() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 AtomicLong count = new AtomicLong(1);
 objectResult.setCount(count);
 }

 // Test of method setTotal
 @Test(expected = UnsupportedOperationException.class)
 public void testSetTotal() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 AtomicLong total = new AtomicLong(1);
 objectResult.setTotal(total);
 }
}