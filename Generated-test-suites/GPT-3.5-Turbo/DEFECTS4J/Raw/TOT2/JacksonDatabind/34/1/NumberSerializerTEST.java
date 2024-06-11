import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        ObjectMapper mapper = new ObjectMapper();
        BigDecimal input = new BigDecimal("123.456");
        String expected = "123.456";

        String actual = serializeAndReturnAsString(serializer, mapper, input);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        ObjectMapper mapper = new ObjectMapper();
        BigInteger input = new BigInteger("12345678901234567890");
        String expected = "12345678901234567890";

        String actual = serializeAndReturnAsString(serializer, mapper, input);

        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        ObjectMapper mapper = new ObjectMapper();
        Integer input = 123;
        String expected = "123";

        String actual = serializeAndReturnAsString(serializer, mapper, input);

        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        ObjectMapper mapper = new ObjectMapper();
        Long input = 1234567890123456789L;
        String expected = "1234567890123456789";

        String actual = serializeAndReturnAsString(serializer, mapper, input);

        assertEquals(expected, actual);
    }

    // Add more test cases for other methods as needed

    private String serializeAndReturnAsString(NumberSerializer serializer, ObjectMapper mapper, Number input) throws IOException {
        StringBuilder sb = new StringBuilder();
        serializer.serialize(input, mapper.getFactory().createGenerator(sb), mapper.getSerializerProvider());
        return sb.toString();
    }
}