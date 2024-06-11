
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class NumberSerializersTest {

    private NumberSerializers numberSerializers;
    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setup() {
        numberSerializers = new NumberSerializers();
        allDeserializers = new HashMap<>();
    }

    @Test
    public void testAddAll() {
        numberSerializers.addAll(allDeserializers);

        assert (allDeserializers.containsKey(Integer.class.getName()));
        assert (allDeserializers.containsKey(Integer.TYPE.getName()));
        assert (allDeserializers.containsKey(Long.class.getName()));
        assert (allDeserializers.containsKey(Long.TYPE.getName()));
        assert (allDeserializers.containsKey(Byte.class.getName()));
        assert (allDeserializers.containsKey(Byte.TYPE.getName()));
        assert (allDeserializers.containsKey(Short.class.getName()));
        assert (allDeserializers.containsKey(Short.TYPE.getName()));
        assert (allDeserializers.containsKey(Float.class.getName()));
        assert (allDeserializers.containsKey(Float.TYPE.getName()));
        assert (allDeserializers.containsKey(Double.class.getName()));
        assert (allDeserializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        Short value = 10;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        shortSerializer.serialize(value, generator, provider);

        verify(generator, times(1)).writeNumber(value.shortValue());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        Integer value = 10;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        integerSerializer.serialize(value, generator, provider);

        verify(generator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        Integer value = 10;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        intLikeSerializer.serialize(value, generator, provider);

        verify(generator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer();
        Long value = 10L;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        longSerializer.serialize(value, generator, provider);

        verify(generator, times(1)).writeNumber(value.longValue());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        Float value = 10.0f;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        floatSerializer.serialize(value, generator, provider);

        verify(generator, times(1)).writeNumber(value.floatValue());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();
        Double value = 10.0;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        doubleSerializer.serialize(value, generator, provider);

        verify(generator, times(1)).writeNumber(value.doubleValue());
    }

}