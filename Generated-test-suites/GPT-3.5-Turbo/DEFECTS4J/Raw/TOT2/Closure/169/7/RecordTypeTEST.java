import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testRecordTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        
        // Define properties for the record type
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordProperty(TYPE_1, propertyNode1));
        properties.put("b", new RecordProperty(TYPE_2, propertyNode2));
        
        RecordType recordType = new RecordType(registry, properties, true);
        
        assertNotNull(recordType);
        assertTrue(recordType.isSynthetic());
        assertFalse(recordType.isFrozen());
    }

    @Test
    public void testRecordTypePropertyDefinition() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, new HashMap<>(), true);
        
        String propertyName = "c";
        JSType propertyType = TYPE_3;
        Node propertyNode = propertyNode3;
        
        recordType.defineProperty(propertyName, propertyType, false, propertyNode);
        
        assertTrue(recordType.hasProperty(propertyName));
        assertEquals(propertyType, recordType.getPropertyType(propertyName));
    }

    @Test
    public void testRecordTypeSubtyping() {
        JSTypeRegistry registry = new JSTypeRegistry();
        
        // Create two record types with different properties
        RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>(), true);
        recordType1.defineProperty("x", TYPE_X, false, propertyNodeX);
        
        RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordProperty>(), true);
        recordType2.defineProperty("y", TYPE_Y, false, propertyNodeY);
        
        assertTrue(RecordType.isSubtype(recordType1, recordType2));
    }

    @Test
    public void testRecordTypeEquivalence() {
        JSTypeRegistry registry = new JSTypeRegistry();
        
        // Create two identical record types
        RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>(), true);
        recordType1.defineProperty("a", TYPE_A, false, propertyNodeA);
        recordType1.defineProperty("b", TYPE_B, false, propertyNodeB);
        
        RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordProperty>(), true);
        recordType2.defineProperty("a", TYPE_A, false, propertyNodeA);
        recordType2.defineProperty("b", TYPE_B, false, propertyNodeB);
        
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
    }
}