package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Test file for NumberSerializers class.
 */
public class NumberSerializersTest {

    @Test
    public void testSerializersRegistration() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, JsonSerializer<?>> serializers = mapper.getSerializationConfig()
                .getDefaultSerializationConfig().getSerializers();

        // Check registration of serializers
        Assertions.assertNotNull(serializers.get(Integer.class.getName()));
        Assertions.assertNotNull(serializers.get(Integer.TYPE.getName()));
        Assertions.assertNotNull(serializers.get(Long.class.getName()));
        Assertions.assertNotNull(serializers.get(Long.TYPE.getName()));
        Assertions.assertNotNull(serializers.get(Short.class.getName()));
        Assertions.assertNotNull(serializers.get(Short.TYPE.getName()));
        Assertions.assertNotNull(serializers.get(Float.class.getName()));
        Assertions.assertNotNull(serializers.get(Float.TYPE.getName()));
        Assertions.assertNotNull(serializers.get(Double.class.getName()));
        Assertions.assertNotNull(serializers.get(Double.TYPE.getName()));
        Assertions.assertNotNull(serializers.get(Byte.class.getName()));
        Assertions.assertNotNull(serializers.get(Byte.TYPE.getName()));
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public void testSerializeInteger() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer number = 123;
        String json = mapper.writeValueAsString(number);
        Assertions.assertEquals("123", json);
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public void testSerializeLong() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Long number = 123L;
        String json = mapper.writeValueAsString(number);
        Assertions.assertEquals("123", json);
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public void testSerializeShort() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Short number = (short) 123;
        String json = mapper.writeValueAsString(number);
        Assertions.assertEquals("123", json);
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public void testSerializeFloat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float number = 123.45f;
        String json = mapper.writeValueAsString(number);
        Assertions.assertEquals("123.45", json);
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public void testSerializeDouble() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Double number = 123.45;
        String json = mapper.writeValueAsString(number);
        Assertions.assertEquals("123.45", json);
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public void testSerializeByte() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Byte number = (byte) 123;
        String json = mapper.writeValueAsString(number);
        Assertions.assertEquals("123", json);
    }

    @Test
    public void testSerializeEmptyJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(null);
        Assertions.assertEquals("null", json);
    }
}