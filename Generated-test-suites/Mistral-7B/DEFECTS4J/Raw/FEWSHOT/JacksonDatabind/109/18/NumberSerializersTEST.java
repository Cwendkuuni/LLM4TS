package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonParser.NumberType.INT;
import static com.fasterxml.jackson.core.JsonParser.NumberType.LONG;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.test.BaseTest;

@SuppressWarnings("unused")
public class NumberSerializersTest extends BaseTest {

    public static void main(String[] args) throws Exception {
        runTest(NumberSerializersTest.class);
    }

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    static class TestBean {
        public int i;
        public Integer iWrapped;
        public long l;
        public Long lWrapped;
    }

    @Test
    public void testSerialization() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        ObjectWriter writer = mapper.writer();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        TestBean bean = new TestBean();
        bean.i = 1;
        bean.iWrapped = 1;
        bean.l = 2;
        bean.lWrapped = 2;

        String json = writer.writeValueAsString(bean);
        assertEquals("{\"i\":1,\"iWrapped\":1,\"l\":2,\"lWrapped\":2}", json);
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class);
        writer.setDefaultPrettyPrinterInUse(false);
        JsonNode result = mapper.readTree(writer.writeValueAsString(1));
        assertEquals(Integer.class.getName(), result.getClass().getName());
        assertEquals("1", result.asText());

        result = mapper.readTree(writer.writeValueAsString(Integer.MAX_VALUE));
        assertEquals(Integer.class.getName(), result.getClass().getName());
        assertEquals(Integer.MAX_VALUE, result.asInt());

        result = mapper.readTree(writer.writeValueAsString(Integer.MIN_VALUE));
        assertEquals(Integer.class.getName(), result.getClass().getName());
        assertEquals(Integer.MIN_VALUE, result.asInt());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<Number> intLikeSerializer = mapper.findValueSerializer(Number.class);
        writer.setDefaultPrettyPrinterInUse(false);
        JsonNode result = mapper.readTree(writer.writeValueAsString(1));
        assertEquals(Number.class.getName(), result.getClass().getName());
        assertEquals("1", result.asText());

        result = mapper.readTree(writer.writeValueAsString(Byte.MAX_VALUE));
        assertEquals(Byte.class.getName(), result.getClass().getName());
        assertEquals(Byte.MAX_VALUE, result.asInt());

        result = mapper.readTree(writer.writeValueAsString(Short.MAX_VALUE));
        assertEquals(Short.class.getName(), result.getClass().getName());
        assertEquals(Short.MAX_VALUE, result.asShort());

        result = mapper.readTree(writer.writeValueAsString(Integer.MAX_VALUE));
        assertEquals(Integer.class.getName(), result.getClass().getName());
        assertEquals(Integer.MAX_VALUE, result.asInt());

        result = mapper.readTree(writer.writeValueAsString(Long.MAX_VALUE));
        assertEquals(Long.class.getName(), result.getClass().getName());
        assertEquals(Long.MAX_VALUE, result.asLong());
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<Short> shortSerializer = mapper.findValueSerializer(Short.class);
        writer.setDefaultPrettyPrinterInUse(false);
        JsonNode result = mapper.readTree(writer.writeValueAsString((short) 1));
        assertEquals(Short.class.getName(), result.getClass().getName());
        assertEquals("1", result.asText());

        result = mapper.readTree(writer.writeValueAsString((short) Short.MAX_VALUE));
        assertEquals(Short.class.getName(), result.getClass().getName());
        assertEquals(Short.MAX_VALUE, result.asShort());

        result = mapper.readTree(writer.writeValueAsString((short) Short.MIN_VALUE));
        assertEquals(Short.class.getName(), result.getClass().getName());
        assertEquals(Short.MIN_VALUE, result.asShort());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<Long> longSerializer = mapper.findValueSerializer(Long.class);
        writer.setDefaultPrettyPrinterInUse(false);
        JsonNode result = mapper.readTree(writer.writeValueAsString(1L));
        assertEquals(Long.class.getName(), result.getClass().getName());
        assertEquals("1", result.asText());

        result = mapper.readTree(writer.writeValueAsString(Long.MAX_VALUE));
        assertEquals(Long.class.getName(), result.getClass().getName());
        assertEquals(Long.MAX_VALUE, result.asLong());

        result = mapper.readTree(writer.writeValueAsString(Long.MIN_VALUE));
        assertEquals(Long.class.getName(), result.getClass().getName());
        assertEquals(Long.MIN_VALUE, result.asLong());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<Float> floatSerializer = mapper.findValueSerializer(Float.class);
        writer.setDefaultPrettyPrinterInUse(false);
        JsonNode result = mapper.readTree(writer.writeValueAsString(1f));
        assertEquals(Float.class.getName(), result.getClass().getName());
        assertEquals("1.0", result.asText());

        result = mapper.readTree(writer.writeValueAsString(Float.MAX_VALUE));
        assertEquals(Float.class.getName(), result.getClass().getName());
        assertEquals(Float.MAX_VALUE, result.asFloat(), 0.0001f);

        result = mapper.readTree(writer.writeValueAsString(Float.MIN_VALUE));
        assertEquals(Float.class.getName(), result.getClass().getName());
        assertEquals(Float.MIN_VALUE, result.asFloat(), 0.0001f);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapperFactory.getMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<Double> doubleSerializer = mapper.findValueSerializer(Double.class);
        writer.setDefaultPrettyPrinterInUse(false);
        JsonNode result = mapper.readTree(writer.writeValueAsString(1d));
        assertEquals(Double.class.getName(), result.getClass().getName());
        assertEquals("1.0", result.asText());

        result = mapper.readTree(writer.writeValueAsString(Double.MAX_VALUE));
        assertEquals(Double.class.getName(), result.getClass().getName());
        assertEquals(Double.MAX_VALUE, result.asDouble(), 0.0001);

        result = mapper.readTree(writer.writeValueAsString(Double.MIN_VALUE));
        assertEquals(Double.class.getName(), result.getClass().getName());
        assertEquals(Double.MIN_VALUE, result.asDouble(), 0.0001);
    }
}