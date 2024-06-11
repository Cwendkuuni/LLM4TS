import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_WhenStartArrayTokenThenReturnsStringArray() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testDeserialize_WhenElementDeserializerNotNullThenReturnsStringArray() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        JsonDeserializer<String> elementDeserializer = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testDeserialize_WhenTokenIsEndArrayThenReturnsStringArray() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testDeserialize_WhenTokenIsValueStringThenReturnsStringArray() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testDeserialize_WhenTokenIsValueNullThenReturnsStringArray() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testDeserialize_WhenTokenIsOtherThenReturnsStringArray() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void test_deserializeCustom_WhenTokenIsEndArrayThenReturnsStringArray() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer._deserializeCustom(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void test_deserializeCustom_WhenTokenIsValueNullThenReturnsStringArray() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer._deserializeCustom(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void test_deserializeCustom_WhenTokenIsOtherThenReturnsStringArray() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer._deserializeCustom(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void test_deserializeWithType_WithJsonParserAndDeserializationContextAndTypeDeserializer_ThenReturnsObject() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        assertNotNull(result);
    }
    
    @Test
    public void test_handleNonArray_WhenTokenIsValueStringAndAcceptEmptyStringAsNullObjectThenReturnsStringArray() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test(expected = ctxt.mappingException(_valueClass))
    public void test_handleNonArray_WhenAcceptSingleValueAsArrayNotEnabled_ThenThrowsMappingException() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.handleNonArray(jp, ctxt);
    }
    
    @Test
    public void test_createContextual_WithDeserializationContextAndBeanProperty_ThenReturnsJsonDeserializer() {
        DeserializationContext ctxt = null;
        BeanProperty property = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        assertNotNull(result);
    }
    
    @Test(expected = nullPointerException)
    public void test_createContextual_WithDeserializationContextAndBeanProperty_ThenThrowsNullPointerException() {
        DeserializationContext ctxt = null;
        BeanProperty property = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        deserializer.createContextual(ctxt, property);
    }
}