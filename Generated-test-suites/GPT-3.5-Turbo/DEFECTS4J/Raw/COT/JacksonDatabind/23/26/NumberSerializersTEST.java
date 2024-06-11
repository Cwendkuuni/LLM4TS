
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class NumberSerializersTest {

    @Test
    public void testShortSerializerSerializesShortValue() throws IOException {
        // Arrange
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);
        Short value = 42;

        // Act
        shortSerializer.serialize(value, genMock, providerMock);

        // Assert
        verify(genMock, times(1)).writeNumber(value.shortValue());
    }

    @Test
    public void testIntegerSerializerSerializesIntegerValue() throws IOException {
        // Arrange
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);
        Integer value = 42;

        // Act
        integerSerializer.serialize(value, genMock, providerMock);

        // Assert
        verify(genMock, times(1)).writeNumber(value.intValue());
    }

    // Add test cases for other public methods in the NumberSerializers class

    @Test
    public void testLongSerializerSerializesLongValue() throws IOException {
        // Arrange
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer();
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);
        Long value = 42L;

        // Act
        longSerializer.serialize(value, genMock, providerMock);

        // Assert
        verify(genMock, times(1)).writeNumber(value.longValue());
    }

    @Test
    public void testFloatSerializerSerializesFloatValue() throws IOException {
        // Arrange
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);
        Float value = 42.42f;

        // Act
        floatSerializer.serialize(value, genMock, providerMock);

        // Assert
        verify(genMock, times(1)).writeNumber(value.floatValue());
    }

    @Test
    public void testDoubleSerializerSerializesDoubleValue() throws IOException {
        // Arrange
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);
        Double value = 42.42;

        // Act
        doubleSerializer.serialize(value, genMock, providerMock);

        // Assert
        verify(genMock, times(1)).writeNumber(value.doubleValue());
    }
}

