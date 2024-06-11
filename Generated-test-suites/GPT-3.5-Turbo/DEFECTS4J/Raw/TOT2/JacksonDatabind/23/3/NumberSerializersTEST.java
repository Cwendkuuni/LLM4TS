import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testAddAll() {
        assertNotNull(allDeserializers);
        assertFalse(allDeserializers.isEmpty());
        assertEquals(8, allDeserializers.size());
    }

    @Test
    public void testShortSerializer() {
        assertNotNull(NumberSerializers.ShortSerializer.instance);
        assertNotNull(NumberSerializers.ShortSerializer.instance._numberType);
        assertNotNull(NumberSerializers.ShortSerializer.instance._schemaType);
    }

    @Test
    public void testIntegerSerializer() {
        assertNotNull(NumberSerializers.IntegerSerializer.instance);
        assertNotNull(NumberSerializers.IntegerSerializer.instance._numberType);
        assertNotNull(NumberSerializers.IntegerSerializer.instance._schemaType);
    }

    @Test
    public void testIntLikeSerializer() {
        assertNotNull(NumberSerializers.IntLikeSerializer.instance);
        assertNotNull(NumberSerializers.IntLikeSerializer.instance._numberType);
        assertNotNull(NumberSerializers.IntLikeSerializer.instance._schemaType);
    }

    @Test
    public void testLongSerializer() {
        assertNotNull(NumberSerializers.LongSerializer.instance);
        assertNotNull(NumberSerializers.LongSerializer.instance._numberType);
        assertNotNull(NumberSerializers.LongSerializer.instance._schemaType);
    }

    @Test
    public void testFloatSerializer() {
        assertNotNull(NumberSerializers.FloatSerializer.instance);
        assertNotNull(NumberSerializers.FloatSerializer.instance._numberType);
        assertNotNull(NumberSerializers.FloatSerializer.instance._schemaType);
    }

    @Test
    public void testDoubleSerializer() {
        assertNotNull(NumberSerializers.DoubleSerializer.instance);
        assertNotNull(NumberSerializers.DoubleSerializer.instance._numberType);
        assertNotNull(NumberSerializers.DoubleSerializer.instance._schemaType);
    }
}