import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultConstructor() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory(pp);
        JsonGenerator g = factory.createGenerator(new StringWriter());
        g.writeStartObject();
        g.writeRaw("name");
        g.writeObjectFieldValueSeparator();
        g.writeString("John Doe");
        g.writeEndObject();
        g.close();

        ObjectMapper mapper = new ObjectMapper(pp);
        String json = mapper.writeValueAsString(new Person("John Doe"));
        assertEquals("{\n" +
                "  \"name\":\n" +
                "  \"John Doe\"\n" +
                "}", json);
    }

    @Test
    public void testCustomConstructor() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(new SerializedString(", "));
        JsonFactory factory = new JsonFactory(pp);
        JsonGenerator g = factory.createGenerator(new StringWriter());
        g.writeStartObject();
        g.writeRaw("name");
        g.writeObjectFieldValueSeparator();
        g.writeString("John Doe");
        g.writeEndObject();
        g.close();

        ObjectMapper mapper = new ObjectMapper(pp);
        String json = mapper.writeValueAsString(new Person("John Doe"));
        assertEquals("{\n" +
                "  \"name\":,\n" +
                "  \"John Doe\"\n" +
                "}", json);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNestingLevel() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp._nesting = -1;
        JsonFactory factory = new JsonFactory(pp);
        JsonGenerator g = factory.createGenerator(new StringWriter());
        g.writeStartObject();
        g.writeEndObject();
        g.close();
    }

    // Add test cases for other methods, using appropriate inputs and expected outputs
}

class Person {
    private final String name;

    public Person(String name) {
        this.name = name;
    }

    public String toString() {
        return "{\"name\":\"" + name + "\"}";
    }
}