
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        BigDecimal input = new BigDecimal(10);

        numberSerializer.serialize(input, mockGenerator, mockProvider);

        verify(mockGenerator).writeNumber(input);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        BigInteger input = new BigInteger("10");

        numberSerializer.serialize(input, mockGenerator, mockProvider);

        verify(mockGenerator).writeNumber(input);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        Integer input = 10;

        numberSerializer.serialize(input, mockGenerator, mockProvider);

        verify(mockGenerator).writeNumber(input.intValue());
    }

    // Add more test cases for other methods and edge cases

}
