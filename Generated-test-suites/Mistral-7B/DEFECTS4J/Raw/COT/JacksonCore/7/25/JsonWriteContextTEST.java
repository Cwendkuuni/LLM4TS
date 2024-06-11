import com.fasterxml.jackson.core.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext createContext(int type, JsonWriteContext parent, DupDetector dups) {
 return new JsonWriteContext(type, parent, dups);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 assertNull(context.getParent());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
 assertEquals(dupDetector, context.getDupDetector());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertNotNull(childContext);
 assertEquals(parentContext, childContext.getParent());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 assertNotNull(childContext);
 assertEquals(parentContext, childContext.getParent());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithoutGotName() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 assertTrue(context.isGotName());
 }

 @Test
 public void testWriteValueAsRootContext() throws JsonProcessingException {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.getStatus());
 }

 @Test
 public void testWriteValueAsArrayContext() throws JsonProcessingException {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 childContext.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, childContext.getStatus());
 }

 @Test
 public void testWriteValueAsObjectContext() throws JsonProcessingException {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 childContext.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, childContext.getStatus());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValueTwiceInObjectContext() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeValue();
 context.writeValue();
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameTwiceInObjectContext() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name1");
 context.writeFieldName("name1");
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithNullName() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName(null);
 }

 @Test
 public void testGetParent() {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals(parentContext, childContext.getParent());
 }

 @Test
 public void testGetCurrentName() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 assertEquals("name", context.getCurrentName());
 }

 @Test
 public void testGetDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
 assertEquals(dupDetector, context.getDupDetector());
 }

}