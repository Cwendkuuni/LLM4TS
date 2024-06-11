import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

public class MappingIteratorTest {

    private MappingIterator<String> iterator;
    private ObjectReader reader;
    private List<String> mockData;
    
    @Before
    public void setup() {
        reader = new ObjectReader(null, null, null, null, null);
        mockData = Arrays.asList("test1", "test2", "test3");
    }
    
    @Test
    public void testHasNextValue_WhenIteratorHasNext_ReturnsTrue() throws IOException {
        iterator = new MappingIterator<>(null, null, null, null, false, null);
        iterator = iterator.emptyIterator();
        iterator = reader.readValues(mockData.iterator());
        
        assertTrue(iterator.hasNextValue());
    }
    
    @Test
    public void testHasNextValue_WhenIteratorHasNoNext_ReturnsFalse() throws IOException {
        iterator = reader.readValues(mockData.iterator());
        while (iterator.hasNextValue()) {
            iterator.nextValue();
        }
        
        assertFalse(iterator.hasNextValue());
    }
    
    @Test(expected = RuntimeJsonMappingException.class)
    public void testHasNextValue_WhenIOExceptionThrown_ThrowsRuntimeJsonMappingException() throws IOException {
        iterator = new MappingIterator<>(null, null, null, null, false, null) {
            @Override
            public boolean hasNextValue() throws IOException {
                throw new IOException();
            }
        };
        
        iterator.hasNextValue();
    }
    
    @Test
    public void testNextValue_WhenIteratorHasNext_ReturnsNextValue() throws IOException {
        iterator = reader.readValues(mockData.iterator());
        
        assertEquals("test1", iterator.nextValue());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testNextValue_WhenIteratorHasNoNext_ThrowsNoSuchElementException() throws IOException {
        iterator = reader.readValues(mockData.iterator());
        while (iterator.hasNextValue()) {
            iterator.nextValue();
        }
        
        iterator.nextValue();
    }
    
    @Test(expected = RuntimeJsonMappingException.class)
    public void testNextValue_WhenIOExceptionThrown_ThrowsRuntimeJsonMappingException() throws IOException {
        iterator = new MappingIterator<>(null, null, null, null, false, null) {
            @Override
            public boolean hasNextValue() throws IOException {
                return true;
            }
            
            @Override
            public String nextValue() throws IOException {
                throw new IOException();
            }
        };
        
        iterator.nextValue();
    }
    
    @Test
    public void testReadAll_WhenIteratorHasValues_ReturnsListWithAllValues() throws IOException {
        iterator = reader.readValues(mockData.iterator());
        
        List<String> resultList = iterator.readAll();
        
        assertEquals(mockData, resultList);
    }
    
    @Test
    public void testReadAll_WhenIteratorHasNoValues_ReturnsEmptyList() throws IOException {
        iterator = reader.readValues(new ArrayList<String>().iterator());
        
        List<String> resultList = iterator.readAll();
        
        assertTrue(resultList.isEmpty());
    }
    
    @Test(expected = RuntimeJsonMappingException.class)
    public void testReadAll_WhenIOExceptionThrown_ThrowsRuntimeJsonMappingException() throws IOException {
        iterator = new MappingIterator<>(null, null, null, null, false, null) {
            @Override
            public boolean hasNextValue() throws IOException {
                return true;
            }
            
            @Override
            public String nextValue() throws IOException {
                return "test";
            }
            
            @Override
            public List<String> readAll() throws IOException {
                throw new IOException();
            }
        };
        
        iterator.readAll();
    }
    
    @Test
    public void testGetParser_WhenParserExists_ReturnsParser() throws IOException {
        iterator = reader.readValues(mockData.iterator());
        
        assertEquals(iterator.getParser(), reader.getParser());
    }
    
    @Test
    public void testGetParserSchema_WhenParserSchemaExists_ReturnsParserSchema() throws IOException {
        iterator = reader.readValues(mockData.iterator());
        
        assertEquals(iterator.getParserSchema(), reader.getParser().getSchema());
    }
    
    @Test
    public void testGetCurrentLocation_WhenLocationExists_ReturnsCurrentLocation() throws IOException {
        iterator = reader.readValues(mockData.iterator());
        
        assertEquals(iterator.getCurrentLocation(), reader.getParser().getCurrentLocation());
    }

}