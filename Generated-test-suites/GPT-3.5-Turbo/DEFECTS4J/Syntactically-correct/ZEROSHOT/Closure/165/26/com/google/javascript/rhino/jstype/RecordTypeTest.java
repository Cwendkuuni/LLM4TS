import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo_SameRecordType_ReturnsTrue() {
        // Arrange
        RecordType recordType1 = createRecordType();
        RecordType recordType2 = createRecordType();
        
        // Act
        boolean result = recordType1.isEquivalentTo(recordType2);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsEquivalentTo_DifferentRecordType_ReturnsFalse() {
        // Arrange
        RecordType recordType1 = createRecordType();
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), new HashMap<>());
        
        // Act
        boolean result = recordType1.isEquivalentTo(recordType2);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testGetImplicitPrototype_Always_ReturnsObjectType() {
        // Arrange
        RecordType recordType = createRecordType();
        
        // Act
        ObjectType result = recordType.getImplicitPrototype();
        
        // Assert
        assertEquals(ObjectType.class, result.getClass());
    }
    
    @Test
    public void testDefineProperty_NotFrozen_ReturnsTrue() {
        // Arrange
        RecordType recordType = createRecordType();
        
        // Act
        boolean result = recordType.defineProperty("testProperty", new JSType(new JSTypeRegistry()), false, new Node());
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testDefineProperty_Frozen_ReturnsFalse() {
        // Arrange
        RecordType recordType = createFrozenRecordType();
        
        // Act
        boolean result = recordType.defineProperty("testProperty", new JSType(new JSTypeRegistry()), false, new Node());
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testToMaybeRecordType_Always_ReturnsThis() {
        // Arrange
        RecordType recordType = createRecordType();
        
        // Act
        RecordType result = recordType.toMaybeRecordType();
        
        // Assert
        assertEquals(recordType, result);
    }
    
    @Test
    public void testIsSubtype_Subtype_ReturnsTrue() {
        // Arrange
        RecordType recordType1 = createRecordType();
        RecordType recordType2 = createSuperRecordType();
        
        // Act
        boolean result = recordType1.isSubtype(recordType2);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsSubtype_NotSubtype_ReturnsFalse() {
        // Arrange
        RecordType recordType1 = createRecordType();
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), new HashMap<>());
        
        // Act
        boolean result = recordType1.isSubtype(recordType2);
        
        // Assert
        assertFalse(result);
    }

    private RecordType createRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        
        // Add properties to record type
        properties.put("property1", new RecordProperty(new JSType(registry), new Node()));
        properties.put("property2", new RecordProperty(new JSType(registry), new Node()));
        
        return new RecordType(registry, properties);
    }
    
    private RecordType createFrozenRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        
        // Add properties to record type
        properties.put("property1", new RecordProperty(new JSType(registry), new Node()));
        properties.put("property2", new RecordProperty(new JSType(registry), new Node()));
        
        RecordType recordType = new RecordType(registry, properties);
        recordType.freeze();
        return recordType;
    }
    
    private RecordType createSuperRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        
        // Add properties to record type
        properties.put("property1", new RecordProperty(new JSType(registry), new Node()));
        
        return new RecordType(registry, properties);
    }
}