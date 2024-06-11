import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonGenerator generator = jsonFactory.createGenerator(System.out);

        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.serialize(new BigDecimal("123.45"), generator, mapper.getSerializerProvider());
        String expectedOutput = "123.45";
        String actualOutput = generator.getOutputStr();
        assertEquals(expectedOutput, actualOutput);
    }
}