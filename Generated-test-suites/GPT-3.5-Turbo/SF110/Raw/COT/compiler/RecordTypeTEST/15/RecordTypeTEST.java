import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {
    
    // 
    
    @Test
    public void testRecordTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        
        assertNotNull(recordType);
        assertTrue(recordType.isSynthetic());
    }
    
    @Test
    public void testRecordTypeDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        
        assertTrue(recordType.defineProperty("property", null, false, null));
        
        // Test if the record type is frozen
        assertFalse(recordType.defineProperty("property2", null, false, null));
    }
    
    @Test
    public void testRecordTypeGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        JSType that = new JSType(registry);
        
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
        
        assertNotNull(greatestSubtype);
    }
    
    @Test
    public void testRecordTypeIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType typeA = new RecordType(registry, properties);
        RecordType typeB = new RecordType(registry, properties);
        
        boolean isSubtype = RecordType.isSubtype(typeA, typeB);
        
        assertTrue(isSubtype);
    }
    
    // 
    
}