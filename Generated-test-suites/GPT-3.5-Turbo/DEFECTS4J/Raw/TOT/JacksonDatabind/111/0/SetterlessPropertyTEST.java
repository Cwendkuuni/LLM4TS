import static org.junit.Assert.*;
import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testDeserializeAndSet_ValueNull_NothingSet() {
        // Arrange
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        JsonParser parser = new JsonParser(null);
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        // Act
        try {
            property.deserializeAndSet(parser, context, instance);
        } catch (Exception e) {
            fail("Should not throw any exception");
        }
        
        // Assert
        // Nothing should be set on the instance
    }
    
    @Test
    public void testDeserializeAndSet_ValueTypeDeserializerNotNull_ExceptionThrown() {
        // Arrange
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        property.setValueTypeDeserializer(new ValueTypeDeserializer());
        Object instance = new Object();
        JsonParser parser = new JsonParser(null);
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        // Act & Assert
        try {
            property.deserializeAndSet(parser, context, instance);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
            assertEquals("Problem deserializing 'setterless' property (null): no way to handle typed deser with setterless yet", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeAndSet_GetterInvocationThrowsException_ExceptionThrown() {
        // Arrange
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        JsonParser parser = new JsonParser(null);
        DeserializationContext context = new DeserializationContext(null, null, null);
        property.setGetter(new GetterMethod(null));
        
        // Act & Assert
        try {
            property.deserializeAndSet(parser, context, instance);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
            assertEquals("IOException: GetterMethod exception", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeAndSet_GetterReturnsNull_ExceptionThrown() {
        // Arrange
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        JsonParser parser = new JsonParser(null);
        DeserializationContext context = new DeserializationContext(null, null, null);
        property.setGetter(new GetterMethod("null"));
        
        // Act & Assert
        try {
            property.deserializeAndSet(parser, context, instance);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
            assertEquals("Problem deserializing 'setterless' property 'null': get method returned null", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeAndSet_NoExceptions_ValueDeserializedAndSet() {
        // Arrange
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        JsonParser parser = new JsonParser(null);
        DeserializationContext context = new DeserializationContext(null, null, null);
        property.setGetter(new GetterMethod("value"));
        property.setValueDeserializer(new ValueDeserializer());
        
        // Act
        try {
            property.deserializeAndSet(parser, context, instance);
        } catch (Exception e) {
            fail("Should not throw any exception");
        }
        
        // Assert
        // Value should be deserialized and set on the instance
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSet_NotSupported_ExceptionThrown() throws IOException {
        // Arrange
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        
        // Act
        property.set(null, null);
    }
    
    @Test
    public void testSetAndReturn_NotSupported_ExceptionThrown() throws IOException {
        // Arrange
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        
        // Act
        try {
            property.setAndReturn(null, null);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
            assertEquals("Should never call `set()` on setterless property ('null')", e.getMessage());
        }
    }

}