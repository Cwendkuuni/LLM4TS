package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.TestBase;

public class NumberSerializersTest extends TestBase {

    @SuppressWarnings("unchecked")
    private static <T> JsonSerializer<T> getSerializer(Class<T> type) throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getSerializerProvider().findValueSerializer(type);
    }

    @Test
    public void testShortSerializer() throws JsonMappingException, IOException {
        ShortSerializer serializer = getSerializer(Short.class);
        String json = mapper(new Short("123"), serializer);
        assertEquals("123", json);
    }

    @Test
    public void testIntegerSerializer() throws JsonMappingException, IOException {
        IntegerSerializer serializer = getSerializer(Integer.class);
        String json = mapper(new Integer(123), serializer);
        assertEquals("123", json);
    }

    @Test
    public void testIntLikeSerializer() throws JsonMappingException, IOException {
        IntLikeSerializer serializer = getSerializer(Number.class);
        String json = mapper(new Integer(123), serializer);
        assertEquals("123", json);
    }

    @Test
    public void testLongSerializer() throws JsonMappingException, IOException {
        LongSerializer serializer = getSerializer(Long.class);
        String json = mapper(new Long(123456), serializer);
        assertEquals("123456", json);
    }

    @Test
    public void testFloatSerializer() throws JsonMappingException, IOException {
        FloatSerializer serializer = getSerializer(Float.class);
        String json = mapper(new Float(123.45f), serializer);
        assertEquals("123.45", json);
    }

    @Test
    public void testDoubleSerializer() throws JsonMappingException, IOException {
        DoubleSerializer serializer = getSerializer(Double.class);
        String json = mapper(new Double(123.456), serializer);
        assertEquals("123.456", json);
    }

    @Test
    public void testIntegerSerializerWithJsonFormat() throws JsonMappingException, IOException {
        JsonFormat.Shape shape = JsonFormat.Shape.NUMBER;
        JsonFormat format = new JsonFormat(shape);

        IntegerSerializer serializer = new IntegerSerializer();
        ContextualSerializer contextualSerializer = serializer.createContextual(null, null);

        String json = mapper(new Integer(123), SerializerProvider.stdInstance(), contextualSerializer, null, format, null, null);
        assertEquals("123", json);
    }

    @Test
    public void testIntLikeSerializerWithJsonFormat() throws JsonMappingException, IOException {
        JsonFormat.Shape shape = JsonFormat.Shape.NUMBER;
        JsonFormat format = new JsonFormat(shape);

        IntLikeSerializer serializer = new IntLikeSerializer();
        ContextualSerializer contextualSerializer = serializer.createContextual(null, null);

        String json = mapper(new Integer(123), SerializerProvider.stdInstance(), contextualSerializer, null, format, null, null);
        assertEquals("123", json);
    }

    @Test
    public void testAddAllToMap() throws JsonMappingException {
        Map<String, JsonSerializer<?>> allDeserializers = new LinkedHashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(6, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
    }

    private static String mapper(Object value, JsonSerializer<?> serializer) throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, value, serializer);
        return writer.toString();
    }

    private static String mapper(Object value, SerializerProvider serializerProvider, ContextualSerializer contextualSerializer, TypeSerializer typeSerializer, JsonFormat jsonFormat, Object context, Object valueHandler) throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleSerializerProvider provider = new SimpleSerializerProvider();
        provider.setSerializerInstance(contextualSerializer);
        provider.setTypeSerializer(typeSerializer);
        provider.setDefaultSerializerProvider(serializerProvider);
        provider.setDefaultSerializer(contextualSerializer);
        provider.setActiveDefaultSerializer(contextualSerializer);

        JsonFactory jsonFactory = new JsonFactory();
        JsonStreamContext context1 = new JsonStreamContext(new ByteArrayOutputStream(), jsonFactory, null, null, null, jsonFormat, context, valueHandler, provider);
        JsonParser parser = jsonFactory.createParser(context1);
        parser.nextToken();
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = new ByteArrayOutputStream().toByteArray();
        parser.readValueAsBytes(bytes, 0, bytes.length);
        String json = new String(bytes, "UTF-8");
        parser.close();

        return json;
    }
}