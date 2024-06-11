import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Test case for default key serializer
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Integer.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);

        // Test case for String key serializer
        rawKeyType = String.class;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);

        // Add more test cases for different scenarios
    }

    // Add more test cases for other public methods in StdKeySerializers class

    @Test
    public void testGetFallbackKeySerializer() {
        // Test case for fallback key serializer
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Calendar.class;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);

        // Add additional test cases for other scenarios
    }

    // Add more test cases for public methods in StdKeySerializers class

}