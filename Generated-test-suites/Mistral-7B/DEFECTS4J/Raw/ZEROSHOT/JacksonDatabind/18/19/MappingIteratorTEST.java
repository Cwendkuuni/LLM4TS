package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private JsonParser jsonParser;

    @BeforeMethod
    public void setup() {
        jsonFactory = new ObjectMapper().getFactory();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testEmptyIterator() {
        MappingIterator<String> emptyIterator = MappingIterator.emptyIterator();

        Assert.assertFalse(emptyIterator.hasNext());
        Assert.assertNull(emptyIterator.next());
    }

    @Test
    public void testIteratorWithSingleValue() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        JsonParser parser = mock(JsonParser.class);
        Object context = mock(Object.class);
        JsonDeserializer<String> deserializer = new FromStringDeserializer();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(parser.getText()).thenReturn("test");

        MappingIterator<String> iterator = new MappingIterator<>(javaType, parser, context, deserializer, false, null, typeDeserializer);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), "test");
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expectedExceptions = IOException.class)
    public void testIteratorWithIOException() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        JsonParser parser = mock(JsonParser.class);
        Object context = mock(Object.class);
        JsonDeserializer<String> deserializer = new FromStringDeserializer();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(parser.nextToken()).thenThrow(IOException.class);

        MappingIterator<String> iterator = new MappingIterator<>(javaType, parser, context, deserializer, false, null, typeDeserializer);

        iterator.next();
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testIteratorWithJsonMappingException() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        JsonParser parser = mock(JsonParser.class);
        Object context = mock(Object.class);
        JsonDeserializer<String> deserializer = new FromStringDeserializer();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        doThrow(JsonMappingException.class).when(parser).nextValue();

        MappingIterator<String> iterator = new MappingIterator<>(javaType, parser, context, deserializer, false, null, typeDeserializer);

        iterator.next();
    }

    @Test
    public void testIteratorWithCloseParser() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        JsonParser parser = jsonFactory.createParser("[]");
        Object context = mock(Object.class);
        JsonDeserializer<String> deserializer = new FromStringDeserializer();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        MappingIterator<String> iterator = new MappingIterator<>(javaType, parser, context, deserializer, true, null, typeDeserializer);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), null);
        Assert.assertFalse(iterator.hasNext());
        Assert.assertNull(parser);

        parser = mock(JsonParser.class);
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(parser.getText()).thenReturn("test");

        iterator = new MappingIterator<>(javaType, parser, context, deserializer, true, null, typeDeserializer);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), "test");
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.hasNext());
        verify(parser).close();
    }

    @Test
    public void testIteratorWithNullParser() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        Object context = mock(Object.class);
        JsonDeserializer<String> deserializer = new FromStringDeserializer();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        MappingIterator<String> iterator = new MappingIterator<>(javaType, null, context, deserializer, false, null, typeDeserializer);

        Assert.assertFalse(iterator.hasNext());
        Assert.assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithUpdateValue() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        JsonParser parser = mock(JsonParser.class);
        Object context = mock(Object.class);
        JsonDeserializer<String> deserializer = new FromStringDeserializer();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        String valueToUpdate = "test";

        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(parser.getText()).thenReturn("updatedTest");

        MappingIterator<String> iterator = new MappingIterator<>(javaType, parser, context, deserializer, false, valueToUpdate, typeDeserializer);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), "test");
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), "updatedTest");
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testReadAll() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class);
        JsonParser parser = jsonFactory.createParser("[1, 2, 3]");
        Object context = mock(Object.class);
        JsonDeserializer<List<Integer>> deserializer = new FromStringDeserializer();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        List<Integer> resultList = new ArrayList<>();

        MappingIterator<List<Integer>> iterator = new MappingIterator<>(javaType, parser, context, deserializer, false, resultList, typeDeserializer);

        iterator.readAll(resultList);

        List<Integer> expectedResultList = Collections.singletonList(Collections.singletonList(1, 2, 3));
        Assert.assertEquals(resultList, expectedResultList);
    }

    @Test
    public void testReadAllWithCustomCollection() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(ArrayList.class);
        JsonParser parser = jsonFactory.createParser("[1, 2, 3]");
        Object context = mock(Object.class);
        JsonDeserializer<ArrayList<Integer>> deserializer = new FromStringDeserializer();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        ArrayList<Integer> results = new ArrayList<>();

        MappingIterator<ArrayList<Integer>> iterator = new MappingIterator<>(javaType, parser, context, deserializer, false, results, typeDeserializer);

        iterator.readAll(results);

        ArrayList<Integer> expectedResults = new ArrayList<>();
        expectedResults.add(1);
        expectedResults.add(2);
        expectedResults.add(3);
        Assert.assertEquals(results, expectedResults);
    }
}