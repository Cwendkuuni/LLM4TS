import org.junit.Assert;import org.junit.Test;import java.util.Arrays;import java.util.Collections;import java.util.List;import static org.mockito.Mockito.mock;import static org.mockito.Mockito.when;

public class ObjectResultTest {

 // Test case for constructor with three arguments
 @Test
 public void testObjectResultConstruction() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 Assert.assertEquals("schema1", objectResult.getSchema());
 Assert.assertEquals("name1", objectResult.getName());
 Assert.assertEquals("type1", objectResult.getType());
 }

 // Test case for of method
 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = mock(ObjectStatus.class);
 when(objectStatus.getType()).thenReturn("type1");
 when(objectStatus.getStatus()).thenReturn(ObjectStatus.Status.SUCCESS);
 when(objectStatus.getName()).thenReturn("name1");
 when(objectStatus.getSchema()).thenReturn("schema1");
 when(objectStatus.getCount()).thenReturn(new AtomicLong(10));
 when(objectStatus.getTotal()).thenReturn(new AtomicLong(20));

 ObjectResult objectResult = ObjectResult.of(objectStatus);
 Assert.assertEquals("schema1", objectResult.getSchema());
 Assert.assertEquals("name1", objectResult.getName());
 Assert.assertEquals("type1", objectResult.getType());
 Assert.assertEquals(10, objectResult.getCount().get());
 Assert.assertEquals(20, objectResult.getTotal().get());
 }

 // Test case for getSummary method with normal scenario
 @Test
 public void testGetSummaryNormalScenario() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
 String expectedSummary = "schema1.name1[type1]";
 String actualSummary = objectResult.getSummary();
 Assert.assertEquals(expectedSummary, actualSummary);
 }

 // Test case for getSummary method with null schema
 @Test(expected = NullPointerException.class)
 public void testGetSummaryNullSchema() {
 ObjectResult objectResult = new ObjectResult(null, "name1", "type1");
 objectResult.getSummary();
 }

 // Test case for getSummary method with null name
 @Test(expected = NullPointerException.class)
 public void testGetSummaryNullName() {
 ObjectResult objectResult = new ObjectResult("schema1", null, "type1");
 objectResult.getSummary();
 }

 // Test case for getSummary method with null type
 @Test(expected = NullPointerException.class)
 public void testGetSummaryNullType() {
 ObjectResult objectResult = new ObjectResult("schema1", "name1", null);
 objectResult.getSummary();
 }

 // Test case for exportPaths getter with null list
 @Test
 public void testExportPathsGetterNullList() {
 ObjectResult objectResult = new ObjectResult();
 List<URL> expectedExportPaths = null;
 List<URL> actualExportPaths = objectResult.getExportPaths();
 Assert.assertNull(actualExportPaths);
 Assert.assertArrayEquals(new URL[0], expectedExportPaths.toArray());
 }

 // Test case for exportPaths getter with empty list
 @Test
 public void testExportPathsGetterEmptyList() {
 ObjectResult objectResult = new ObjectResult();
 List<URL> expectedExportPaths = Collections.emptyList();
 List<URL> actualExportPaths = objectResult.getExportPaths();
 Assert.assertEquals(expectedExportPaths, actualExportPaths);
 }

 // Test case for exportPaths getter with non-empty list
 @Test
 public void testExportPathsGetterNonEmptyList() {
 ObjectResult objectResult = new ObjectResult();
 URL url1 = new URL("url1");
 URL url2 = new URL("url2");
 List<URL> expectedExportPaths = Arrays.asList(url1, url2);
 List<URL> actualExportPaths = objectResult.getExportPaths();
 objectResult.setExportPaths(expectedExportPaths);
 Assert.assertEquals(expectedExportPaths, actualExportPaths);
 }
}