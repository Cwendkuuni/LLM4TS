import static org.junit.Assert.*;
import org.junit.*;
import com.fasterxml.jackson.databind.*;

public class MappingIteratorTest {

    // Test hasNext() method
    @Test
    public void testHasNext() throws Exception {
        MappingIterator<String> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
        
        ObjectReader reader = new ObjectMapper().readerFor(String.class);
        JsonParser parser = new JsonFactory().createParser("[\"test\"]");
        iterator = new MappingIterator<>(null, parser, null, reader, true, null);
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }
    
    // Test next() method
    @Test
    public void testNext() throws Exception {
        ObjectReader reader = new ObjectMapper().readerFor(String.class);
        JsonParser parser = new JsonFactory().createParser("[\"test\"]");
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, reader, true, null);
        assertEquals("test", iterator.next());
    }
    
    // Test remove() method
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws Exception {
        ObjectReader reader = new ObjectMapper().readerFor(String.class);
        JsonParser parser = new JsonFactory().createParser("[\"test\"]");
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, reader, true, null);
        iterator.remove();
    }
    
    // Test close() method
    @Test
    public void testClose() throws Exception {
        ObjectReader reader = new ObjectMapper().readerFor(String.class);
        JsonParser parser = new JsonFactory().createParser("[\"test\"]");
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, reader, true, null);
        iterator.close();
        assertTrue(parser.isClosed());
    }
    
    // Test readAll() method
    @Test
    public void testReadAll() throws Exception {
        ObjectReader reader = new ObjectMapper().readerFor(String.class);
        JsonParser parser = new JsonFactory().createParser("[\"test1\",\"test2\"]");
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, reader, true, null);
        assertEquals("[test1, test2]", iterator.readAll().toString());
    }
    
    // Test getParser() method
    @Test
    public void testGetParser() throws Exception {
        ObjectReader reader = new ObjectMapper().readerFor(String.class);
        JsonParser parser = new JsonFactory().createParser("[\"test\"]");
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, reader, true, null);
        assertSame(parser, iterator.getParser());
    }
    
    // Test getParserSchema() method
    @Test
    public void testGetParserSchema() throws Exception {
        ObjectReader reader = new ObjectMapper().readerFor(String.class);
        JsonParser parser = new JsonFactory().createParser("[\"test\"]");
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, reader, true, null);
        assertNull(iterator.getParserSchema());
    }
    
    // Test getCurrentLocation() method
    @Test
    public void testGetCurrentLocation() throws Exception {
        ObjectReader reader = new ObjectMapper().readerFor(String.class);
        JsonParser parser = new JsonFactory().createParser("[\"test\"]");
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, reader, true, null);
        assertNotNull(iterator.getCurrentLocation());
    }
    
}