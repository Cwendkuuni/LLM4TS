import com.fasterxml.jackson.core.JsonGenerationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() throws JsonGenerationException {
        JsonWriteContext context = JsonWriteContext.createRootContext();

        // Test writing field name first time
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("fieldName"));
        assertEquals("fieldName", context.getCurrentName());

        // Test writing field name second time
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeFieldName("fieldName2"));
        assertEquals("fieldName2", context.getCurrentName());

        // Test writing field name after value is expected
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("fieldName3"));

        // Test writing field name after colon is expected
        assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, context.writeValue());

        // Test duplicate field name
        try {
            context.writeFieldName("fieldName2");
            fail("Expected JsonGenerationException to be thrown");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'fieldName2'", e.getMessage());
        }
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();

        // Test writing value in object context
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());

        // Test writing value in array context
        context = context.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());

        // Test writing value in root context
        context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
    }
}