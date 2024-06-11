import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingIteratorTest {

    private MappingIterator<Integer> mappingIterator;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        mappingIterator = objectMapper.readerFor(Integer.class).readValues(numbers);
    }

    @After
    public void tearDown() throws IOException {
        mappingIterator.close();
    }

    @Test
    public void testHasNext() throws IOException {
        assertTrue(mappingIterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        assertEquals(Integer.valueOf(1), mappingIterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        mappingIterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        mappingIterator.close();
    }

    @Test
    public void testReadAll() throws IOException {
        List<Integer> resultList = mappingIterator.readAll();
        assertEquals(2, resultList.size());
        assertTrue(resultList.contains(1));
        assertTrue(resultList.contains(2));
    }

    @Test
    public void testReadAllCustomList() throws IOException {
        List<Integer> customList = new ArrayList<>();
        mappingIterator.readAll(customList);
        assertEquals(2, customList.size());
    }

    @Test
    public void testReadAllCustomCollection() throws IOException {
        List<Integer> customCollection = new ArrayList<>();
        mappingIterator.readAll(customCollection);
        assertEquals(2, customCollection.size());
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