import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

public class ObjectResultTest {

 private ObjectStatus objectStatus;
 private ObjectResult objectResult;

 @Before
 public void setup() {
 objectStatus = mock(ObjectStatus.class);
 when(objectStatus.getType()).thenReturn("type");
 when(objectStatus.getStatus()).thenReturn(Status.SUCCESS);
 when(objectStatus.getName()).thenReturn("name");
 when(objectStatus.getSchema()).thenReturn("schema");
 when(objectStatus.getCount()).thenReturn(new AtomicLong(10));
 when(objectStatus.getTotal()).thenReturn(new AtomicLong(20));
 }

 @Test
 public void testObjectResultCreation() {
 // Test constructor
 ObjectResult objectResult = new ObjectResult("schema", "name", "type");
 assertEquals("schema", objectResult.getSchema());
 assertEquals("name", objectResult.getName());
 assertEquals("type", objectResult.getType());

 // Test of method
 ObjectResult result = ObjectResult.of(objectStatus);
 assertEquals("schema", result.getSchema());
 assertEquals("name", result.getName());
 assertEquals("type", result.getType());
 assertEquals(Status.SUCCESS, result.getStatus());
 assertEquals(new AtomicLong(10), result.getCount());
 assertEquals(new AtomicLong(20), result.getTotal());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema", "name", "type");
 assertEquals("schema.name[type]", objectResult.getSummary());
 }

 @Test
 public void testJsonIgnore() {
 ObjectResult objectResult = new ObjectResult("schema", "name", "type");
 List<URL> exportPaths = new ArrayList<>();
 objectResult.setExportPaths(exportPaths);
 assertNotNull(objectResult.getExportPaths());
 assertEquals(0, objectResult.getExportPaths().size());
 }
}