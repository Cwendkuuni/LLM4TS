import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("testField"));
            assertEquals("testField", context.getCurrentName());
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        } catch (JsonProcessingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testWriteValueArray() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            context.createChildArrayContext();
            assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        } catch (JsonProcessingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testWriteValueObject() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            context.createChildObjectContext();
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        } catch (JsonProcessingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testWriteValueRoot() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
        } catch (JsonProcessingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testWriteFieldNameDuplicate() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("testField"));
            assertEquals("testField", context.getCurrentName());
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
            assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("testField"));
            fail("Duplicate field not handled");
        } catch (JsonGenerationException e) {
            // Duplicate field exception should be thrown
            assertTrue(true);
        } catch (JsonProcessingException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testWithDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        DupDetector dd = new DupDetector(null);
        context.withDupDetector(dd);
        assertEquals(dd, context.getDupDetector());
    }

}