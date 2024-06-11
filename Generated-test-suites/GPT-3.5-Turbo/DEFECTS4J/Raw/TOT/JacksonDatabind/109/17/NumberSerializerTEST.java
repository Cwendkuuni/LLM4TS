import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeBigDecimal() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        BigDecimal number = new BigDecimal("123.456");

        // Act
        numberSerializer.serialize(number, generator, mapper.getSerializerProvider());

        // Assert
        generator.flush();
        Assert.assertEquals(number.toString(), writer.toString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        BigInteger number = new BigInteger("123456789");

        // Act
        numberSerializer.serialize(number, generator, mapper.getSerializerProvider());

        // Assert
        generator.flush();
        Assert.assertEquals(number.toString(), writer.toString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Long.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        Long number = 123456789L;

        // Act
        numberSerializer.serialize(number, generator, mapper.getSerializerProvider());

        // Assert
        generator.flush();
        Assert.assertEquals(number.toString(), writer.toString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        Double number = 123.456;

        // Act
        numberSerializer.serialize(number, generator, mapper.getSerializerProvider());

        // Assert
        generator.flush();
        Assert.assertEquals(number.toString(), writer.toString());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Float.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        Float number = 123.456f;

        // Act
        numberSerializer.serialize(number, generator, mapper.getSerializerProvider());

        // Assert
        generator.flush();
        Assert.assertEquals(number.toString(), writer.toString());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        Integer number = 123456789;

        // Act
        numberSerializer.serialize(number, generator, mapper.getSerializerProvider());

        // Assert
        generator.flush();
        Assert.assertEquals(number.toString(), writer.toString());
    }

    @Test
    public void testSerializeByte() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Byte.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        Byte number = 123;

        // Act
        numberSerializer.serialize(number, generator, mapper.getSerializerProvider());

        // Assert
        generator.flush();
        Assert.assertEquals(number.toString(), writer.toString());
    }

    @Test
    public void testSerializeShort() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Short.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        Short number = 123;

        // Act
        numberSerializer.serialize(number, generator, mapper.getSerializerProvider());

        // Assert
        generator.flush();
        Assert.assertEquals(number.toString(), writer.toString());
    }

    @Test
    public void testSerializeUnknownType() throws IOException {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        Number number = new CustomNumber();

        // Act
        numberSerializer.serialize(number, generator, mapper.getSerializerProvider());

        // Assert
        generator.flush();
        Assert.assertEquals(number.toString(), writer.toString());
    }

    private static class CustomNumber extends Number {
        @Override
        public int intValue() {
            return 0;
        }
        @Override
        public long longValue() {
            return 0L;
        }
        @Override
        public float floatValue() {
            return 0.0f;
        }
        @Override
        public double doubleValue() {
            return 0.0;
        }
        @Override
        public String toString() {
            return "CustomNumber";
        }
    }
}