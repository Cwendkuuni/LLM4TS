import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializerTest {

    @Test
    public void testSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Number number = 123;
        numberSerializer.serialize(number, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        String expectedOutput = "123";
        String actualOutput = jsonGenerator.getOutputStream().toString();
        assertEquals(expectedOutput, actualOutput);
    }
}