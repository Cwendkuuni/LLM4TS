import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {
    
    // Test cases for deserialize method
    @Test
    public void testDeserialize_WithValidInput_ShouldReturnDeserializedObject() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        DeserializationContext ctxt = null;
        
        String json = "\"test\"";
        String result = deserializer.deserialize(json, ctxt);
        
        assertEquals("test", result);
    }
    
    @Test(expected = JsonParseException.class)
    public void testDeserialize_WithInvalidInput_ShouldThrowJsonParseException() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        DeserializationContext ctxt = null;
        
        String json = "{ \"test\" : \"value\" }";
        deserializer.deserialize(json, ctxt);
    }
    
    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_WithInvalidFormat_ShouldThrowInvalidFormatException() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Integer.class, 0);
        DeserializationContext ctxt = null;
        
        String json = "\"test\"";
        deserializer.deserialize(json, ctxt);
    }
    
    @Test
    public void testDeserialize_WithEmptyStringInput_ShouldReturnNull() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        DeserializationContext ctxt = null;
        
        String json = "\"\"";
        String result = deserializer.deserialize(json, ctxt);
        
        assertNull(result);
    }
    
    // Test cases for _deserialize method
    @Test
    public void testDeserialize_ValidValue_ShouldReturnDeserializedObject() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        DeserializationContext ctxt = null;
        
        String value = "test";
        String result = deserializer._deserialize(value, ctxt);
        
        assertEquals("test", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_WithInvalidValue_ShouldThrowIllegalArgumentException() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Integer.class, 0);
        DeserializationContext ctxt = null;
        
        String value = "test";
        deserializer._deserialize(value, ctxt);
    }
    
    // Test cases for _deserializeEmbedded method
    @Test
    public void testDeserializeEmbedded_ValidObject_ShouldReturnDeserializedObject() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        DeserializationContext ctxt = null;
        
        Object ob = "test";
        String result = deserializer._deserializeEmbedded(ob, ctxt);
        
        assertEquals("test", result);
    }
    
    @Test(expected = JsonParseException.class)
    public void testDeserializeEmbedded_WithInvalidObject_ShouldThrowJsonParseException() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        DeserializationContext ctxt = null;
        
        Object ob = new Integer(10);
        deserializer._deserializeEmbedded(ob, ctxt);
    }
    
    // Test cases for _deserializeFromEmptyString method
    @Test
    public void testDeserializeFromEmptyString_WithStringClass_ShouldReturnNull() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        String result = deserializer._deserializeFromEmptyString();
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeFromEmptyString_WithURIClass_ShouldReturnEmptyUri() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 0);
        
        URI result = deserializer._deserializeFromEmptyString();
        
        assertEquals("", result.toString());
    }
    
    @Test
    public void testDeserializeFromEmptyString_WithOtherClasses_ShouldReturnNull() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Integer.class, 0);
        
        Integer result = deserializer._deserializeFromEmptyString();
        
        assertNull(result);
    }
}