import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializersTest {

    private JsonGenerator generator;
    private SerializerProvider provider;

    @Before
    public void setUp() {
        generator = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
    }

    @Test
    public void testShortSerializer() throws Exception {
        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;

        Short value = 10;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(10);
    }

    @Test
    public void testIntegerSerializer() throws Exception {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);

        Integer value = 100;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(100);
    }

    @Test
    public void testIntLikeSerializer() throws Exception {
        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;

        Number value = 50;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(50);
    }

    @Test
    public void testLongSerializer() throws Exception {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);

        Long value = 1000L;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(1000L);
    }

    @Test
    public void testFloatSerializer() throws Exception {
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;

        Float value = 0.5f;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(0.5f);
    }

    @Test
    public void testDoubleSerializer() throws Exception {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);

        Double value = 0.12345;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(0.12345);
    }

    @Test
    public void testShortSerializerWithNullValue() throws Exception {
        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;

        Short value = null;
        serializer.serialize(value, generator, provider);

        verify(generator, never()).writeNumber(anyInt());
    }

    @Test
    public void testIntegerSerializerWithNullValue() throws Exception {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);

        Integer value = null;
        serializer.serialize(value, generator, provider);

        verify(generator, never()).writeNumber(anyInt());
    }

    @Test
    public void testIntLikeSerializerWithNullValue() throws Exception {
        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;

        Number value = null;
        serializer.serialize(value, generator, provider);

        verify(generator, never()).writeNumber(anyInt());
    }

    @Test
    public void testLongSerializerWithNullValue() throws Exception {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);

        Long value = null;
        serializer.serialize(value, generator, provider);

        verify(generator, never()).writeNumber(anyLong());
    }

    @Test
    public void testFloatSerializerWithNullValue() throws Exception {
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;

        Float value = null;
        serializer.serialize(value, generator, provider);

        verify(generator, never()).writeNumber(anyFloat());
    }

    @Test
    public void testDoubleSerializerWithNullValue() throws Exception {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);

        Double value = null;
        serializer.serialize(value, generator, provider);

        verify(generator, never()).writeNumber(anyDouble());
    }

    @Test
    public void testShortSerializerWithZeroValue() throws Exception {
        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;

        Short value = 0;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(0);
    }

    @Test
    public void testIntegerSerializerWithZeroValue() throws Exception {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);

        Integer value = 0;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(0);
    }

    @Test
    public void testIntLikeSerializerWithZeroValue() throws Exception {
        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;

        Number value = 0;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(0);
    }

    @Test
    public void testLongSerializerWithZeroValue() throws Exception {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);

        Long value = 0L;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(0L);
    }

    @Test
    public void testFloatSerializerWithZeroValue() throws Exception {
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;

        Float value = 0.0f;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(0.0f);
    }

    @Test
    public void testDoubleSerializerWithZeroValue() throws Exception {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);

        Double value = 0.0;
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(0.0);
    }
}