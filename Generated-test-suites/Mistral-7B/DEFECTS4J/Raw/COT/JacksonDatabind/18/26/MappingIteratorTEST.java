package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonDeserializer.EmptyCollectionDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializationContext;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.test.BaseTest;
import com.fasterxml.jackson.test.util.JacksonTestUtils;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MappingIteratorTest extends BaseTest {

    private ObjectMapper objectMapper;
    private ObjectNode jsonSource;
    private JsonParser jsonParser;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        jsonSource = objectMapper.createObjectNode();
        jsonParser = objectMapper.getFactory().createParser(jsonSource);
    }

    @Test
    public void testEmptyMappingIterator() {
        MappingIterator<Object> mappingIterator = MappingIterator.emptyIterator();
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNext_hasNextValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        ObjectReader objectReader = objectMapper.readerFor(javaType);
        JsonNode jsonNode = objectMapper.readTree("['apple', 'banana', 'orange']");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, null, true, null);

        assertTrue(mappingIterator.hasNext());
        assertTrue(mappingIterator.hasNextValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testHasNext_noNextValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        ObjectReader objectReader = objectMapper.readerFor(javaType);
        JsonNode jsonNode = objectMapper.readTree("[]");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, null, true, null);

        assertTrue(mappingIterator.hasNext());
        assertFalse(mappingIterator.hasNextValue());
        mappingIterator.next(); // consume the null value
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test(expected = IOException.class)
    public void testHasNext_ioException() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{ \"error\": \"Invalid Input\" }");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

        MappingIterator<String> mappingIterator = new MappingIterator<>(String.class, jsonParser, null, null, true, null);

        assertTrue(mappingIterator.hasNext());
        mappingIterator.next();
        mappingIterator.next();

        mappingIterator.hasNext(); // this should throw an IOException
    }

    @Test
    public void testNextValue_nullUpdatedValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        ObjectReader objectReader = objectMapper.readerFor(javaType);
        JsonNode jsonNode = objectMapper.readTree("['apple', 'banana', 'orange']");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, null, true, null);

        assertEquals("apple", mappingIterator.nextValue());
        assertEquals("banana", mappingIterator.nextValue());
        assertEquals("orange", mappingIterator.nextValue());
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testNextValue_updatedValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        ObjectReader objectReader = objectMapper.readerFor(javaType);
        JsonNode jsonNode = objectMapper.readTree("['apple', 'banana', 'orange']");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

        String initialValue = "initialValue";
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, null, true, initialValue);

        assertEquals("apple", mappingIterator.nextValue());
        assertEquals(initialValue, mappingIterator.nextValue());
        assertEquals("banana", mappingIterator.nextValue());
        assertEquals("orange", mappingIterator.nextValue());
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        ObjectReader objectReader = objectMapper.readerFor(javaType);
        JsonNode jsonNode = objectMapper.readTree("['apple', 'banana', 'orange']");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

        List<String> resultList = new ArrayList<>();
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, null, true, null);
        mappingIterator.readAll(resultList);

        List<String> expectedList = Arrays.asList("apple", "banana", "orange");
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testReadAll_withUpdatedValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        ObjectReader objectReader = objectMapper.readerFor(javaType);
        JsonNode jsonNode = objectMapper.readTree("['apple', 'banana', 'orange']");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

        String initialValue = "initialValue";
        List<String> resultList = new ArrayList<>();
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, null, true, initialValue);
        mappingIterator.readAll(resultList);

        List<String> expectedList = Arrays.asList("apple", initialValue, "banana", "orange");
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testClose() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'name': 'John Doe'}");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

        MappingIterator<User> mappingIterator = new MappingIterator<>(User.class, jsonParser, null, null, true, null);
        mappingIterator.next(); // consume the first value

        mappingIterator.close();
        assertFalse(mappingIterator.hasNextValue());
        jsonParser.nextToken(); // this should not throw an IOException
    }

    @Test(expected = IllegalStateException.class)
    public void testHasNext_deserializerException() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);
        ObjectReader objectReader = objectMapper.readerFor(javaType);
        JsonNode jsonNode = objectMapper.readTree("['apple', 'banana', 'orange']");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

        JsonDeserializer<User> userDeserializer = new JsonDeserializer<User>() {
            @Override
            public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new IOException("Deserializer Exception");
            }
        };
        MappingIterator<User> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, userDeserializer, true, null);

        mappingIterator.hasNext();
        mappingIterator.next(); // consume the null value

        mappingIterator.hasNext(); // this should throw an IOException
    }

    @Test(expected = JsonMappingException.class)
    public void testHasNext_mappingException() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);
        ObjectReader objectReader = objectMapper.readerFor(javaType);
        JsonNode jsonNode = objectMapper.readTree("{'type': 'user'}");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

        MappingIterator<User> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, null, true, null);
        mappingIterator.next(); // consume the null value

        mappingIterator.hasNext(); // this should throw a JsonMappingException
    }

    @Test
    public void testEmptyCollectionDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule() {
            @Override
            public List<MapperDelegate<?>> getDeserializers() {
                return Arrays.asList(new EmptyCollectionDeserializer());
            }
        });

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        ObjectReader objectReader = objectMapper.readerFor(javaType);
        JsonNode jsonNode = objectMapper.readTree("[]");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

        List<String> resultList = new ArrayList<>();
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, null, true, null);
        mappingIterator.readAll(resultList);

        List<String> expectedList = Collections.emptyList();
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testNullValueDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        ObjectReader objectReader = objectMapper.readerFor(javaType);
        JsonNode jsonNode = objectMapper.readTree("[]");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, new NullValueDeserializer(), true, null);
        List<String> resultList = new ArrayList<>();
        mappingIterator.readAll(resultList);

        List<String> expectedList = Arrays.asList(null);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testMappingIteratorWithCustomSerializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        StdSerializer<User> userSerializer = new StdSerializer<User>(User.class) {
            @Override
            public void serialize(User value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString("User_" + value.getId());
            }
        };
        objectMapper.registerSerializer(User.class, userSerializer);

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);
        ObjectReader objectReader = objectMapper.readerFor(javaType);
        JsonNode jsonNode = objectMapper.readTree("[{\"id\": 1}, {\"id\": 2}]");
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);

        List<User> resultList = new ArrayList<>();
        MappingIterator<User> mappingIterator = new MappingIterator<>(javaType, jsonParser, null, null, true, null);
        mappingIterator.readAll(resultList);

        User user1 = new User(1L);
        User user2 = new User(2L);
        List<User> expectedList = Arrays.asList(user1, user2);

        assertEquals(expectedList, resultList);

        for (User user : resultList) {
            assertEquals("User_1", user.toString());
            assertEquals("User_2", mappingIterator.nextValue().toString());
        }

        assertFalse(mappingIterator.hasNextValue());
    }

    static class User {
        private final Long id;

        public User(Long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    '}';
        }

        @JsonDeserialize(using = UserDeserializer.class)
        public static User fromJson(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new User(jp.getValueAsLong());
        }
    }

    static class UserDeserializer extends StdDeserializer<User> {
        public UserDeserializer() {
            this(null);
        }

        protected UserDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return User.fromJson(p, ctxt);
        }
    }
}