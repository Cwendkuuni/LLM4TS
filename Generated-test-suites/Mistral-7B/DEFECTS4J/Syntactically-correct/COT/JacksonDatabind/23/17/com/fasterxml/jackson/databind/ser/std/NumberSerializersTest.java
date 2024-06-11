import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.Math;
import java.lang.reflect.Type;

public class NumberSerializersTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testAddAll() {
        // This method is a static method and not directly testable, but we can test the side effect of it
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        // Check if all expected classes are added to the map
        Assert.assertEquals(10, allDeserializers.size());
        Assert.assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Long.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Short.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Float.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Double.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer_NormalCase() throws IOException {
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        shortSerializer.serialize(Short.MIN_VALUE, gen, provider);
        String serializedShort = gen.getOutputBuffer().toString();
        gen.close();

        Assert.assertEquals("-2147483640", serializedShort);
    }

    @Test
    public void testShortSerializer_MaxValue() throws IOException {
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        shortSerializer.serialize(Short.MAX_VALUE, gen, provider);
        String serializedShort = gen.getOutputBuffer().toString();
        gen.close();

        Assert.assertEquals("32767", serializedShort);
    }

    @Test
    public void testIntegerSerializer_NormalCase() throws IOException {
        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        integerSerializer.serialize(Integer.MIN_VALUE, gen, provider);
        String serializedInt = gen.getOutputBuffer().toString();
        gen.close();

        Assert.assertEquals("-2147483648", serializedInt);
    }

    @Test
    public void testIntegerSerializer_MaxValue() throws IOException {
        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        integerSerializer.serialize(Integer.MAX_VALUE, gen, provider);
        String serializedInt = gen.getOutputBuffer().toString();
        gen.close();

        Assert.assertEquals("2147483647", serializedInt);
    }

    @Test
    public void testIntLikeSerializer_NormalCase() throws IOException {
        IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        intLikeSerializer.serialize(Byte.MIN_VALUE, gen, provider);
        String serializedByte = gen.getOutputBuffer().toString();
        gen.close();

        Assert.assertEquals("-128", serializedByte);
    }

    @Test
    public void testIntLikeSerializer_MaxValue() throws IOException {
        IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        intLikeSerializer.serialize(Byte.MAX_VALUE, gen, provider);
        String serializedByte = gen.getOutputBuffer().toString();
        gen.close();

        Assert.assertEquals("127", serializedByte);
    }

    @Test
    public void testLongSerializer_NormalCase() throws IOException {
        LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        longSerializer.serialize(Long.MIN_VALUE, gen, provider);
        String serializedLong = gen.getOutputBuffer().toString();
        gen.close();

        Assert.assertEquals("-9223372036854775808", serializedLong);
    }

    @Test
    public void testLongSerializer_MaxValue() throws IOException {
        LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        longSerializer.serialize(Long.MAX_VALUE, gen, provider);
        String serializedLong = gen.getOutputBuffer().toString();
        gen.close();

        Assert.assertEquals("9223372036854775807", serializedLong);
    }

    @Test
    public void testFloatSerializer_NormalCase() throws IOException {
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        floatSerializer.serialize(Float.MIN_VALUE, gen, provider);
        String serializedFloat = gen.getOutputBuffer().toString();
        gen.close();

        Assert.assertEquals("-3.402823E38", serializedFloat);
    }

    @Test
    public void testFloatSerializer_MaxValue() throws IOException {
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        floatSerializer.serialize(Float.MAX_VALUE, gen, provider);
        String serializedFloat = gen.getOutputBuffer().toString();
        gen.close();

        Assert.assertEquals("3.402823E38", serializedFloat);
    }

    @Test
    public void testDoubleSerializer_NormalCase() throws IOException {
        DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        doubleSerializer.serialize(Double.MIN_VALUE, gen, provider);
        String serializedDouble = gen.getOutputBuffer().toString();
        gen.close();

        Assert.assertEquals("-4.94065645841237E-324", serializedDouble);
    }

    @Test
    public void testDoubleSerializer_MaxValue() throws IOException {
        DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        doubleSerializer.serialize(Double.MAX_VALUE, gen, provider);
        String serializedDouble = gen.getOutputBuffer().toString();
        gen.close();

        Assert.assertEquals("1.79769313486231E308", serializedDouble);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase_InvalidType() {
        new Base<>(String.class, JsonParser.NumberType.INT, "integer");
    }
}