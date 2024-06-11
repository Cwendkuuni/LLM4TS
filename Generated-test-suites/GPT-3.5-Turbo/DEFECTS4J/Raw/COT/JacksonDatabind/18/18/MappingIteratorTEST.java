
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class MappingIteratorTest {
    
    @Test
    public void testHasNext() {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.treeAsTokens(values);
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        
        Assert.assertTrue(iterator.hasNext());
        
        try {
            iterator.next();
            iterator.next();
            iterator.next();

            Assert.assertFalse(iterator.hasNext());
        } catch (IOException e) {
            Assert.fail("An unexpected IOException occurred");
        }
    }
    
    @Test
    public void testNext() {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.treeAsTokens(values);
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        
        try {
            Assert.assertEquals("value1", iterator.next());
            Assert.assertEquals("value2", iterator.next());
            Assert.assertEquals("value3", iterator.next());
        } catch (IOException e) {
            Assert.fail("An unexpected IOException occurred");
        }
    }
    
    @Test
    public void testRemove() {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.treeAsTokens(values);
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        
        try {
            iterator.next();
            iterator.remove();
            
            Assert.assertEquals("value2", iterator.next());
            Assert.assertEquals("value3", iterator.next());
        } catch (IOException e) {
            Assert.fail("An unexpected IOException occurred");
        }
        
        try {
            iterator.remove();
            Assert.fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testClose() {
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        values.add("value3");
        
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.treeAsTokens(values);
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        
        try {
            iterator.close();
        } catch (IOException e) {
            Assert.fail("An unexpected IOException occurred");
        }
        
        try {
            iterator.hasNext();
            Assert.fail("An expected IOException was not thrown");
        } catch (IOException e) {
            // expected
        }
    }
    
    @Test
    public void testReadAll() {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.treeAsTokens(values);
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        
        try {
            List<String> resultList = iterator.readAll();
            Assert.assertEquals(values, resultList);
        } catch (IOException e) {
            Assert.fail("An unexpected IOException occurred");
        }
    }
    
    @Test
    public void testGetParser() {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.treeAsTokens(values);
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        
        Assert.assertEquals(parser, iterator.getParser());
    }
    
    @Test
    public void testGetParserSchema() {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.treeAsTokens(values);
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        
        Assert.assertEquals(null, iterator.getParserSchema());
    }
    
    @Test
    public void testGetCurrentLocation() {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.treeAsTokens(values);
        MappingIterator<String> iterator = new MappingIterator<>(null, parser, null, null, false, null);
        
        Assert.assertNotNull(iterator.getCurrentLocation());
    }
}
