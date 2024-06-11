import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        // Test default constructor
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeObjectFieldName(jsonGenerator, "field1");
        prettyPrinter.writeObjectFieldValue(jsonGenerator, "value1");
        prettyPrinter.writeObjectFieldName(jsonGenerator, "field2");
        prettyPrinter.writeObjectFieldValue(jsonGenerator, "value2");
        prettyPrinter.writeEndObject(jsonGenerator);

        String json = jsonGenerator.getOutputContent();
        assertEquals("{\"field1\":\"value1\",\"field2\":\"value2\"}", json);

        // Test constructor with root separator
        prettyPrinter = new DefaultPrettyPrinter(":");
        jsonGenerator.setPrettyPrinter(prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldName("field1");
        jsonGenerator.writeObjectFieldValue("value1");
        jsonGenerator.writeObjectFieldName("field2");
        jsonGenerator.writeObjectFieldValue("value2");
        jsonGenerator.writeEndObject();

        json = jsonGenerator.getOutputContent();
        assertEquals("{\"field1\":\"value1\",\"field2\":\"value2\"}", json);
        assertEquals(":\n{\n \"field1\":\"value1\",\n \"field2\":\"value2\"\n}", jsonGenerator.getOutputText());
    }
}