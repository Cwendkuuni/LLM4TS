import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {
  
  @Test
  public void testGetCurrentValue() {
    JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
    // Case 1: Current value is null
    Assert.assertNull(jsonWriteContext.getCurrentValue());
    
    // Case 2: Current value is not null
    Object value = "test";
    jsonWriteContext.setCurrentValue(value);
    Assert.assertEquals(value, jsonWriteContext.getCurrentValue());
  }
  
  @Test
  public void testCreateRootContext() {
    // Case 1: Without duplicate detector
    JsonWriteContext rootContext1 = JsonWriteContext.createRootContext();
    Assert.assertEquals(JsonWriteContext.TYPE_ROOT, rootContext1.getType());
    Assert.assertNull(rootContext1.getParent());
    Assert.assertNull(rootContext1.getDupDetector());
    
    // Case 2: With duplicate detector
    DupDetector dupDetector = new DupDetector(null);
    JsonWriteContext rootContext2 = JsonWriteContext.createRootContext(dupDetector);
    Assert.assertEquals(JsonWriteContext.TYPE_ROOT, rootContext2.getType());
    Assert.assertNull(rootContext2.getParent());
    Assert.assertEquals(dupDetector, rootContext2.getDupDetector());
  }
  
  @Test
  public void testCreateChildArrayContext() {
    JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
    
    // Case 1: Child context is null
    JsonWriteContext childContext1 = parentContext.createChildArrayContext();
    Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, childContext1.getType());
    Assert.assertEquals(parentContext, childContext1.getParent());
    Assert.assertNull(childContext1.getDupDetector());
    
    // Case 2: Child context is not null
    JsonWriteContext childContext2 = parentContext.createChildArrayContext();
    Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, childContext2.getType());
    Assert.assertEquals(parentContext, childContext2.getParent());
    Assert.assertNull(childContext2.getDupDetector());
  }
  
  @Test
  public void testCreateChildObjectContext() {
    JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
    
    // Case 1: Child context is null
    JsonWriteContext childContext1 = parentContext.createChildObjectContext();
    Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, childContext1.getType());
    Assert.assertEquals(parentContext, childContext1.getParent());
    Assert.assertNull(childContext1.getDupDetector());
    
    // Case 2: Child context is not null
    JsonWriteContext childContext2 = parentContext.createChildObjectContext();
    Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, childContext2.getType());
    Assert.assertEquals(parentContext, childContext2.getParent());
    Assert.assertNull(childContext2.getDupDetector());
  }
  
  @Test
  public void testWriteFieldName() throws JsonProcessingException {
    JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
    String name = "field";
    
    // Case 1: Got name is false
    context._gotName = false;
    int status1 = context.writeFieldName(name);
    Assert.assertTrue(context._gotName);
    Assert.assertEquals(name, context._currentName);
    Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status1);
    
    // Case 2: Got name is true
    context._gotName = true;
    int status2 = context.writeFieldName(name);
    Assert.assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, status2);
  }
  
  @Test
  public void testWriteValue() {
    // Case 1: Type is JsonWriteContext.TYPE_OBJECT
    JsonWriteContext context1 = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
    int status1 = context1.writeValue();
    Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status1);
    Assert.assertTrue(context1._gotName);
    Assert.assertEquals(0, context1._index);
    
    // Case 2: Type is JsonWriteContext.TYPE_ARRAY
    JsonWriteContext context2 = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
    int status2 = context2.writeValue();
    Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status2);
    Assert.assertEquals(-1, context2._index);
    
    // Case 3: Type is neither JsonWriteContext.TYPE_OBJECT nor JsonWriteContext.TYPE_ARRAY
    JsonWriteContext context3 = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
    int status3 = context3.writeValue();
    Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, status3);
    Assert.assertEquals(0, context3._index);
  }
  
  @Test
  public void testToString() {
    // Case 1: Type is JsonWriteContext.TYPE_OBJECT
    JsonWriteContext context1 = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
    context1._currentName = "field";
    String expected1 = "{\"field\"}";
    Assert.assertEquals(expected1, context1.toString());
    
    // Case 2: Type is JsonWriteContext.TYPE_ARRAY
    JsonWriteContext context2 = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
    context2._index = 10;
    String expected2 = "[10]";
    Assert.assertEquals(expected2, context2.toString());
    
    // Case 3: Type is neither JsonWriteContext.TYPE_OBJECT nor JsonWriteContext.TYPE_ARRAY
    JsonWriteContext context3 = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
    context3._index = 0;
    String expected3 = "/";
    Assert.assertEquals(expected3, context3.toString());
  }
}