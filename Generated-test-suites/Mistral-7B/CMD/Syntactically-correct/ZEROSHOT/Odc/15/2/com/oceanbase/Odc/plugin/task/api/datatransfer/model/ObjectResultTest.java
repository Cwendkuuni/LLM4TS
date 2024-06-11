import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 @Test
 public void testObjectResultConstruction() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "INT");
 assertEquals("schema1", objectResult.getSchema());
 assertEquals("table1", objectResult.getName());
 assertEquals("INT", objectResult.getType());
 assertNotNull(objectResult.getStatus());
 assertNotNull(objectResult.getCount());
 assertNotNull(objectResult.getTotal());
 }

 @Test
 public void testOfMethod() {
 ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(10), new AtomicLong(50), null);
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 assertEquals("name1", objectResult.getName());
 assertEquals(objectStatus.getStatus(), objectResult.getStatus());
 assertEquals("name1", objectResult.getSummary());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "INT");
 assertEquals("schema1.table1[INT]", objectResult.getSummary());
 }

}