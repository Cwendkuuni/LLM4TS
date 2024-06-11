import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class MappingIteratorTest {

    @Test
    public void testHasNextEmpty() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNextEmpty() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        try {
            iterator.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // Expected exception
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testRemoveUnsupported() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testClose() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        try {
            iterator.close();
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testHasNextWithValues() {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        MappingIterator<String> iterator = new MappingIterator<String>(null, null, null, null, false, null);
        iterator = createIterator(values);
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNextWithValues() {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        MappingIterator<String> iterator = createIterator(values);
        try {
            assertEquals("value1", iterator.next());
            assertEquals("value2", iterator.next());
            assertEquals("value3", iterator.next());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        try {
            iterator.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // Expected exception
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testReadAllWithValues() {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        MappingIterator<String> iterator = createIterator(values);
        try {
            List<String> resultList = iterator.readAll();
            assertEquals(values, resultList);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testReadAllEmpty() {
        List<String> values = new ArrayList<>();
        MappingIterator<String> iterator = createIterator(values);
        try {
            List<String> resultList = iterator.readAll();
            assertTrue(resultList.isEmpty());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    private <T> MappingIterator<T> createIterator(List<T> values) {
        return new MappingIterator<T>(null, null, null, null, false, null) {
            private final List<T> list = values;
            private int currentIndex = 0;

            @Override
            public boolean hasNextValue() throws IOException {
                return currentIndex < list.size();
            }

            @Override
            public T nextValue() throws IOException {
                if (currentIndex >= list.size()) {
                    throw new NoSuchElementException();
                }
                return list.get(currentIndex++);
            }

            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };
    }
}