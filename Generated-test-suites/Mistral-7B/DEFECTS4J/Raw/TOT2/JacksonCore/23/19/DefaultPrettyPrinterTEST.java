import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException {
        JsonFactory factory = new JsonFactory();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = factory.createGenerator(System.out, prettyPrinter);
        generator.writeStartObject();
        generator.writeFieldName("name");
        generator.writeValue("John Doe");
        generator.writeFieldName("age");
        generator.writeNumber(25);
        generator.writeEndObject();
        generator.close();
        // Check the output
        String expected = "{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 25\n" +
                "}";
        String actual = System.out.getBytes("UTF-8").toString();
        assertEquals(expected, actual);
    }

    // Add test cases for other methods here
}