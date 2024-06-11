import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameTwice() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("fieldName");
        context.writeFieldName("fieldName");
    }
}