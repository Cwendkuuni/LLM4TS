import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

 @Test
 public void testObjectResultConstruction() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 assertEquals("schema1", objectResult.getSchema());
 assertEquals("table1", objectResult.getName());
 assertEquals("type1", objectResult.getType());
 }

 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(10), null);
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 assertEquals("name1", objectResult.getName());
 assertEquals("name1", objectResult.getStatus().getName());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 assertEquals("schema1.table1[type1]", objectResult.getSummary());
 }

 @Test
 public void testToString() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 assertEquals("ObjectResult(schema=schema1, name=table1, type=type1, status=STATUS_NONE, count=0, total=0)", objectResult.toString());
 }

}