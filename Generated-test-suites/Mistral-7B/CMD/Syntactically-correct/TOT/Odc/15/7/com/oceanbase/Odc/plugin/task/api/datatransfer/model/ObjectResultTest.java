import org.junit.Test;
import static org.junit.Assert.*;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 @Test
 public void testObjectResultCreation() {
 ObjectResult objectResult = new ObjectResult("schema1", "tableName", "TYPE1");
 ObjectStatus objectStatus = new ObjectStatus("tableName", new AtomicLong(0), new AtomicLong(0), null);
 ObjectResult expectedObjectResult = ObjectResult.of(objectStatus);

 assertEquals(objectResult.getName(), expectedObjectResult.getName());
 assertEquals(objectResult.getType(), expectedObjectResult.getType());
 assertEquals(objectResult.getSchema(), expectedObjectResult.getSchema());
 assertEquals(objectResult.getStatus(), expectedObjectResult.getStatus());
 assertEquals(objectResult.getCount().get(), expectedObjectResult.getCount().get());
 assertEquals(objectResult.getTotal().get(), expectedObjectResult.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "tableName", "TYPE1");
 String expectedSummary = "schema1.tableName[TYPE1]";
 String actualSummary = objectResult.getSummary();
 assertEquals(expectedSummary, actualSummary);
 }

 @Test(expected = NullPointerException.class)
 public void testNullObjectStatus() {
 ObjectResult objectResult = new ObjectResult(null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptySchema() {
 ObjectResult objectResult = new ObjectResult("", "tableName", "TYPE1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTableName() {
 ObjectResult objectResult = new ObjectResult("schema1", "", "TYPE1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyType() {
 ObjectResult objectResult = new ObjectResult("schema1", "tableName", "");
 }
}