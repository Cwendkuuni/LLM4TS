import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Typical use case: rawKeyType is Integer
        Class<?> rawKeyType = Integer.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(result);
        
        // Edge case: rawKeyType is null
        rawKeyType = null;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(result);
        
        // Error scenario: rawKeyType is Enum
        rawKeyType = Enum.class;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNull(result);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Typical use case: rawKeyType is Long
        Class<?> rawKeyType = Long.class;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(result);
        
        // Edge case: rawKeyType is null
        rawKeyType = null;
        result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(result);
    }

    @Test
    public void testGetDefault() {
        JsonSerializer<Object> result = StdKeySerializers.getDefault();
        assertNotNull(result);
    }

    @Test
    public void testDefaultSerializer() {
        // Typical use case: TYPE_DATE
        int typeId = StdKeySerializers.Default.TYPE_DATE;
        Class<?> type = Date.class;
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(typeId, type);
        JsonGenerator g = null; // dummy generator
        SerializerProvider provider = null; // dummy provider
        try {
            defaultSerializer.serialize(value, g, provider);
        } catch (IOException e) {
            fail("Unexpected IOException thrown.");
        }

        // Edge case: TYPE_CALENDAR
        typeId = StdKeySerializers.Default.TYPE_CALENDAR;
        type = Calendar.class;
        defaultSerializer = new StdKeySerializers.Default(typeId, type);
        try {
            defaultSerializer.serialize(value, g, provider);
        } catch (IOException e) {
            fail("Unexpected IOException thrown.");
        }

        // Error scenario: unknown typeId
        typeId = 100;
        type = String.class;
        defaultSerializer = new StdKeySerializers.Default(typeId, type);
        try {
            defaultSerializer.serialize(value, g, provider);
            fail("Expected IOException not thrown.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDynamicSerializer() {
        // Typical use case
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        JsonSerializer<Object> ser = new StringKeySerializer();
        PropertySerializerMap map = dynamicSerializer._dynamicSerializers;
        map = map.newWith(Class.class, ser);
        dynamicSerializer._dynamicSerializers = map;
        String value = "test";
        JsonGenerator g = null; // dummy generator
        SerializerProvider provider = null; // dummy provider
        try {
            dynamicSerializer.serialize(value, g, provider);
        } catch (IOException e) {
            fail("Unexpected IOException thrown.");
        }

        // Error scenario: serializer not found
        dynamicSerializer = new StdKeySerializers.Dynamic();
        value = 10;
        try {
            dynamicSerializer.serialize(value, g, provider);
            fail("Expected IOException not thrown.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testStringKeySerializer() {
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        String value = "test";
        JsonGenerator g = null; // dummy generator
        SerializerProvider provider = null; // dummy provider
        try {
            stringKeySerializer.serialize(value, g, provider);
        } catch (IOException e) {
            fail("Unexpected IOException thrown.");
        }
    }
}