import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testConstructor() {
        EnumValues enumValues = new EnumValues(MyEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);

        assertEquals(enumValues, enumSerializer.getEnumValues());
        assertTrue(enumSerializer._serializeAsIndex);
    }

    @Test
    public void testSerializeAsIndex() {
        EnumValues enumValues = new EnumValues(MyEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);

        assertTrue(enumSerializer._serializeAsIndex(null));
    }

    @Test
    public void testSerializeAsString() {
        EnumValues enumValues = new EnumValues(MyEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        assertFalse(enumSerializer._serializeAsIndex(null));
    }

    @Test
    public void testIsShapeWrittenUsingIndex() {
        JsonFormat.Value format = new JsonFormat.Value();
        Class<?> enumClass = MyEnum.class;

        Boolean result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, true);

        assertNull(result);
    }
}