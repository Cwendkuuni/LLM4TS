import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out, prettyPrinter);

        // Test default root value separator
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", "John Doe");
        jsonGenerator.writeEndObject();

        JsonParser jsonParser = jsonFactory.createParser(System.in);
        String json = readAllLinesFromStandardInput(jsonParser);
        assertEquals("{\n" +
                "  \"name\": \"John Doe\"\n" +
                "}", json);

        // Test with custom root value separator
        prettyPrinter = new DefaultPrettyPrinter(":");
        jsonGenerator = jsonFactory.createGenerator(System.out, prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", "John Doe");
        jsonGenerator.writeEndObject();

        jsonParser = jsonFactory.createParser(System.in);
        json = readAllLinesFromStandardInput(jsonParser);
        assertEquals("{\n" +
                "  :name: \"John Doe\"\n" +
                "}", json);

        // Test indenting arrays
        prettyPrinter = new DefaultPrettyPrinter();
        jsonGenerator = jsonFactory.createGenerator(System.out, prettyPrinter);
        jsonGenerator.writeStartArray();
        jsonGenerator.writeValue("apple");
        jsonGenerator.writeValue("banana");
        jsonGenerator.writeEndArray();

        jsonParser = jsonFactory.createParser(System.in);
        json = readAllLinesFromStandardInput(jsonParser);
        assertEquals("[\"apple\",\"banana\"]", json);

        // Test indenting objects
        prettyPrinter = new DefaultPrettyPrinter();
        jsonGenerator = jsonFactory.createGenerator(System.out, prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldStart("person");
        jsonGenerator.writeStringField("name", "John Doe");
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();

        jsonParser = jsonFactory.createParser(System.in);
        json = readAllLinesFromStandardInput(jsonParser);
        assertEquals("{\n" +
                "  \"person\": {\n" +
                "    \"name\": \"John Doe\"\n" +
                "  }\n" +
                "}", json);

        // Test spaces in object entries
        prettyPrinter = new DefaultPrettyPrinter();
        jsonGenerator = jsonFactory.createGenerator(System.out, prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldStart("person");
        jsonGenerator.writeStringField("name", "John Doe");
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();

        jsonParser = jsonFactory.createParser(System.in);
        json = readAllLinesFromStandardInput(jsonParser);
        assertEquals("{\n" +
                "  \"person\": {\n" +
                "    \"name\": \"John Doe\"\n" +
                "  }\n" +
                "}", json);

        prettyPrinter = new DefaultPrettyPrinter(DefaultPrettyPrinter.Feature.SPACE_IN_OBJECT_ENTRY_VALUE);
        jsonGenerator = jsonFactory.createGenerator(System.out, prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldStart("person");
        jsonGenerator.writeStringField("name", "John Doe");
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();

        jsonParser = jsonFactory.createParser(System.in);
        json = readAllLinesFromStandardInput(jsonParser);
        assertEquals("{\n" +
                "  \"person\": {\n" +
                "    \"name\": \"John Doe\"\n" +
                "  }\n" +
                "}", json);

        // Test indenting with custom indenters
        prettyPrinter = new DefaultPrettyPrinter();
        jsonGenerator = jsonFactory.createGenerator(System.out, prettyPrinter);
        jsonGenerator.indentArraysWith(new FixedSpaceIndenter());
        jsonGenerator.indentObjectsWith(new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE());
        jsonGenerator.writeStartArray();
        jsonGenerator.writeValue("apple");
        jsonGenerator.writeValue("banana");
        jsonGenerator.writeEndArray();

        jsonParser = jsonFactory.createParser(System.in);
        json = readAllLinesFromStandardInput(jsonParser);
        assertEquals("[\"apple\",\"banana\"]\n", json);

        jsonGenerator.reset();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldStart("person");
        jsonGenerator.writeStringField("name", "John Doe");
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();

        jsonParser = jsonFactory.createParser(System.in);
        json = readAllLinesFromStandardInput(jsonParser);
        assertEquals("{\n" +
                "  \"person\": {\n" +
                "    \"name\": \"John Doe\"\n" +
                "  }\n" +
                "}", json);

        jsonGenerator.close();
    }

    private static String readAllLinesFromStandardInput(JsonParser parser) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String line; (line = parser.nextLine()) != null;) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}