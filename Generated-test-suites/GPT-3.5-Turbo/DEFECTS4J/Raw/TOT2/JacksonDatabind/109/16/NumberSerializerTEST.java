import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Test
    public void testSerialize_withBigDecimal_shouldWriteNumber() {
        // Arrange
        numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        BigDecimal number = new BigDecimal("123.45");

        // Act
        numberSerializer.serialize(number, mockGenerator, null);

        // Assert
        verify(mockGenerator).writeNumber(number);
    }

    @Test
    public void testSerialize_withBigInteger_shouldWriteNumber() {
        // Arrange
        numberSerializer = new NumberSerializer(BigInteger.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        BigInteger number = new BigInteger("12345");

        // Act
        numberSerializer.serialize(number, mockGenerator, null);

        // Assert
        verify(mockGenerator).writeNumber(number);
    }

    @Test
    public void testSerialize_withLong_shouldWriteNumber() {
        // Arrange
        numberSerializer = new NumberSerializer(Long.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        Long number = 123L;

        // Act
        numberSerializer.serialize(number, mockGenerator, null);

        // Assert
        verify(mockGenerator).writeNumber(number);
    }

    // Add more test cases for other scenarios

}