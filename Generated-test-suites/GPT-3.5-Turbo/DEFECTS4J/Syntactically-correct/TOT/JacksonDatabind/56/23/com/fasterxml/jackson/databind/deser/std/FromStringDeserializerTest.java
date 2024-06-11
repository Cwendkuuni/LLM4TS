import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FromStringDeserializerTest {

    FromStringDeserializer fsd;
    
    @Before
    public void setUp() {
        fsd = new FromStringDeserializer.Std(String.class, 1);
    }
    
    @Test
    public void testDeserialize_ValidStringValue_ReturnsDeserializedObject() throws Exception {
        String jsonValue = "\"testString\"";
        
        String result = (String) fsd.deserialize(JsonParser.mockJsonParser(jsonValue), DeserializationContext.mockDeserializationContext());
        
        assertEquals("testString", result);
    }
    
    @Test
    public void testDeserialize_NullValue_ReturnsNull() throws Exception {
        String jsonValue = "null";
        
        String result = (String) fsd.deserialize(JsonParser.mockJsonParser(jsonValue), DeserializationContext.mockDeserializationContext());
        
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_EmptyStringValue_ReturnsDeserializedObject() throws Exception {
        String jsonValue = "\"\"";
        
        String result = (String) fsd.deserialize(JsonParser.mockJsonParser(jsonValue), DeserializationContext.mockDeserializationContext());
        
        assertEquals("", result);
    }
    
    @Test
    public void testDeserialize_InvalidStringValue_ThrowsException() throws Exception {
        String jsonValue = "\"invalidString\"";
        
        try {
            fsd.deserialize(JsonParser.mockJsonParser(jsonValue), DeserializationContext.mockDeserializationContext());
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("not a valid textual representation", e.getMessage());
            assertNotNull(e.getCause());
            assertEquals("problem: Invalid value", e.getCause().getMessage());
        }
    }
    
    @Test
    public void testDeserialize_StartArray_UnwrapSingleValueArraysEnabled_ReturnsDeserializedObject() throws Exception {
        String jsonValue = "[\"testString\"]";
        DeserializationContext ctxt = DeserializationContext.mockDeserializationContext();
        ctxt.enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);
        
        String result = (String) fsd.deserialize(JsonParser.mockJsonParser(jsonValue), ctxt);
        
        assertEquals("testString", result);
    }
    
    @Test
    public void testDeserialize_StartArray_UnwrapSingleValueArraysNotEnabled_ThrowsException() throws Exception {
        String jsonValue = "[\"testString\"]";
        
        try {
            fsd.deserialize(JsonParser.mockJsonParser(jsonValue), DeserializationContext.mockDeserializationContext());
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Attempted to unwrap single value array for single 'java.lang.String' value but there was more than a single value in the array", e.getMessage());
        }
    }
    
    @Test
    public void testDeserialize_EmbeddedObject_NullValue_ReturnsNull() throws Exception {
        DeserializationContext ctxt = DeserializationContext.mockDeserializationContext();
        when(ctxt.getTypeFactory()).thenReturn(TypeFactory.mockTypeFactory());
        Object obj = null;
        when(parser.getEmbeddedObject()).thenReturn(obj);
        
        String result = (String) fsd.deserialize(JsonParser.mockJsonParser(null), ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_EmbeddedObject_ValueAssignableToValueType_ReturnsDeserializedObject() throws Exception {
        DeserializationContext ctxt = DeserializationContext.mockDeserializationContext();
        when(parser.getEmbeddedObject()).thenReturn("testString");
        
        String result = (String) fsd.deserialize(JsonParser.mockJsonParser(null), ctxt);
        
        assertEquals("testString", result);
    }
    
    @Test
    public void testDeserialize_EmbeddedObject_ValueNotAssignableToValueType_ThrowsException() throws Exception {
        DeserializationContext ctxt = DeserializationContext.mockDeserializationContext();
        when(parser.getEmbeddedObject()).thenReturn(5);
        
        try {
            fsd.deserialize(JsonParser.mockJsonParser(null), ctxt);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Don't know how to convert embedded Object of type java.lang.Integer into java.lang.String", e.getMessage());
        }
    }
    
    @Test
    public void testDeserialize_NotStartArrayOrEmbeddedObject_ThrowsException() throws Exception {
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        
        try {
            fsd.deserialize(JsonParser.mockJsonParser(null), DeserializationContext.mockDeserializationContext());
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Cannot deserialize value of type java.lang.String from Token null", e.getMessage());
        }
    }
    
    @Test
    public void testDeserialize_InvalidKindValue_ThrowsException() throws Exception {
        fsd = new FromStringDeserializer.Std(String.class, 13);
        
        try {
            fsd.deserialize(JsonParser.mockJsonParser(null), DeserializationContext.mockDeserializationContext());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(null, e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeWithEmptyStringForURI_ReturnsEmptyURI() throws Exception {
        fsd = new FromStringDeserializer.Std(URI.class, 3);
        
        URI result = (URI) fsd.deserialize(JsonParser.mockJsonParser("\"\""), DeserializationContext.mockDeserializationContext());
        
        assertEquals(URI.create(""), result);
    }
    
    @Test
    public void testDeserializeWithEmptyStringForLocale_ReturnsRootLocale() throws Exception {
        fsd = new FromStringDeserializer.Std(Locale.class, 3);
        
        Locale result = (Locale) fsd.deserialize(JsonParser.mockJsonParser("\"\""), DeserializationContext.mockDeserializationContext());
        
        assertEquals(Locale.ROOT, result);
    }

}