import static org.junit.Assert.*;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

public class MappingIteratorTest {

    @Test
    public void testHasNext() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.writer().getFactory().createParser(list.toString());
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testNext() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.writer().getFactory().createParser(list.toString());
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        
        assertEquals(new Integer(1), iterator.next());
    }

    @Test
    public void testRemove() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.writer().getFactory().createParser(list.toString());
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        
        try {
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            assertEquals("Operation not supported", e.getMessage());
        }
    }

    @Test
    public void testClose() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.writer().getFactory().createParser(list.toString());
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, null, null, false, null);

        try {
            iterator.close();
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testHasNextValue() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.writer().getFactory().createParser(list.toString());
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        
        assertTrue(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.writer().getFactory().createParser(list.toString());
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        
        assertEquals(new Integer(1), iterator.nextValue());
    }

    @Test
    public void testReadAll() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.writer().getFactory().createParser(list.toString());
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, null, null, false, null);

        List<Integer> resultList = new ArrayList<>();
        try {
            resultList = iterator.readAll();
        } catch (Exception e) {
            fail("Exception occurred");
        }

        assertEquals(list, resultList);
    }

    @Test
    public void testReadAllWithResultList() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.writer().getFactory().createParser(list.toString());
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, null, null, false, null);

        List<Integer> resultList = new ArrayList<>();
        try {
            resultList = iterator.readAll(resultList);
        } catch (Exception e) {
            fail("Exception occurred");
        }

        assertEquals(list, resultList);
    }

    @Test
    public void testReadAllWithResults() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.writer().getFactory().createParser(list.toString());
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, null, null, false, null);

        Set<Integer> resultSet = new HashSet<>();
        try {
            resultSet = iterator.readAll(resultSet);
        } catch (Exception e) {
            fail("Exception occurred");
        }

        assertEquals(new HashSet<>(list), resultSet);
    }

    @Test
    public void testGetParser() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.writer().getFactory().createParser(list.toString());
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, null, null, false, null);

        assertEquals(parser, iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.writer().getFactory().createParser(list.toString());
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, null, null, false, null);

        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.writer().getFactory().createParser(list.toString());
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, null, null, false, null);

        assertNotNull(iterator.getCurrentLocation());
    }
}