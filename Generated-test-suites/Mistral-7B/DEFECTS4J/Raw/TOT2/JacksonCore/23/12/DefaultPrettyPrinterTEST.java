import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeObjectFieldName("name");
        prettyPrinter.writeObjectFieldValue("John Doe");
        prettyPrinter.writeObjectFieldName("age");
        prettyPrinter.writeObjectFieldValue(30);
        prettyPrinter.writeEndObject(jsonGenerator, 2);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new Object() {{
            String name = "John Doe";
            int age = 30;
        }});

        String expected = "{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30\n" +
                "}";

        assertEquals(expected, json);

        jsonGenerator.close();
    }
}