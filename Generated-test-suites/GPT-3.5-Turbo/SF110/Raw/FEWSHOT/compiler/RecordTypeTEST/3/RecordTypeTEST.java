import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testConstructor() {
        RecordType recordType = new RecordType(registry, properties);
        
        // Test that the properties are defined correctly
        assertEquals(properties.size(), recordType.getOwnPropertyNames().size());
        for (String property : properties.keySet()) {
            assertTrue(recordType.hasProperty(property));
            assertEquals(properties.get(property).getType(), recordType.getPropertyType(property));
        }
        
        // Test that the record type is declared
        assertTrue(recordType.isDeclared());
        
        // Test that the record type is frozen
        assertTrue(recordType.isFrozen());
    }
    
    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(registry, properties);
        
        // Try defining a new property
        String newProperty = "newProperty";
        JSType newType = new JSType();
        boolean result = recordType.defineProperty(newProperty, newType, false, null);
        
        // Check that the property is defined correctly
        assertTrue(result);
        assertTrue(recordType.hasProperty(newProperty));
        assertEquals(newType, recordType.getPropertyType(newProperty));
        
        // Try defining a property that already exists
        String existingProperty = properties.keySet().iterator().next();
        JSType existingType = properties.get(existingProperty).getType();
        boolean result2 = recordType.defineProperty(existingProperty, existingType, false, null);
        
        // Check that the existing property remains the same
        assertFalse(result2);
        assertEquals(existingType, recordType.getPropertyType(existingProperty));
    }
    
    @Test
    public void testIsSynthetic() {
        RecordType recordType1 = new RecordType(registry, properties);
        assertFalse(recordType1.isSynthetic());
        
        RecordType recordType2 = new RecordType(registry, properties, false);
        assertTrue(recordType2.isSynthetic());
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);
        
        // Test that two identical record types are equivalent
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, eqMethod));
        
        // Test that two record types with different properties are not equivalent
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("newProperty", new RecordProperty(new JSType(), null));
        RecordType recordType3 = new RecordType(registry, properties2);
        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, eqMethod));
    }
    
    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties2);
        
        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        
        // Test that the greatest subtype contains all properties from both types
        assertEquals(properties.size() + properties2.size(), greatestSubtype.getOwnPropertyNames().size());
        for (String property : properties.keySet()) {
            assertTrue(greatestSubtype.hasProperty(property));
            assertEquals(properties.get(property).getType(), greatestSubtype.getPropertyType(property));
        }
        for (String property : properties2.keySet()) {
            assertTrue(greatestSubtype.hasProperty(property));
            assertEquals(properties2.get(property).getType(), greatestSubtype.getPropertyType(property));
        }
    }
    
    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties2);
        
        // Test that a record type is a subtype of itself
        assertTrue(recordType1.isSubtype(recordType1));
        
        // Test that a record type is a subtype of a record type with the same properties
        assertTrue(recordType1.isSubtype(recordType2));
        
        // Test that a record type is not a subtype of a record type with different properties
        assertFalse(recordType2.isSubtype(recordType1));
    }
}