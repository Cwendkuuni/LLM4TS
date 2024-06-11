import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void serialize_shouldWriteBigDecimalValue() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        BigDecimal value = new BigDecimal("123.45");
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(value, generator, new SerializerProvider());

        // Assert - Output should be "123.45"
    }

    @Test
    public void serialize_shouldWriteBigIntegerValue() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        BigInteger value = new BigInteger("12345678901234567890");
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(value, generator, new SerializerProvider());

        // Assert - Output should be "12345678901234567890"
    }

    @Test
    public void serialize_shouldWriteIntegerValue() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        Integer value = 123;
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(value, generator, new SerializerProvider());

        // Assert - Output should be "123"
    }

    @Test
    public void serialize_shouldWriteLongValue() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        Long value = 1234567890L;
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(value, generator, new SerializerProvider());

        // Assert - Output should be "1234567890"
    }

    @Test
    public void serialize_shouldWriteDoubleValue() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        Double value = 123.45;
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(value, generator, new SerializerProvider());

        // Assert - Output should be "123.45"
    }

    @Test
    public void serialize_shouldWriteFloatValue() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        Float value = 123.45f;
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(value, generator, new SerializerProvider());

        // Assert - Output should be "123.45"
    }

    @Test
    public void serialize_shouldWriteByteValue() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        Byte value = 123;
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(value, generator, new SerializerProvider());

        // Assert - Output should be "123"
    }

    @Test
    public void serialize_shouldWriteShortValue() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        Short value = 123;
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(value, generator, new SerializerProvider());

        // Assert - Output should be "123"
    }

    @Test
    public void serialize_shouldWriteFallbackValue() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        MyClass value = new MyClass();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(value, generator, new SerializerProvider());

        // Assert - Output should be "MyClass@<hashcode>"
    }

    private static class MyClass {
        @Override
        public String toString() {
            return "MyClass@<hashcode>";
        }
    }

}