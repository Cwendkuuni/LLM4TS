import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;

public class JsonWriteContextTest {
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        String name1 = "field1";
        String name2 = "field2";

        int result1 = context.writeFieldName(name1);
        int result2 = context.writeFieldName(name2);

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result1);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result2);
    }
}