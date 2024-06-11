
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

public class MappingIteratorTest {
    // Test variables
    private MappingIterator<Integer> iterator;
    private ObjectMapper mapper;
    private JsonParser parser;

    @org.junit.Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        parser = mapper.getFactory().createParser("[1, 2, 3]");
        JavaType type = mapper.getTypeFactory().constructType(Integer.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<Integer> deser = mapper.findRootValueDeserializer(type);

        iterator = new MappingIterator<>(type, parser, ctxt, deser, false, null);
    }

    @org.junit.Test
    public void testHasNext() throws IOException {
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testNext() throws IOException {
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(Integer.valueOf(3), iterator.next());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        iterator.remove();
    }

    @org.junit.Test
    public void testClose() throws IOException {
        iterator.close();
        assertNull(iterator.getParser());
    }

    @org.junit.Test
    public void testHasNextValue() throws IOException {
        assertTrue(iterator.hasNextValue());
        iterator.next();
        assertTrue(iterator.hasNextValue());
        iterator.next();
        assertTrue(iterator.hasNextValue());
        iterator.next();
        assertFalse(iterator.hasNextValue());
    }

    @org.junit.Test
    public void testNextValue() throws IOException {
        assertEquals(Integer.valueOf(1), iterator.nextValue());
        assertEquals(Integer.valueOf(2), iterator.nextValue());
        assertEquals(Integer.valueOf(3), iterator.nextValue());
    }

    @org.junit.Test
    public void testReadAll1() throws IOException {
        List<Integer> resultList = iterator.readAll();
        assertEquals(Arrays.asList(1, 2, 3), resultList);
    }

    @org.junit.Test
    public void testReadAll2() throws IOException {
        List<Integer> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals(Arrays.asList(1, 2, 3), resultList);
    }

    @org.junit.Test
    public void testReadAll3() throws IOException {
        Set<Integer> resultSet = new HashSet<>();
        iterator.readAll(resultSet);
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3)), resultSet);
    }

    @org.junit.Test
    public void testGetParser() {
        assertEquals(parser, iterator.getParser());
    }

    @org.junit.Test
    public void testGetParserSchema() {
        assertNull(iterator.getParserSchema());
    }

    @org.junit.Test
    public void testGetCurrentLocation() {
        assertNull(iterator.getCurrentLocation());
    }

    @org.junit.Test(expected = NoSuchElementException.class)
    public void test_throwNoSuchElement() {
        iterator._throwNoSuchElement();
    }

    @org.junit.Test(expected = RuntimeJsonMappingException.class)
    public void test_handleMappingException() {
        iterator._handleMappingException(new RuntimeJsonMappingException());
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void test_handleIOException() {
        iterator._handleIOException(new RuntimeException());
    }
}

