import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RecordTypeTest {
    
    @Test
    public void testIsEquivalentTo() {
        // Create RecordType instances
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties1.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties1.put("c", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties2.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties2.put("c", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        RecordType recordType2 = new RecordType(registry, properties2);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties3 = new HashMap<>();
        properties3.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties3.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties3.put("d", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        RecordType recordType3 = new RecordType(registry, properties3);
        
        // Test equivalence with identical properties
        assertEquals(true, recordType1.isEquivalentTo(recordType2));
        
        // Test equivalence with different properties
        assertEquals(false, recordType1.isEquivalentTo(recordType3));
    }
    
    @Test
    public void testGetLeastSupertype() {
        // Create RecordType instances
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties1.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties1.put("c", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties2.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties2.put("c", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties2.put("d", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        RecordType recordType2 = new RecordType(registry, properties2);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties3 = new HashMap<>();
        properties3.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties3.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        RecordType recordType3 = new RecordType(registry, properties3);
        
        // Test least supertype with identical properties
        RecordType leastSupertype1 = recordType1.getLeastSupertype(recordType2);
        assertEquals(true, leastSupertype1.isEquivalentTo(recordType1));
        
        // Test least supertype with overlapping properties
        RecordType leastSupertype2 = recordType1.getLeastSupertype(recordType3);
        assertEquals(true, leastSupertype2.isEquivalentTo(recordType1));
    }
    
    @Test
    public void testIsSubtype() {
        // Create RecordType instances
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties1.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        RecordType recordType1 = new RecordType(registry, properties1);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties2.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties2.put("c", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        RecordType recordType2 = new RecordType(registry, properties2);
        
        Map<String, RecordTypeBuilder.RecordProperty> properties3 = new HashMap<>();
        properties3.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        properties3.put("d", new RecordTypeBuilder.RecordProperty(new JSType(), null));
        RecordType recordType3 = new RecordType(registry, properties3);
        
        // Test subtype with identical properties
        assertEquals(true, recordType1.isSubtype(recordType2));
        assertEquals(true, recordType2.isSubtype(recordType1));
        
        // Test subtype with overlapping properties
        assertEquals(true, recordType1.isSubtype(recordType3));
        assertEquals(false, recordType3.isSubtype(recordType1));
    }
}