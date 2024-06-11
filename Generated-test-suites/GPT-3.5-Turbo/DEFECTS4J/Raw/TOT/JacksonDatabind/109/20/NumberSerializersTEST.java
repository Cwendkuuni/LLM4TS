import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }
    
    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        serializer.serialize((short) 100, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(100);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        serializer.serialize(100, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(100);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        serializer.serialize(100, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(100);
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer serializer = new LongSerializer(Long.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        serializer.serialize(100L, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(100L);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        serializer.serialize(10.5f, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(10.5f);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        serializer.serialize(10.5, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(10.5);
    }
}