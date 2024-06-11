import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        Number number = 123;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.serialize(number, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        String expected = "123";
        String actual = jsonGenerator.toString();
        assertEquals(expected, actual);
    }
}