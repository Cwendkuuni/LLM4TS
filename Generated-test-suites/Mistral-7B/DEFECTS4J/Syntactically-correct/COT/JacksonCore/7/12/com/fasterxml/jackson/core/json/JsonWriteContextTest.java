import com.fasterxml.jackson.core.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext createTestContext() {
 return new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 }

 @Test
 public void testWriteFieldName_NullName() {
 JsonWriteContext context = createTestContext();
 try {
 context.writeFieldName(null);
 fail("Should have thrown JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testWriteFieldName_ValidName() {
 JsonWriteContext context = createTestContext();
 String name = "testName";
 int result = context.writeFieldName(name);
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 }

 @Test
 public void testWriteFieldName_DupDetectorPresent_DupName() {
 JsonWriteContext context = createTestContext();
 DupDetector dupDetector = new DupDetector();
 context.withDupDetector(dupDetector);
 String name = "testName";
 dupDetector.markDuplicate(name);
 try {
 context.writeFieldName(name);
 fail("Should have thrown JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testWriteValue_RootContext() {
 JsonWriteContext context = createTestContext();
 context.reset(JsonWriteContext.TYPE_ROOT);
 int result = context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
 }

 @Test
 public void testWriteValue_ObjectContext() {
 JsonWriteContext context = createTestContext();
 int result = context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 }

 @Test
 public void testWriteValue_ArrayContext() {
 JsonWriteContext context = createTestContext();
 context.createChildArrayContext();
 int result = context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
 }

 @Test
 public void testWriteValue_ArrayContext_IndexOutOfBounds() {
 JsonWriteContext context = createTestContext();
 context.createChildArrayContext();
 context.writeIndex(Integer.MAX_VALUE);
 int result = context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
 }
}