import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.util.StdConverter;

public class NumberSerializerTest {

    private ObjectMapper mapper = new ObjectMapper();
    private JsonGenerator generator;

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal number = new BigDecimal("123.456789");
        String expected = "123.456789";
        String actual = serializeAndReturn(number);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger number = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);
        String expected = "9223372036854775808";
        String actual = serializeAndReturn(number);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Integer number = 42;
        String expected = "42";
        String actual = serializeAndReturn(number);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long number = 123456789L;
        String expected = "123456789";
        String actual = serializeAndReturn(number);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Double number = 123.456;
        String expected = "123.456";
        String actual = serializeAndReturn(number);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Float number = 123.456f;
        String expected = "123.456";
        String actual = serializeAndReturn(number);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeByte() throws IOException {
        Byte number = 100;
        String expected = "100";
        String actual = serializeAndReturn(number);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeShort() throws IOException {
        Short number = 1000;
        String expected = "1000";
        String actual = serializeAndReturn(number);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeOtherType() throws IOException {
        TestObject object = new TestObject("test");
        String expected = "\"test\"";
        String actual = serializeAndReturn(object);
        assertEquals(expected, actual);
    }

    private String serializeAndReturn(Object obj) throws IOException {
        generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.serialize(obj, generator, new DefaultSerializerProvider.Impl());
        generator.close();
        return generator.toString();
    }

    private class TestObject {
        @JsonSerialize(using = NumberSerializer.class)
        private String value;

        public TestObject(String value) {
            this.value = value;
        }
    }
}