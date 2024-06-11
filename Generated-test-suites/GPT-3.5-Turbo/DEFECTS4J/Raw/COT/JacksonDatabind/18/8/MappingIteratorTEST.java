import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.*;

public class MappingIteratorTest {

    private MappingIterator<Integer> emptyIterator;
    private MappingIterator<Integer> singleElementIterator;
    private MappingIterator<Integer> multipleElementsIterator;

    @Before
    public void setUp() {
        // Create an empty iterator
        emptyIterator = MappingIterator.emptyIterator();

        // Create a single element iterator
        List<Integer> list = new ArrayList<>();
        list.add(1);
        singleElementIterator = new MappingIterator<>(list);

        // Create a multiple elements iterator
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        multipleElementsIterator = new MappingIterator<>(list2);
    }
    
    @Test
    public void testEmptyIterator() {
        assertFalse(emptyIterator.hasNext());
        assertNull(emptyIterator.next());
        try {
            emptyIterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Test passed
        }
        try {
            emptyIterator.close();
            // Test passed
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testSingleElementIterator() {
        assertTrue(singleElementIterator.hasNext());
        assertEquals(Integer.valueOf(1), singleElementIterator.next());
        assertFalse(singleElementIterator.hasNext());
        assertNull(singleElementIterator.next());
        try {
            singleElementIterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Test passed
        }
        try {
            singleElementIterator.close();
            // Test passed
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testMultipleElementsIterator() {
        assertTrue(multipleElementsIterator.hasNext());
        assertEquals(Integer.valueOf(1), multipleElementsIterator.next());
        assertTrue(multipleElementsIterator.hasNext());
        assertEquals(Integer.valueOf(2), multipleElementsIterator.next());
        assertTrue(multipleElementsIterator.hasNext());
        assertEquals(Integer.valueOf(3), multipleElementsIterator.next());
        assertFalse(multipleElementsIterator.hasNext());
        assertNull(multipleElementsIterator.next());
        try {
            multipleElementsIterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Test passed
        }
        try {
            multipleElementsIterator.close();
            // Test passed
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testExceptionHandling() {
        // Test JsonMappingException handling
        MappingIterator<List<Integer>> listIterator = new MappingIterator<>(Arrays.asList(Arrays.asList(1, 2)));
        try {
            listIterator.nextValue();
            fail("Expected RuntimeJsonMappingException");
        } catch (RuntimeJsonMappingException e) {
            // Test passed
        } catch (IOException e) {
            fail("IOException thrown");
        }
        try {
            listIterator.hasNextValue();
            fail("Expected RuntimeJsonMappingException");
        } catch (RuntimeJsonMappingException e) {
            // Test passed
        } catch (IOException e) {
            fail("IOException thrown");
        }

        // Test IOException handling
        MappingIterator<Integer> ioExceptionIterator = new MappingIterator<>(Arrays.asList(1).iterator());
        try {
            ioExceptionIterator.nextValue();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Test passed
        } catch (IOException e) {
            fail("IOException thrown");
        }
        try {
            ioExceptionIterator.hasNextValue();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Test passed
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
}