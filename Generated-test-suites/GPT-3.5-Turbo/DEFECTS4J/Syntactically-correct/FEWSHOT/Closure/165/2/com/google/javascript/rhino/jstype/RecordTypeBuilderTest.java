import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class RecordTypeBuilderTest {
    
    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        
        JSType type1 = new JSType("type1");
        JSType type2 = new JSType("type2");
        Node propertyNode1 = new Node("propertyNode1");
        Node propertyNode2 = new Node("propertyNode2");
        
        // Add properties
        assertNull(builder.addProperty("property1", type1, propertyNode1));
        assertEquals(builder, builder.addProperty("property2", type2, propertyNode2));
        assertNull(builder.addProperty("property1", type1, propertyNode1)); // Duplicate property
        
        // Build record type
        JSType recordType = builder.build();
        
        // Verify record type
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType);
    }
    
    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        
        JSType type1 = new JSType("type1");
        JSType type2 = new JSType("type2");
        Node propertyNode1 = new Node("propertyNode1");
        Node propertyNode2 = new Node("propertyNode2");
        
        // Add properties
        builder.addProperty("property1", type1, propertyNode1);
        builder.addProperty("property2", type2, propertyNode2);
        
        // Build record type
        JSType recordType = builder.build();
        
        // Verify record type
        assertEquals(new RecordType(registry, Collections.unmodifiableMap(properties)), recordType);
    }
}