import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            int status = context.writeFieldName("fieldName");
            assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        } catch (JsonProcessingException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext childArrayContext = parent.createChildArrayContext();
        assertNotNull(childArrayContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext childObjectContext = parent.createChildObjectContext();
        assertNotNull(childObjectContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
    }

}