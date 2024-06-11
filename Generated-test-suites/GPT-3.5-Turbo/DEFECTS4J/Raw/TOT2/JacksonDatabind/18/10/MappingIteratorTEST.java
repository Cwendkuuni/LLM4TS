import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;

public class MappingIteratorTest {

    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.nextValue());
    }

    @Test
    public void testRemove() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testClose() throws IOException {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        iterator.close();
        assertNull(iterator.getParser());
    }

    @Test
    public void testReadAll() throws IOException {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        List<Integer> resultList = iterator.readAll(list);
        assertEquals(list.size(), resultList.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), resultList.get(i));
        }
    }

    @Test
    public void testEmptyIterator() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testGetParser() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        assertNull(iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        try {
            MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
            iterator.getParserSchema();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetCurrentLocation() {
        try {
            MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
            iterator.getCurrentLocation();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}