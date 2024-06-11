import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;

public class NumberSerializersTest {
    
    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        ShortSerializer shortSerializer = ShortSerializer.instance;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        shortSerializer.serialize((short) 10, generator, provider);
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        integerSerializer.serialize(100, generator, provider);
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        IntLikeSerializer intLikeSerializer = IntLikeSerializer.instance;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        intLikeSerializer.serialize(200, generator, provider);
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        LongSerializer longSerializer = LongSerializer.instance;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        longSerializer.serialize(1000L, generator, provider);
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        FloatSerializer floatSerializer = FloatSerializer.instance;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        floatSerializer.serialize(10.5f, generator, provider);
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        DoubleSerializer doubleSerializer = DoubleSerializer.instance;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        doubleSerializer.serialize(20.5, generator, provider);
    }

    @Test
    public void testAddAll() {
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertNotNull(allDeserializers.get(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertNotNull(allDeserializers.get(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertNotNull(allDeserializers.get(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertNotNull(allDeserializers.get(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
    }
}