import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.DefaultSerializableString;

public class JsonGeneratorImplTest {

    @Test
    public void testDefaultConstructor() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        assertNotNull(jsonGenerator);
    }

    @Test
    public void testWriteFieldNameAndValue() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(System.out);
        String fieldName = "testField";
        String fieldValue = "testValue";

        jsonGenerator.writeStringField(fieldName, fieldValue);

        assertTrue(System.out.toString().contains(String.format("\"%s\":\"%s\"", fieldName, fieldValue)));
    }

    @Test
    public void testSetRootValueSeparator() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(System.out);
        SerializableString rootValueSeparator = new DefaultSerializableString(",");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        String fieldName = "testField";
        String fieldValue = "testValue";

        jsonGenerator.writeStringField(fieldName, fieldValue);

        assertTrue(System.out.toString().contains(String.format("\"%s\":\"%s\"%,", fieldName, fieldValue)));
    }
}