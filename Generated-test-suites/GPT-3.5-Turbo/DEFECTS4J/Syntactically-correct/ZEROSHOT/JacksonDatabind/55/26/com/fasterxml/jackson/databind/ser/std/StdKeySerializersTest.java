import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Test case when rawKeyType is null
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig(provider);
        Class<?> rawKeyType = null;
        boolean useDefault = true;
        assert(StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault) instanceof StdKeySerializers.Dynamic);

        // Test case when rawKeyType is Object.class
        rawKeyType = Object.class;
        assert(StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault) instanceof StdKeySerializers.Dynamic);

        // Test case when rawKeyType is String.class
        rawKeyType = String.class;
        assert(StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault) instanceof StdKeySerializers.StringKeySerializer);

        // Test case when rawKeyType is primitive
        rawKeyType = int.class;
        assert(StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault) instanceof StdKeySerializers.Default);

        // Test case when rawKeyType is Number.class
        rawKeyType = Number.class;
        assert(StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault) instanceof StdKeySerializers.Default);

        // Test case when rawKeyType is Class.class
        rawKeyType = Class.class;
        assert(StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault) instanceof StdKeySerializers.Default);

        // Test case when rawKeyType is Date.class
        rawKeyType = Date.class;
        assert(StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault) instanceof StdKeySerializers.Default);

        // Test case when rawKeyType is Calendar.class
        rawKeyType = Calendar.class;
        assert(StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault) instanceof StdKeySerializers.Default);

        // Test case when rawKeyType is java.util.UUID.class
        rawKeyType = java.util.UUID.class;
        assert(StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault) instanceof StdKeySerializers.Default);

        // Test case when rawKeyType doesn't match any of the conditions
        rawKeyType = StdKeySerializersTest.class;
        assert(StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault) == null);

    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Test case when rawKeyType is null
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig(provider);
        Class<?> rawKeyType = null;
        assert(StdKeySerializers.getFallbackKeySerializer(config, rawKeyType) instanceof StdKeySerializers.Default);

        // Test case when rawKeyType is Enum.class
        rawKeyType = Enum.class;
        assert(StdKeySerializers.getFallbackKeySerializer(config, rawKeyType) instanceof StdKeySerializers.Dynamic);

        // Test case when rawKeyType is enum
        rawKeyType = TestEnum.class;
        assert(StdKeySerializers.getFallbackKeySerializer(config, rawKeyType) instanceof StdKeySerializers.Default);

        // Test case when rawKeyType doesn't match any of the conditions
        rawKeyType = StdKeySerializersTest.class;
        assert(StdKeySerializers.getFallbackKeySerializer(config, rawKeyType) instanceof StdKeySerializers.Default);

    }

    @Test
    public void testGetDefault() {
        assert(StdKeySerializers.getDefault() instanceof StdKeySerializers.Default);
    }
    
    @Test
    public void testDefaultSerializeDateKey() throws IOException {
        // Test case for TYPE_DATE in Default.serialize()
        JsonGenerator g = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        ProviderConfig config = new ProviderConfig();
        provider.setConfig(config);
        Date date = new Date();
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        defaultSerializer.serialize(date, g, provider);
        g.close();
    }

    @Test
    public void testDefaultSerializeCalendarKey() throws IOException {
        // Test case for TYPE_CALENDAR in Default.serialize()
        JsonGenerator g = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        ProviderConfig config = new ProviderConfig();
        provider.setConfig(config);
        Calendar calendar = Calendar.getInstance();
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        defaultSerializer.serialize(calendar, g, provider);
        g.close();
    }

    @Test
    public void testDefaultSerializeClassKey() throws IOException {
        // Test case for TYPE_CLASS in Default.serialize()
        JsonGenerator g = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        ProviderConfig config = new ProviderConfig();
        provider.setConfig(config);
        Class<?> classType = StdKeySerializersTest.class;
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        defaultSerializer.serialize(classType, g, provider);
        g.close();
    }

    @Test
    public void testDefaultSerializeEnumKey() throws IOException {
        // Test case for TYPE_ENUM in Default.serialize()
        JsonGenerator g = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        ProviderConfig config = new ProviderConfig();
        provider.setConfig(config);
        TestEnum enumValue = TestEnum.TEST_ENUM_1;
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, TestEnum.class);
        defaultSerializer.serialize(enumValue, g, provider);
        g.close();
    }

    @Test
    public void testDefaultSerializeToStringKey() throws IOException {
        // Test case for TYPE_TO_STRING in Default.serialize()
        JsonGenerator g = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        ProviderConfig config = new ProviderConfig();
        provider.setConfig(config);
        Object object = new Object();
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, Object.class);
        defaultSerializer.serialize(object, g, provider);
        g.close();
    }

    private enum TestEnum {
        TEST_ENUM_1,
        TEST_ENUM_2
    }
}