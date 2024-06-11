import com.fasterxml.jackson.core.*;
import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {

 private JsonWriteContext createTestContext() {
 return JsonWriteContext.createRootContext(new NoOpDupDetector());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 JsonWriteContext ctxt = createTestContext();
 String name = "name1";
 int result = ctxt.writeFieldName(name);
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);

 name = "name2";
 result = ctxt.writeFieldName(name);
 Assert.assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithDuplicate() throws JsonProcessingException {
 JsonWriteContext ctxt = createTestContext();
 DupDetector dd = new DupDetector();
 String name = "name";
 ctxt = ctxt.withDupDetector(dd);
 ctxt.writeFieldName("name");
 ctxt.writeFieldName("name");
 }

 @Test
 public void testWriteValueInRootContext() throws JsonProcessingException {
 JsonWriteContext ctxt = createTestContext();
 int result = ctxt.writeValue();
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
 }

 @Test
 public void testWriteValueInArrayContext() throws JsonProcessingException {
 JsonWriteContext parent = createTestContext();
 JsonWriteContext child = parent.createChildArrayContext();
 int result = child.writeValue();
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
 }

 @Test
 public void testWriteValueInObjectContext() throws JsonProcessingException {
 JsonWriteContext parent = createTestContext();
 JsonWriteContext child = parent.createChildObjectContext();
 child.writeFieldName("name1");
 int result = child.writeValue();
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 }

 @Test
 public void testGetParent() {
 JsonWriteContext parent = createTestContext();
 JsonWriteContext child = parent.createChildArrayContext();
 Assert.assertNotNull(child.getParent());
 Assert.assertEquals(parent, child.getParent());
 }

 @Test
 public void testGetCurrentName() {
 JsonWriteContext ctxt = createTestContext();
 ctxt.writeFieldName("name1");
 Assert.assertEquals("name1", ctxt.getCurrentName());
 }

 @Test
 public void testGetDupDetector() {
 JsonWriteContext ctxt = createTestContext();
 Assert.assertNull(ctxt.getDupDetector());
 }
}

class NoOpDupDetector implements DupDetector {
 @Override public boolean isDup(String name) {
 return false;
 }

 @Override public DupDetector child() {
 return this;
 }

 @Override public void reset() {
 // no-op
 }
}