import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_TypeByte_MinValue() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BYTE, Byte.class);
        assertEquals(Byte.valueOf(Byte.MIN_VALUE), deserializer.deserializeKey(String.valueOf(Byte.MIN_VALUE), null));
    }

    @Test
    public void testDeserializeKey_TypeByte_MaxValue() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BYTE, Byte.class);
        assertEquals(Byte.valueOf((byte) 255), deserializer.deserializeKey("255", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_TypeByte_Overflow() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BYTE, Byte.class);
        deserializer.deserializeKey("256", null);
    }

    // Similar test methods for other cases and exceptions

    @Test
    public void testDeserializeKey_NullKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_UnknownEnumValueAsNull() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_ENUM, MyEnum.class);
        DeserializationContext ctxt = createDeserializationContextWithFeature(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        assertNull(deserializer.deserializeKey("UNKNOWN_VALUE", ctxt));
    }

    private DeserializationContext createDeserializationContextWithFeature(DeserializationFeature feature) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getDeserializationContext().without(feature);
    }
}

enum MyEnum {
    VALUE_1,
    VALUE_2
}