import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonWriteContextTest {

    private static JsonWriteContext context;

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("field1"));
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeFieldName("field2"));
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("field3"));
    }

    @Test
    public void testWriteValue() {
        context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        context = new JsonWriteContext(JsonStreamContext.TYPE_ARRAY, null, null);
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        context = new JsonWriteContext(JsonStreamContext.TYPE_ROOT, null, null);
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
    }

    @Test
    public void testCreateRootContext() {
        context = JsonWriteContext.createRootContext();
        Assert.assertNotNull(context);
        Assert.assertEquals(JsonStreamContext.TYPE_ROOT, context.getType());
    }

    @Test
    public void testCreateChildArrayContext() {
        context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildArrayContext();
        Assert.assertEquals(JsonStreamContext.TYPE_ARRAY, childContext.getType());
    }

    @Test
    public void testCreateChildObjectContext() {
        context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildObjectContext();
        Assert.assertEquals(JsonStreamContext.TYPE_OBJECT, childContext.getType());
    }

    @Test
    public void testGetParent() {
        JsonWriteContext parent = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        context = new JsonWriteContext(JsonStreamContext.TYPE_ARRAY, parent, null);
        Assert.assertEquals(parent, context.getParent());
    }

    @Test
    public void testGetCurrentName() {
        context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        context.writeFieldName("field1");
        Assert.assertEquals("field1", context.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        Assert.assertNull(context.getDupDetector());
    }
}