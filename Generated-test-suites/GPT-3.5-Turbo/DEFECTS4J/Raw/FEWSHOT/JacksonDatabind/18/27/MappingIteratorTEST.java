package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.deser.RuntimeJsonMappingException;

public class MappingIteratorTest {

    private MappingIterator<String> iterator;
    private List<String> inputList;

    @Before
    public void setUp() {
        inputList = new ArrayList<>(Arrays.asList("value1", "value2", "value3"));

        iterator = new MappingIterator<String>(null, null, null, null, false, null) {
            boolean calledHasNextValue = false;
            boolean calledNextValue = false;

            @Override
            public boolean hasNextValue() throws IOException {
                calledHasNextValue = true;
                return inputList.size() > 0;
            }

            @Override
            public String nextValue() throws IOException {
                calledNextValue = true;
                if (inputList.size() > 0) {
                    String value = inputList.get(0);
                    inputList.remove(0);
                    return value;
                } else {
                    throw new NoSuchElementException("No more elements");
                }
            }
        };
    }

    @Test
    public void testHasNext() throws IOException {
        assertTrue(iterator.hasNext());
        assertTrue(iterator.calledHasNextValue);
        assertTrue(iterator.calledNextValue);
    }

    @Test
    public void testHasNext_emptyList() throws IOException {
        inputList.clear();

        assertFalse(iterator.hasNext());
        assertTrue(iterator.calledHasNextValue);
        assertFalse(iterator.calledNextValue);
    }

    @Test(expected = IOException.class)
    public void testHasNextException() throws IOException {
        MappingIterator<String> exceptionIterator = new MappingIterator<String>(null, null, null, null, false, null) {

            @Override
            public boolean hasNextValue() throws IOException {
                throw new IOException("I/O Exception");
            }

        };

        exceptionIterator.hasNext();
    }

    @Test
    public void testNext() throws IOException {
        String expected = "value1";
        String actual = iterator.next();

        assertEquals(expected, actual);
        assertTrue(iterator.calledHasNextValue);
        assertTrue(iterator.calledNextValue);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNext_noSuchElementException() throws IOException {
        inputList.clear();

        iterator.next();
    }

    @Test(expected = JsonParseException.class)
    public void testNext_runtimeJsonMappingException() throws IOException {
        MappingIterator<String> exceptionIterator = new MappingIterator<String>(null, null, null, null, false, null) {

            @Override
            public String nextValue() throws IOException {
                throw new JsonParseException(null, "JsonParseException");
            }

        };

        exceptionIterator.next();
    }

    @Test(expected = IOException.class)
    public void testNext_runtimeException() throws IOException {
        MappingIterator<String> exceptionIterator = new MappingIterator<String>(null, null, null, null, false, null) {

            @Override
            public String nextValue() throws IOException {
                throw new IOException("I/O Exception");
            }

        };

        exceptionIterator.next();
    }

    @Test
    public void testRemove() {
        try {
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            return;
        }
        throw new AssertionError("remove() should throw UnsupportedOperationException");
    }

    @Test
    public void testClose() throws IOException {
        iterator.close();

        assertNull(iterator._parser);
    }

    @Test
    public void testReadAll() throws IOException {
        List<String> expected = new ArrayList<>(Arrays.asList("value1", "value2", "value3"));
        List<String> actual = iterator.readAll();

        assertEquals(expected, actual);
        assertTrue(iterator.calledHasNextValue);
        assertTrue(iterator.calledNextValue);
    }

    @Test
    public void testReadAll_noElements() throws IOException {
        inputList.clear();

        List<String> expected = new ArrayList<>();
        List<String> actual = iterator.readAll();

        assertEquals(expected, actual);
        assertTrue(iterator.calledHasNextValue);
        assertFalse(iterator.calledNextValue);
    }

    @Test
    public void testReadAll_multipleCalls() throws IOException {
        List<String> expected = new ArrayList<>(Arrays.asList("value1", "value2", "value3"));

        List<String> actual1 = iterator.readAll();
        List<String> actual2 = iterator.readAll();
        List<String> actual3 = iterator.readAll();

        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
        assertEquals(expected, actual3);
        assertTrue(iterator.calledHasNextValue);
        assertTrue(iterator.calledNextValue);
    }

    @Test
    public void testReadAll_genericList() throws IOException {
        List<String> expected = new ArrayList<>(Arrays.asList("value1", "value2", "value3"));
        List<String> resultList = new ArrayList<>();

        List<String> actual = iterator.readAll(resultList);

        assertEquals(expected, resultList);
        assertEquals(expected, actual);
        assertTrue(iterator.calledHasNextValue);
        assertTrue(iterator.calledNextValue);
    }
}