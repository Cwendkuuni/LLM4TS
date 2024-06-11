import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

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
 ObjectMapper objectMapper = new ObjectMapper();
 objectStatus = new ObjectStatus("schema", "name", ObjectStatus.Status.OK, new AtomicLong(1), new AtomicLong(10L));
 objectResult = ObjectResult.of(objectStatus);
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testObjectResultConstruction() {
 ObjectResult result = new ObjectResult("schema", "name", "type");
 assertEquals("schema", result.getSchema());
 assertEquals("name", result.getName());
 assertEquals("type", result.getType());
 }

 @Test
 public void testOfMethod() {
 ObjectResult result = ObjectResult.of(objectStatus);
 assertEquals(objectStatus.getSchema(), result.getSchema());
 assertEquals(objectStatus.getName(), result.getName());
 assertEquals(objectStatus.getType(), result.getType());
 assertEquals(objectStatus.getCount().get(), result.getCount().get());
 assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 ObjectResult result = ObjectResult.of(objectStatus);
 assertEquals("schema.name[type]", result.getSummary());
 }

 @Test
 public void testExportPathsAreIgnored() {
 ObjectResult result = ObjectResult.of(objectStatus);
 verify(objectStatus, never()).setExportPaths(anyList());
 }

 @Test
 public void testGetCount() {
 ObjectResult result = ObjectResult.of(objectStatus);
 assertEquals(objectStatus.getCount().get(), result.getCount().get());
 }

 @Test
 public void testGetTotal() {
 ObjectResult result = ObjectResult.of(objectStatus);
 assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
 }

 @Test
 public void testSetCount() {
 ObjectResult result = ObjectResult.of(objectStatus);
 AtomicLong newCount = new AtomicLong(2);
 result.setCount(newCount);
 assertEquals(newCount.get(), result.getCount().get());
 }

 @Test
 public void testSetTotal() {
 ObjectResult result = ObjectResult.of(objectStatus);
 AtomicLong newTotal = new AtomicLong(3);
 result.setTotal(newTotal);
 assertEquals(newTotal.get(), result.getTotal().get());
 }

 @Test
 public void testObjectResultWithExportPaths() {
 ObjectStatus statusWithExportPaths = new ObjectStatus("schema", "name", ObjectStatus.Status.OK, new AtomicLong(1), new AtomicLong(10L), new ArrayList<URL>());
 ObjectResult resultWithExportPaths = ObjectResult.of(statusWithExportPaths);
 List<URL> exportPaths = new ArrayList<URL>();
 exportPaths.add(new URL("http://localhost:8080/exportPath1"));
 resultWithExportPaths.setExportPaths(exportPaths);
 assertEquals(exportPaths, resultWithExportPaths.getExportPaths());
 }
}