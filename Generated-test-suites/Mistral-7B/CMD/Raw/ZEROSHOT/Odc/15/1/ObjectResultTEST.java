import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testObjectResultConstructor() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 assertNotNull(objectResult);
 assertEquals("schema1", objectResult.getSchema());
 assertEquals("table1", objectResult.getName());
 assertEquals("type1", objectResult.getType());
 assertNull(objectResult.getExportPaths());
 assertEquals(ObjectStatus.Status.UNPROCESSED, objectResult.getStatus());
 }

 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1L), new AtomicLong(1L), null);
 ObjectResult objectResult = ObjectResult.of(objectStatus);

 assertNotNull(objectResult);
 assertEquals(objectStatus.getName(), objectResult.getName());
 assertEquals(objectStatus.getType(), objectResult.getType());
 assertEquals(objectStatus.getStatus(), objectResult.getStatus());
 assertEquals(objectStatus.getCount().get(), objectResult.getTotal().get());
 assertNull(objectResult.getExportPaths());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 assertEquals("schema1.table1[type1]", objectResult.getSummary());
 }

 @Test
 public void testSetExportPaths() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 List<URL> exportPaths = new ArrayList<>();
 exportPaths.add(new URL("file:/path1/export1.csv"));
 exportPaths.add(new URL("file:/path2/export2.csv"));
 objectResult.setExportPaths(exportPaths);

 assertEquals(2, objectResult.getExportPaths().size());
 assertEquals("file:/path1/export1.csv", objectResult.getExportPaths().get(0).toString());
 assertEquals("file:/path2/export2.csv", objectResult.getExportPaths().get(1).toString());
 }

 @Test
 public void testJsonSerialization() throws Exception {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
 objectResult.setExportPaths(new ArrayList<>(List.of(new URL("file:/path1/export1.csv"))));

 String jsonString = objectMapper.writeValueAsString(objectResult);

 ObjectResult deserializedObjectResult = objectMapper.readValue(jsonString, ObjectResult.class);

 assertEquals(objectResult, deserializedObjectResult);
 }
}