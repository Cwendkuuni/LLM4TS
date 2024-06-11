import static org.junit.Assert.*;
import org.junit.*;

public class MappingIteratorTest {

    @Test
    public void testHasNext() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        try {
            iterator.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testRemove() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testClose() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        try {
            iterator.close();
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testHasNextValue() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        try {
            assertFalse(iterator.hasNextValue());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testNextValue() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        try {
            iterator.nextValue();
            fail("Expected RuntimeJsonMappingException");
        } catch (RuntimeJsonMappingException e) {
            // expected
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadAll() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        try {
            assertEquals(0, iterator.readAll().size());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}