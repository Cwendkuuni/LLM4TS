package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class NumberSerializersTest extends BaseTest {

    public static void main(String[] args) throws Exception {
        runTest(NumberSerializersTest.class);
    }

    @Test
    public void testSerialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializers serializers = new NumberSerializers();
        serializers.addAll(mapper.getTypeFactory().constructMapType(Map.class, String.class, JsonSerializer.class));

        Map<String, JsonSerializer<?>> allDeserializers = serializers.getSerializers();

        testSerializationHelper(mapper, allDeserializers.get(Integer.class.getName()));
        testSerializationHelper(mapper, allDeserializers.get(Integer.TYPE.getName()));
        testSerializationHelper(mapper, allDeserializers.get(Long.class.getName()));
        testSerializationHelper(mapper, allDeserializers.get(Long.TYPE.getName()));
        testSerializationHelper(mapper, allDeserializers.get(Byte.class.getName()));
        testSerializationHelper(mapper, allDeserializers.get(Byte.TYPE.getName()));
        testSerializationHelper(mapper, allDeserializers.get(Short.class.getName()));
        testSerializationHelper(mapper, allDeserializers.get(Short.TYPE.getName()));
        testSerializationHelper(mapper, allDeserializers.get(Double.class.getName()));
        testSerializationHelper(mapper, allDeserializers.get(Double.TYPE.getName()));
        testSerializationHelper(mapper, allDeserializers.get(Float.class.getName()));
        testSerializationHelper(mapper, allDeserializers.get(Float.TYPE.getName()));
    }

    private void testSerializationHelper(ObjectMapper mapper, JsonSerializer<?> serializer) throws IOException, JsonProcessingException {
        ObjectWriter writer = mapper.writer();
        String json = writer.withRootValueSerializer(serializer)
                .writeValueAsString(123);
        JsonParser parser = mapper.getFactory()
                .getParser();
        Object readValue = parser.parse(json);
        assertEquals(123, readValue);
    }
}