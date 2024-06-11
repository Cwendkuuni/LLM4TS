import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNotNull(context);
        assertEquals(JsonWriteContext.TYPE_ROOT, context.type);
        assertNull(context.getParent());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.type);
        assertNotNull(childContext.getParent());
        assertEquals(parentContext, childContext.getParent());
    }

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, context.writeFieldName("field"));
        assertEquals("field", context.getCurrentName());
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext objectContext = JsonWriteContext.createRootContext().createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.writeValue());

        JsonWriteContext arrayContext = JsonWriteContext.createRootContext().createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, arrayContext.writeValue());

        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, rootContext.writeValue());
    }
}