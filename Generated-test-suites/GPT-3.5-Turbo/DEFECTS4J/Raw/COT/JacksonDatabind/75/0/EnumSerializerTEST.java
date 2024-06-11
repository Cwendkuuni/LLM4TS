
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class EnumSerializerTest {

    @Test
    public void testSerializeAsIndex() throws IOException {
        // Arrange
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(JsonMapper.getEmptyInstance().getSerializationConfig(), TestEnum.class), true);
        Enum<?> enumValue = TestEnum.VALUE1;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        serializer.serialize(enumValue, generator, provider);

        // Assert
        verify(generator).writeNumber(0);
    }

    @Test
    public void testSerializeAsString() throws IOException {
        // Arrange
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(JsonMapper.getEmptyInstance().getSerializationConfig(), TestEnum.class), false);
        Enum<?> enumValue = TestEnum.VALUE1;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        serializer.serialize(enumValue, generator, provider);

        // Assert
        verify(generator).writeString("VALUE1");
    }
    
    // TODO: Add additional test cases for edge cases and exception handling

}

enum TestEnum {
    VALUE1,
    VALUE2,
    VALUE3
}
