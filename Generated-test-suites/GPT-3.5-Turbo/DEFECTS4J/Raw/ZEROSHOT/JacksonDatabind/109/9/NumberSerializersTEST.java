import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NumberSerializersTest {

    private NumberSerializers.IntegerSerializer integerSerializer;
    private NumberSerializers.IntLikeSerializer intLikeSerializer;
    private NumberSerializers.ShortSerializer shortSerializer;
    private NumberSerializers.LongSerializer longSerializer;
    private NumberSerializers.FloatSerializer floatSerializer;
    private NumberSerializers.DoubleSerializer doubleSerializer;

    @Before
    public void setUp() {
        integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
        intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        shortSerializer = new NumberSerializers.ShortSerializer();
        longSerializer = new NumberSerializers.LongSerializer(Long.class);
        floatSerializer = new NumberSerializers.FloatSerializer();
        doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        Integer value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        integerSerializer.serialize(value, gen, provider);
        verify(gen).writeNumber(value.intValue());
    }

    @Test
    public void testIntegerSerializerSerializeWithType() throws IOException {
        Integer value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        integerSerializer.serializeWithType(value, gen, provider, null);
        verify(gen).writeNumber(value.intValue());
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        Number value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        intLikeSerializer.serialize(value, gen, provider);
        verify(gen).writeNumber(value.intValue());
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        Short value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        shortSerializer.serialize(value, gen, provider);
        verify(gen).writeNumber(value.shortValue());
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        Long value = 10L;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        longSerializer.serialize(value, gen, provider);
        verify(gen).writeNumber(value.longValue());
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        Float value = 10.5f;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        floatSerializer.serialize(value, gen, provider);
        verify(gen).writeNumber(value.floatValue());
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        Double value = 10.5;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        doubleSerializer.serialize(value, gen, provider);
        verify(gen).writeNumber(value.doubleValue());
    }
}