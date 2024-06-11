import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = context.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals("name", context.getCurrentName());
    }

    @Test
    public void testWriteFieldNameDuplicateName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, new DupDetector());
        context.writeFieldName("name1");
        try {
            context.writeFieldName("name1");
            fail("Expected JsonGenerationException to be thrown");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'name1'", e.getMessage());
        }
    }

    @Test
    public void testWriteValueInObject() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertEquals(0, context.getCurrentIndex());
    }

    @Test
    public void testWriteValueInArray() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals(0, context.getCurrentIndex());
    }

    @Test
    public void testWriteValueInRoot() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals(0, context.getCurrentIndex());
    }
}