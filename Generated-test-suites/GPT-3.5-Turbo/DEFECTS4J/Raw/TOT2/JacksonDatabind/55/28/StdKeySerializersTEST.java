import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.ser.std.*;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig.Builder().build();
        Class<?> rawKeyType = String.class;
        boolean useDefault = true;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(serializer instanceof StdKeySerializers.StringKeySerializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig.Builder().build();
        Class<?> rawKeyType = Enum.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetDefault() {
        JsonSerializer<Object> defaultSerializer = StdKeySerializers.getDefault();
        assertTrue(defaultSerializer instanceof StdKeySerializers.StdKeySerializer);
    }

    @Test
    public void testDefaultSerializer() throws IOException {
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, TestEnum.class);
        // Test for Enum key serializer
        assertEquals("EnumValue", defaultSerializer.convertTo(KeySerializerTest.TestEnum.EnumValue));
    }

    @Test
    public void testDynamicSerializer() throws IOException {
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        // Test for dynamic key serializer with different types
        assertEquals("1", dynamicSerializer.convertTo(1));
        assertEquals("text", dynamicSerializer.convertTo("text"));
    }

    @Test
    public void testStringKeySerializer() throws IOException {
        StdKeySerializers.StringKeySerializer stringSerializer = new StdKeySerializers.StringKeySerializer();
        // Test for string key serializer
        assertEquals("key", stringSerializer.convertTo("key"));
    }

    enum TestEnum {
        EnumValue
    }

}