package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private ObjectReader objectReader;
    private JsonFactory jsonFactory;

    @BeforeMethod
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonFactory = objectMapper.getFactory();
        objectReader = objectMapper.readerForListOf(String.class);
    }

    @Test
    public void testEmptyIterator() {
        MappingIterator<String> iterator = MappingIterator.emptyIterator();

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNext() throws IOException {
        JsonParser parser = jsonFactory.createParser("[\"foo\",\"bar\"]");
        ObjectReader reader = objectReader;

        MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser.getText()), parser, objectMapper.getDeserializationContext(), null, true, null);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), "foo");
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), "bar");
        Assert.assertFalse(iterator.hasNext());

        iterator.close();
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testNoSuchElement() throws IOException {
        JsonParser parser = jsonFactory.createParser("[]");
        ObjectReader reader = objectReader;

        MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser.getText()), parser, objectMapper.getDeserializationContext(), null, true, null);

        iterator.next();
        iterator.next();

        iterator.next(); // should throw NoSuchElementException
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser = jsonFactory.createParser("[\"foo\",\"bar\"]");
        ObjectReader reader = objectReader;

        MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser.getText()), parser, objectMapper.getDeserializationContext(), null, true, null);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), "foo");
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), "bar");

        iterator.close();

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testReadAll() throws IOException {
        JsonParser parser = jsonFactory.createParser("[\"foo\",\"bar\",\"baz\"]");
        ObjectReader reader = objectReader;

        List<String> list = new ArrayList<>();
        MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser.getText()), parser, objectMapper.getDeserializationContext(), null, true, null);

        iterator.readAll(list);

        Assert.assertEquals(list.size(), 3);
        Assert.assertEquals(list.get(0), "foo");
        Assert.assertEquals(list.get(1), "bar");
        Assert.assertEquals(list.get(2), "baz");
    }

    @Test
    public void testReadAllWithList() throws IOException {
        JsonParser parser = jsonFactory.createParser("[\"foo\",\"bar\",\"baz\"]");
        ObjectReader reader = objectReader;

        List<String> list = new ArrayList<>();

        MappingIterator<String> iterator = new MappingIterator<>(reader.getType(parser.getText()), parser, objectMapper.getDeserializationContext(), null, true, list);

        iterator.close();

        Assert.assertEquals(list.size(), 3);
        Assert.assertEquals(list.get(0), "foo");
        Assert.assertEquals(list.get(1), "bar");
        Assert.assertEquals(list.get(2), "baz");
    }

    @Test
    public void testDeserializeWithUpdatedValue() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("{\"foo\": \"bar\", \"baz\": \"qux\"}");
        ObjectReader reader = objectMapper.readerFor(Foo.class);

        Foo foo = new Foo();

        MappingIterator<Foo> iterator = new MappingIterator<>(reader.getType(parser.getText()), parser, objectMapper.getDeserializationContext(), null, true, foo);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), foo);

        Assert.assertEquals(foo.getBar(), "bar");
        Assert.assertEquals(foo.getBaz(), null);

        Assert.assertTrue(iterator.hasNext());

        Foo foo2 = new Foo();
        iterator.nextValue();
        Assert.assertEquals(foo2.getBar(), "baz");
        Assert.assertEquals(foo2.getBaz(), "qux");

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testDeserializeWithFromStringDeserializer() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[1, 2, 3]");
        ObjectReader reader = objectMapper.readerForListOf(Integer.class);

        reader.setValueHandler(new FromStringDeserializer(Integer.TYPE));

        List<Integer> list = new ArrayList<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(reader.getType(parser.getText()), parser, objectMapper.getDeserializationContext(), null, true, list);

        iterator.nextValue();
        Assert.assertEquals(iterator.nextValue(), 1);
        Assert.assertEquals(iterator.nextValue(), 2);
        Assert.assertEquals(iterator.nextValue(), 3);

        Assert.assertFalse(iterator.hasNext());
    }

    static class Foo {
        private String bar;
        private String baz;

        public String getBar() {
            return bar;
        }

        public void setBar(String bar) {
            this.bar = bar;
        }

        public String getBaz() {
            return baz;
        }

        public void setBaz(String baz) {
            this.baz = baz;
        }
    }
}