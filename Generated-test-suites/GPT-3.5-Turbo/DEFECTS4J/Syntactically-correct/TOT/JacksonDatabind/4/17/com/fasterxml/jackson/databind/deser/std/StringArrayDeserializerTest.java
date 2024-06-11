import static org.junit.Assert.*;

import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_NullValue() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected = null;
        String[] actual = deserializer.deserialize(null, null);
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_EmptyArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonToken[] tokens = {JsonToken.START_ARRAY, JsonToken.END_ARRAY};
        
        for (JsonToken token : tokens) {
            JsonParser parser = new JsonParser();
            DeserializationContext ctxt = new DeserializationContext();
            String[] expected = new String[0];
            String[] actual = deserializer.deserialize(parser, ctxt);
            
            assertArrayEquals(expected, actual);
        }
    }
    
    @Test
    public void testDeserialize_SingleValue() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonToken[] tokens = {JsonToken.VALUE_STRING, JsonToken.VALUE_NUMBER_INT, JsonToken.VALUE_NULL};
        
        for (JsonToken token : tokens) {
            JsonParser parser = new JsonParser();
            DeserializationContext ctxt = new DeserializationContext();
            String[] expected = new String[1];
            String[] actual = deserializer.deserialize(parser, ctxt);
            
            if (token == JsonToken.VALUE_NULL) {
                expected[0] = null;
            } else {
                expected[0] = parser.getText();
            }
            
            assertArrayEquals(expected, actual);
        }
    }
    
    @Test
    public void testDeserialize_MultipleValues() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonToken[] tokens = {JsonToken.VALUE_STRING, JsonToken.VALUE_NUMBER_INT, JsonToken.VALUE_NULL};
        
        for (JsonToken token : tokens) {
            JsonParser parser = new JsonParser();
            DeserializationContext ctxt = new DeserializationContext();
            String[] expected = new String[3];
            String[] actual = deserializer.deserialize(parser, ctxt);
            
            if (token == JsonToken.VALUE_NULL) {
                expected[0] = null;
            } else {
                expected[0] = parser.getText();
            }
            
            if (token == JsonToken.VALUE_NULL) {
                expected[1] = null;
            } else {
                expected[1] = parser.getText();
            }
            
            if (token == JsonToken.VALUE_NULL) {
                expected[2] = null;
            } else {
                expected[2] = parser.getText();
            }
            
            assertArrayEquals(expected, actual);
        }
    }
    
    @Test
    public void testHandleNonArray_AcceptSingleValueAsArrayEnabled() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonToken[] tokens = {JsonToken.VALUE_STRING, JsonToken.VALUE_NUMBER_INT, JsonToken.VALUE_NULL};
        
        for (JsonToken token : tokens) {
            JsonParser parser = new JsonParser();
            DeserializationContext ctxt = new DeserializationContext();
            ctxt.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            String[] expected = new String[1];
            String[] actual = deserializer.handleNonArray(parser, ctxt);
            
            if (token == JsonToken.VALUE_NULL) {
                expected[0] = null;
            } else {
                expected[0] = parser.getText();
            }
            
            assertArrayEquals(expected, actual);
        }
    }
    
    @Test(expected = Exception.class)
    public void testHandleNonArray_AcceptSingleValueAsArrayDisabled() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonToken[] tokens = {JsonToken.VALUE_STRING, JsonToken.VALUE_NUMBER_INT, JsonToken.VALUE_NULL};
        
        for (JsonToken token : tokens) {
            JsonParser parser = new JsonParser();
            DeserializationContext ctxt = new DeserializationContext();
            ctxt.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            deserializer.handleNonArray(parser, ctxt);
        }
    }
    
    @Test(expected = Exception.class)
    public void testHandleNonArray_EmptyStringAsNullObjectDisabled() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        ctxt.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        deserializer.handleNonArray(parser, ctxt);
    }
    
    @Test
    public void testContextual_NullDeserializer() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        JsonDeserializer<?> expected = null;
        JsonDeserializer<?> actual = deserializer.createContextual(ctxt, null);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testContextual_StringDeserializer() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        BeanProperty property = new BeanProperty();
        JsonDeserializer<?> expected = ctxt.constructType(String.class);
        JsonDeserializer<?> actual = deserializer.createContextual(ctxt, property);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testContextual_NonStringDeserializer() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        BeanProperty property = new BeanProperty();
        JsonDeserializer<?> expected = ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property);
        JsonDeserializer<?> actual = deserializer.createContextual(ctxt, property);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testContextual_DefaultDeserializer() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new DefaultDeserializer());
        DeserializationContext ctxt = new DeserializationContext();
        BeanProperty property = new BeanProperty();
        JsonDeserializer<?> expected = null;
        JsonDeserializer<?> actual = deserializer.createContextual(ctxt, property);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsDefaultDeserializer_DefaultDeserializer() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new DefaultDeserializer());
        JsonDeserializer<?> deser = new DefaultDeserializer();
        boolean expected = true;
        boolean actual = deserializer.isDefaultDeserializer(deser);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsDefaultDeserializer_NonDefaultDeserializer() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> deser = new NonDefaultDeserializer();
        boolean expected = false;
        boolean actual = deserializer.isDefaultDeserializer(deser);
        
        assertEquals(expected, actual);
    }

}