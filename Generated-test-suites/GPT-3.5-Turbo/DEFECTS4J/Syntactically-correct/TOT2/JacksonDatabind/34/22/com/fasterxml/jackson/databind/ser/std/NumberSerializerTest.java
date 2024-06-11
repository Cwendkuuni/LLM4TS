import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer = new NumberSerializer(Number.class);
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeBigDecimal() {
        BigDecimal input = new BigDecimal("123.456");
        String expected = "123.456";
        assertEquals(expected, serializeNumber(input));
    }

    @Test
    public void testSerializeBigInteger() {
        BigInteger input = new BigInteger("123456");
        String expected = "123456";
        assertEquals(expected, serializeNumber(input));
    }

    @Test
    public void testSerializeInteger() {
        Integer input = 123;
        String expected = "123";
        assertEquals(expected, serializeNumber(input));
    }

    @Test
    public void testSerializeLong() {
        Long input = 123L;
        String expected = "123";
        assertEquals(expected, serializeNumber(input));
    }

    @Test
    public void testSerializeDouble() {
        Double input = 123.456;
        String expected = "123.456";
        assertEquals(expected, serializeNumber(input));
    }

    @Test
    public void testSerializeFloat() {
        Float input = 123.456f;
        String expected = "123.456";
        assertEquals(expected, serializeNumber(input));
    }

    @Test
    public void testSerializeByte() {
        Byte input = 123;
        String expected = "123";
        assertEquals(expected, serializeNumber(input));
    }

    @Test
    public void testSerializeShort() {
        Short input = 123;
        String expected = "123";
        assertEquals(expected, serializeNumber(input));
    }

    @Test
    public void testSerializeString() {
        String input = "123";
        String expected = "123";
        assertEquals(expected, serializeNumber(input));
    }

    private String serializeNumber(Number number) {
        try {
            StringBuilder sb = new StringBuilder();
            numberSerializer.serialize(number, mapper.getFactory().createGenerator(sb), mapper.getSerializerProvider());
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}