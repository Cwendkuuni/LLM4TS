package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        JsonSerializer<?> serializer = allDeserializers.get(Integer.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.IntegerSerializer);

        serializer = allDeserializers.get(Integer.TYPE.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.IntegerSerializer);

        serializer = allDeserializers.get(Long.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.LongSerializer);

        serializer = allDeserializers.get(Long.TYPE.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.LongSerializer);

        serializer = allDeserializers.get(Byte.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.IntLikeSerializer);

        serializer = allDeserializers.get(Byte.TYPE.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.IntLikeSerializer);

        serializer = allDeserializers.get(Short.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.ShortSerializer);

        serializer = allDeserializers.get(Short.TYPE.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.ShortSerializer);

        serializer = allDeserializers.get(Double.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.DoubleSerializer);

        serializer = allDeserializers.get(Double.TYPE.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.DoubleSerializer);

        serializer = allDeserializers.get(Float.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.FloatSerializer);

        serializer = allDeserializers.get(Float.TYPE.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.FloatSerializer);
    }

    @SuppressWarnings("serial")
    public class TestSerializer extends NumberSerializers.Base<Integer> {

        public TestSerializer() {
            super(Integer.class, JsonParser.NumberType.INT, "number");
        }

        @Override
        public void serialize(Integer value, JsonGenerator gen,
                              SerializerProvider provider) throws IOException {
            gen.writeNumber(value.intValue());
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(_schemaType, true);
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor,
                                            JavaType typeHint) throws JsonMappingException {
            if (_isInt) {
                visitIntFormat(visitor, typeHint, _numberType);
            } else {
                visitFloatFormat(visitor, typeHint, _numberType);
            }
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider prov,
                                                   BeanProperty property) throws JsonMappingException {
            JsonFormat.Value format = findFormatOverrides(prov, property, handledType());
            if (format != null) {
                switch (format.getShape()) {
                    case STRING:
                        return ToStringSerializer.instance;
                    default:
                }
            }
            return this;
        }
    }

    @Test
    public void testBaseClass() {
        NumberSerializers.Base<Integer> serializer = new TestSerializer();
        Assert.assertEquals(Integer.class, serializer.handledType());

        JsonNode schema = serializer.getSchema(null, null);
        Assert.assertEquals("number", schema.asText());

        SerializerProvider provider = new SerializerProvider();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(provider);
        serializer.acceptJsonFormatVisitor(visitor, null);

        JsonSerializer<?> contextualSerializer = serializer.createContextual(null, null);
        Assert.assertEquals(serializer, contextualSerializer);
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Assert.assertEquals(Short.class, serializer.handledType());

        SerializerProvider provider = new SerializerProvider();
        JsonGenerator generator = new JsonSerializerGenerator(provider, null, null);
        serializer.serialize((short) 10, generator, provider);
        generator.close();
        Assert.assertEquals("10", generator.getOutput().toString());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Assert.assertEquals(Integer.class, serializer.handledType());

        SerializerProvider provider = new SerializerProvider();
        JsonGenerator generator = new JsonSerializerGenerator(provider, null, null);
        serializer.serialize(10, generator, provider);
        generator.close();
        Assert.assertEquals("10", generator.getOutput().toString());

        TypeSerializer typeSer = provider.findTypeSerializer(null, null);
        generator = new JsonSerializerGenerator(provider, null, typeSer);
        serializer.serializeWithType(10, generator, provider, typeSer);
        generator.close();
        Assert.assertEquals("10", generator.getOutput().toString());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        Assert.assertEquals(Number.class, serializer.handledType());

        SerializerProvider provider = new SerializerProvider();
        JsonGenerator generator = new JsonSerializerGenerator(provider, null, null);
        serializer.serialize((short) 10, generator, provider);
        generator.close();
        Assert.assertEquals("10", generator.getOutput().toString());
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        Assert.assertEquals(Long.class, serializer.handledType());

        SerializerProvider provider = new SerializerProvider();
        JsonGenerator generator = new JsonSerializerGenerator(provider, null, null);
        serializer.serialize(10L, generator, provider);
        generator.close();
        Assert.assertEquals("10", generator.getOutput().toString());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        Assert.assertEquals(Float.class, serializer.handledType());

        SerializerProvider provider = new SerializerProvider();
        JsonGenerator generator = new JsonSerializerGenerator(provider, null, null);
        serializer.serialize(10.0f, generator, provider);
        generator.close();
        Assert.assertEquals("10.0", generator.getOutput().toString());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        Assert.assertEquals(Double.class, serializer.handledType());

        SerializerProvider provider = new SerializerProvider();
        JsonGenerator generator = new JsonSerializerGenerator(provider, null, null);
        serializer.serialize(10.0, generator, provider);
        generator.close();
        Assert.assertEquals("10.0", generator.getOutput().toString());

        TypeSerializer typeSer = provider.findTypeSerializer(null, null);
        generator = new JsonSerializerGenerator(provider, null, typeSer);
        serializer.serializeWithType(10.0, generator, provider, typeSer);
        generator.close();
        Assert.assertEquals("10.0", generator.getOutput().toString());
    }
}