
package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class MappingIteratorTest {

    @Test
    public void testHasNextValue() {
        String json = "[1, 2, 3, 4, 5]";
        InputStream stream = new ByteArrayInputStream(json.getBytes());

        MappingIterator<Integer> iterator = null;
        try {
            iterator = new MappingIterator<Integer>(null, JsonMapper.builder().build().createParser(stream), null, null,
                    false, null);

            assertTrue(iterator.hasNextValue());

            while (iterator.hasNextValue()) {
                iterator.nextValue();
            }

            assertFalse(iterator.hasNextValue());
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (iterator != null) {
                try {
                    iterator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValueWithEmptyStream() {
        String json = "[]";
        InputStream stream = new ByteArrayInputStream(json.getBytes());

        MappingIterator<Integer> iterator = null;
        try {
            iterator = new MappingIterator<Integer>(null, JsonMapper.builder().build().createParser(stream), null, null,
                    false, null);

            iterator.nextValue();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (iterator != null) {
                try {
                    iterator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testNextValue() {
        String json = "[1, 2, 3, 4, 5]";
        InputStream stream = new ByteArrayInputStream(json.getBytes());

        MappingIterator<Integer> iterator = null;
        try {
            iterator = new MappingIterator<Integer>(null, JsonMapper.builder().build().createParser(stream), null, null,
                    false, null);

            List<Integer> result = new ArrayList<>();
            while (iterator.hasNextValue()) {
                Integer value = iterator.nextValue();
                result.add(value);
            }

            assertEquals(5, result.size());
            assertEquals(1, result.get(0).intValue());
            assertEquals(2, result.get(1).intValue());
            assertEquals(3, result.get(2).intValue());
            assertEquals(4, result.get(3).intValue());
            assertEquals(5, result.get(4).intValue());
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (iterator != null) {
                try {
                    iterator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testReadAll() {
        String json = "[1, 2, 3, 4, 5]";
        InputStream stream = new ByteArrayInputStream(json.getBytes());

        MappingIterator<Integer> iterator = null;
        try {
            iterator = new MappingIterator<Integer>(null, JsonMapper.builder().build().createParser(stream), null, null,
                    false, null);

            List<Integer> result = iterator.readAll();

            assertEquals(5, result.size());
            assertEquals(1, result.get(0).intValue());
            assertEquals(2, result.get(1).intValue());
            assertEquals(3, result.get(2).intValue());
            assertEquals(4, result.get(3).intValue());
            assertEquals(5, result.get(4).intValue());
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (iterator != null) {
                try {
                    iterator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testReadAllWithArrayList() {
        String json = "[1, 2, 3, 4, 5]";
        InputStream stream = new ByteArrayInputStream(json.getBytes());

        MappingIterator<Integer> iterator = null;
        try {
            iterator = new MappingIterator<Integer>(null, JsonMapper.builder().build().createParser(stream), null, null,
                    false, null);

            List<Integer> result = iterator.readAll(new ArrayList<Integer>());

            assertEquals(5, result.size());
            assertEquals(1, result.get(0).intValue());
            assertEquals(2, result.get(1).intValue());
            assertEquals(3, result.get(2).intValue());
            assertEquals(4, result.get(3).intValue());
            assertEquals(5, result.get(4).intValue());
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (iterator != null) {
                try {
                    iterator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testReadAllWithLinkedList() {
        String json = "[1, 2, 3, 4, 5]";
        InputStream stream = new ByteArrayInputStream(json.getBytes());

        MappingIterator<Integer> iterator = null;
        try {
            iterator = new MappingIterator<Integer>(null, JsonMapper.builder().build().createParser(stream), null, null,
                    false, null);

            List<Integer> result = iterator.readAll(new ArrayList<Integer>());

            assertEquals(5, result.size());
            assertEquals(1, result.get(0).intValue());
            assertEquals(2, result.get(1).intValue());
            assertEquals(3, result.get(2).intValue());
            assertEquals(4, result.get(3).intValue());
            assertEquals(5, result.get(4).intValue());
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (iterator != null) {
                try {
                    iterator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
