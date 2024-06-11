
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void testSerialize_BigDecimal() throws IOException {
        // Arrange
        BigDecimal value = new BigDecimal("10.01");
        JsonGeneratorMock g = new JsonGeneratorMock();
        SerializerProviderMock provider = new SerializerProviderMock();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(value, g, provider);

        // Assert
        Assert.assertEquals("10.01", g.getWrittenValue());
    }

    @Test
    public void testSerialize_BigInteger() throws IOException {
        // Arrange
        BigInteger value = new BigInteger("100000000000000000000000000000000000");
        JsonGeneratorMock g = new JsonGeneratorMock();
        SerializerProviderMock provider = new SerializerProviderMock();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(value, g, provider);

        // Assert
        Assert.assertEquals("100000000000000000000000000000000000", g.getWrittenValue());
    }

    @Test
    public void testSerialize_Integer() throws IOException {
        // Arrange
        Integer value = 100;
        JsonGeneratorMock g = new JsonGeneratorMock();
        SerializerProviderMock provider = new SerializerProviderMock();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(value, g, provider);

        // Assert
        Assert.assertEquals("100", g.getWrittenValue());
    }

    @Test
    public void testSerialize_Long() throws IOException {
        // Arrange
        Long value = 100000000000L;
        JsonGeneratorMock g = new JsonGeneratorMock();
        SerializerProviderMock provider = new SerializerProviderMock();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(value, g, provider);

        // Assert
        Assert.assertEquals("100000000000", g.getWrittenValue());
    }

    @Test
    public void testSerialize_Double() throws IOException {
        // Arrange
        Double value = 10.01;
        JsonGeneratorMock g = new JsonGeneratorMock();
        SerializerProviderMock provider = new SerializerProviderMock();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(value, g, provider);

        // Assert
        Assert.assertEquals("10.01", g.getWrittenValue());
    }

    @Test
    public void testSerialize_Float() throws IOException {
        // Arrange
        Float value = 10.01f;
        JsonGeneratorMock g = new JsonGeneratorMock();
        SerializerProviderMock provider = new SerializerProviderMock();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(value, g, provider);

        // Assert
        Assert.assertEquals("10.01", g.getWrittenValue());
    }

    @Test
    public void testSerialize_Byte() throws IOException {
        // Arrange
        Byte value = 10;
        JsonGeneratorMock g = new JsonGeneratorMock();
        SerializerProviderMock provider = new SerializerProviderMock();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(value, g, provider);

        // Assert
        Assert.assertEquals("10", g.getWrittenValue());
    }

    @Test
    public void testSerialize_Short() throws IOException {
        // Arrange
        Short value = 10;
        JsonGeneratorMock g = new JsonGeneratorMock();
        SerializerProviderMock provider = new SerializerProviderMock();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(value, g, provider);

        // Assert
        Assert.assertEquals("10", g.getWrittenValue());
    }

    @Test
    public void testSerialize_OtherNumberClass() throws IOException {
        // Arrange
        Number value = new CustomNumber();
        JsonGeneratorMock g = new JsonGeneratorMock();
        SerializerProviderMock provider = new SerializerProviderMock();
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(value, g, provider);

        // Assert
        Assert.assertEquals("CustomNumber", g.getWrittenValue());
    }

    private static class CustomNumber extends Number {
        @Override
        public int intValue() {
            return 0;
        }

        @Override
        public long longValue() {
            return 0;
        }

        @Override
        public float floatValue() {
            return 0;
        }

        @Override
        public double doubleValue() {
            return 0;
        }
    }

    private static class JsonGeneratorMock extends JsonGenerator {
        private String writtenValue;

        public String getWrittenValue() {
            return writtenValue;
        }

        @Override
        public void writeNumber(String value) throws IOException {
            this.writtenValue = value;
        }

        // Other overridden methods; not needed for test cases
    }

    private static class SerializerProviderMock extends SerializerProvider {
        // Overridden methods; not needed for test cases
    }
}
