import com.fasterxml.jackson.core.*;
import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {

 private JsonWriteContext createContext(int type, JsonWriteContext parent, DupDetector dups) {
 return new JsonWriteContext(type, parent, dups);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 Assert.assertNull(context.getParent());
 Assert.assertNull(context.getCurrentName());
 Assert.assertNull(context.getDupDetector());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
 Assert.assertEquals(dupDetector, context.getDupDetector());
 Assert.assertNull(context.getParent());
 Assert.assertNull(context.getCurrentName());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 Assert.assertEquals(parentContext, childContext.getParent());
 Assert.assertNull(childContext.getCurrentName());
 Assert.assertNull(childContext.getDupDetector());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 Assert.assertEquals(parentContext, childContext.getParent());
 Assert.assertNull(childContext.getCurrentName());
 Assert.assertNull(childContext.getDupDetector());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithGotName() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.writeFieldName("age");
 context.writeValue(); // reset _gotName
 context.writeFieldName("name"); // should throw an exception
 }

 @Test
 public void testWriteValueAsRootContext() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 context.writeValue();
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.getStatus());
 }

 @Test
 public void testWriteValueAsArrayContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 childContext.writeValue();
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, childContext.getStatus());
 }

 @Test
 public void testWriteValueAsArrayContextWithFirstElement() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 childContext.writeValue(); // first element
 childContext.writeValue(); // second element
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, childContext.getStatus());
 }

 @Test
 public void testWriteValueAsObjectContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 childContext.writeFieldName("name");
 childContext.writeValue("John");
 childContext.writeValue(); // second value
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, childContext.getStatus());
 }

 @Test
 public void testWriteValueAsObjectContextWithFirstName() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 childContext.writeFieldName("name");
 childContext.writeValue("John");
 childContext.writeValue("Doe"); // second value
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, childContext.getStatus());
 }

}