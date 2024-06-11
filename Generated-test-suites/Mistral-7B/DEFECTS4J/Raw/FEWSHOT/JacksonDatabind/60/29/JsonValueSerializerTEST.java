package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.test.BaseTest;

public class JsonValueSerializerTest extends BaseTest {

    @SuppressWarnings("unchecked")
    private static <T> JsonValueSerializer<T> createJsonValueSerializer(Class<T> clazz, Object value) {
        ObjectMapper mapper = new ObjectMapper();
        return (JsonValueSerializer<T>) mapper.findValueSerializer(clazz, null);
    }

    @Test
    public void testSerialize() throws Exception {
        String value = "test";
        JsonValueSerializer<String> serializer = createJsonValueSerializer(String.class, value);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(value, gen, null);
        assertEquals("\"test\"", gen.getOutputContentAsString());
    }

    @Test
    public void testSerializeWithType() throws Exception {
        int value = 1;
        JsonValueSerializer<Integer> serializer = createJsonValueSerializer(Integer.class, value);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        serializer.serializeWithType(value, gen, null, typeSerializer);
        assertEquals("1", gen.getOutputContentAsString());
        assertEquals("number", typeSerializer.writeTypePrefixForScalar(null, gen).toString());
        assertEquals("number", typeSerializer.writeTypeSuffixForScalar(null, gen).toString());
    }

    @Test
    public void testSerializeEnum() throws Exception {
        Foo foo = Foo.BAR;
        JsonValueSerializer<Foo> serializer = createJsonValueSerializer(Foo.class, foo);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(foo, gen, null);
        assertEquals("\"BAR\"", gen.getOutputContentAsString());
    }

    @JsonValue
    public static class Foo {
        public static enum EnumValue {
            FOO,
            BAR
        }

        public EnumValue getValue() {
            return value;
        }

        private EnumValue value;
    }
}