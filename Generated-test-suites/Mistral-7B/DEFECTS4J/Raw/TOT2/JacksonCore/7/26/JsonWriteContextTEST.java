import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;

public class JsonWriteContextTest {

    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext ctxt = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int result = ctxt.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
    }
}