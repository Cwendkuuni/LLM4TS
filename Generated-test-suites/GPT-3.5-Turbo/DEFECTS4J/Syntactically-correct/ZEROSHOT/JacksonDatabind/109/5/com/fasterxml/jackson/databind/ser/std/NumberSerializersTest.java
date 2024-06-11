package com.fasterxml.jackson.databind.ser.std;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Assert;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        Assert.assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Long.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Short.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Double.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Float.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testBaseDeserializeWithType() throws IOException {
        NumberSerializers.Base<Object> base = new NumberSerializers.Base<Object>(Number.class, JsonParser.NumberType.INT, "integer") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeNumber(((Number) value).intValue());
            }
        };
        SerializerProvider provider = new SerializerProvider();
        TypeSerializer typeSer = null;
        base.serializeWithType(1, null, provider, typeSer);
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProvider();
        shortSerializer.serialize((short) 1, gen, provider);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProvider();
        integerSerializer.serialize(1, gen, provider);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProvider();
        intLikeSerializer.serialize(1, gen, provider);
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.class);
        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProvider();
        longSerializer.serialize(1L, gen, provider);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProvider();
        floatSerializer.serialize(1.0f, gen, provider);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProvider();
        doubleSerializer.serialize(1.0, gen, provider);
    }
}