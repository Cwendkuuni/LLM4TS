import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Properties;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SerializationConfig config = mapper.getSerializationConfig();

        // Test with null rawKeyType
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, true);
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof StdKeySerializers.Dynamic);

        // Test with String rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, true);
        Assert.assertTrue(serializer instanceof StdKeySerializers.StringKeySerializer);

        // Test with Number rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, true);
        Assert.assertTrue(serializer instanceof StdKeySerializers.DefaultKeySerializer);

        // Test with Class rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, true);
        Assert.assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test with Date rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, true);
        Assert.assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test with Calendar rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, true);
        Assert.assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test with UUID rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, true);
        Assert.assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test with Enum rawKeyType
        EnumSet<MyEnum> enums = EnumSet.allOf(MyEnum.class);
        serializer = StdKeySerializers.getStdKeySerializer(config, MyEnum.class, true);
        Assert.assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test with custom class rawKeyType
        MyCustomClass customClass = new MyCustomClass();
        serializer = StdKeySerializers.getStdKeySerializer(config, customClass.getClass(), true);
        Assert.assertTrue(serializer instanceof StdKeySerializers.Dynamic);
    }

    @Test(expected = NullPointerException.class)
    public void testGetStdKeySerializerNullConfig() {
        StdKeySerializers.getStdKeySerializer(null, Object.class, true);
    }

    @Test(expected = NullPointerException.class)
    public void testGetStdKeySerializerNullRawKeyType() {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        StdKeySerializers.getStdKeySerializer(config, null, true);
    }

    @Test(expected = NullPointerException.class)
    public void testGetStdKeySerializerNullUseDefault() {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        StdKeySerializers.getStdKeySerializer(config, Object.class, null);
    }

    @Test
    public void testGetFallbackKeySerializer() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SerializationConfig config = mapper.getSerializationConfig();

        // Test with null rawKeyType
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        Assert.assertNull(serializer);

        // Test with String rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        Assert.assertTrue(serializer instanceof StdKeySerializers.StringKeySerializer);

        // Test with Number rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, int.class);
        Assert.assertTrue(serializer instanceof StdKeySerializers.DefaultKeySerializer);

        // Test with Class rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Class.class);
        Assert.assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test with Date rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Date.class);
        Assert.assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test with Calendar rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Calendar.class);
        Assert.assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test with UUID rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, java.util.UUID.class);
        Assert.assertTrue(serializer instanceof StdKeySerializers.Default);

        // Test with Enum rawKeyType
        EnumSet<MyEnum> enums = EnumSet.allOf(MyEnum.class);
        serializer = StdKeySerializers.getFallbackKeySerializer(config, MyEnum.class);
        Assert.assertTrue(serializer instanceof StdKeySerializers.Dynamic);

        // Test with custom class rawKeyType
        MyCustomClass customClass = new MyCustomClass();
        serializer = StdKeySerializers.getFallbackKeySerializer(config, customClass.getClass());
        Assert.assertTrue(serializer instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testDefault() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SerializationConfig config = mapper.getSerializationConfig();

        // Test with String key
        ObjectWriter writer = mapper.writer();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        writer.writeValueAsString(new ObjectMapper().writeValueAsString(new String("test")));
        String result = writer.writeValueAsString(new Object());
        Assert.assertEquals("test", result);

        // Test with Number key
        result = writer.writeValueAsString(new ObjectMapper().writeValueAsString(new Integer(123)));
        Assert.assertEquals("123", result);

        // Test with Class key
        result = writer.writeValueAsString(new ObjectMapper().writeValueAsString(Class.class));
        Assert.assertEquals("class java.lang.Class", result);

        // Test with Date key
        Date date = new Date();
        result = writer.writeValueAsString(new ObjectMapper().writeValueAsString(date));
        Assert.assertEquals(date.toString(), result);

        // Test with Calendar key
        Calendar calendar = Calendar.getInstance();
        result = writer.writeValueAsString(new ObjectMapper().writeValueAsString(calendar));
        Assert.assertEquals(calendar.toString(), result);

        // Test with UUID key
        java.util.UUID uuid = java.util.UUID.randomUUID();
        result = writer.writeValueAsString(new ObjectMapper().writeValueAsString(uuid));
        Assert.assertEquals(uuid.toString(), result);

        // Test with Enum key
        EnumSet<MyEnum> enums = EnumSet.allOf(MyEnum.class);
        result = writer.writeValueAsString(new ObjectMapper().writeValueAsString(enums));
        Assert.assertEquals(enums.stream().map(Enum::name).collect(Collectors.joining(",")), result);

        // Test with custom class key
        MyCustomClass customClass = new MyCustomClass();
        result = writer.writeValueAsString(new ObjectMapper().writeValueAsString(customClass));
        Assert.assertEquals(customClass.toString(), result);
    }
}

enum MyEnum {
    ONE, TWO, THREE
}

class MyCustomClass {
    @Override
    public String toString() {
        return "MyCustomClass";
    }
}