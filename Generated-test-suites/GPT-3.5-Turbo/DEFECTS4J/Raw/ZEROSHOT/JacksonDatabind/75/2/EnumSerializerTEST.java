import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerializeEnumIndex() throws Exception {
        EnumValues values = EnumValues.constructFromName(null, TestEnum.class);
        EnumSerializer serializer = new EnumSerializer(values, true);
        TestEnum testEnum = TestEnum.VALUE1;

        // Test serialization
        String serialized = serializer.serialize(testEnum, null, null);
        assertEquals("0", serialized);
    }

    @Test
    public void testSerializeEnumToString() throws Exception {
        EnumValues values = EnumValues.constructFromName(null, TestEnum.class);
        EnumSerializer serializer = new EnumSerializer(values, false);
        TestEnum testEnum = TestEnum.VALUE2;

        // Test serialization
        String serialized = serializer.serialize(testEnum, null, null);
        assertEquals("VALUE2", serialized);
    }

    @Test
    public void testSerializeEnumUsingTostring() throws Exception {
        EnumValues values = EnumValues.constructFromName(null, TestEnum.class);
        EnumSerializer serializer = new EnumSerializer(values, null);
        TestEnum testEnum = TestEnum.VALUE3;

        // Test serialization
        String serialized = serializer.serialize(testEnum, null, null);
        assertEquals("VALUE3", serialized);
    }

    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }
}