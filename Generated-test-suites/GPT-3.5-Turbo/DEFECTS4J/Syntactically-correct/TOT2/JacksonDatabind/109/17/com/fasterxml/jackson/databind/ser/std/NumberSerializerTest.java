import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal decimal = new BigDecimal("123.45");
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        numberSerializer.serialize(decimal, jsonGenerator, null);

        verify(jsonGenerator).writeNumber(decimal);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger bigInteger = new BigInteger("1234567890");
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        numberSerializer.serialize(bigInteger, jsonGenerator, null);

        verify(jsonGenerator).writeNumber(bigInteger);
    }

    @Test
    public void testSerializeLong() throws IOException {
        long value = 1234567890L;
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        numberSerializer.serialize(value, jsonGenerator, null);

        verify(jsonGenerator).writeNumber(value);
    }

    // Include tests for other data types and edge cases

}