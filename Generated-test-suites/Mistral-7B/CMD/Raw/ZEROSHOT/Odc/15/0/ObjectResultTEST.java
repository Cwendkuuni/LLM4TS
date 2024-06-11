import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.AtomicLong;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 private ObjectResult objectResult;
 private ObjectStatus objectStatus;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectStatus = new ObjectStatus("schema1", "table1", ObjectStatus.Status.COMPLETED, new AtomicLong(1), new AtomicLong(1));
 objectMapper = new ObjectMapper();
 objectResult = new ObjectResult("schema2", "table2", "type1");
 }

 @Test
 public void testObjectResultConstruction() {
 ObjectResult result = new ObjectResult("schema3", "table3", "type2");
 assertEquals("schema3", result.getSchema());
 assertEquals("table3", result.getName());
 assertEquals("type2", result.getType());
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
 ObjectResult result = new ObjectResult("schema", "table", "type");
 assertEquals("schema.table[type]", result.getSummary());
 }

 @Test
 public void testJsonIgnore() {
 ObjectResult result = new ObjectResult("schema", "table", "type");
 assertNull(result.getExportPaths());
 }

 @Test
 public void testJsonSerialization() throws Exception {
 String json = objectMapper.writeValueAsString(objectResult);
 ObjectResult deserializedObject = objectMapper.readValue(json, ObjectResult.class);
 assertEquals(objectResult.getSchema(), deserializedObject.getSchema());
 assertEquals(objectResult.getName(), deserializedObject.getName());
 assertEquals(objectResult.getType(), deserializedObject.getType());
 assertEquals(objectResult.getCount().get(), deserializedObject.getCount().get());
 assertEquals(objectResult.getTotal().get(), deserializedObject.getTotal().get());
 }

 @Test
 public void testEqualsAndHashCode() {
 ObjectResult result1 = new ObjectResult("schema1", "table1", "type1");
 ObjectResult result2 = new ObjectResult("schema2", "table2", "type1");
 ObjectResult result3 = new ObjectResult("schema1", "table1", "type1");

 assertEquals(result1, result1);
 assertEquals(result1, result3);
 assertNotEquals(result1, result2);
 }
}