import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import org.junit.Test;

public class ObjectResultTest {

 private ObjectMapper objectMapper = new ObjectMapper();
 private ObjectStatus objectStatus = new ObjectStatus("schema1", "table1", ObjectStatus.Status.SUCCEED, 10L, 50L, null);

 @Test
 public void testObjectResultConstruction() {
 ObjectResult objectResult = new ObjectResult("schema2", "table2", "type2");
 assertEquals("schema2", objectResult.getSchema());
 assertEquals("table2", objectResult.getName());
 assertEquals("type2", objectResult.getType());
 assertNull(objectResult.getExportPaths());
 }

 @Test
 public void testObjectResultOf() {
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 assertEquals(objectStatus.getSchema(), objectResult.getSchema());
 assertEquals(objectStatus.getName(), objectResult.getName());
 assertEquals(objectStatus.getType(), objectResult.getType());
 assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
 assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema3", "table3", "type3");
 assertEquals("schema3.table3[type3]", objectResult.getSummary());
 }

 @Test
 public void testJsonSerialization() throws Exception {
 ObjectResult objectResult = new ObjectResult("schema4", "table4", "type4");
 String json = objectMapper.writeValueAsString(objectResult);
 ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
 assertEquals(objectResult.getSchema(), deserializedObjectResult.getSchema());
 assertEquals(objectResult.getName(), deserializedObjectResult.getName());
 assertEquals(objectResult.getType(), deserializedObjectResult.getType());
 assertNull(deserializedObjectResult.getExportPaths());
 }

 @Test
 public void testJsonSerializationWithExportPaths() throws Exception {
 List<URL> exportPaths = mock(List.class);
 when(exportPaths.size()).thenReturn(3);
 when(exportPaths.get(0)).thenReturn(new URL("path1"));
 when(exportPaths.get(1)).thenReturn(new URL("path2"));
 when(exportPaths.get(2)).thenReturn(new URL("path3"));
 ObjectResult objectResult = new ObjectResult("schema5", "table5", "type5", exportPaths);
 String json = objectMapper.writeValueAsString(objectResult);
 ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
 assertEquals(objectResult.getSchema(), deserializedObjectResult.getSchema());
 assertEquals(objectResult.getName(), deserializedObjectResult.getName());
 assertEquals(objectResult.getType(), deserializedObjectResult.getType());
 assertEquals(exportPaths, deserializedObjectResult.getExportPaths());
 }
}