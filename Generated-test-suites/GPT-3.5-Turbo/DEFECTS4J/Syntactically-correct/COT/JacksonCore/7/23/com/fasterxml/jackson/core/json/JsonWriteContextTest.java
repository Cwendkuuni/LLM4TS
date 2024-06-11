import org.junit.Test;
import org.junit.Assert;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("field1"));
        Assert.assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("field2"));
    }
    
    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }
    
    @Test
    public void testWithDupDetector() {
        DupDetector dups = new DupDetector(null);
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dups);
        Assert.assertEquals(context, context.withDupDetector(dups));
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parentContext.createChildArrayContext();
        Assert.assertEquals(parentContext, context.getParent());
        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext context = parentContext.createChildObjectContext();
        Assert.assertEquals(parentContext, context.getParent());
        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
    }
    
    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("field1");
        Assert.assertEquals("field1", context.getCurrentName());
    }
    
    @Test
    public void testGetDupDetector() {
        DupDetector dups = new DupDetector(null);
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dups);
        Assert.assertEquals(dups, context.getDupDetector());
    }
}