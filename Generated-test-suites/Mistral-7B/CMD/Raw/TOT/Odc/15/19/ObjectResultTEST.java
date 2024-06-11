import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

@ExtendWith(MockitoExtension.class)
public class ObjectResultTest {

 // Test constructor
 @Test
 public void testObjectResultConstructor() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 Assertions.assertEquals("schema1", objectResult.getSchema());
 Assertions.assertEquals("name1", objectResult.getName());
 Assertions.assertEquals("type1", objectResult.getType());
 Assertions.assertNull(objectResult.getExportPaths());
 }

 // Test of method: getSummary
 @Test
 public void testGetSummary() {
 ObjectStatus objectStatus = Mockito.mock(ObjectStatus.class);
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 Mockito.when(objectStatus.getSchema()).thenReturn("schema1");
 Mockito.when(objectStatus.getName()).thenReturn("name1");
 Mockito.when(objectStatus.getType()).thenReturn("type1");

 String expectedSummary = "schema1.name1[type1]";
 String resultSummary = objectResult.getSummary();

 Assertions.assertEquals(expectedSummary, resultSummary);
 }

 // Test of method: of
 @Test
 public void testOf() {
 ObjectStatus objectStatus = new ObjectStatus();
 ObjectResult objectResult = ObjectResult.of(objectStatus);

 Assertions.assertEquals(objectStatus.getName(), objectResult.getName());
 Assertions.assertEquals(objectStatus.getSchema(), objectResult.getSchema());
 Assertions.assertEquals(objectStatus.getType(), objectResult.getType());
 Assertions.assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
 Assertions.assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
 }

 // Test of method: setSchema
 @Test
 public void testSetSchema() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setSchema("schema1");
 Assertions.assertEquals("schema1", objectResult.getSchema());
 }

 // Test of method: setName
 @Test
 public void testSetName() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setName("name1");
 Assertions.assertEquals("name1", objectResult.getName());
 }

 // Test of method: setType
 @Test
 public void testSetType() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setType("type1");
 Assertions.assertEquals("type1", objectResult.getType());
 }
}