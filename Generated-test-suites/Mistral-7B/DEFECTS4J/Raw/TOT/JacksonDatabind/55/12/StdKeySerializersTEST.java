import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class StdKeySerializersTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetStdKeySerializer() throws Exception {
        // Test with null rawKeyType
        assertNull(StdKeySerializers.getStdKeySerializer(null, null, true));

        // Test with Object.class rawKeyType
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, Object.class, true));

        // Test with String rawKeyType
        assertSame(StdKeySerializers.DEFAULT_STRING_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, String.class, true));

        // Test with primitive types
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, int.class, true));

        // Test with Number rawKeyType
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, Double.class, true));

        // Test with Class rawKeyType
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, Class.class, true));

        // Test with Date rawKeyType
        assertSame(StdKeySerializers.getDefault(), StdKeySerializers.getStdKeySerializer(null, Date.class, true));

        // Test with Calendar rawKeyType
        assertSame(StdKeySerializers.getDefault(), StdKeySerializers.getStdKeySerializer(null, Calendar.class, true));

        // Test with UUID rawKeyType
        assertSame(StdKeySerializers.getDefault(), StdKeySerializers.getStdKeySerializer(null, UUID.class, true));

        // Test with enum rawKeyType
        assertSame(StdKeySerializers.getDefault(), StdKeySerializers.getStdKeySerializer(null, MyEnum.class, true));

        // Test with custom class rawKeyType
        assertSame(new MyCustomSerializer(), StdKeySerializers.getStdKeySerializer(null, MyCustomClass.class, true));
    }

    @Test
    public void testGetFallbackKeySerializer() throws Exception {
        // Test with null rawKeyType
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getFallbackKeySerializer(null, null));

        // Test with String rawKeyType
        assertSame(StdKeySerializers.DEFAULT_STRING_SERIALIZER, StdKeySerializers.getFallbackKeySerializer(null, String.class));

        // Test with primitive types
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getFallbackKeySerializer(null, int.class));

        // Test with Number rawKeyType
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getFallbackKeySerializer(null, Double.class));

        // Test with Class rawKeyType
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getFallbackKeySerializer(null, Class.class));

        // Test with Date rawKeyType
        assertSame(StdKeySerializers.getDefault(), StdKeySerializers.getFallbackKeySerializer(null, Date.class));

        // Test with Calendar rawKeyType
        assertSame(StdKeySerializers.getDefault(), StdKeySerializers.getFallbackKeySerializer(null, Calendar.class));

        // Test with UUID rawKeyType
        assertSame(StdKeySerializers.getDefault(), StdKeySerializers.getFallbackKeySerializer(null, UUID.class));

        // Test with Enum rawKeyType
        assertSame(new MyEnumSerializer(), StdKeySerializers.getFallbackKeySerializer(null, MyEnum.class));

        // Test with custom class rawKeyType
        assertNull(StdKeySerializers.getFallbackKeySerializer(null, MyCustomClass.class));
    }

    @Test
    public void testDefault() throws Exception {
        assertSame(StdKeySerializers.getDefault(), StdKeySerializers.getDefault());
    }

    @Test
    public void testDefaultSerializeDateKey() throws JsonGenerationException, JsonMappingException, IOException {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(stringWriter);
        mapper.writeValue(jsonGenerator, StdKeySerializers.getDefault(), date);

        assertEquals("\"2022-12-31T15:23:21.000Z\"", stringWriter.toString());
    }

    @Test
    public void testDefaultSerializeDateKeyMillis() throws JsonGenerationException, JsonMappingException, IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Date date = calendar.getTime();

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(stringWriter);
        mapper.writerWithView(JsonView.Full.class).writeValue(jsonGenerator, StdKeySerializers.getDefault(), date);

        assertEquals("\"2022-12-31T15:23:21.083Z\"", stringWriter.toString());
    }

    @Test
    public void testDefaultSerializeClassKey() throws JsonGenerationException, JsonMappingException, IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(stringWriter);
        mapper.writeValue(jsonGenerator, StdKeySerializers.getDefault(), MyCustomClass.class);

        assertEquals("\"com.mypackage.MyCustomClass\"", stringWriter.toString());
    }

    @Test
    public void testDefaultSerializeEnumKey() throws JsonGenerationException, JsonMappingException, IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(stringWriter);
        mapper.writeValue(jsonGenerator, StdKeySerializers.getDefault(), MyEnum.RED);

        assertEquals("\"RED\"", stringWriter.toString());
    }

    @Test
    public void testDefaultSerializeStringKey() throws JsonGenerationException, JsonMappingException, IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(stringWriter);
        mapper.writeValue(jsonGenerator, StdKeySerializers.getDefault(), "test");

        assertEquals("\"test\"", stringWriter.toString());
    }

    @Test
    public void testDynamicSerializeKey() throws JsonGenerationException, JsonMappingException, IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(stringWriter);

        ObjectMapper dynamicMapper = new ObjectMapper();
        dynamicMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        dynamicMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        dynamicMapper.writeValue(jsonGenerator, new DynamicKeyTestObject("test123"));

        assertEquals("{\"testKey\":\"test123\"}", stringWriter.toString());
    }

    @Test
    public void testDynamicFindAndAddKeySerializer() throws JsonGenerationException, JsonMappingException, IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(stringWriter);

        ObjectMapper dynamicMapper = new ObjectMapper();
        dynamicMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        dynamicMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        StdKeySerializers.Dynamic dynamic = new StdKeySerializers.Dynamic();
        PropertySerializerMap map = dynamic._dynamicSerializers;

        ObjectMapper testMapper = new ObjectMapper();
        JsonSerializer<Object> serializer = testMapper.getSerializerProvider().findValueSerializer(MyCustomClass.class);
        map.findAndAddKeySerializer(MyCustomClass.class, testMapper.getSerializerProvider(), null);

        dynamicMapper.writeValue(jsonGenerator, dynamic, new MyCustomClass());

        assertEquals("{\"testKey\":{\"@class\":\"com.mypackage.MyCustomClass\"}}", stringWriter.toString());
    }

    @Test
    public void testStringKeySerializer() throws JsonGenerationException, JsonMappingException, IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(stringWriter);
        mapper.writeValue(jsonGenerator, StdKeySerializers.STRING_KEY_SERIALIZER, "test");

        assertEquals("\"test\"", stringWriter.toString());
    }

    private static class MyCustomClass {
        private String testKey;

        public String getTestKey() {
            return testKey;
        }

        public void setTestKey(String testKey) {
            this.testKey = testKey;
        }
    }

    private static class MyCustomSerializer extends StdSerializer<MyCustomClass> {
        public MyCustomSerializer() { this(null); }

        protected MyCustomSerializer(Class<MyCustomClass> t) { super(t, false); }

        @Override
        public void serialize(MyCustomClass value, JsonGenerator g, SerializerProvider provider) throws IOException {
            g.writeStartObject();
            g.writeFieldName("testKey");
            StdKeySerializers.StringKeySerializer stringKeySerializer = StdKeySerializers.STRING_KEY_SERIALIZER;
            stringKeySerializer.serialize(value.getTestKey(), g, provider);
            g.writeEndObject();
        }
    }

    private static enum MyEnum {
        RED, GREEN, BLUE
    }

    private static class MyEnumSerializer extends StdSerializer<MyEnum> {
        public MyEnumSerializer() { this(null); }

        protected MyEnumSerializer(Class<MyEnum> t) { super(t, false); }

        @Override
        public void serialize(MyEnum value, JsonGenerator g, SerializerProvider provider) throws IOException {
            g.writeFieldName(value.name());
        }
    }

    private static class DynamicKeyTestObject {
        private String testKey;

        public String getTestKey() {
            return testKey;
        }

        public void setTestKey(String testKey) {
            this.testKey = testKey;
        }
    }
}