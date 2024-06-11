import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerationException;

public class JsonWriteContextTest {
    
    // Create an instance of JsonWriteContext to use for testing
    JsonWriteContext context = JsonWriteContext.createRootContext();
    
    @Test
    public void testWriteFieldName() throws JsonGenerationException {
        String fieldName = "test";
        int result = context.writeFieldName(fieldName);
        assert(result == JsonWriteContext.STATUS_OK_AS_IS);
        assert(context.getCurrentName().equals(fieldName));
    }
    
    @Test
    public void testWriteValueForObject() {
        int result = context.writeValue();
        assert(result == JsonWriteContext.STATUS_OK_AFTER_SPACE);
        assert(context.getType() == JsonWriteContext.TYPE_OBJECT);
    }
    
    @Test
    public void testWriteValueForArray() {
        int result = context.writeValue();
        assert(result == JsonWriteContext.STATUS_OK_AFTER_SPACE);
        assert(context.getType() == JsonWriteContext.TYPE_ARRAY);
    }
    
}