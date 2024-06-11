import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RecordTypeBuilderTest {
    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType type = new JSType();
        Node propertyNode = new Node();
        
        // Test adding a property
        assertNull(builder.addProperty("name", type, propertyNode));
        
        // Test adding the same property again, should return null
        assertNull(builder.addProperty("name", type, propertyNode));
    }
    
    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType type = new JSType();
        Node propertyNode = new Node();
        
        // Test building an empty record type
        JSType recordType = builder.build();
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType);
        
        // Test building a non-empty record type
        builder.addProperty("name", type, propertyNode);
        recordType = builder.build();
        assertEquals(new RecordType(registry, Collections.unmodifiableMap(properties)), recordType);
    }
}