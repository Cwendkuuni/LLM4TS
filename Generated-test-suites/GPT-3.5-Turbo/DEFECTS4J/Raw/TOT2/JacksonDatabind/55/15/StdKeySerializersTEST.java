import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Testing for null rawKeyType
        assertNull(StdKeySerializers.getStdKeySerializer(null, null, true));

        // Testing for String rawKeyType
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, String.class, true));

        // Testing for primitive rawKeyType
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, int.class, true));

        // Testing for Date rawKeyType
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Date.class, true).getClass());

        // Testing for Calendar rawKeyType
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Calendar.class, true).getClass());

        // Testing for UUID rawKeyType
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true).getClass());
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Testing for Enum rawKeyType
        assertEquals(StdKeySerializers.Dynamic.class, StdKeySerializers.getFallbackKeySerializer(null, Enum.class).getClass());

        // Testing for Enum Type rawKeyType
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getFallbackKeySerializer(null, TestEnum.class).getClass());
    }

    @Test
    public void testGetDefault() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getDefault());
    }

}

enum TestEnum {
    VALUE1, VALUE2
}