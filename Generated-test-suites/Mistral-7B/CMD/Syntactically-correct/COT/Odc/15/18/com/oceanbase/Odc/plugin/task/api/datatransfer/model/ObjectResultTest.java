import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import java.util.ArrayList;
import java.util.List;

public class ObjectResultTest {

 // Test constructor
 @Test
 public void testObjectResultConstruction() {
 ObjectResult result = new ObjectResult("schema1", "name1", "type1");
 assertEquals("schema1", result.getSchema());
 assertEquals("name1", result.getName());
 assertEquals("type1", result.getType());
 }

 // Test static method of
 @Test
 public void testObjectResultOf() {
 ObjectStatus status = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(10), null);
 ObjectResult result = ObjectResult.of(status);
 assertEquals(status.getName(), result.getName());
 assertEquals(status.getType(), result.getType());
 assertEquals(status.getStatus(), result.getStatus());
 }

 // Test getSummary method
 @Test
 public void testGetSummary() {
 ObjectResult result = new ObjectResult("schema1", "name1", "type1");
 assertEquals("schema1.name1[type1]", result.getSummary());
 }

 // Test JsonIgnore annotation for exportPaths
 @Test
 public void testJsonIgnoreAnnotation() {
 ObjectResult result = new ObjectResult("schema1", "name1", "type1");
 assertNull(result.getExportPaths());
 }

 // Test equals and hashcode methods
 @Test
 public void testEqualsAndHashCode() {
 ObjectResult result1 = new ObjectResult("schema1", "name1", "type1");
 ObjectResult result2 = new ObjectResult("schema1", "name1", "type1");
 ObjectResult result3 = new ObjectResult("schema2", "name1", "type1");
 assertEquals(result1, result1);
 assertEquals(result1, result2);
 assertNotSame(result1, result3);
 }

 // Edge cases and exception handling tests

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullSchema() {
 new ObjectResult(null, "name1", "type1");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullName() {
 new ObjectResult("schema1", null, "type1");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullType() {
 new ObjectResult("schema1", "name1", null);
 }

 @Test
 public void testGetSummaryWithEmptySchema() {
 ObjectResult result = new ObjectResult("", "name1", "type1");
 assertEquals("", result.getSummary());
 }

 @Test
 public void testGetSummaryWithEmptyName() {
 ObjectResult result = new ObjectResult("schema1", "", "type1");
 assertEquals("schema1[]", result.getSummary());
 }

 @Test
 public void testGetSummaryWithEmptyType() {
 ObjectResult result = new ObjectResult("schema1", "name1", "");
 assertEquals("schema1.name1[]", result.getSummary());
 }

 @Test
 public void testGetSummaryWithNullType() {
 ObjectResult result = new ObjectResult("schema1", "name1", null);
 assertEquals("schema1.name1[]", result.getSummary());
 }

 @Test
 public void testObjectResultOfWithNullObjectStatus() {
 assertNull(ObjectResult.of(null));
 }

 @Test
 public void testSetTypeWithNullType() {
 ObjectResult result = new ObjectResult("schema1", "name1", null);
 result.setType(null);
 assertEquals(null, result.getType());
 }

 @Test
 public void testSetTypeWithEmptyType() {
 ObjectResult result = new ObjectResult("schema1", "name1", "type1");
 result.setType("");
 assertEquals("", result.getType());
 }

 @Test
 public void testSetStatusWithNullStatus() {
 ObjectResult result = new ObjectResult("schema1", "name1", "type1");
 result.setStatus(null);
 assertNull(result.getStatus());
 }

 @Test
 public void testSetStatusWithSameNameButDifferentObject() {
 ObjectResult result1 = new ObjectResult("schema1", "name1", "type1");
 ObjectResult result2 = new ObjectResult("schema1", "name1", "type2");
 result1.setStatus(result2.getStatus());
 assertNotSame(result1.getStatus(), result2.getStatus());
 }

 @Test
 public void testSetCountWithNegativeValue() {
 ObjectResult result = new ObjectResult("schema1", "name1", "type1");
 result.setCount(new AtomicLong(-1));
 assertEquals(-1, result.getCount().get());
 }

 @Test
 public void testSetTotalWithNegativeValue() {
 ObjectResult result = new ObjectResult("schema1", "name1", "type1");
 result.setTotal(new AtomicLong(-1));
 assertEquals(-1, result.getTotal().get());
 }

 @Test
 public void testListExportPathsWithNullList() {
 ObjectResult result = new ObjectResult("schema1", "name1", "type1");
 List<URL> nullList = null;
 result.setExportPaths(nullList);
 assertNull(result.getExportPaths());
 }

 @Test
 public void testListExportPathsWithEmptyList() {
 ObjectResult result = new ObjectResult("schema1", "name1", "type1");
 List<URL> emptyList = new ArrayList<>();
 result.setExportPaths(emptyList);
 assertEquals(emptyList, result.getExportPaths());
 }

 @Test
 public void testListExportPathsWithNullElement() {
 ObjectResult result = new ObjectResult("schema1", "name1", "type1");
 List<URL> list = new ArrayList<>();
 list.add(null);
 result.setExportPaths(list);
 assertNull(result.getExportPaths().get(0));
 }

}