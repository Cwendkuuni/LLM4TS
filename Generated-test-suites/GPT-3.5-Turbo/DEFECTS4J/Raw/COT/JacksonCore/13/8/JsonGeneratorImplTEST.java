
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.writeStringField("fieldName", "value");
        // Check if the field name and value are written correctly
        assertEquals("\"fieldName\":\"value\"", jsonGenerator.toString());
    }
}
