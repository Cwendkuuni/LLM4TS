import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class MappingIteratorTest {

    @Test
    public void testHasNext_ValueExists_ReturnsTrue() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.createParser("[1]");
        MappingIterator<Integer> iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), parser, null, null, false, null);
        
        // Act
        boolean result = iterator.hasNext();
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testHasNext_NoValueExists_ReturnsFalse() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.createParser("[]");
        MappingIterator<Integer> iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), parser, null, null, false, null);
        
        // Act
        boolean result = iterator.hasNext();
        
        // Assert
        assertFalse(result);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testHasNext_JsonMappingException_ThrowsJsonMappingException() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.createParser("[\"abc\"]");
        MappingIterator<Integer> iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), parser, null, null, false, null);
        
        // Act
        boolean result = iterator.hasNext();
    }
    
    @Test(expected = IOException.class)
    public void testHasNext_IOException_ThrowsIOException() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.createParser("[1]");
        MappingIterator<Integer> iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), parser, null, null, false, null);
        iterator.close();
        
        // Act
        boolean result = iterator.hasNext();
    }
    
    @Test
    public void testNext_ValueExists_ReturnsNextValue() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.createParser("[1]");
        MappingIterator<Integer> iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), parser, null, null, false, null);
        
        // Act
        Integer result = iterator.next();
        
        // Assert
        assertEquals(Integer.valueOf(1), result);
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testNext_NoValueExists_ThrowsNoSuchElementException() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.createParser("[]");
        MappingIterator<Integer> iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), parser, null, null, false, null);
        
        // Act
        Integer result = iterator.next();
    }
    
    @Test(expected = JsonMappingException.class)
    public void testNext_JsonMappingException_ThrowsJsonMappingException() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.createParser("[\"abc\"]");
        MappingIterator<Integer> iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), parser, null, null, false, null);
        
        // Act
        Integer result = iterator.next();
    }
    
    @Test(expected = IOException.class)
    public void testNext_IOException_ThrowsIOException() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.createParser("[1]");
        MappingIterator<Integer> iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), parser, null, null, false, null);
        iterator.close();
        
        // Act
        Integer result = iterator.next();
    }
    
    @Test
    public void testRemove_Always_ThrowsUnsupportedOperationException() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.createParser("[1]");
        MappingIterator<Integer> iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), parser, null, null, false, null);
        
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }
    
    @Test
    public void testClose_Always_ClosesParser() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.createParser("[1]");
        MappingIterator<Integer> iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), parser, null, null, false, null);
        
        // Act
        iterator.close();
        
        // Assert
        assertFalse(iterator.getParser().isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE));
    }
    
    
    @Test
    public void testReadAll_ValuesExist_ReturnsAllValues() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.createParser("[1, 2, 3]");
        MappingIterator<Integer> iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), parser, null, null, false, null);
        List<Integer> resultList = new ArrayList<>();
        
        // Act
        List<Integer> result = iterator.readAll(resultList);
        
        // Assert
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(1), result.get(0));
        assertEquals(Integer.valueOf(2), result.get(1));
        assertEquals(Integer.valueOf(3), result.get(2));
    }
    
    @Test(expected = JsonMappingException.class)
    public void testReadAll_JsonMappingException_ThrowsJsonMappingException() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.createParser("[1, \"abc\"]");
        MappingIterator<Integer> iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), parser, null, null, false, null);
        List<Integer> resultList = new ArrayList<>();
        
        // Act
        List<Integer> result = iterator.readAll(resultList);
    }
    
    @Test(expected = IOException.class)
    public void testReadAll_IOException_ThrowsIOException() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.createParser("[1]");
        MappingIterator<Integer> iterator = new MappingIterator<>(TypeFactory.defaultInstance().constructType(Integer.class), parser, null, null, false, null);
        iterator.close();
        List<Integer> resultList = new ArrayList<>();
        
        // Act
        List<Integer> result = iterator.readAll(resultList);
    }
}