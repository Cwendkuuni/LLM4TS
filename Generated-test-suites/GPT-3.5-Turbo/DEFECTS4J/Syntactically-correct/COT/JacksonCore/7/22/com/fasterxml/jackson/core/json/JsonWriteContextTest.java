import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.json.JsonWriteContext;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() throws JsonGenerationException {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext();

        // Act
        int result = context.writeFieldName("fieldName");

        // Assert
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        Assert.assertEquals("fieldName", context.getCurrentName());
    }

    @Test
    public void testWriteFieldName_duplicate() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext()
                .withDupDetector(new DummyDupDetector());

        // Act

        // Assert
        Assert.assertThrows(JsonGenerationException.class, () -> context.writeFieldName("fieldName"));
    }

    @Test
    public void testWriteValue_objectContext() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext();

        // Act
        int result = context.writeValue();

        // Assert
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
    }

    @Test
    public void testWriteValue_arrayContext() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext().createChildArrayContext();

        // Act
        int result = context.writeValue();

        // Assert
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    @Test
    public void testWriteValue_rootContext() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext();

        // Act
        int result = context.writeValue();

        // Assert
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }

    private class DummyDupDetector implements DupDetector {
        @Override
        public void reset() {
        }

        @Override
        public void child() {
        }

        @Override
        public boolean isDup(String name) {
            return true;
        }
    }

}