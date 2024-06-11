import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testRecordTypeConstructor() {
        // Typical use case
        // Given
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordProperty(TYPE_1, propertyNode));
        
        // When
        RecordType recordType = new RecordType(registry, properties);
        
        // Then
        assertNotNull(recordType);
        assertEquals(true, recordType.isSynthetic());
    }

    @Test
    public void testRecordTypeDefineProperty() {
        // Edge case
        // Given
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordProperty(TYPE_1, propertyNode));
        RecordType recordType = new RecordType(registry, properties);
        
        // When
        boolean defined = recordType.defineProperty("b", TYPE_2, false, propertyNode);
        
        // Then
        assertEquals(true, defined);
        assertEquals(TYPE_2, recordType.getPropertyType("b"));
    }

    @Test
    public void testRecordTypeSubtype() {
        // Error scenario
        // Given
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = new HashMap<>();
        propertiesA.put("a", new RecordProperty(TYPE_1, propertyNode));
        RecordType recordTypeA = new RecordType(registry, propertiesA);
        
        Map<String, RecordProperty> propertiesB = new HashMap<>();
        propertiesB.put("a", new RecordProperty(TYPE_2, propertyNode));
        RecordType recordTypeB = new RecordType(registry, propertiesB);
        
        // When
        boolean isSubtype = recordTypeA.isSubtype(recordTypeB);
        
        // Then
        assertEquals(false, isSubtype);
    }

}