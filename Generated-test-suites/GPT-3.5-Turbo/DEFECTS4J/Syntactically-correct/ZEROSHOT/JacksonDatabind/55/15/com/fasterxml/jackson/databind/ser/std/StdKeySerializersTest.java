import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Test case 1
        // Prepare
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = Object.class;
        boolean useDefault = true;

        // Execute
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assert result != null;

        // Test case 2
        // Prepare
        rawKeyType = String.class;
        useDefault = true;

        // Execute
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assert result == StdKeySerializers.DEFAULT_STRING_SERIALIZER;

        // Test case 3
        // Prepare
        rawKeyType = int.class;
        useDefault = true;

        // Execute
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assert result == StdKeySerializers.DEFAULT_KEY_SERIALIZER;

        // Test case 4
        // Prepare
        rawKeyType = Class.class;
        useDefault = true;

        // Execute
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assert result != null;

        // Test case 5
        // Prepare
        rawKeyType = Date.class;
        useDefault = true;

        // Execute
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assert result != null;

        // Test case 6
        // Prepare
        rawKeyType = Calendar.class;
        useDefault = true;

        // Execute
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assert result != null;

        // Test case 7
        // Prepare
        rawKeyType = java.util.UUID.class;
        useDefault = true;

        // Execute
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assert result != null;

        // Test case 8
        // Prepare
        rawKeyType = int.class;
        useDefault = false;

        // Execute
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assert result == null;
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Test case 1
        // Prepare
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = Object.class;

        // Execute
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assert result == StdKeySerializers.DEFAULT_KEY_SERIALIZER;

        // Test case 2
        // Prepare
        rawKeyType = Enum.class;

        // Execute
        result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assert result != null;

        // Test case 3
        // Prepare
        rawKeyType = Enum.class;

        // Execute
        result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assert result != null;

        // Test case 4
        // Prepare
        rawKeyType = null;

        // Execute
        result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assert result == StdKeySerializers.DEFAULT_KEY_SERIALIZER;
    }

    @Test
    public void testGetDefault() {
        // Prepare + Execute
        JsonSerializer<Object> result = StdKeySerializers.getDefault();

        // Assert
        assert result == StdKeySerializers.DEFAULT_KEY_SERIALIZER;
    }

    @Test
    public void testDefaultSerialize() throws IOException {
        // Prepare
        StdKeySerializers.Default serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, String.class);
        JsonGenerator g = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Assert
                assert value.equals("test");
            }
        };

        // Execute
        serializer.serialize("test", g, provider);

        // Clean-up
        g.close();
    }

    @Test
    public void testDynamic() throws IOException {
        // Prepare
        StdKeySerializers.Dynamic serializer = new StdKeySerializers.Dynamic();
        serializer._dynamicSerializers = new PropertySerializerMap.SerializerAndMapResult() {
            @Override
            public JsonSerializer<Object> serializer() {
                return new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

                    }
                };
            }
        };

        JsonGenerator g = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Assert
                assert value.equals("test");
            }
        };

        // Execute
        serializer.serialize("test", g, provider);

        // Clean-up
        g.close();
    }

    @Test
    public void testStringKeySerializer() throws IOException {
        // Prepare
        StdKeySerializers.StringKeySerializer serializer = new StdKeySerializers.StringKeySerializer();
        JsonGenerator g = new JsonFactory().createGenerator(System.out);

        // Execute
        serializer.serialize("test", g, null);

        // Clean-up
        g.close();
    }
}