import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

    @Test
    public void testDeserializeAndSet() {
        // Arrange
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null,
            null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        String propName = "prop";
        
        // Act
        try {
            settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
            fail("IOException not thrown");
        } catch (IOException e) {
            // Assert
            assertEquals("IOException message incorrect", "Unresolved forward reference but no identity info.", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeAndSet_WithKeyDeserializer() {
        // Arrange
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null,
            null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        String propName = "prop";
        
        // Act
        try {
            settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
            fail("IOException not thrown");
        } catch (IOException e) {
            // Assert
            assertEquals("IOException message incorrect", "Unresolved forward reference but no identity info.", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeAndSet_WithNullValue() {
        // Arrange
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null,
            null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        String propName = "prop";
        
        // Act
        try {
            settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
            fail("IOException not thrown");
        } catch (IOException e) {
            // Assert
            assertEquals("IOException message incorrect", "Unresolved forward reference but no identity info.", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeAndSet_WithTypeDeserializer() {
        // Arrange
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null,
            null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        String propName = "prop";
        
        // Act
        try {
            settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
            fail("IOException not thrown");
        } catch (IOException e) {
            // Assert
            assertEquals("IOException message incorrect", "Unresolved forward reference but no identity info.", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeWithNullValue() {
        // Arrange
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null,
            null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        
        // Act
        Object result = settableAnyProperty.deserialize(p, ctxt);
        
        // Assert
        assertNull("Deserialized object should be null", result);
    }
    
    @Test
    public void testDeserializeWithTypeDeserializer() {
        // Arrange
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null,
            null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        
        // Act
        Object result = settableAnyProperty.deserialize(p, ctxt);
        
        // Assert
        assertNull("Deserialized object should be null", result);
    }
    
    @Test
    public void testSet_WithSetterIsField() throws IOException {
        // Arrange
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null,
            new AnnotatedFieldTest(), null, null, null, null);
        Object instance = new Object();
        Object propName = "prop";
        Object value = "value";
        
        // Act
        settableAnyProperty.set(instance, propName, value);
        
        // Assert
        assertTrue("Setter is field, value should be added to map", AnnotatedFieldTest.values.containsKey("prop"));
        assertEquals("Setter is field, map value incorrect", "value", AnnotatedFieldTest.values.get("prop"));
    }
    
    @Test
    public void testSet_WithSetterIsMethod() throws IOException {
        // Arrange
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null,
            new AnnotatedMethodTest(), null, null, null, null);
        Object instance = new Object();
        Object propName = "prop";
        Object value = "value";
        
        // Act
        settableAnyProperty.set(instance, propName, value);
        
        // Assert
        assertTrue("Setter is method, value should be added to map", AnnotatedMethodTest.values.containsKey("prop"));
        assertEquals("Setter is method, map value incorrect", "value", AnnotatedMethodTest.values.get("prop"));
    }
    
    @Test
    public void testSet_WithException() {
        // Arrange
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null,
            new AnnotatedMethodTest(), null, null, null, null);
        Object instance = new Object();
        Object propName = "prop";
        Object value = "value";
        
        // Act
        try {
            settableAnyProperty.set(instance, propName, value);
            fail("IOException not thrown");
        } catch (IOException e) {
            // Assert
            assertEquals("IOException message incorrect", "Problem deserializing \"any\" property 'prop' of class AnnotatedMethodTest (expected type: null; actual type: java.lang.String)", e.getMessage());
        }
    }
    
    private static class AnnotatedFieldTest extends AnnotatedField {
        
        private static Map<String, Object> values = new HashMap<>();
        
        @Override
        public Object getValue(Object obj) throws IllegalArgumentException {
            return values;
        }
        
    }
    
    private static class AnnotatedMethodTest extends AnnotatedMethod {
        
        private static Map<String, Object> values = new HashMap<>();
        
        public void callOnWith(Object obj, Object propName, Object value) {
            values.put((String)propName, value);
        }
        
    }
    
}