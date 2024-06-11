import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 @Test
 public void testObjectResultConstruction() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 assertEquals("schema1", objectResult.getSchema());
 assertEquals("table1", objectResult.getName());
 assertEquals("type1", objectResult.getType());
 assertNull(objectResult.getExportPaths());
 }

 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = mock(ObjectStatus.class);
 when(objectStatus.getType()).thenReturn("type1");
 when(objectStatus.getStatus()).thenReturn(ObjectStatus.Status.SUCCESS);
 when(objectStatus.getName()).thenReturn("table1");
 when(objectStatus.getSchema()).thenReturn("schema1");
 when(objectStatus.getCount()).thenReturn(1L);
 when(objectStatus.getTotal()).thenReturn(1L);

 ObjectResult objectResult = ObjectResult.of(objectStatus);
 assertEquals("schema1", objectResult.getSchema());
 assertEquals("table1", objectResult.getName());
 assertEquals("type1", objectResult.getType());
 assertEquals(1, objectResult.getCount().get());
 assertEquals(1, objectResult.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 assertEquals("schema1.table1[type1]", objectResult.getSummary());
 }
}