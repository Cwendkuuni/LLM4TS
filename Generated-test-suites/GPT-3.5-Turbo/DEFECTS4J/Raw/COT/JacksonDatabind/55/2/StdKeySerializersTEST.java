
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializerWithNullRawKeyType() {
        assertNull(StdKeySerializers.getStdKeySerializer(null, null, false));
    }

    @Test
    public void testGetStdKeySerializerWithObjectRawKeyType() {
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Object.class, false));
    }

    @Test
    public void testGetStdKeySerializerWithStringRawKeyType() {
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, String.class, false));
    }

    @Test
    public void testGetStdKeySerializerWithPrimitiveRawKeyType() {
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, int.class, false));
    }

    @Test
    public void testGetStdKeySerializerWithNumberSubtypeRawKeyType() {
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Integer.class, false));
    }

    @Test
    public void testGetStdKeySerializerWithClassRawKeyType() {
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Class.class, false));
    }

    @Test
    public void testGetStdKeySerializerWithDateSubtypeRawKeyType() {
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, java.sql.Date.class, false));
    }

    @Test
    public void testGetStdKeySerializerWithCalendarSubtypeRawKeyType() {
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, java.util.GregorianCalendar.class, false));
    }

    @Test
    public void testGetStdKeySerializerWithUuidRawKeyType() {
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, false));
    }

    @Test
    public void testGetFallbackKeySerializerWithEnumRawKeyType() {
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, Enum.class));
    }

    @Test
    public void testGetFallbackKeySerializerWithEnumSubtypeRawKeyType() {
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, StdKeySerializers.Default.TYPE_ENUM));
    }

    @Test
    public void testGetDefault() {
        assertNotNull(StdKeySerializers.getDefault());
    }
}