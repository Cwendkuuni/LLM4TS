import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        serializer.serialize(new BigDecimal("123.45"), gen, mapper.getSerializerProvider());
        String expectedJson = "123.45";
        String actualJson = gen.getOutputContent();
        assertEquals(expectedJson, actualJson);
    }
}