import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MappingIteratorTest {

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;
    private ObjectReader objectReader;
    private List<Integer> integerList;

    @Before
    public void setUp() throws IOException {
        jsonFactory = new JsonFactoryBuilder()
                .build();
        objectMapper = new ObjectMapper(jsonFactory);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectReader = objectMapper.readerForListOf(Integer.class);
        integerList = new ArrayList<>();
    }

    @After
    public void tearDown() throws IOException {
        if (objectMapper != null) {
            objectMapper.close();
        }
    }

    @Test
    public void testHasNext() {
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        MappingIterator<Integer> iterator = new MappingIterator<>(
                TypeFactory.defaultInstance.constructType(List.class),
                jsonParser,
                objectMapper.getDeserializationContext(),
                objectMapper.getTypeFactory().constructValueType(List.class, Integer.class),
                true,
                null);

        for (int i = 0; i < 3; i++) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());

        jsonParser.close();
    }

    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<Integer> iterator = createTestIterator();

        assertTrue(iterator.hasNextValue());

        JsonParser jsonParser = iterator.getParser();
        jsonParser.nextToken();
        assertFalse(iterator.hasNextValue());

        iterator.close();
    }

    @Test
    public void testNext() {
        MappingIterator<Integer> iterator = createTestIterator();

        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertNull(iterator.next());

        iterator.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextWhenNoMoreElements() {
        MappingIterator<Integer> iterator = createTestIterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();

        iterator.next();
    }

    @Test(expected = RuntimeJsonMappingException.class)
    public void testMappingException() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("{ \"name\": \"John\" }");
        MappingIterator<Person> iterator = new MappingIterator<>(
                TypeFactory.defaultInstance.constructType(List.class, Person.class),
                jsonParser,
                objectMapper.getDeserializationContext(),
                objectMapper.getTypeFactory().constructValueType(List.class, Person.class),
                true,
                null);

        jsonParser.nextToken();

        assertNull(iterator.nextValue());
    }

    @Test
    public void testCloseParser() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        MappingIterator<Integer> iterator = new MappingIterator<>(
                TypeFactory.defaultInstance.constructType(List.class),
                jsonParser,
                objectMapper.getDeserializationContext(),
                objectMapper.getTypeFactory().constructValueType(List.class, Integer.class),
                true,
                null);

        iterator.nextValue();
        jsonParser.nextToken();

        iterator.close();

        assertFalse(iterator.hasNextValue());
        assertNull(iterator.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        MappingIterator<Integer> iterator = new MappingIterator<>(
                TypeFactory.defaultInstance.constructType(List.class),
                jsonParser,
                objectMapper.getDeserializationContext(),
                objectMapper.getTypeFactory().constructValueType(List.class, Integer.class),
                true,
                null);

        List<Integer> resultList = iterator.readAll();

        assertEquals(3, resultList.size());
        assertEquals(1, resultList.get(0).intValue());
        assertEquals(2, resultList.get(1).intValue());
        assertEquals(3, resultList.get(2).intValue());

        jsonParser.close();
    }

    @Test
    public void testReadAllWithNullList() throws IOException {
        MappingIterator<Integer> iterator = createTestIterator();

        iterator.readAll(null);

        iterator.close();
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        MappingIterator<Integer> iterator = new MappingIterator<>(
                TypeFactory.defaultInstance.constructType(ArrayList.class),
                jsonParser,
                objectMapper.getDeserializationContext(),
                objectMapper.getTypeFactory().constructValueType(ArrayList.class, Integer.class),
                true,
                null);

        ArrayList<Integer> resultList = new ArrayList<>();
        iterator.readAll(resultList);

        assertEquals(3, resultList.size());
        assertEquals(1, resultList.get(0).intValue());
        assertEquals(2, resultList.get(1).intValue());
        assertEquals(3, resultList.get(2).intValue());

        jsonParser.close();
    }

    private MappingIterator<Integer> createTestIterator() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");

        return new MappingIterator<>(
                TypeFactory.defaultInstance.constructType(List.class),
                jsonParser,
                objectMapper.getDeserializationContext(),
                objectMapper.getTypeFactory().constructValueType(List.class, Integer.class),
                true,
                null);
    }

    private static class Person {
        private String name;

        public String getName() {
            return name;
        }
    }
}