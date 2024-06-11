import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class MappingIteratorTest {

    private MappingIterator<Integer> mappingIterator;

    @Before
    public void setUp() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3, 4, 5]");
        ObjectMapper objectMapper = new ObjectMapper();
        mappingIterator = objectMapper.readValues(jsonParser, Integer.class);
    }

    @After
    public void tearDown() throws Exception {
        if (mappingIterator != null) {
            mappingIterator.close();
        }
    }

    @Test
    public void testHasNextValueWithEmptyIterator() throws IOException {
        MappingIterator.emptyIterator().hasNextValue();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testHasNextValueWithNonEmptyIterator() throws IOException {
        assertTrue(mappingIterator.hasNextValue());
    }

    @Test
    public void testHasNextValueAfterConsumingAllValues() throws IOException {
        while (mappingIterator.hasNext()) {
            mappingIterator.next();
        }
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testNextValueWithEmptyIterator() throws IOException {
        MappingIterator.emptyIterator().nextValue();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testNextValueWithNonEmptyIterator() throws IOException {
        assertEquals(1, (int) mappingIterator.nextValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValueAfterConsumingAllValues() throws IOException {
        while (mappingIterator.hasNext()) {
            mappingIterator.next();
        }
        mappingIterator.nextValue();
    }

    @Test
    public void testReadAllWithEmptyIterator() throws IOException {
        List<Integer> resultList = mappingIterator.readAll();
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }

    @Test
    public void testReadAllWithNonEmptyIterator() throws IOException {
        List<Integer> resultList = new ArrayList<>();
        mappingIterator.readAll(resultList);
        assertNotNull(resultList);
        assertEquals(5, resultList.size());
        assertEquals(1, (int) resultList.get(0));
        assertEquals(2, (int) resultList.get(1));
        assertEquals(3, (int) resultList.get(2));
        assertEquals(4, (int) resultList.get(3));
        assertEquals(5, (int) resultList.get(4));
    }

    @Test
    public void testReadAllWithEmptyResultList() throws IOException {
        List<Integer> resultList = new ArrayList<>();
        mappingIterator.readAll(resultList);
        assertNotNull(resultList);
        assertEquals(5, resultList.size());
        assertEquals(1, (int) resultList.get(0));
        assertEquals(2, (int) resultList.get(1));
        assertEquals(3, (int) resultList.get(2));
        assertEquals(4, (int) resultList.get(3));
        assertEquals(5, (int) resultList.get(4));
    }

    @Test
    public void testReadAllWithExistingResultList() throws IOException {
        List<Integer> resultList = new ArrayList<>();
        resultList.add(0);
        resultList.add(9);
        resultList.add(6);
        mappingIterator.readAll(resultList);
        assertNotNull(resultList);
        assertEquals(8, resultList.size());
        assertEquals(0, (int) resultList.get(0));
        assertEquals(9, (int) resultList.get(1));
        assertEquals(6, (int) resultList.get(2));
        assertEquals(1, (int) resultList.get(3));
        assertEquals(2, (int) resultList.get(4));
        assertEquals(3, (int) resultList.get(5));
        assertEquals(4, (int) resultList.get(6));
        assertEquals(5, (int) resultList.get(7));
    }

    @Test
    public void testGetParser() {
        assertNotNull(mappingIterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        assertNull(mappingIterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        assertNotNull(mappingIterator.getCurrentLocation());
    }
}