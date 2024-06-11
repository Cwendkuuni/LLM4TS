import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResultBuilder;

public class ObjectResultTest {

 // Test case for constructor with valid arguments
 @Test
 public void testObjectResultConstructor() {
 String schema = "testSchema";
 String name = "testName";
 String type = "testType";
 ObjectResult objectResult = new ObjectResult(schema, name, type);
 assertEquals(schema, objectResult.getSchema());
 assertEquals(name, objectResult.getName());
 assertEquals(type, objectResult.getType());
 }

 // Test case for of method with valid ObjectStatus
 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(1), new AtomicLong(1), null);
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 assertEquals(objectStatus.getName(), objectResult.getName());
 assertEquals(objectStatus.getType(), objectResult.getType());
 }

 // Test case for getSummary method
 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setSchema("testSchema");
 objectResult.setName("testName");
 objectResult.setType("testType");
 assertEquals("testSchema.testName[testType]", objectResult.getSummary());
 }

 // Test case for equals and hashCode methods
 @Test
 public void testEqualsAndHashCode() {
 ObjectResult objectResult1 = new ObjectResult();
 ObjectResult objectResult2 = new ObjectResult();
 assertTrue(objectResult1.equals(objectResult1));
 assertFalse(objectResult1.equals(objectResult2));
 ObjectResult objectResult3 = new ObjectResult();
 objectResult3.setName("testName");
 assertFalse(objectResult1.equals(objectResult3));
 ObjectResult objectResult4 = new ObjectResult();
 objectResult4.setName("testName");
 objectResult4.setSchema("testSchema");
 assertTrue(objectResult3.equals(objectResult4));
 }

 // Test case for constructor with empty arguments
 @Test(expected = IllegalArgumentException.class)
 public void testObjectResultConstructorEmpty() {
 new ObjectResult();
 }

 // Test case for getType method with null argument
 @Test(expected = NullPointerException.class)
 public void testGetTypeWithNullArgument() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setType(null);
 }

 // Test case for getSchema method with null argument
 @Test(expected = NullPointerException.class)
 public void testGetSchemaWithNullArgument() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setSchema(null);
 }

 // Test case for getName method with null argument
 @Test(expected = NullPointerException.class)
 public void testgetNameWithNullArgument() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setName(null);
 }
}