import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Typical Use Case
        // Test when rawKeyType is String
        Class<?> rawKeyType = String.class;
        assertTrue(StdKeySerializers.getStdKeySerializer(null, rawKeyType, true) instanceof StdKeySerializers.StringKeySerializer);

        // Edge Case
        // Test when rawKeyType is null
        rawKeyType = null;
        assertTrue(StdKeySerializers.getStdKeySerializer(null, rawKeyType, false) instanceof StdKeySerializers.Dynamic);

        // Error Scenario
        // Test when rawKeyType is Enum
        rawKeyType = Enum.class;
        assertNull(StdKeySerializers.getStdKeySerializer(null, rawKeyType, true));
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Typical Use Case
        // Test when rawKeyType is Enum class
        Class<?> rawKeyType = Enum.class;
        assertTrue(StdKeySerializers.getFallbackKeySerializer(null, rawKeyType) instanceof StdKeySerializers.Dynamic);

        // Edge Case
        // Test when rawKeyType is Enum
        rawKeyType = MyEnum.class;
        assertTrue(StdKeySerializers.getFallbackKeySerializer(null, rawKeyType) instanceof StdKeySerializers.Default);

        // Error Scenario
        // Test when rawKeyType is null
        rawKeyType = null;
        assertTrue(StdKeySerializers.getFallbackKeySerializer(null, rawKeyType) instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetDefault() {
        assertNotNull(StdKeySerializers.getDefault());
    }

}