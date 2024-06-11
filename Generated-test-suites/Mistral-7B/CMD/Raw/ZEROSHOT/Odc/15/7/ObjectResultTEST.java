import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 private ObjectResult objectResult;
 private ObjectStatus objectStatus;

 @Before
 public void setUp() {
 objectStatus = mock(ObjectStatus.class);
 objectResult = new ObjectResult();
 }

 @Test
 public void testObjectResultConstruction() {
 objectResult = new ObjectResult("testSchema", "testName", "testType");
 assertEquals("testSchema", objectResult.getSchema());
 assertEquals("testName", objectResult.getName());
 assertEquals("testType", objectResult.getType());
 assertEquals(ObjectStatus.UNPROCESSED, objectResult.getStatus());
 assertEquals(0L, objectResult.getCount().get());
 assertEquals(0L, objectResult.getTotal().get());
 }

 @Test
 public void testOfMethod() {
 when(objectStatus.getType()).thenReturn("testType");
 when(objectStatus.getStatus()).thenReturn(ObjectStatus.PROCESSING);
 when(objectStatus.getName()).thenReturn("testName");
 when(objectStatus.getSchema()).thenReturn("testSchema");
 when(objectStatus.getCount()).thenReturn(new AtomicLong(1));
 when(objectStatus.getTotal()).thenReturn(new AtomicLong(10));

 objectResult = ObjectResult.of(objectStatus);
 assertEquals("testSchema", objectResult.getSchema());
 assertEquals("testName", objectResult.getName());
 assertEquals("testType", objectResult.getType());
 assertEquals(ObjectStatus.PROCESSING, objectResult.getStatus());
 assertEquals(1L, objectResult.getCount().get());
 assertEquals(10L, objectResult.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 objectResult.setSchema("testSchema");
 objectResult.setName("testName");
 objectResult.setType("testType");
 assertEquals("testSchema.testName[testType]", objectResult.getSummary());
 }

 @Test
 public void testJsonIgnore() {
 objectResult.setExportPaths(Arrays.asList(new URL("testUrl1"), new URL("testUrl2")));
 assertNotNull(objectResult.getExportPaths());
 assertEquals(2, objectResult.getExportPaths().size());
 }
}