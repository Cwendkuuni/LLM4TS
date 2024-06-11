import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer integerSerializer = new IntegerSerializer();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        // Test with valid integer value
        Integer value = 123;
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        integerSerializer.serialize(value, gen, serializerProvider);
        gen.flush();
        assertEquals("123", mapper.readTree(System.out.toString()).asText());

        // Test with negative integer value
        value = -123;
        gen = mapper.getFactory().createGenerator(System.out);
        integerSerializer.serialize(value, gen, serializerProvider);
        gen.flush();
        assertEquals("-123", mapper.readTree(System.out.toString()).asText());

        // Test with zero
        value = 0;
        gen = mapper.getFactory().createGenerator(System.out);
        integerSerializer.serialize(value, gen, serializerProvider);
        gen.flush();
        assertEquals("0", mapper.readTree(System.out.toString()).asText());
    }

    // Add test cases for other methods here...
}