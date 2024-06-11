import com.fasterxml.jackson.core.*;
import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {

 private JsonWriteContext createTestContext() {
 return new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 Assert.assertNull(rootContext.getParent());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 JsonWriteContext context = createTestContext();
 context.writeFieldName("fieldName");

 Assert.assertEquals("fieldName", context.getCurrentName());
 Assert.assertTrue(context.isWriterName());
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameTwice() throws JsonProcessingException {
 JsonWriteContext context = createTestContext();
 context.writeFieldName("fieldName");
 context.writeFieldName("fieldName");
 }

 @Test
 public void testWriteValue() {
 JsonWriteContext context = createTestContext();
 context.writeValue();

 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = createTestContext();
 JsonWriteContext childContext = parentContext.createChildArrayContext();

 Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
 Assert.assertEquals(parentContext, childContext.getParent());
 Assert.assertNull(childContext.getCurrentName());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameInArrayContext() throws JsonProcessingException {
 JsonWriteContext context = createTestContext().createChildArrayContext();
 context.writeFieldName("fieldName");
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = createTestContext();
 JsonWriteContext childContext = parentContext.createChildObjectContext();

 Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
 Assert.assertEquals(parentContext, childContext.getParent());
 Assert.assertNull(childContext.getCurrentName());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameInObjectContextWithoutName() throws JsonProcessingException {
 JsonWriteContext context = createTestContext().createChildObjectContext();
 context.writeFieldName(null);
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameEmptyName() throws JsonProcessingException {
 JsonWriteContext context = createTestContext().createChildObjectContext();
 context.writeFieldName("");
 }

 @Test
 public void testToString() {
 JsonWriteContext context = createTestContext();
 Assert.assertEquals("{}", context.toString());
 }
}