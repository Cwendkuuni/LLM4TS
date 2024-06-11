import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testIntegerSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Integer.class.getName());
        assertEquals(NumberSerializers.IntegerSerializer.class, serializer.getClass());
    }

    @Test
    public void testPrimitiveIntegerSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Integer.TYPE.getName());
        assertEquals(NumberSerializers.IntegerSerializer.class, serializer.getClass());
    }

    @Test
    public void testLongSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Long.class.getName());
        assertEquals(NumberSerializers.LongSerializer.class, serializer.getClass());
    }

    @Test
    public void testPrimitiveLongSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Long.TYPE.getName());
        assertEquals(NumberSerializers.LongSerializer.class, serializer.getClass());
    }

    @Test
    public void testByteSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Byte.class.getName());
        assertEquals(NumberSerializers.IntLikeSerializer.class, serializer.getClass());
    }

    @Test
    public void testPrimitiveByteSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Byte.TYPE.getName());
        assertEquals(NumberSerializers.IntLikeSerializer.class, serializer.getClass());
    }

    @Test
    public void testShortSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Short.class.getName());
        assertEquals(NumberSerializers.ShortSerializer.class, serializer.getClass());
    }

    @Test
    public void testPrimitiveShortSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Short.TYPE.getName());
        assertEquals(NumberSerializers.ShortSerializer.class, serializer.getClass());
    }

    @Test
    public void testDoubleSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Double.class.getName());
        assertEquals(NumberSerializers.DoubleSerializer.class, serializer.getClass());
    }

    @Test
    public void testPrimitiveDoubleSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Double.TYPE.getName());
        assertEquals(NumberSerializers.DoubleSerializer.class, serializer.getClass());
    }

    @Test
    public void testFloatSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Float.class.getName());
        assertEquals(NumberSerializers.FloatSerializer.class, serializer.getClass());
    }

    @Test
    public void testPrimitiveFloatSerializer() {
        JsonSerializer<?> serializer = allDeserializers.get(Float.TYPE.getName());
        assertEquals(NumberSerializers.FloatSerializer.class, serializer.getClass());
    }

}