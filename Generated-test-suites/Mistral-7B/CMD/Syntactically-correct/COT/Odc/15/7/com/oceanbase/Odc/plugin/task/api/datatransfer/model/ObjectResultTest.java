import org.junit.Assert;import org.junit.Test;import static org.junit.Assert.*;import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;import java.util.Arrays;import java.util.AtomicLong;

public class ObjectResultTest {

 private List<URL> exportPaths;
 private String schema;
 private String name;
 private String type;
 private AtomicLong count;
 private AtomicLong total;
 private ObjectStatus status;

 @Test
 public void testObjectResultConstuctor() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 Assert.assertEquals("schema1", objectResult.getSchema());
 Assert.assertEquals("name1", objectResult.getName());
 Assert.assertEquals("type1", objectResult.getType());
 Assert.assertNull(objectResult.getExportPaths());
 Assert.assertEquals(ObjectStatus.UNPROCESSED, objectResult.getStatus());
 Assert.assertEquals(new AtomicLong(0), objectResult.getCount());
 Assert.assertEquals(new AtomicLong(0), objectResult.getTotal());
 }

 @Test
 public void testOf() {
 ObjectStatus status = new ObjectStatus();
 status.setName("name1");
 status.setType("type1");
 status.setSchema("schema1");
 status.setCount(new AtomicLong(10));
 status.setTotal(new AtomicLong(20));
 status.setStatus(ObjectStatus.PROCESSED);

 ObjectResult objectResult = ObjectResult.of(status);
 Assert.assertEquals("schema1", objectResult.getSchema());
 Assert.assertEquals("name1", objectResult.getName());
 Assert.assertEquals("type1", objectResult.getType());
 Assert.assertNull(objectResult.getExportPaths());
 Assert.assertEquals(status.getStatus(), objectResult.getStatus());
 Assert.assertEquals(new AtomicLong(10), objectResult.getCount());
 Assert.assertEquals(new AtomicLong(20), objectResult.getTotal());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setSchema("schema1");
 objectResult.setName("name1");
 objectResult.setType("type1");

 String expectedSummary = "schema1.name1[type1]";
 String actualSummary = objectResult.getSummary();
 Assert.assertEquals(expectedSummary, actualSummary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetSummaryWhenSchemaIsNull() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setName("name1");
 objectResult.setType("type1");
 objectResult.setSchema(null);

 objectResult.getSummary();
 }

 @Test(expected = NullPointerException.class)
 public void testGetSummaryWhenNameIsNull() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setSchema("schema1");
 objectResult.setType("type1");
 objectResult.setName(null);

 objectResult.getSummary();
 }

 @Test(expected = NullPointerException.class)
 public void testGetSummaryWhenTypeIsNull() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setSchema("schema1");
 objectResult.setName("name1");
 objectResult.setType(null);

 objectResult.getSummary();
 }

 @Test
 public void testSetExportPaths() {
 ObjectResult objectResult = new ObjectResult();
 List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export1"), new URL("http://localhost:8080/export2"));
 objectResult.setExportPaths(exportPaths);
 List<URL> actualExportPaths = objectResult.getExportPaths();
 Assert.assertEquals(exportPaths.size(), actualExportPaths.size());
 for (int i = 0; i < exportPaths.size(); i++) {
 URL expectedURL = exportPaths.get(i);
 URL actualURL = actualExportPaths.get(i);
 Assert.assertEquals(expectedURL.toString(), actualURL.toString());
 }
 }

 @Test
 public void testSetExportPathsWhenNull() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setExportPaths(null);
 Assert.assertNull(objectResult.getExportPaths());
 }

}