import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerWithNullType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertNotNull(serializer);
        assertNull(serializer);
    }

    @Test
    public void testGetStdKeySerializerWithObjectType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, false);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStdKeySerializerWithInvalidType() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = Integer.TYPE;
        StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializerWithNullType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializerWithEnumType() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = Enum.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFallbackKeySerializerWithInvalidType() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = Integer.TYPE;
        StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
    }

    @Test
    public void testDefaultConstructor() {
        StdKeySerializers instance = new StdKeySerializers();
        assertNotNull(instance);
        assertEquals(0, instance.hashCode());
    }

    @Test
    public void testGetDefault() {
        StdKeySerializers instance = new StdKeySerializers();
        JsonSerializer<Object> serializer = instance.getDefault();
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
    }

    @Test
    public void testGetDynamicSerializer() {
        SerializationConfig config = new SerializationConfig();
        Object value = new Object() {
            @Override
            public Class<?> getClass() {
                return Integer.TYPE;
            }
        };
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault().serializeValue(value, config, null);
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testDynamicSerializerSerialization() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        Object value = new Object() {
            @Override
            public Class<?> getClass() {
                return Integer.TYPE;
            }
        };
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault().serializeValue(value, config, null);
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new StringWriter());
        serializer.serialize(value, generator, config);
        generator.flush();
        String json = generator.getOutput().toString();
        String expected = "\"\njava.lang.Object\n\"";
        assertEquals(expected, json);
    }

    @Test
    public void testDynamicSerializerAddSerializer() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        Object value = new Object() {
            @Override
            public Class<?> getClass() {
                return Integer.TYPE;
            }
        };
        PropertySerializerMap map = new PropertySerializerMap();
        StdKeySerializers.Dynamic dynamic = new StdKeySerializers.Dynamic();
        JsonSerializer<Object> serializer = dynamic.serializeValue(value, config, null);
        map.setSerializer("java.lang.Integer", serializer);
        JsonSerializer<Object> addedSerializer = dynamic.serializeValue(value, config, map);
        assertNotNull(addedSerializer);
        assertEquals(serializer, addedSerializer);
    }

    @Test
    public void testDefaultSerializerSerialization() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        Object value = new Object() {
            @Override
            public Class<?> getClass() {
                return String.class;
            }
        };
        JsonSerializer<Object> serializer = StdKeySerializers.DEFAULT_STRING_SERIALIZER.serializeValue(value, config, null);
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(new StringWriter());
        serializer.serialize(value, generator, config);
        generator.flush();
        String json = generator.getOutput().toString();
        String expected = "\"" + value.toString() + "\"";
        assertEquals(expected, json);
    }

}