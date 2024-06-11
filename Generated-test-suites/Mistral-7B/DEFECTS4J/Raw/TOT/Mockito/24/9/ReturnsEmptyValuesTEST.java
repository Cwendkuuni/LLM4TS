package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.MockitoMock;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @MockitoMock(answer = Answers.RETURNS_DEEP_STUBS)
    private MockitoTest mock;

    @Test
    public void testToStringMethod() {
        String expectedToString = "Mock for MyClass, hashCode: 123456";
        when(mock.toString()).thenAnswer(new ReturnsEmptyValues());

        assertEquals(expectedToString, mock.toString());
    }

    @Test
    public void testCompareToMethod() {
        int expected = 1;
        when(mock.compareTo(mock)).thenAnswer(new ReturnsEmptyValues());

        assertEquals(expected, mock.compareTo(mock));
    }

    @Test
    public void testPrimitiveTypes() {
        int expectedInt = 0;
        byte expectedByte = (byte) 0;
        char expectedChar = '\u0000';
        boolean expectedBoolean = false;

        //test int
        when(mock.intValue()).thenAnswer(new ReturnsEmptyValues());
        assertEquals(expectedInt, mock.intValue());

        //test byte
        when(mock.byteValue()).thenAnswer(new ReturnsEmptyValues());
        assertEquals(expectedByte, mock.byteValue());

        //test char
        when(mock.charValue()).thenAnswer(new ReturnsEmptyValues());
        assertEquals(expectedChar, mock.charValue());

        //test boolean
        when(mock.booleanValue()).thenAnswer(new ReturnsEmptyValues());
        assertEquals(expectedBoolean, mock.booleanValue());

        //test short
        byte shortValue = (byte) 0;
        when(mock.shortValue()).thenReturn(shortValue);
        assertEquals(expectedInt, mock.shortValue()); //returns int value because of auto-boxing

        //test long
        long longValue = 0L;
        when(mock.longValue()).thenReturn(longValue);
        assertEquals(expectedLong, mock.longValue()); //returns long value because of auto-boxing

        //test float
        float floatValue = 0F;
        when(mock.floatValue()).thenReturn(floatValue);
        assertEquals(expectedFloat, mock.floatValue(), 0.0f); //returns float value because of auto-boxing

        //test double
        double doubleValue = 0.0;
        when(mock.doubleValue()).thenReturn(doubleValue);
        assertEquals(expectedDouble, mock.doubleValue(), 0.0d); //returns double value because of auto-boxing
    }

    @Test
    public void testEmptyCollections() {
        List<String> list = new ArrayList<>();
        Set<String> set = new HashSet<>();
        Map<String, String> map = new HashMap<>();

        when(mock.getList()).thenAnswer(new ReturnsEmptyValues());
        assertEquals(list, mock.getList());

        when(mock.getSet()).thenAnswer(new ReturnsEmptyValues());
        assertEquals(set, mock.getSet());

        when(mock.getMap()).thenAnswer(new ReturnsEmptyValues());
        assertEquals(map, mock.getMap());
    }

    @Test
    public void testEmptyArrays() {
        String[] array = new String[0];

        when(mock.getArray()).thenAnswer(new ReturnsEmptyValues());
        assertEquals(Arrays.asList(array), mock.getArray());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyingEmptyCollection() {
        List<String> list = new ArrayList<>();
        when(mock.getList()).thenAnswer(new ReturnsEmptyValues());

        list.add("Hello");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyingEmptySet() {
        Set<String> set = new HashSet<>();
        when(mock.getSet()).thenAnswer(new ReturnsEmptyValues());

        set.add("Hello");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyingEmptyMap() {
        Map<String, String> map = new HashMap<>();
        when(mock.getMap()).thenAnswer(new ReturnsEmptyValues());

        map.put("Key", "Value");
    }
}