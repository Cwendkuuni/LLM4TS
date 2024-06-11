import com.fasterxml.jackson.core.JsonGenerationException;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("field"));
            assertFalse(context._gotName);
            assertEquals("field", context._currentName);
        } catch (JsonGenerationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWriteFieldNameWithDup() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.withDupDetector(new SimpleDupDetector());
        try {
            context.writeFieldName("field");
            assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("field"));
            fail("Should throw JsonGenerationException for duplicate field");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'field'", e.getMessage());
        }
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        } catch (JsonGenerationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWriteValueInArray() {
        JsonWriteContext context = JsonWriteContext.createRootContext().createChildArrayContext();
        try {
            assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        } catch (JsonGenerationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWriteValueInObject() {
        JsonWriteContext context = JsonWriteContext.createRootContext().createChildObjectContext();
        try {
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
            assertFalse(context._gotName);
        } catch (JsonGenerationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAppendDescForObject() {
        JsonWriteContext context = JsonWriteContext.createRootContext()
                .createChildObjectContext().writeFieldName("field");
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("{\"field\"}", sb.toString());
    }

    @Test
    public void testAppendDescForArray() {
        JsonWriteContext context = JsonWriteContext.createRootContext().createChildArrayContext();
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("[0]", sb.toString());
    }

    @Test
    public void testAppendDescForRoot() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("/", sb.toString());
    }

    @Test
    public void testToStringForObject() {
        JsonWriteContext context = JsonWriteContext.createRootContext()
                .createChildObjectContext().writeFieldName("field");
        assertEquals("{\"field\"}", context.toString());
    }

    @Test
    public void testToStringForArray() {
        JsonWriteContext context = JsonWriteContext.createRootContext().createChildArrayContext();
        assertEquals("[0]", context.toString());
    }

    @Test
    public void testToStringForRoot() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals("/", context.toString());
    }

    private static class SimpleDupDetector extends DupDetector {
        private boolean isDup = false;

        @Override
        public boolean isDup(String str) {
            return isDup;
        }

        @Override
        public void reset() {
            //nothing to do
        }

        @Override
        public DupDetector child() {
            return null;
        }
    }

}