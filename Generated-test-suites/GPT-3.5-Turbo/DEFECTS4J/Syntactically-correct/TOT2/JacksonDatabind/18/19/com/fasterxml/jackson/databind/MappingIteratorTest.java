import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MappingIteratorTest {
    
    @Test
    public void testHasNext() {
        // Test when there is a next value
        MappingIterator<Integer> iterator = new MappingIterator<>();
        assertTrue(iterator.hasNext());
        
        // Test when there is no next value
        MappingIterator<String> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
    }
    
    @Test
    public void testNext() {
        // Test successfully getting the next value
        MappingIterator<String> iterator = new MappingIterator<>();
        assertEquals("test", iterator.next());
        
        // Test throwing exception when no next value
        MappingIterator<Integer> emptyIterator = MappingIterator.emptyIterator();
        try {
            emptyIterator.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // expected
        }
    }
    
    @Test
    public void testRemove() {
        // Test UnsupportedOperationException when calling remove
        MappingIterator<Integer> iterator = new MappingIterator<>();
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testClose() {
        // Test closing the iterator
        MappingIterator<Integer> iterator = new MappingIterator<>();
        try {
            iterator.close();
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }
    
    @Test
    public void testReadAll() {
        // Test reading all entries into a list
        MappingIterator<Integer> iterator = new MappingIterator<>();
        List<Integer> resultList = new ArrayList<>();
        resultList.add(1);
        resultList.add(2);
        iterator.readAll(resultList);
        assertEquals(2, resultList.size());
        assertEquals(Integer.valueOf(1), resultList.get(0));
        assertEquals(Integer.valueOf(2), resultList.get(1));
    }
    
    @Test
    public void testGetParser() {
        // Test getting the parser
        MappingIterator<Integer> iterator = new MappingIterator<>();
        assertNotNull(iterator.getParser());
    }
}