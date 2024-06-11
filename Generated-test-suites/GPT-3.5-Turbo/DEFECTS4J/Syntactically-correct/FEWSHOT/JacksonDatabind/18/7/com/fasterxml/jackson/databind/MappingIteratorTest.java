
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class MappingIteratorTest {

    private MappingIterator<Integer> iterator;

    @Before
    public void setUp() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);

        ObjectMapper mapper = new ObjectMapper();
        iterator = new MappingIterator<>(mapper.constructType(Integer.class),
                mapper.treeAsTokens(mapper.valueToTree(numbers)).parser(),
                mapper.getDeserializationContext(),
                new StdDeserializer<Integer>(Integer.class) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Integer deserialize(com.fasterxml.jackson.core.JsonParser p,
                            com.fasterxml.jackson.databind.DeserializationContext ctxt)
                            throws java.io.IOException, com.fasterxml.jackson.core.JsonProcessingException {
                        return p.getIntValue();
                    }
                },
                true, null);
    }

    @After
    public void tearDown() throws IOException {
        iterator.close();
    }

    @Test
    public void testHasNextValue() throws IOException {
        assertTrue(iterator.hasNextValue());
        iterator.nextValue();
        assertTrue(iterator.hasNextValue());
        iterator.nextValue();
        assertTrue(iterator.hasNextValue());
        iterator.nextValue();
        assertFalse(iterator.hasNextValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValueWithNoSuchElementException() throws IOException {
        iterator.nextValue();
        iterator.nextValue();
        iterator.nextValue();
        iterator.nextValue();
    }

    @Test
    public void testNextValue() throws IOException {
        assertEquals(1, (int) iterator.nextValue());
        assertEquals(2, (int) iterator.nextValue());
        assertEquals(3, (int) iterator.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        List<Integer> resultList = iterator.readAll();
        assertEquals(3, resultList.size());
        assertEquals(1, (int) resultList.get(0));
        assertEquals(2, (int) resultList.get(1));
        assertEquals(3, (int) resultList.get(2));
    }
}
