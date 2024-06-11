
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.SerializerProvider;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializersTest {

    @Mock
    private JsonGenerator generator;

    @Mock
    private SerializerProvider provider;

    private NumberSerializers numberSerializers;

    @Before
    public void setUp() {
        numberSerializers = new NumberSerializers();
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();

        numberSerializers.addAll(deserializers);

        assertEquals(12, deserializers.size());

        // Verify that the expected serializers have been added

        // Integer
        assertEquals(NumberSerializers.IntegerSerializer.class, deserializers.get(Integer.class.getName()).getClass());
        assertEquals(NumberSerializers.IntegerSerializer.class, deserializers.get(Integer.TYPE.getName()).getClass());

        // Long
        assertEquals(NumberSerializers.LongSerializer.class, deserializers.get(Long.class.getName()).getClass());
        assertEquals(NumberSerializers.LongSerializer.class, deserializers.get(Long.TYPE.getName()).getClass());

        // Byte
        assertEquals(NumberSerializers.IntLikeSerializer.class, deserializers.get(Byte.class.getName()).getClass());
        assertEquals(NumberSerializers.IntLikeSerializer.class, deserializers.get(Byte.TYPE.getName()).getClass());

        // Short
        assertEquals(NumberSerializers.ShortSerializer.class, deserializers.get(Short.class.getName()).getClass());
        assertEquals(NumberSerializers.ShortSerializer.class, deserializers.get(Short.TYPE.getName()).getClass());

        // Double
        assertEquals(NumberSerializers.DoubleSerializer.class, deserializers.get(Double.class.getName()).getClass());
        assertEquals(NumberSerializers.DoubleSerializer.class, deserializers.get(Double.TYPE.getName()).getClass());

        // Float
        assertEquals(NumberSerializers.FloatSerializer.class, deserializers.get(Float.class.getName()).getClass());
        assertEquals(NumberSerializers.FloatSerializer.class, deserializers.get(Float.TYPE.getName()).getClass());
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();

        short value = 10;
        shortSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);

        int value = 100;
        integerSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;

        int value = 50;
        intLikeSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.class);

        long value = 1000L;
        longSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;

        float value = 10.5f;
        floatSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);

        double value = 8.8;
        doubleSerializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }
}
