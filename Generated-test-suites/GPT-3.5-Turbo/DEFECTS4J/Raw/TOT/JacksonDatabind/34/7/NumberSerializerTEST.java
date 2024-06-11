import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerialize_WhenValueIsBigDecimal_ShouldWriteNumberToGenerator() throws IOException {
        // Arrange
        BigDecimal value = new BigDecimal("10.5");

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_WhenValueIsBigInteger_ShouldWriteNumberToGenerator() throws IOException {
        // Arrange
        BigInteger value = new BigInteger("100000000000000000000000000000000");

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_WhenValueIsInteger_ShouldWriteNumberToGenerator() throws IOException {
        // Arrange
        Integer value = 10;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_WhenValueIsLong_ShouldWriteNumberToGenerator() throws IOException {
        // Arrange
        Long value = 100000000L;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_WhenValueIsDouble_ShouldWriteNumberToGenerator() throws IOException {
        // Arrange
        Double value = 10.5;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_WhenValueIsFloat_ShouldWriteNumberToGenerator() throws IOException {
        // Arrange
        Float value = 5.4f;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_WhenValueIsByte_ShouldWriteNumberToGenerator() throws IOException {
        // Arrange
        Byte value = 7;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_WhenValueIsShort_ShouldWriteNumberToGenerator() throws IOException {
        // Arrange
        Short value = 8;

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_WhenValueIsNonNumeric_ShouldWriteNumberAsStringToGenerator() throws IOException {
        // Arrange
        Object value = "non-numeric";

        // Act
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator, times(1)).writeNumber(value.toString());
    }

    // Additional test cases for getSchema and acceptJsonFormatVisitor can be added here

}