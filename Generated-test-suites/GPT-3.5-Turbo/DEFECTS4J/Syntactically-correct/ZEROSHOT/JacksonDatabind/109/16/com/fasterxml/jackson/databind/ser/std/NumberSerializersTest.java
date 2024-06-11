package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testIntegerClass() {
        assertNotNull(allDeserializers.get(Integer.class.getName()));
        assertTrue(allDeserializers.get(Integer.class.getName()) instanceof NumberSerializers.IntegerSerializer);
    }

    @Test
    public void testIntegerType() {
        assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        assertTrue(allDeserializers.get(Integer.TYPE.getName()) instanceof NumberSerializers.IntegerSerializer);
    }

    @Test
    public void testLongClass() {
        assertNotNull(allDeserializers.get(Long.class.getName()));
        assertTrue(allDeserializers.get(Long.class.getName()) instanceof NumberSerializers.LongSerializer);
    }

    @Test
    public void testLongType() {
        assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        assertTrue(allDeserializers.get(Long.TYPE.getName()) instanceof NumberSerializers.LongSerializer);
    }
    
    @Test
    public void testByteClass() {
        assertNotNull(allDeserializers.get(Byte.class.getName()));
        assertTrue(allDeserializers.get(Byte.class.getName()) instanceof NumberSerializers.IntLikeSerializer);
    }

    @Test
    public void testByteType() {
        assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        assertTrue(allDeserializers.get(Byte.TYPE.getName()) instanceof NumberSerializers.IntLikeSerializer);
    }

    @Test
    public void testShortClass() {
        assertNotNull(allDeserializers.get(Short.class.getName()));
        assertTrue(allDeserializers.get(Short.class.getName()) instanceof NumberSerializers.ShortSerializer);
    }

    @Test
    public void testShortType() {
        assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        assertTrue(allDeserializers.get(Short.TYPE.getName()) instanceof NumberSerializers.ShortSerializer);
    }

    @Test
    public void testDoubleClass() {
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertTrue(allDeserializers.get(Double.class.getName()) instanceof NumberSerializers.DoubleSerializer);
    }

    @Test
    public void testDoubleType() {
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
        assertTrue(allDeserializers.get(Double.TYPE.getName()) instanceof NumberSerializers.DoubleSerializer);
    }

    @Test
    public void testFloatClass() {
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertTrue(allDeserializers.get(Float.class.getName()) instanceof NumberSerializers.FloatSerializer);
    }

    @Test
    public void testFloatType() {
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
        assertTrue(allDeserializers.get(Float.TYPE.getName()) instanceof NumberSerializers.FloatSerializer);
    }

}