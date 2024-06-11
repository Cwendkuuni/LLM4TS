import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        Assert.assertEquals(8, allDeserializers.size());

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
    public void testShortSerializerSerialize() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Short("123");

        shortSerializer.serialize(value, gen, provider);

        verify(gen).writeNumber(123);
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Integer(456);

        integerSerializer.serialize(value, gen, provider);

        verify(gen).writeNumber(456);
    }

    @Test
    public void testIntegerSerializerSerializeWithType() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        Object value = new Integer(456);

        integerSerializer.serializeWithType(value, gen, provider, typeSer);

        verify(gen).writeNumber(456);
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Integer(789);

        intLikeSerializer.serialize(value, gen, provider);

        verify(gen).writeNumber(789);
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Long(12345);

        longSerializer.serialize(value, gen, provider);

        verify(gen).writeNumber(12345);
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        FloatSerializer floatSerializer = new FloatSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Float(3.14159);

        floatSerializer.serialize(value, gen, provider);

        verify(gen).writeNumber(3.14159f);
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Double(2.71828);

        doubleSerializer.serialize(value, gen, provider);

        verify(gen).writeNumber(2.71828);
    }

    @Test
    public void testDoubleSerializerSerializeWithType() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        Object value = new Double(2.71828);

        doubleSerializer.serializeWithType(value, gen, provider, typeSer);

        verify(gen).writeNumber(2.71828);
    }

}