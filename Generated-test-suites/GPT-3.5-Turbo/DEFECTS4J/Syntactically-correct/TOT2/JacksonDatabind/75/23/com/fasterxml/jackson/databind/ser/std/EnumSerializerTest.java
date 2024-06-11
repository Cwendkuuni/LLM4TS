import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testConstructor() {
        EnumValues mockValues = new EnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(mockValues, true);

        assertEquals(mockValues, enumSerializer.getEnumValues());
        assertTrue(enumSerializer._serializeAsIndex);
    }

    @Test
    public void testSerialize() {
        EnumValues mockValues = new EnumValues();
        EnumSerializer enumSerializer = new EnumSerializer(mockValues, false);

        // Test with null JsonGenerator
        assertThrows(NullPointerException.class, () -> enumSerializer.serialize(null, null, null));

        // Add more test cases for serialization
    }

    @Test
    public void testConstruct() {
        Class<?> enumClass = TestEnum.class;
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        JsonFormat.Value format = new JsonFormat.Value();

        EnumSerializer enumSerializer = EnumSerializer.construct(enumClass, config, beanDesc, format);

        assertNotNull(enumSerializer);
    }

    // Add more test methods for other public methods in EnumSerializer class

    @Test
    public void testIsShapeWrittenUsingIndex() {
        Class<?> enumClass = TestEnum.class;
        JsonFormat.Value format = new JsonFormat.Value();
        boolean fromClass = true;

        Boolean result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, fromClass);

        assertNotNull(result);
    }

    @Test
    public void testCreateContextual() {
        // Add test cases for createContextual method
    }

    // Add more test methods for edge cases and exception handling scenarios

}

enum TestEnum {
    VALUE1, VALUE2, VALUE3
}