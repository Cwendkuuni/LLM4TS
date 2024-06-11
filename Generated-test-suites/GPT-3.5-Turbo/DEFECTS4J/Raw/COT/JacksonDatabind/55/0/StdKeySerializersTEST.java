import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Test case 1
        Class<?> rawKeyType1 = null;
        boolean useDefault1 = true;
        JsonSerializer<Object> result1 = StdKeySerializers.getStdKeySerializer(null, rawKeyType1, useDefault1);
        assertNull(result1);

        // Test case 2
        Class<?> rawKeyType2 = String.class;
        boolean useDefault2 = true;
        JsonSerializer<Object> result2 = StdKeySerializers.getStdKeySerializer(null, rawKeyType2, useDefault2);
        assertNotNull(result2);
        assertTrue(result2 instanceof StdKeySerializers.StringKeySerializer);

        // Test case 3
        Class<?> rawKeyType3 = Integer.class;
        boolean useDefault3 = true;
        JsonSerializer<Object> result3 = StdKeySerializers.getStdKeySerializer(null, rawKeyType3, useDefault3);
        assertNotNull(result3);
        assertTrue(result3 instanceof StdKeySerializer);

        // Test case 4
        Class<?> rawKeyType4 = Object.class;
        boolean useDefault4 = true;
        JsonSerializer<Object> result4 = StdKeySerializers.getStdKeySerializer(null, rawKeyType4, useDefault4);
        assertNotNull(result4);
        assertTrue(result4 instanceof Dynamic);

        // Test case 5
        Class<?> rawKeyType5 = Class.class;
        boolean useDefault5 = true;
        JsonSerializer<Object> result5 = StdKeySerializers.getStdKeySerializer(null, rawKeyType5, useDefault5);
        assertNotNull(result5);
        assertTrue(result5 instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Test case 1
        Class<?> rawKeyType1 = null;
        JsonSerializer<Object> result1 = StdKeySerializers.getFallbackKeySerializer(null, rawKeyType1);
        assertNotNull(result1);
        assertTrue(result1 instanceof StdKeySerializer);

        // Test case 2
        Class<?> rawKeyType2 = Enum.class;
        JsonSerializer<Object> result2 = StdKeySerializers.getFallbackKeySerializer(null, rawKeyType2);
        assertNotNull(result2);
        assertTrue(result2 instanceof Dynamic);

        // Test case 3
        Class<?> rawKeyType3 = String.class;
        JsonSerializer<Object> result3 = StdKeySerializers.getFallbackKeySerializer(null, rawKeyType3);
        assertNotNull(result3);
        assertTrue(result3 instanceof StdKeySerializers.Default);
    }

    @Test
    @Deprecated
    public void testGetDefault() {
        JsonSerializer<Object> result = StdKeySerializers.getDefault();
        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializer);
    }
}