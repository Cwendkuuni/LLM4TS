import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MappingIteratorTest {
    
    private MappingIterator<String> stringIterator;
    private MappingIterator<Integer> integerIterator;
    
    @Before
    public void setUp() throws Exception {
        stringIterator = new MappingIterator<String>(null, null, null, null, false, null);
        integerIterator = new MappingIterator<Integer>(null, null, null, null, false, null);
    }
    
    @Test
    public void testHasNext() {
        assertFalse(stringIterator.hasNext());
        assertFalse(integerIterator.hasNext());
    }
    
    @Test
    public void testNext() {
        try {
            stringIterator.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // expected
        }
        
        try {
            integerIterator.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // expected
        }
    }
    
    @Test
    public void testRemove() {
        try {
            stringIterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        
        try {
            integerIterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testClose() {
        try {
            stringIterator.close();
            // no exception thrown
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        try {
            integerIterator.close();
            // no exception thrown
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testHasNextValue() throws Exception {
        assertFalse(stringIterator.hasNextValue());
        assertFalse(integerIterator.hasNextValue());
    }
    
    @Test
    public void testNextValue() throws Exception {
        try {
            stringIterator.nextValue();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // expected
        }
        
        try {
            integerIterator.nextValue();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // expected
        }
    }
    
    @Test
    public void testReadAll() throws Exception {
        assertEquals(new ArrayList<String>(), stringIterator.readAll());
        assertEquals(new ArrayList<Integer>(), integerIterator.readAll());
    }
    
    @Test
    public void testReadAllWithList() throws Exception {
        List<String> stringList = new ArrayList<String>();
        List<Integer> integerList = new ArrayList<Integer>();
        
        stringIterator.readAll(stringList);
        integerIterator.readAll(integerList);
        
        assertEquals(new ArrayList<String>(), stringList);
        assertEquals(new ArrayList<Integer>(), integerList);
    }
    
    @Test
    public void testReadAllWithCollection() throws Exception {
        List<String> stringList = new ArrayList<String>();
        List<Integer> integerList = new ArrayList<Integer>();
        
        stringIterator.readAll(stringList);
        integerIterator.readAll(integerList);
        
        assertEquals(new ArrayList<String>(), stringList);
        assertEquals(new ArrayList<Integer>(), integerList);
    }
    
    @Test
    public void testGetParser() {
        assertNull(stringIterator.getParser());
        assertNull(integerIterator.getParser());
    }
    
    @Test
    public void testGetParserSchema() {
    	assertNull(stringIterator.getParserSchema());
    	assertNull(integerIterator.getParserSchema());
    }
    
    @Test
    public void testGetCurrentLocation() {
        assertNull(stringIterator.getCurrentLocation());
        assertNull(integerIterator.getCurrentLocation());
    }
    
}