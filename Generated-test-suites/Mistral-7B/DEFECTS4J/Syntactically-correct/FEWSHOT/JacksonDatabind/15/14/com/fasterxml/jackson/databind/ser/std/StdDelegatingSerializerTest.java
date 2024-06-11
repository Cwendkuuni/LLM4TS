package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtil.createSerializerProvider;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @SuppressWarnings("unchecked")
    private static <T> StdDelegatingSerializer<T> createDelegatingSerializer(Converter<T,?> converter) {
        return new StdDelegatingSerializer<T>(converter);
    }

    @SuppressWarnings("unchecked")
    private static <T> StdDelegatingSerializer<T> createDelegatingSerializer(Class<T> cls, Converter<T,?> converter) {
        return new StdDelegatingSerializer<T>(cls, converter);
    }

    @Test
    public void testConvertValue() throws JsonMappingException, JsonProcessingException {
        Converter<String, Integer> stringToIntConverter = new StringToIntConverter();

        StdDelegatingSerializer<String> stringSerializer = createDelegatingSerializer(stringToIntConverter);
        assertEquals((Integer) 1, stringSerializer.convertValue("1"));

        StdDelegatingSerializer<Integer> intSerializer = createDelegatingSerializer(stringToIntConverter, Integer.class);
        assertEquals((Integer) 1, intSerializer.convertValue("1"));

        StdDelegatingSerializer<String> stringSerializer2 = createDelegatingSerializer(stringToIntConverter, String.class, null);
        assertEquals((Integer) 1, stringSerializer2.convertValue("1"));

        StdDelegatingSerializer<Integer> intSerializer2 = createDelegatingSerializer(stringToIntConverter, Integer.class, JsonSerializerFactory.instance.findValueSerializer(String.class));
        assertEquals((Integer) 1, intSerializer2.convertValue("1"));
    }

    @Test
    public void testSerialize() throws IOException {
        Converter<String, Integer> stringToIntConverter = new StringToIntConverter();
        StdDelegatingSerializer<String> stringSerializer = createDelegatingSerializer(stringToIntConverter);

        JsonSerializer<Object> intSerializer = createIntSerializer();
        JavaType intType = JsonTypeFactory.instance.constructType(Integer.class);
        StdDelegatingSerializer<String> stringSerializerWithDelegate = stringSerializer.withDelegate(stringToIntConverter, intType, intSerializer);

        ObjectNode node = TestUtil.writeValueAsTree("test", stringSerializerWithDelegate);
        JsonNode readValue = TestUtil.readTree(TestUtil.writeValueAsString(node));
        assertEquals(1, readValue.asInt());

        JsonGenerator gen = createSerializerProvider().getGenerator();
        stringSerializerWithDelegate.serialize(null, gen, createSerializerProvider());
        assertEquals(0, gen.getBufferSize());

        JsonGenerator genWithType = createSerializerProvider().getGenerator();
        stringSerializerWithDelegate.serializeWithType(null, genWithType, createSerializerProvider(), ObjectMapper.getDefaultInstance().getTypeSerializer());
        assertEquals(0, genWithType.getBufferSize());
    }

    @Test
    public void testResolve() throws JsonMappingException {
        Converter<String, Integer> stringToIntConverter = new StringToIntConverter();
        StdDelegatingSerializer<String> stringSerializer = createDelegatingSerializer(stringToIntConverter);

        JsonSerializer<Object> intSerializer = createIntSerializer();
        JavaType intType = JsonTypeFactory.instance.constructType(Integer.class);
        StdDelegatingSerializer<String> stringSerializerWithDelegate = stringSerializer.withDelegate(stringToIntConverter, intType, intSerializer);

        SerializerProvider provider = createSerializerProvider();
        provider.setResolverFactory(TestUtil.createEmptyContextualResolver());
        JsonSerializer<Object> resolvedDelegateSerializer = stringSerializerWithDelegate.resolve(provider);
        assertEquals(intSerializer, resolvedDelegateSerializer);

        StdDelegatingSerializer<String> resolvedSerializer = (StdDelegatingSerializer<String>) provider.findValueSerializer(String.class);
        assertEquals(stringSerializerWithDelegate, resolvedSerializer);
    }

    private static JsonSerializer<Object> createIntSerializer() {
        return new StdSerializer<Integer>(Integer.class) {
            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeNumber(value);
            }
        };
    }
}

class StringToIntConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String text) {
        return Integer.parseInt(text);
    }
}