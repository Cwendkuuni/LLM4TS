import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Test case 1
        Class<?> rawKeyType1 = null;
        boolean useDefault1 = true;
        JsonSerializer<Object> result1 = StdKeySerializers.getStdKeySerializer(null, rawKeyType1, useDefault1);
        assertTrue(result1 instanceof StdKeySerializers.Dynamic);

        // Test case 2
        Class<?> rawKeyType2 = String.class;
        boolean useDefault2 = false;
        JsonSerializer<Object> result2 = StdKeySerializers.getStdKeySerializer(null, rawKeyType2, useDefault2);
        assertTrue(result2 instanceof StdKeySerializers.StringKeySerializer);

        // Test case 3
        Class<?> rawKeyType3 = int.class;
        boolean useDefault3 = false;
        JsonSerializer<Object> result3 = StdKeySerializers.getStdKeySerializer(null, rawKeyType3, useDefault3);
        assertTrue(result3 instanceof StdKeySerializers.Default);

        // Test case 4
        Class<?> rawKeyType4 = Class.class;
        boolean useDefault4 = false;
        JsonSerializer<Object> result4 = StdKeySerializers.getStdKeySerializer(null, rawKeyType4, useDefault4);
        assertTrue(result4 instanceof StdKeySerializers.Default);

        // Test case 5
        Class<?> rawKeyType5 = Date.class;
        boolean useDefault5 = false;
        JsonSerializer<Object> result5 = StdKeySerializers.getStdKeySerializer(null, rawKeyType5, useDefault5);
        assertTrue(result5 instanceof StdKeySerializers.Default);

        // Test case 6
        Class<?> rawKeyType6 = Calendar.class;
        boolean useDefault6 = false;
        JsonSerializer<Object> result6 = StdKeySerializers.getStdKeySerializer(null, rawKeyType6, useDefault6);
        assertTrue(result6 instanceof StdKeySerializers.Default);

        // Test case 7
        Class<?> rawKeyType7 = java.util.UUID.class;
        boolean useDefault7 = false;
        JsonSerializer<Object> result7 = StdKeySerializers.getStdKeySerializer(null, rawKeyType7, useDefault7);
        assertTrue(result7 instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Test case 1
        Class<?> rawKeyType1 = Object.class;
        JsonSerializer<Object> result1 = StdKeySerializers.getFallbackKeySerializer(null, rawKeyType1);
        assertTrue(result1 instanceof StdKeySerializers.Default);

        // Test case 2
        Class<?> rawKeyType2 = Enum.class;
        JsonSerializer<Object> result2 = StdKeySerializers.getFallbackKeySerializer(null, rawKeyType2);
        assertTrue(result2 instanceof StdKeySerializers.Dynamic);

        // Test case 3
        Class<?> rawKeyType3 = SomeEnum.class;
        JsonSerializer<Object> result3 = StdKeySerializers.getFallbackKeySerializer(null, rawKeyType3);
        assertTrue(result3 instanceof StdKeySerializers.Default);
    }

    @Test
    public void testGetDefault() {
        // Test case 1
        JsonSerializer<Object> result1 = StdKeySerializers.getDefault();
        assertTrue(result1 instanceof StdKeySerializers.Default);
    }

    private enum SomeEnum {
        VALUE_1, VALUE_2
    }

}