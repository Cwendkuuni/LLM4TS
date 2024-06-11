import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ObjectResultTest {

 private ObjectResult objectResult;
 private ObjectStatus objectStatus;
 private AtomicLong count;
 private AtomicLong total;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 objectStatus = new ObjectStatus("name", "status", new AtomicLong(1), new AtomicLong(10));
 objectResult = new ObjectResult("schema", "name", "type");
 count = new AtomicLong(1);
 total = new AtomicLong(10);
 objectResult.setCount(count);
 objectResult.setTotal(total);
 objectResult.setStatus(objectStatus);
 }

 @Test
 public void testObjectResultConstructor() {
 ObjectResult result = new ObjectResult("schema", "name", "type");
 assertEquals("schema", result.getSchema());
 assertEquals("name", result.getName());
 assertEquals("type", result.getType());
 assertEquals(ObjectStatus.UNPROCESSED, result.getStatus());
 assertEquals(0, result.getCount().get());
 assertEquals(0, result.getTotal().get());
 }

 @Test
 public void testObjectResultOf() {
 ObjectResult result = ObjectResult.of(objectStatus);
 assertEquals(objectStatus.getName(), result.getName());
 assertEquals(objectStatus.getStatus(), result.getStatus().getStatus());
 assertEquals(objectStatus.getSchema(), result.getSchema());
 assertEquals(count, result.getCount());
 assertEquals(total, result.getTotal());
 }

 @Test
 public void testGetSummary() {
 ObjectMapper mapper = new ObjectMapper();
 ObjectResult result = mapper.convertValue(objectStatus, ObjectResult.class);
 assertEquals("schema.name[type]", result.getSummary());
 }

 @Test
 public void testJsonIgnore() {
 ObjectMapper mapper = new ObjectMapper();
 ObjectResult result = mapper.convertValue(objectStatus, ObjectResult.class);
 assertNull(result.getExportPaths());
 }

 @Test
 public void testSetGetType() {
 ObjectResult result = new ObjectResult();
 result.setType("type1");
 assertEquals("type1", result.getType());
 }

 @Test
 public void testSetGetStatus() {
 ObjectResult result = new ObjectResult();
 ObjectStatus status = new ObjectStatus("name", "status", count, total);
 result.setStatus(status);
 assertEquals(status, result.getStatus());
 }

 @Test
 public void testSetGetSchema() {
 ObjectResult result = new ObjectResult();
 result.setSchema("schema1");
 assertEquals("schema1", result.getSchema());
 }

 @Test
 public void testSetGetCount() {
 ObjectResult result = new ObjectResult();
 result.setCount(count);
 assertEquals(count, result.getCount());
 }

 @Test
 public void testSetGetTotal() {
 ObjectResult result = new ObjectResult();
 result.setTotal(total);
 assertEquals(total, result.getTotal());
 }

 @Test
 public void testSetExportPaths() {
 ObjectResult result = new ObjectResult();
 List<URL> exportPaths = new ArrayList<>();
 result.setExportPaths(exportPaths);
 assertNull(result.getExportPaths());
 }

 @Test
 public void testSetExportPathsWithValue() {
 ObjectResult result = new ObjectResult();
 List<URL> exportPaths = new ArrayList<>();
 URL url = mock(URL.class);
 exportPaths.add(url);
 result.setExportPaths(exportPaths);
 assertEquals(exportPaths, result.getExportPaths());
 }
}