import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class ObjectIdReferencePropertyTest {
    
    @Test
    public void testWithCreatorIndex() {
        ObjectIdInfo info = new ObjectIdInfo("name", ObjectIdGenerators.IntSequenceGenerator.class, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, info);
        
        assertEquals(0, property.getCreatorIndex());
    }
    
    @Test
    public void testDeserializeAndSet() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        
        try {
            property.deserializeAndSet(null, null, null);
            fail("Expected IOException was not thrown");
        } catch (IOException e) {
            // Expected
        }
    }
    
    @Test
    public void testPropertyReferringHandleResolvedForwardReference() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty.PropertyReferring referring = property.new PropertyReferring(property, null, String.class, null);
        
        try {
            referring.handleResolvedForwardReference("id", "value");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IOException e) {
            // Expected
        }
    }
}