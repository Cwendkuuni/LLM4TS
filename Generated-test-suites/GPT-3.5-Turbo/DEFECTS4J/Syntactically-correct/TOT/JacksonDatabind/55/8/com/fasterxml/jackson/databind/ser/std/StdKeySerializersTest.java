import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StdKeySerializersTest {

    private static StdKeySerializers keySerializer;

    @BeforeClass
    public static void setUp() {
        keySerializer = new StdKeySerializers();
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsNull_ReturnsDynamicSerializer() {
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        assertNull(keySerializer.getStdKeySerializer(config, null, false));
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsString_ReturnsDefaultStringSerializer() {
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        assertEquals(keySerializer.getStdKeySerializer(config, String.class, true), keySerializer.DEFAULT_STRING_SERIALIZER);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsPrimitiveOrAssignableToNumber_ReturnsDefaultKeySerializer() {
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        assertEquals(keySerializer.getStdKeySerializer(config, int.class, true), keySerializer.DEFAULT_KEY_SERIALIZER);
        assertEquals(keySerializer.getStdKeySerializer(config, Integer.class, true), keySerializer.DEFAULT_KEY_SERIALIZER);
        assertEquals(keySerializer.getStdKeySerializer(config, double.class, true), keySerializer.DEFAULT_KEY_SERIALIZER);
        assertEquals(keySerializer.getStdKeySerializer(config, Double.class, true), keySerializer.DEFAULT_KEY_SERIALIZER);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsClass_ReturnsDefaultSerializerWithClassAsField() {
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        Object serializer = keySerializer.getStdKeySerializer(config, Class.class, true);
        assertTrue(serializer instanceof StdKeySerializers.Default);
        assertEquals(((StdKeySerializers.Default)serializer)._typeId, StdKeySerializers.Default.TYPE_CLASS);
        assertEquals(((StdKeySerializers.Default)serializer).handledType(), Class.class);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsDate_ReturnsDefaultSerializerWithDateAsField() {
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        Object serializer = keySerializer.getStdKeySerializer(config, Date.class, true);
        assertTrue(serializer instanceof StdKeySerializers.Default);
        assertEquals(((StdKeySerializers.Default)serializer)._typeId, StdKeySerializers.Default.TYPE_DATE);
        assertEquals(((StdKeySerializers.Default)serializer).handledType(), Date.class);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsCalendar_ReturnsDefaultSerializerWithCalendarAsField() {
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        Object serializer = keySerializer.getStdKeySerializer(config, Calendar.class, true);
        assertTrue(serializer instanceof StdKeySerializers.Default);
        assertEquals(((StdKeySerializers.Default)serializer)._typeId, StdKeySerializers.Default.TYPE_CALENDAR);
        assertEquals(((StdKeySerializers.Default)serializer).handledType(), Calendar.class);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsUUID_ReturnsDefaultSerializerWithToStringAsField() {
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        Object serializer = keySerializer.getStdKeySerializer(config, java.util.UUID.class, true);
        assertTrue(serializer instanceof StdKeySerializers.Default);
        assertEquals(((StdKeySerializers.Default)serializer)._typeId, StdKeySerializers.Default.TYPE_TO_STRING);
        assertEquals(((StdKeySerializers.Default)serializer).handledType(), java.util.UUID.class);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsEnum_ReturnsDefaultSerializerWithEnumAsField() {
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        Object serializer = keySerializer.getStdKeySerializer(config, Enum.class, true);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);
        assertEquals(((StdKeySerializers.Dynamic)serializer)._dynamicSerializers.size(), 0);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsEnum_ReturnsDefaultSerializerWithToStringAsField() {
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        Object serializer = keySerializer.getStdKeySerializer(config, TestEnum.class, true);
        assertTrue(serializer instanceof StdKeySerializers.Default);
        assertEquals(((StdKeySerializers.Default)serializer)._typeId, StdKeySerializers.Default.TYPE_ENUM);
        assertEquals(((StdKeySerializers.Default)serializer).handledType(), TestEnum.class);
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsNotKnown_ReturnsNullIfUseDefaultIsFalse() {
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        assertNull(keySerializer.getStdKeySerializer(config, Object.class, false));
    }

    @Test
    public void testGetFallbackKeySerializer_WhenRawKeyTypeIsNotKnown_ReturnsDefaultKeySerializer() {
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        Object serializer = keySerializer.getFallbackKeySerializer(config, Object.class);
        assertTrue(serializer instanceof StdKeySerializers.Default);
        assertEquals(((StdKeySerializers.Default)serializer)._typeId, StdKeySerializers.Default.TYPE_TO_STRING);
        assertEquals(((StdKeySerializers.Default)serializer).handledType(), Object.class);
    }

    @Test
    public void testGetDefault_ReturnsDefaultKeySerializer() {
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        assertEquals(keySerializer.getDefault(), keySerializer.DEFAULT_KEY_SERIALIZER);
    }

    @Test
    public void testSerialize_WhenTypeIsDate_CallsDefaultSerializeDateKey() {
        FakeJsonGenerator g = new FakeJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        StdKeySerializers.Default serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        Date date = new Date();
        serializer.serialize(date, g, provider);
        assertTrue(g.defaultSerializeDateKeyWasCalled);
        assertEquals(g.defaultSerializeDateKeyDateParam, date);
    }

    @Test
    public void testSerialize_WhenTypeIsCalendar_CallsDefaultSerializeDateKey() {
        FakeJsonGenerator g = new FakeJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        StdKeySerializers.Default serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        Calendar calendar = Calendar.getInstance();
        serializer.serialize(calendar, g, provider);
        assertTrue(g.defaultSerializeDateKeyWasCalled);
        assertEquals(g.defaultSerializeDateKeyLongParam, calendar.getTimeInMillis());
    }

    @Test
    public void testSerialize_WhenTypeIsClass_WritesFieldNameAsClassName() {
        FakeJsonGenerator g = new FakeJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        StdKeySerializers.Default serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        Class<?> cls = String.class;
        serializer.serialize(cls, g, provider);
        assertEquals(g.writeFieldNameParam, cls.getName());
    }

    @Test
    public void testSerialize_WhenTypeIsEnum_IfWriteEnumsUsingToStringIsEnabled_WritesFieldNameAsEnumToStringValue() {
        FakeJsonGenerator g = new FakeJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        provider.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        StdKeySerializers.Default serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, TestEnum.class);
        TestEnum testEnum = TestEnum.VALUE1;
        serializer.serialize(testEnum, g, provider);
        assertEquals(g.writeFieldNameParam, testEnum.toString());
    }

    @Test
    public void testSerialize_WhenTypeIsEnum_IfWriteEnumsUsingToStringIsDisabled_WritesFieldNameAsEnumName() {
        FakeJsonGenerator g = new FakeJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        provider.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        StdKeySerializers.Default serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, TestEnum.class);
        TestEnum testEnum = TestEnum.VALUE1;
        serializer.serialize(testEnum, g, provider);
        assertEquals(g.writeFieldNameParam, testEnum.name());
    }

    @Test
    public void testSerialize_WhenTypeIsDefault_AndToStringIsDefault_WritesFieldNameAsToString() {
        FakeJsonGenerator g = new FakeJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        StdKeySerializers.Default serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, Object.class);
        Object obj = new Object();
        serializer.serialize(obj, g, provider);
        assertEquals(g.writeFieldNameParam, obj.toString());
    }

    private enum TestEnum {
        VALUE1,
        VALUE2
    }

    private class FakeJsonGenerator extends JsonGenerator {

        public boolean defaultSerializeDateKeyWasCalled = false;
        public Date defaultSerializeDateKeyDateParam;
        public long defaultSerializeDateKeyLongParam;
        public String writeFieldNameParam;

        @Override
        public void writeFieldName(String name) {
            writeFieldNameParam = name;
        }

        @Override
        public void defaultSerializeDateKey(Date date) {
            defaultSerializeDateKeyWasCalled = true;
            defaultSerializeDateKeyDateParam = date;
        }

        @Override
        public void defaultSerializeDateKey(long timestamp) {
            defaultSerializeDateKeyWasCalled = true;
            defaultSerializeDateKeyLongParam = timestamp;
        }

        // Other methods overridden with empty implementation

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }
    }
}