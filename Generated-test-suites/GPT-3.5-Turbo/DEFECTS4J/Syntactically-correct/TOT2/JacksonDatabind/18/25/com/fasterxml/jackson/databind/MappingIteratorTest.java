import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.MappingIterator;

public class MappingIteratorTest {

    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<String> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        MappingIterator<String> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNextValue());
        assertNull(iterator.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        List<Integer> expectedList = new ArrayList<>();
        expectedList.add(1);
        expectedList.add(2);
        expectedList.add(3);

        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();

        List<Integer> resultList = new ArrayList<>();
        assertEquals(expectedList, iterator.readAll(resultList));
    }

    @Test
    public void testGetParser() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        assertNull(iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        assertNull(iterator.getCurrentLocation());
    }

    @Test(expected = NoSuchElementException.class)
    public void testThrowNoSuchElement() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        iterator._throwNoSuchElement();
    }

    @Test(expected = RuntimeException.class)
    public void testHandleMappingException() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        iterator._handleMappingException(new RuntimeException());
    }

    @Test(expected = RuntimeException.class)
    public void testHandleIOException() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        iterator._handleIOException(new IOException());
    }
}