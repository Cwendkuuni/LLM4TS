import org.junit.Assert;import org.junit.Test;import static org.junit.Assert.*;import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;import static com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus.*;

public class ObjectResultTest {

 private ObjectResult objectResult;

 @Before
 public void setUp() {
 objectResult = new ObjectResult("schema", "name", "type");
 }

 @Test
 public void testObjectResultConstructor() {
 ObjectResult result = new ObjectResult("schema", "name", "type");
 assertNotNull(result);
 assertEquals("name", result.getName());
 assertEquals("type", result.getType());
 assertEquals("schema", result.getSchema());
 assertEquals(ObjectStatus.OK, result.getStatus());
 assertEquals(new AtomicLong(0), result.getCount());
 assertEquals(new AtomicLong(0), result.getTotal());
 }

 @Test
 public void testOf() {
 ObjectStatus objectStatus = new ObjectStatus();
 objectStatus.setName("name");
 objectStatus.setType("type");
 ObjectStatus that = new ObjectStatus();
 that.setName("name");
 that.setType("type");
 ObjectStatus expected = new ObjectStatus();
 expected.setName("name");
 expected.setType("type");
 expected.setStatus(ObjectStatus.OK);
 ObjectResult result = ObjectResult.of(objectStatus);
 assertEquals(expected, result);
 }

 @Test
 public void testGetSummary() {
 String expected = "schema.name[type]";
 String result = objectResult.getSummary();
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectResultNullConstructor() {
 ObjectResult result = new ObjectResult(null, null, null);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectResultNullSchema() {
 ObjectResult result = new ObjectResult(null, "name", "type");
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectResultNullName() {
 ObjectResult result = new ObjectResult("schema", null, "type");
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectResultNullType() {
 ObjectResult result = new ObjectResult("schema", "name", null);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testSetType() {
 ObjectResult result = new ObjectResult();
 result.setType("type1");
 result.setType("type2");
 assertEquals("type2", result.getType());
 }

 @Test
 public void testSetStatus() {
 ObjectResult result = new ObjectResult();
 result.setStatus(ERROR);
 result.setStatus(WARNING);
 assertEquals(WARNING, result.getStatus());
 }

 @Test
 public void testSetName() {
 ObjectResult result = new ObjectResult();
 result.setName("name1");
 result.setName("name2");
 assertEquals("name2", result.getName());
 }

 @Test
 public void testSetSchema() {
 ObjectResult result = new ObjectResult();
 result.setSchema("schema1");
 result.setSchema("schema2");
 assertEquals("schema2", result.getSchema());
 }

 @Test
 public void testSetCount() {
 ObjectResult result = new ObjectResult();
 result.setCount(new AtomicLong(1));
 result.setCount(new AtomicLong(2));
 assertEquals(new AtomicLong(2), result.getCount());
 }

 @Test
 public void testSetTotal() {
 ObjectResult result = new ObjectResult();
 result.setTotal(new AtomicLong(1));
 result.setTotal(new AtomicLong(2));
 assertEquals(new AtomicLong(2), result.getTotal());
 }

}