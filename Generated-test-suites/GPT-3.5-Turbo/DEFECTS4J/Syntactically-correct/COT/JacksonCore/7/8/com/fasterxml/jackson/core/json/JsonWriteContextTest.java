
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerationException;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() throws JsonGenerationException {
        JsonWriteContext writeContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = writeContext.writeFieldName("name");
        
        assert(result == JsonWriteContext.STATUS_OK_AFTER_COLON);
        assert(writeContext.getCurrentName() == "name");
    }

    @Test
    public void testWriteFieldNameWithDup() throws JsonGenerationException {
        DupDetector dupDetector = new DupDetector(null);
        JsonWriteContext writeContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        int result1 = writeContext.writeFieldName("name");
        int result2 = writeContext.writeFieldName("name");
        
        assert(result1 == JsonWriteContext.STATUS_OK_AFTER_COLON);
        assert(result2 == JsonWriteContext.STATUS_OK_AFTER_COLON); // Duplicate field should not throw an exception
    }

    @Test
    public void testWriteValueForObject() {
        JsonWriteContext writeContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result1 = writeContext.writeValue();
        int result2 = writeContext.writeValue();
        
        assert(result1 == JsonWriteContext.STATUS_OK_AFTER_COLON);
        assert(result2 == JsonWriteContext.STATUS_OK_AFTER_COLON);
    }

    @Test
    public void testWriteValueForArray() {
        JsonWriteContext writeContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        int result1 = writeContext.writeValue();
        int result2 = writeContext.writeValue();
        
        assert(result1 == JsonWriteContext.STATUS_OK_AFTER_COMMA);
        assert(result2 == JsonWriteContext.STATUS_OK_AFTER_COMMA);
    }

    @Test
    public void testWriteValueForRoot() {
        JsonWriteContext writeContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        int result1 = writeContext.writeValue();
        int result2 = writeContext.writeValue();
        
        assert(result1 == JsonWriteContext.STATUS_OK_AS_IS);
        assert(result2 == JsonWriteContext.STATUS_OK_AFTER_SPACE);
    }
}
