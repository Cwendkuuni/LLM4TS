import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;

public class MappingIteratorTest {
    
    private MappingIterator<String> mappingIterator;
    
    private JsonParser parser;
    private DeserializationContext context;
    private JsonDeserializer<String> deserializer;
    private boolean closeParser;
    
    private JavaType type;
    private Object valueToUpdate;
    
    @Before
    public void setUp() {
        type = new JavaType();
        parser = new JsonParser();
        context = new DeserializationContext();
        deserializer = new JsonDeserializer<String>();
        closeParser = true;
        valueToUpdate = new Object();
        mappingIterator = new MappingIterator<String>(type, parser, context, deserializer, closeParser, valueToUpdate);
    }
    
    @Test
    public void testHasNextValue() {
        // TODO: implement test
    }
    
    @Test
    public void testNextValue() {
        // TODO: implement test
    }
    
    @Test
    public void testReadAll_List() {
        // TODO: implement test
    }
    
    @Test
    public void testReadAll_Collection() {
        // TODO: implement test
    }
    
    @Test
    public void testGetParser() {
        // TODO: implement test
    }
    
    @Test
    public void testGetParserSchema() {
        // TODO: implement test
    }
    
    @Test
    public void testGetCurrentLocation() {
        // TODO: implement test
    }
}