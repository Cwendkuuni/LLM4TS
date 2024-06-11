import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeFieldName("test");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDuplicateField() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("test");
        context.writeFieldName("test");
    }

    @Test
    public void testParent() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertEquals(parentContext, childContext.getParent());
    }

    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("test");
        assertEquals("test", context.getCurrentName());
    }
}