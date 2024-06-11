
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        
        // Test with Date class
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(null, Date.class, true);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof StdKeySerializers.Default);
        Assert.assertEquals(defaultSerializer._typeId, ((StdKeySerializers.Default) result)._typeId);
        Assert.assertEquals(Date.class, result.handledType());

        // Test with String class
        result = StdKeySerializers.getStdKeySerializer(null, String.class, true);
        Assert.assertNotNull(result);
        Assert.assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
        Assert.assertEquals(String.class, result.handledType());

        // Test with primitive boolean class
        result = StdKeySerializers.getStdKeySerializer(null, boolean.class, true);
        Assert.assertNotNull(result);
        Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
        Assert.assertEquals(boolean.class, result.handledType());

        // Test with Number class
        result = StdKeySerializers.getStdKeySerializer(null, Number.class, true);
        Assert.assertNotNull(result);
        Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
        Assert.assertEquals(Number.class, result.handledType());

        // Test with Class class
        result = StdKeySerializers.getStdKeySerializer(null, Class.class, true);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof StdKeySerializers.Default);
        Assert.assertEquals(StdKeySerializers.Default.TYPE_CLASS, ((StdKeySerializers.Default) result)._typeId);
        Assert.assertEquals(Class.class, result.handledType());

        // Test with Date class
        result = StdKeySerializers.getStdKeySerializer(null, Date.class, true);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof StdKeySerializers.Default);
        Assert.assertEquals(StdKeySerializers.Default.TYPE_DATE, ((StdKeySerializers.Default) result)._typeId);
        Assert.assertEquals(Date.class, result.handledType());

        // Test with Calendar class
        result = StdKeySerializers.getStdKeySerializer(null, Calendar.class, true);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof StdKeySerializers.Default);
        Assert.assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, ((StdKeySerializers.Default) result)._typeId);
        Assert.assertEquals(Calendar.class, result.handledType());

        // Test with UUID class
        result = StdKeySerializers.getStdKeySerializer(null, UUID.class, true);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof StdKeySerializers.Default);
        Assert.assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) result)._typeId);
        Assert.assertEquals(UUID.class, result.handledType());

        // Test with other class
        result = StdKeySerializers.getStdKeySerializer(null, Object.class, true);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof StdKeySerializers.Dynamic);
        Assert.assertEquals(String.class, result.handledType());

        // Test with null class
        result = StdKeySerializers.getStdKeySerializer(null, null, true);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof StdKeySerializers.Dynamic);
        Assert.assertEquals(String.class, result.handledType());
    }

    @Test
    public void testGetFallbackKeySerializer() {
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, Enum.class);

        // Test with Enum class
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(null, Enum.class);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof StdKeySerializers.Default);
        Assert.assertEquals(defaultSerializer._typeId, ((StdKeySerializers.Default) result)._typeId);
        Assert.assertEquals(Enum.class, result.handledType());

        // Test with null class
        result = StdKeySerializers.getFallbackKeySerializer(null, null);
        Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);

        // Test with non-enum class
        result = StdKeySerializers.getFallbackKeySerializer(null, Object.class);
        Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testDefaultSerializer() throws IOException {
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_STRING, String.class);
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        // Test with string
        String str = "Test";
        defaultSerializer.serialize(str, jsonGenerator, provider);
        Assert.assertEquals(str, jsonGenerator.getCurrentName());

        // Test with Date
        Date date = new Date();
        defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        defaultSerializer.serialize(date, jsonGenerator, provider);
        Assert.assertEquals(date, jsonGenerator.getCurrentName());

        // Test with Calendar
        Calendar calendar = Calendar.getInstance();
        defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        defaultSerializer.serialize(calendar, jsonGenerator, provider);
        Assert.assertEquals(calendar.getTimeInMillis(), jsonGenerator.getCurrentName());

        // Test with Enum
        Enum<TestEnum> enumValue = TestEnum.VALUE1;
        defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, enumValue.getClass());
        jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        defaultSerializer.serialize(enumValue, jsonGenerator, provider);
        Assert.assertEquals(enumValue.name(), jsonGenerator.getCurrentName());

        // Test with non-standard class
        Integer intValue = 1;
        defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, intValue.getClass());
        jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        defaultSerializer.serialize(intValue, jsonGenerator, provider);
        Assert.assertEquals(intValue.toString(), jsonGenerator.getCurrentName());
    }

    @Test
    public void testDynamicSerializer() throws IOException {
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();

        // Test with string
        String str = "Test";
        dynamicSerializer.serialize(str, jsonGenerator, provider);
        Assert.assertEquals(str, jsonGenerator.getCurrentName());

        // Test with Date
        Date date = new Date();
        jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        dynamicSerializer.serialize(date, jsonGenerator, provider);
        Assert.assertEquals(date.toString(), jsonGenerator.getCurrentName());

        // Test with non-standard class
        Integer intValue = 1;
        jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        dynamicSerializer.serialize(intValue, jsonGenerator, provider);
        Assert.assertEquals(intValue.toString(), jsonGenerator.getCurrentName());
    }

    @Test
    public void testStringKeySerializer() throws IOException {
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();

        // Test with string
        String str = "Test";
        stringKeySerializer.serialize(str, jsonGenerator, provider);
        Assert.assertEquals(str, jsonGenerator.getCurrentName());
    }
}
