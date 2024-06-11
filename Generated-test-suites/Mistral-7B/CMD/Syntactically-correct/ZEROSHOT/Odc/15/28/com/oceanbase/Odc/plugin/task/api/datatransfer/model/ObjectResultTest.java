import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.AtomicLong;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

public class ObjectResultTest {

 @Test
 public void testObjectResultConstructor() {
 // Given
 String schema = "testSchema";
 String name = "testName";
 String type = "testType";
 ObjectResult objectResult = new ObjectResult(schema, name, type);

 // Then
 assertEquals(schema, objectResult.getSchema());
 assertEquals(name, objectResult.getName());
 assertEquals(type, objectResult.getType());
 assertNull(objectResult.getExportPaths());
 assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
 assertEquals(0, objectResult.getCount().get());
 assertEquals(0, objectResult.getTotal().get());
 }

 @Test
 public void testObjectResultOf() {
 // Given
 ObjectStatus objectStatus = Mockito.spy(new ObjectStatus());
 ObjectResult objectResult = ObjectResult.of(objectStatus);

 // Then
 assertEquals(objectStatus.getType(), objectResult.getType());
 assertEquals(objectStatus.getStatus(), objectResult.getStatus());
 assertEquals(objectStatus.getName(), objectResult.getName());
 assertEquals(objectStatus.getSchema(), objectResult.getSchema());
 assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
 assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 // Given
 ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");

 // Then
 String expectedSummary = "testSchema.testName[testType]";
 assertEquals(expectedSummary, objectResult.getSummary());
 }

 @Test
 public void testJsonIgnore() {
 // Given
 ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
 List<URL> exportPaths = new ArrayList<>();
 objectResult.setExportPaths(exportPaths);

 ObjectMapper objectMapper = new ObjectMapper();
 String jsonString = "";
 try {
 jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectResult);
 } catch (Exception e) {
 fail("Failed to parse JSON string");
 }

 // Then
 assertFalse(jsonString.contains("exportPaths"));
 }
}