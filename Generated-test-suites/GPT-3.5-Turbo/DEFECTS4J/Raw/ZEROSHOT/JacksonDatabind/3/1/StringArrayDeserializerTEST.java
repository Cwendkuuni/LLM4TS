import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
    
    @Test
    public void testDeserialize() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jsonParser = null; // initialize your json parser here
        DeserializationContext deserializationContext = null; // initialize your deserialization context here
        
        String[] expectedResult = null;
        String[] actualResult = deserializer.deserialize(jsonParser, deserializationContext);
        
        assertArrayEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testDeserializeCustom() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jsonParser = null; // initialize your json parser here
        DeserializationContext deserializationContext = null; // initialize your deserialization context here
        
        String[] expectedResult = null;
        String[] actualResult = deserializer._deserializeCustom(jsonParser, deserializationContext);
        
        assertArrayEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testDeserializeWithType() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jsonParser = null; // initialize your json parser here
        DeserializationContext deserializationContext = null; // initialize your deserialization context here
        TypeDeserializer typeDeserializer = null; // initialize your type deserializer here
        
        Object expectedResult = null;
        Object actualResult = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testHandleNonArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jsonParser = null; // initialize your json parser here
        DeserializationContext deserializationContext = null; // initialize your deserialization context here
        
        String[] expectedResult = null;
        String[] actualResult = deserializer.handleNonArray(jsonParser, deserializationContext);
        
        assertArrayEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext deserializationContext = null; // initialize your deserialization context here
        BeanProperty property = null; // initialize your bean property here
        
        JsonDeserializer<?> expectedResult = null;
        JsonDeserializer<?> actualResult = deserializer.createContextual(deserializationContext, property);
        
        assertEquals(expectedResult, actualResult);
    }
}