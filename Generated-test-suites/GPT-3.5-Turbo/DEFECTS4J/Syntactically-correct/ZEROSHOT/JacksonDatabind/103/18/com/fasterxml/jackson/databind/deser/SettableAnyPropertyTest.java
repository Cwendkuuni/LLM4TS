import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SettableAnyPropertyTest {
    
    private SettableAnyProperty property;
    
    @Before
    public void setUp() {
        property = new SettableAnyProperty(
                        null, 
                        null, 
                        null, 
                        null, 
                        null, 
                        null
                   );
    }
    
    @Test
    public void testGetProperty() {
        assertNull(property.getProperty());
    }
    
    @Test
    public void testHasValueDeserializer() {
        assertFalse(property.hasValueDeserializer());
    }
    
    @Test
    public void testGetType() {
        assertNull(property.getType());
    }
    
    @Test
    public void testDeserializeAndSet() {
        // TODO: Implement test
    }
    
    @Test
    public void testDeserialize() {
        // TODO: Implement test
    }
    
    @Test
    public void testSet() {
        // TODO: Implement test
    }
    
    @Test
    public void testThrowAsIOE() {
        // TODO: Implement test
    }
    
    @Test
    public void testToString() {
        assertNotNull(property.toString());
    }
    
}