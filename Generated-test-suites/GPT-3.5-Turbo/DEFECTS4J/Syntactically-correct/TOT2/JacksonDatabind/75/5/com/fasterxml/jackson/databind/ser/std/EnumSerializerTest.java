import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testConstructor() {
        EnumValues enumValues = new EnumValues(MyEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);

        assertNotNull(enumSerializer);
        assertEquals(enumValues, enumSerializer.getEnumValues());
        assertTrue(enumSerializer._serializeAsIndex);
    }

    @Test
    public void testSerializeAsIndex() {
        EnumValues enumValues = new EnumValues(MyEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);

        assertTrue(enumSerializer._serializeAsIndex(mock(SerializerProvider.class)));
    }

    @Test
    public void testIsShapeWrittenUsingIndex() {
        Class<?> enumClass = MyEnum.class;
        JsonFormat.Value format = new JsonFormat.Value(Shape.STRING);
        
        assertNull(EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, false));
    }

    // Add more test cases for each method if needed

}