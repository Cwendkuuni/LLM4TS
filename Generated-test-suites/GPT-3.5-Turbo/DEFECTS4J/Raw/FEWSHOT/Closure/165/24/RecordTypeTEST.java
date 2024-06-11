import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RecordTypeTest {

    @Test
    public void testProperties() {
        RecordType recordType = new RecordType(registry, properties);
        
        // Test that all properties are defined correctly
        for (String property : properties.keySet()) {
            assertEquals(properties.get(property), recordType.getPropertyType(property));
        }
    }
    
    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);
        
        // Test that recordType1 is a subtype of recordType2
        assertEquals(true, recordType1.isSubtype(recordType2));
    }
    
    @Test
    public void testIsEquivalentTo() {
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);
        
        // Test that recordType1 is equivalent to recordType2
        assertEquals(true, recordType1.isEquivalentTo(recordType2));
    }
    
    @Test
    public void testResolveInternal() {
        RecordType recordType = new RecordType(registry, properties);
        
        // Test that the properties are correctly resolved
        for (String property : properties.keySet()) {
            JSType resolvedType = properties.get(property).resolveInternal(t, scope);
            assertEquals(resolvedType, recordType.getPropertyType(property));
        }
    }
}