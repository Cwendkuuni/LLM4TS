package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private MappingIterator<String> mappingIterator;

    @Before
    public void setup() throws IOException {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testEmptyIterator() {
        mappingIterator = MappingIterator.emptyIterator();
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNext() throws IOException {
        String jsonString = "[\"apple\", \"banana\", \"cherry\"]";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        mappingIterator = objectMapper.readValues(JsonParser.class, String.class, jsonParser)
                .nextValue();
        assertTrue(mappingIterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        String jsonString = "[\"apple\", \"banana\", \"cherry\"]";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        mappingIterator = objectMapper.readValues(JsonParser.class, String.class, jsonParser)
                .nextValue();
        String nextValue = mappingIterator.next();
        assertNotNull(nextValue);
        assertEquals("apple", nextValue);
        nextValue = mappingIterator.next();
        assertNotNull(nextValue);
        assertEquals("banana", nextValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        String jsonString = "[\"apple\", \"banana\", \"cherry\"]";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        mappingIterator = objectMapper.readValues(JsonParser.class, String.class, jsonParser)
                .nextValue();
        mappingIterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        String jsonString = "[\"apple\", \"banana\", \"cherry\"]";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        mappingIterator = objectMapper.readValues(JsonParser.class, String.class, jsonParser)
                .nextValue();
        mappingIterator.close();
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws IOException {
        String jsonString = "[\"apple\", \"banana\", \"cherry\"]";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        mappingIterator = objectMapper.readValues(JsonParser.class, String.class, jsonParser)
                .nextValue();
        assertTrue(mappingIterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        String jsonString = "[\"apple\", \"banana\", \"cherry\"]";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        mappingIterator = objectMapper.readValues(JsonParser.class, String.class, jsonParser)
                .nextValue();
        String nextValue = mappingIterator.nextValue();
        assertNotNull(nextValue);
        assertEquals("apple", nextValue);
        nextValue = mappingIterator.nextValue();
        assertNotNull(nextValue);
        assertEquals("banana", nextValue);
    }

    @Test
    public void testReadAll() throws IOException, JsonProcessingException {
        String jsonString = "[\"apple\", \"banana\", \"cherry\"]";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        List<String> result = mappingIterator.readAll();
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList("apple", "banana", "cherry")));
    }

    @Test
    public void testReadAllCollection() throws IOException {
        String jsonString = "[\"apple\", \"banana\", \"cherry\"]";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        Collection<String> result = mappingIterator.readAll(new ArrayList<String>());
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList("apple", "banana", "cherry")));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleMappingException() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser(jsonStringWithInvalidJson());
        mappingIterator = objectMapper.readValues(JsonParser.class, String.class, jsonParser)
                .nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue(); // Should throw JsonMappingException
    }

    @Test(expected = IOException.class)
    public void testHandleIOException() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser(jsonStringWithInvalidJson());
        mappingIterator = objectMapper.readValues(JsonParser.class, String.class, jsonParser)
                .nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue(); // Should throw IOException
    }

    private String jsonStringWithInvalidJson() {
        return "{\"invalidJson\":\"this is not valid json\"}";
    }

    static class TestBean {
        @JsonDeserialize(using = TestTypeDeserializer.class)
        private String name;

        public TestBean() {
        }

        public TestBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        static class TestTypeDeserializer extends StdDeserializer<TestBean> {
            public TestTypeDeserializer() {
                this(null);
            }

            protected TestTypeDeserializer(Class<?> vc) {
                super(vc);
            }

            @Override
            public TestBean deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                TestBean testBean = new TestBean();
                testBean.setName(ctxt.readValueAs(String.class, p));
                return testBean;
            }
        }
    }

    @Test
    public void testReadAllWithTypeDeserializer() throws IOException {
        String jsonString = "[{\"name\":\"testBean1\"}, {\"name\":\"testBean2\"}]";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        List<TestBean> result = mappingIterator.readAll(new ArrayList<TestBean>());
        assertEquals(2, result.size());
        TestBean firstBean = result.get(0);
        TestBean secondBean = result.get(1);
        assertEquals("testBean1", firstBean.getName());
        assertEquals("testBean2", secondBean.getName());
    }

}