import com.fasterxml.jackson.core.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext createTestContext() {
 return new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 assertNull(rootContext.getParent());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = createTestContext();
 JsonWriteContext arrayContext = parentContext.createChildArrayContext();
 assertEquals(parentContext, arrayContext.getParent());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = createTestContext();
 JsonWriteContext objectContext = parentContext.createChildObjectContext();
 assertEquals(parentContext, objectContext.getParent());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 JsonWriteContext context = createTestContext();
 context.writeFieldName("testField");
 assertEquals("testField", context.getCurrentName());
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameTwice() throws JsonProcessingException {
 JsonWriteContext context = createTestContext();
 context.writeFieldName("testField");
 context.writeFieldName("testField");
 }

 @Test
 public void testWriteValue() {
 JsonWriteContext context = createTestContext();
 context.writeValue();
 assertEquals(0, context.getCurrentIndex());
 }

 @Test
 public void testWriteValueWithIndex() {
 JsonWriteContext context = createTestContext();
 context.createChildArrayContext();
 context.writeValue();
 assertEquals(0, context.getCurrentIndex());
 context.writeValue();
 assertEquals(1, context.getCurrentIndex());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValueWithoutFieldName() {
 JsonWriteContext context = createTestContext();
 context.writeValue();
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValueTwice() {
 JsonWriteContext context = createTestContext();
 context.writeValue();
 context.writeValue();
 }

 @Test
 public void testToString() {
 JsonWriteContext context = createTestContext();
 context.writeFieldName("testField");
 context.writeValue();
 assertEquals("{\"testField\"}", context.toString());
 }

}