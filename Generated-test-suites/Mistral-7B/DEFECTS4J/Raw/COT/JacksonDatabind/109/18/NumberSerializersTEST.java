import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class NumberSerializersTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        // You can initialize any shared setup here if needed
    }

    // Testing addAll method
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        Assert.assertEquals(7, allDeserializers.size());
        Assert.assertNotNull(allDeserializers.get(Integer.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        // Add other assertions for other classes
    }

    // Testing ShortSerializer
    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = objectMapper.getFactory()
                .createGenerator(byteArrayOutputStream);

        shortSerializer.serialize((short) 123, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        JsonParser jsonParser = objectMapper.getFactory().createParser(byteArrayInputStream);

        Assert.assertEquals((short) 123, shortSerializer.deserialize(jsonParser, Short.class, null));
    }

    // Testing IntegerSerializer
    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = objectMapper.getFactory()
                .createGenerator(byteArrayOutputStream);

        integerSerializer.serialize(123, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        JsonParser jsonParser = objectMapper.getFactory().createParser(byteArrayInputStream);

        Assert.assertEquals(123, integerSerializer.deserialize(jsonParser, Integer.class, null).intValue());
    }

    // Testing IntLikeSerializer
    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = objectMapper.getFactory()
                .createGenerator(byteArrayOutputStream);

        intLikeSerializer.serialize(123, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        JsonParser jsonParser = objectMapper.getFactory().createParser(byteArrayInputStream);

        Assert.assertEquals(123, intLikeSerializer.deserialize(jsonParser, Number.class, null).intValue());
    }

    // Testing LongSerializer
    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer longSerializer = new LongSerializer(Long.class);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = objectMapper.getFactory()
                .createGenerator(byteArrayOutputStream);

        longSerializer.serialize(123L, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        JsonParser jsonParser = objectMapper.getFactory().createParser(byteArrayInputStream);

        Assert.assertEquals(123, longSerializer.deserialize(jsonParser, Long.class, null).longValue());
    }

    // Testing FloatSerializer
    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer floatSerializer = new FloatSerializer();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = objectMapper.getFactory()
                .createGenerator(byteArrayOutputStream);

        floatSerializer.serialize((float) 123.45, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        JsonParser jsonParser = objectMapper.getFactory().createParser(byteArrayInputStream);

        Assert.assertEquals(123.45f, floatSerializer.deserialize(jsonParser, Float.class, null).floatValue(), 0.01);
    }

    // Testing DoubleSerializer
    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = objectMapper.getFactory()
                .createGenerator(byteArrayOutputStream);

        doubleSerializer.serialize(123.45, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        JsonParser jsonParser = objectMapper.getFactory().createParser(byteArrayInputStream);

        Assert.assertEquals(123.45, doubleSerializer.deserialize(jsonParser, Double.class, null).doubleValue(), 0.01);
    }

    // Testing edge cases and exceptions
    // Add test cases for null inputs, negative numbers, large numbers, etc.

    // Testing JsonFormatVisitorWrapper
    @Test
    public void testAcceptJsonFormatVisitorWrapper() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);

        JsonFormatVisitorWrapper visitorWrapper = new JsonFormatVisitorWrapper(new ObjectMapper().getSerializationConfig());
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);

        integerSerializer.acceptJsonFormatVisitor(visitorWrapper, null);

        Assert.assertEquals(format.getShape(), JsonFormat.Shape.INT);
    }

    // Testing findFormatOverrides
    @Test
    public void testFindFormatOverrides() throws IOException {
        // Add test case for JsonFormat annotation
        // Add test case for custom serializer with JsonFormat annotation
    }
}