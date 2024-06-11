package com.example.junit;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import org.junit.*;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @BeforeClass
    public static void setUpClass() {
        // Initialize any static resources or shared setup here
    }

    @AfterClass
    public static void tearDownClass() {
        // Cleanup any static resources or shared teardown here
    }

    @Before
    public void setUp() {
        // Per test setup
    }

    @After
    public void tearDown() {
        // Per test teardown
    }

    @Test
    public void testDeserialize() throws Exception {
        String inputJson = "[\"hello\", \"world\", \"test\"]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = mapper.readerForValueType(String[].class)
                .readValue(new StringReader(inputJson), deserializer);
        assertArrayEquals(new String[]{"hello", "world", "test"}, result);
    }

    @Test
    public void testEmptyJsonArray() throws Exception {
        String inputJson = "[]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = mapper.readerForValueType(String[].class)
                .readValue(new StringReader(inputJson), deserializer);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testJsonArrayWithNullElements() throws Exception {
        String inputJson = "[null, \"hello\", null]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = mapper.readerForValueType(String[].class)
                .readValue(new StringReader(inputJson), deserializer);
        assertArrayEquals(new String[]{"null", "hello", "null"}, result);
    }

    @Test
    public void testJsonArrayWithNonStringElements() throws Exception {
        String inputJson = "[1, 2, 3]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        try {
            String[] result = mapper.readerForValueType(String[].class)
                    .readValue(new StringReader(inputJson), deserializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testJsonArrayWithInvalidJson() throws Exception {
        String inputJson = "[{\"name\":\"John\"}]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        try {
            String[] result = mapper.readerForValueType(String[].class)
                    .readValue(new StringReader(inputJson), deserializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testJsonArrayWithMoreThanIntMaxElements() throws Exception {
        String inputJson = String.format("%s%n%s%n%s%n", new String(new char[1024 * 1024]),
                "\"element1\", \"element2\", \"element3\"");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        try {
            String[] result = mapper.readerForValueType(String[].class)
                    .readValue(new StringReader(inputJson), deserializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testJsonArrayWithCustomDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = mapper.deserializerFactory().getDeserializer(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String inputJson = "[\"hello\", \"world\"]";
        String[] result = mapper.readerForValueType(String[].class)
                .readValue(new StringReader(inputJson), deserializer);
        assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @AfterClass
    public static void tearDownClass() {
        // Cleanup any static resources or shared teardown here
    }
}