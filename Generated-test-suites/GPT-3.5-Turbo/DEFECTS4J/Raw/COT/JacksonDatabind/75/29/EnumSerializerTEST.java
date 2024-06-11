
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;

public class EnumSerializerTest {

    @Test
    public void testSerializeByIndex() throws IOException {
        // Arrange
        EnumValues enumValues = createMockEnumValues();
        EnumSerializer serializer = new EnumSerializer(enumValues, true);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Enum value = MyEnum.FOO;

        // Act
        serializer.serialize(value, generator, provider);

        // Assert
        Mockito.verify(generator).writeNumber(Mockito.anyInt());
    }

    @Test
    public void testSerializeByName() throws IOException {
        // Arrange
        EnumValues enumValues = createMockEnumValues();
        EnumSerializer serializer = new EnumSerializer(enumValues, false);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Enum value = MyEnum.FOO;

        // Act
        serializer.serialize(value, generator, provider);

        // Assert
        Mockito.verify(generator).writeString(Mockito.anyString());
    }

    private EnumValues createMockEnumValues() {
        EnumValues enumValues = mock(EnumValues.class);
        Iterator<Enum<?>> iterator = mock(Iterator.class);
        Mockito.when(enumValues.values()).thenReturn(iterator);
        Mockito.when(iterator.hasNext()).thenReturn(true, false);
        Mockito.when(iterator.next()).thenReturn(MyEnum.FOO);

        return enumValues;
    }

    private enum MyEnum {
        FOO,
        BAR
    }
}
