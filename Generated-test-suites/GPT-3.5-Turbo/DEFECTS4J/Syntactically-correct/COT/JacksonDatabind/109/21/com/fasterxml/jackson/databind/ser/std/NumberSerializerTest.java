
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerializeBigDecimal() {
        //Arrange
        BigDecimal number = new BigDecimal("123.45");
        String expected = "123.45";

        //Act
        String actual = numberSerializer.serialize(number);

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeBigInteger() {
        //Arrange
        BigInteger number = new BigInteger("12345");
        String expected = "12345";

        //Act
        String actual = numberSerializer.serialize(number);

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeLong() {
        //Arrange
        Long number = 12345L;
        String expected = "12345";

        //Act
        String actual = numberSerializer.serialize(number);

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeDouble() {
        //Arrange
        Double number = 123.45;
        String expected = "123.45";

        //Act
        String actual = numberSerializer.serialize(number);

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeFloat() {
        //Arrange
        Float number = 123.45f;
        String expected = "123.45";

        //Act
        String actual = numberSerializer.serialize(number);

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeInteger() {
        //Arrange
        Integer number = 123;
        String expected = "123";

        //Act
        String actual = numberSerializer.serialize(number);

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeByte() {
        //Arrange
        Byte number = 123;
        String expected = "123";

        //Act
        String actual = numberSerializer.serialize(number);

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeShort() {
        //Arrange
        Short number = 123;
        String expected = "123";

        //Act
        String actual = numberSerializer.serialize(number);

        //Assert
        assertEquals(expected, actual);
    }

}
