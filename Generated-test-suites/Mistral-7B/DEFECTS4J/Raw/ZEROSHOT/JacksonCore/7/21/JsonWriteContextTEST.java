import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.util.DupDetector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext arrayContext;
 private JsonWriteContext objectContext;
 private JsonWriteContext objectContextWithName;
 private DupDetector dupDetector;

 @Before
 public void setup() {
 dupDetector = new DupDetector();
 rootContext = JsonWriteContext.createRootContext(dupDetector);
 arrayContext = rootContext.createChildArrayContext();
 objectContext = rootContext.createChildObjectContext();
 objectContextWithName = objectContext.reset(JsonWriteContext.TYPE_OBJECT);
 }

 @Test
 public void testWriteValue_RootContext() {
 JsonWriteContext context = rootContext;
 assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
 }

 @Test
 public void testWriteValue_ArrayContext() {
 JsonWriteContext context = arrayContext;
 context.writeFieldName("field1");
 context.writeValue();
 context.writeValue();
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValue_ArrayContext_DuplicateName() {
 JsonWriteContext context = arrayContext;
 context.writeFieldName("field1");
 context.writeValue();
 context.writeFieldName("field1");
 context.writeValue();
 }

 @Test
 public void testWriteValue_ObjectContext() {
 JsonWriteContext context = objectContext;
 context.writeFieldName("field1");
 context.writeValue("value1");
 context.writeValue("value2");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValue_ObjectContext_DuplicateName() {
 JsonWriteContext context = objectContext;
 context.writeFieldName("field1");
 context.writeValue("value1");
 context.writeFieldName("field1");
 context.writeValue("value2");
 }

 @Test
 public void testWriteValue_ObjectContextWithName() {
 JsonWriteContext context = objectContextWithName;
 context.writeFieldName("field1");
 context.writeValue("value1");
 context.writeValue("value2");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
 }

 @Test
 public void testGetParent() {
 JsonWriteContext arrayContext = rootContext.createChildArrayContext();
 JsonWriteContext objectContext = arrayContext.createChildObjectContext();
 assertEquals(arrayContext, objectContext.getParent());
 }

 @Test
 public void testGetCurrentName() {
 JsonWriteContext objectContextWithName = rootContext.createChildObjectContext();
 objectContextWithName.writeFieldName("field1");
 assertEquals("field1", objectContextWithName.getCurrentName());
 }

 @Test
 public void testGetDupDetector() {
 JsonWriteContext objectContextWithDupDetector = rootContext.createChildObjectContext();
 objectContextWithDupDetector.withDupDetector(dupDetector);
 assertEquals(dupDetector, objectContextWithDupDetector.getDupDetector());
 }
}