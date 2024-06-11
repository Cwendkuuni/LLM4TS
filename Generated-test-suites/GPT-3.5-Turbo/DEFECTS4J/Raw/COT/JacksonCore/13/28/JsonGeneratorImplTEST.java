
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl();
        String fieldName = "exampleField";
        String value = "exampleValue";
        try {
            jsonGenerator.writeStringField(fieldName, value);
            assertEquals(fieldName, jsonGenerator.getCurrentName());
            assertEquals(value, jsonGenerator.getText());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}
