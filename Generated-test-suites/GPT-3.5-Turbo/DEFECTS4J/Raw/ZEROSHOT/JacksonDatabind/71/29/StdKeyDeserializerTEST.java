package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer stdKeyDeserializer;

    @Before
    public void setUp() {
        stdKeyDeserializer = new StdKeyDeserializer(0, Object.class);
    }

    @Test
    public void testDeserializeKey() {
        //Test case 1
        String key1 = "test";
        //Assert
        assertNotNull(stdKeyDeserializer.deserializeKey(key1));

        //Test case 2
        String key2 = null;
        //Assert
        assertNull(stdKeyDeserializer.deserializeKey(key2));
    }

    @Test
    public void testGetKeyClass() {
        //Act
        Class<?> keyClass = stdKeyDeserializer.getKeyClass();
        //Assert
        assertEquals(Object.class, keyClass);
    }

    @Test
    public void testParse() {
        //Test case 1
        String key1 = "test";
        //Act
        Object result1 = stdKeyDeserializer._parse(key1);
        //Assert
        assertEquals(key1, result1);

        //Test case 2
        String key2 = null;
        //Act
        Object result2 = stdKeyDeserializer._parse(key2);
        //Assert
        assertNull(result2);
    }

    @Test
    public void testParseInt() {
        //Test case 1
        String key1 = "123";
        //Act
        int result1 = stdKeyDeserializer._parseInt(key1);
        //Assert
        assertEquals(123, result1);

        //Test case 2
        String key2 = "test";
        //Assert
        assertThrows(IllegalArgumentException.class, () -> stdKeyDeserializer._parseInt(key2));
    }

    @Test
    public void testParseLong() {
        //Test case 1
        String key1 = "1234567890";
        //Act
        long result1 = stdKeyDeserializer._parseLong(key1);
        //Assert
        assertEquals(1234567890L, result1);

        //Test case 2
        String key2 = "test";
        //Assert
        assertThrows(IllegalArgumentException.class, () -> stdKeyDeserializer._parseLong(key2));
    }

    @Test
    public void testParseDouble() {
        //Test case 1
        String key1 = "3.14";
        //Act
        double result1 = stdKeyDeserializer._parseDouble(key1);
        //Assert
        assertEquals(3.14, result1, 0.001);

        //Test case 2
        String key2 = "test";
        //Assert
        assertThrows(IllegalArgumentException.class, () -> stdKeyDeserializer._parseDouble(key2));
    }
}