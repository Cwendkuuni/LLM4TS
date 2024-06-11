import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonStreamContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertNull(context.getCurrentName());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildArrayContext();
        assertEquals(JsonStreamContext.TYPE_ARRAY, context.getType());
        assertEquals(parent, context.getParent());
        assertNull(context.getCurrentName());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildObjectContext();
        assertEquals(JsonStreamContext.TYPE_OBJECT, context.getType());
        assertEquals(parent, context.getParent());
        assertNull(context.getCurrentName());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals("name", context.getCurrentName());
        status = context.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, status);
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals(0, context.getCurrentIndex());
        context = context.createChildArrayContext();
        status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        context = JsonWriteContext.createRootContext();
        context.writeValue();
        status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, status);
    }

    @Test
    public void testToString() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals("/", context.toString());
        context = context.createChildArrayContext();
        assertEquals("[0]", context.toString());
        context = context.createChildObjectContext();
        assertEquals("[0]/{}", context.toString());
        context.writeFieldName("name");
        assertEquals("[0]/{\"name\"}", context.toString());
        context.writeFieldName("name");
        assertEquals("[0]/{\"name\"}", context.toString());
    }
}