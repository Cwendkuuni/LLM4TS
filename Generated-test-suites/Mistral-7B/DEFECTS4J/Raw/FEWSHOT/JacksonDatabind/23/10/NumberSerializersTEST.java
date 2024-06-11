package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonParser.NumberType.INT;
import static com.fasterxml.jackson.core.JsonParser.NumberType.LONG;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() throws Exception {
        ObjectMapper mapper = new JsonMapper();
        Map<String, JsonSerializer<?>> allDeserializers = mapper.getSerializationConfig().getDefaultSerializerProvider().getDeserializerProvider().findDeserializersForLangKey(Object.class);
        NumberSerializers.addAll(allDeserializers);
        assertEquals(11, allDeserializers.size());

        for (Map.Entry<String, JsonSerializer<?>> entry : allDeserializers.entrySet()) {
            assertEquals(entry.getKey(), entry.getValue().getClass().getSimpleName());
        }
    }

    @Test
    public void testIntegerSerializer() throws Exception {
        JsonMapper mapper = new JsonMapper();
        IntegerSerializer intSerializer = mapper.findValueSerializer(Integer.class);

        JsonParser parser = mapper.getFactory().createParser("42");
        assertEquals(Integer.TYPE, parser.readValueAs(TypeReference.of(Integer.class)));

        JsonGenerator gen = mapper.createGenerator(new JsonFactory(), null);
        intSerializer.serialize(42, gen, null);
        assertEquals("42", gen.getOutputValueAsString());
        gen.close();

        parser.nextToken();
        Object readValue = intSerializer.deserialize(parser, Integer.TYPE);
        assertEquals(42, readValue);
    }

    @Test
    public void testIntLikeSerializer() throws Exception {
        JsonMapper mapper = new JsonMapper();
        IntLikeSerializer intLikeSerializer = mapper.findValueSerializer(Byte.class);

        JsonParser parser = mapper.getFactory().createParser("42");
        assertEquals(Byte.TYPE, parser.readValueAs(TypeReference.of(Byte.class)));

        JsonGenerator gen = mapper.createGenerator(new JsonFactory(), null);
        intLikeSerializer.serialize(42, gen, null);
        assertEquals("42", gen.getOutputValueAsString());
        gen.close();

        parser.nextToken();
        Object readValue = intLikeSerializer.deserialize(parser, Byte.TYPE);
        assertEquals((byte) 42, readValue);
    }

    @Test
    public void testShortSerializer() throws Exception {
        JsonMapper mapper = new JsonMapper();
        ShortSerializer shortSerializer = mapper.findValueSerializer(Short.class);

        JsonParser parser = mapper.getFactory().createParser("42");
        assertEquals(Short.TYPE, parser.readValueAs(TypeReference.of(Short.class)));

        JsonGenerator gen = mapper.createGenerator(new JsonFactory(), null);
        shortSerializer.serialize((short) 42, gen, null);
        assertEquals("42", gen.getOutputValueAsString());
        gen.close();

        parser.nextToken();
        Object readValue = shortSerializer.deserialize(parser, Short.TYPE);
        assertEquals((short) 42, readValue);
    }

    @Test
    public void testLongSerializer() throws Exception {
        JsonMapper mapper = new JsonMapper();
        LongSerializer longSerializer = mapper.findValueSerializer(Long.class);

        JsonParser parser = mapper.getFactory().createParser("42");
        assertEquals(Long.TYPE, parser.readValueAs(TypeReference.of(Long.class)));

        JsonGenerator gen = mapper.createGenerator(new JsonFactory(), null);
        longSerializer.serialize(42L, gen, null);
        assertEquals("42", gen.getOutputValueAsString());
        gen.close();

        parser.nextToken();
        Object readValue = longSerializer.deserialize(parser, Long.TYPE);
        assertEquals(42L, readValue);
    }

    @Test
    public void testFloatSerializer() throws Exception {
        JsonMapper mapper = new JsonMapper();
        FloatSerializer floatSerializer = mapper.findValueSerializer(Float.class);

        JsonParser parser = mapper.getFactory().createParser("42.0");
        assertEquals(Float.TYPE, parser.readValueAs(TypeReference.of(Float.class)));

        JsonGenerator gen = mapper.createGenerator(new JsonFactory(), null);
        gen.setSerializationInclusion(Json.Include.NON_NULL);
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        floatSerializer.serialize(42.0f, gen, null);
        assertEquals("42.0", gen.getOutputValueAsString());
        gen.close();

        parser.nextToken();
        Object readValue = floatSerializer.deserialize(parser, Float.TYPE);
        assertEquals(42.0f, readValue);
    }

    @Test
    public void testDoubleSerializer() throws Exception {
        JsonMapper mapper = new JsonMapper();
        DoubleSerializer doubleSerializer = mapper.findValueSerializer(Double.class);

        JsonParser parser = mapper.getFactory().createParser("42.0");
        assertEquals(Double.TYPE, parser.readValueAs(TypeReference.of(Double.class)));

        JsonGenerator gen = mapper.createGenerator(new JsonFactory(), null);
        doubleSerializer.serialize(42.0, gen, null);
        assertEquals("42.0", gen.getOutputValueAsString());
        gen.close();

        parser.nextToken();
        Object readValue = doubleSerializer.deserialize(parser, Double.TYPE);
        assertEquals(42.0, readValue);
    }

    @Test
    public void testContextualSerialization() throws Exception {
        JsonMapper mapper = new JsonMapper();

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        static class TestBean {
            private Integer i;

            public Integer getI() {
                return i;
            }

            public void setI(Integer i) {
                this.i = i;
            }
        }

        AnnotatedClass ac = mapper.getAnnotationIntrospector().findProperties(TestBean.class);
        TestBean bean = new TestBean();
        bean.setI(42);

        JsonParser parser = mapper.getFactory().createParser(mapper.writerWithRootValue(bean).getValueAsString());
        Object deserialized = parser.readValueAs(TestBean.class);
        assertEquals(bean, deserialized);

        JsonGenerator gen = mapper.createGenerator(new JsonFactory(), null);
        gen.setSerializationInclusion(Json.Include.NON_NULL);
        mapper.writeValue(gen, bean);
        assertEquals(mapper.writerWithRootValue(bean).getValueAsString(), gen.getOutputValueAsString());
        gen.close();
    }

    @Test
    public void testNullSerializer() throws Exception {
        JsonMapper mapper = new JsonMapper();
        JsonParser parser = mapper.getFactory().createParser("");
        ObjectSerializer nullSerializer = mapper.findNullValueSerializer(Byte.TYPE);

        assertNull(nullSerializer.serialize(null, null, null));
        assertNull(nullSerializer.deserialize(parser, Byte.TYPE));
    }
}