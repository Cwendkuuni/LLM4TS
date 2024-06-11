package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.util.Map;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

public class NumberSerializersTest {

    private NumberSerializers numberSerializers;

    @Before
    public void setup() {
        numberSerializers = new NumberSerializers();
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        numberSerializers.addAll(allDeserializers);

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
        assertTrue(allDeserializers.get(Integer.class.getName()) instanceof IntegerSerializer);
        assertTrue(allDeserializers.get(Integer.TYPE.getName()) instanceof IntegerSerializer);
        assertTrue(allDeserializers.get(Long.class.getName()) instanceof LongSerializer);
        assertTrue(allDeserializers.get(Long.TYPE.getName()) instanceof LongSerializer);
        assertTrue(allDeserializers.get(Byte.class.getName()) instanceof IntLikeSerializer);
        assertTrue(allDeserializers.get(Byte.TYPE.getName()) instanceof IntLikeSerializer);
        assertTrue(allDeserializers.get(Short.class.getName()) instanceof ShortSerializer);
        assertTrue(allDeserializers.get(Short.TYPE.getName()) instanceof ShortSerializer);
        assertTrue(allDeserializers.get(Double.class.getName()) instanceof DoubleSerializer);
        assertTrue(allDeserializers.get(Double.TYPE.getName()) instanceof DoubleSerializer);
        assertTrue(allDeserializers.get(Float.class.getName()) instanceof FloatSerializer);
        assertTrue(allDeserializers.get(Float.TYPE.getName()) instanceof FloatSerializer);
    }
}