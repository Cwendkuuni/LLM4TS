import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonGeneratorImplTest {
    @Test
    public void testWriteStringField() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        ObjectMapper objectMapper = new ObjectMapper();

        jsonGenerator.writeStringField("fieldName", "expectedValue");
        String json = objectMapper.writeValueAsString(jsonGenerator.getFactory().getRootValue());

        assertTrue(json.contains("\"fieldName\":\"expectedValue\""));

        jsonGenerator.close();
    }
}