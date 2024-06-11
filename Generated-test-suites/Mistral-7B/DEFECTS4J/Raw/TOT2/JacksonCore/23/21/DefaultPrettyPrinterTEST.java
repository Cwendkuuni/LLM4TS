import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectMapper;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out, prettyPrinter);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(new String[]{"apple", "banana", "orange"});
        jsonGenerator.writeRaw(jsonString);
        jsonGenerator.close();

        JsonParser jsonParser = jsonFactory.createParser(System.in);
        String result = "";
        while (jsonParser.nextValue() != null) {
            result += jsonParser.getValueAsString();
        }

        assertEquals("[\"apple\", \"banana\", \"orange\"]", result);
    }
}