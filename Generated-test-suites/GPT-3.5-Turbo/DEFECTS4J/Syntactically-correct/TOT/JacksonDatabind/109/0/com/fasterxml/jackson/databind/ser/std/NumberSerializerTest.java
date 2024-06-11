
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializerTest {

    @Mock
    private JsonGenerator mockJsonGenerator;

    @Mock
    private SerializerProvider mockSerializerProvider;

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        numberSerializer = NumberSerializer.instance;
    }

    @Test
    public void testSerialize_WhenBigDecimal_ThenWriteBigDecimalToGenerator() throws IOException {
        // Arrange
        BigDecimal value = new BigDecimal("123.45");

        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_WhenBigInteger_ThenWriteBigIntegerToGenerator() throws IOException {
        // Arrange
        BigInteger value = new BigInteger("123456789");

        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_WhenLong_ThenWriteLongToGenerator() throws IOException {
        // Arrange
        Long value = 123L;

        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeNumber(value.longValue());
    }

    @Test
    public void testSerialize_WhenDouble_ThenWriteDoubleToGenerator() throws IOException {
        // Arrange
        Double value = 123.45;

        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerialize_WhenFloat_ThenWriteFloatToGenerator() throws IOException {
        // Arrange
        Float value = 123.45f;

        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeNumber(value.floatValue());
    }

    @Test
    public void testSerialize_WhenInteger_ThenWriteIntegerToGenerator() throws IOException {
        // Arrange
        Integer value = 123;

        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_WhenByte_ThenWriteIntegerToGenerator() throws IOException {
        // Arrange
        Byte value = 123;

        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_WhenShort_ThenWriteIntegerToGenerator() throws IOException {
        // Arrange
        Short value = 123;

        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_WhenInvalidNumber_ThenWriteNumberAsStringToGenerator() throws IOException {
        // Arrange
        Number value = mock(Number.class);
        when(value.toString()).thenReturn("123");

        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeNumber(value.toString());
    }
}
