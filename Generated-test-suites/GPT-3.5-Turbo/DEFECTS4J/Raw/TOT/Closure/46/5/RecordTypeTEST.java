import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordTypeBuilder.RecordProperty("TYPE_1"));
        properties.put("b", new RecordTypeBuilder.RecordProperty("TYPE_2"));
        recordType = new RecordType(new JSTypeRegistry(), properties);
    }

    @Test
    public void testIsEquivalentTo_SameRecordType_ReturnsTrue() {
        // Arrange
        RecordType other = recordType;

        // Act
        boolean result = recordType.isEquivalentTo(other);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEquivalentTo_RecordTypesWithSamePropertiesAndEquivalentTypes_ReturnsTrue() {
        // Arrange
        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty("TYPE_1"));
        otherProperties.put("b", new RecordTypeBuilder.RecordProperty("TYPE_2"));
        RecordType other = new RecordType(new JSTypeRegistry(), otherProperties);

        // Act
        boolean result = recordType.isEquivalentTo(other);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEquivalentTo_RecordTypesWithSamePropertiesAndDifferentTypes_ReturnsFalse() {
        // Arrange
        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty("TYPE_2"));
        otherProperties.put("b", new RecordTypeBuilder.RecordProperty("TYPE_1"));
        RecordType other = new RecordType(new JSTypeRegistry(), otherProperties);

        // Act
        boolean result = recordType.isEquivalentTo(other);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetImplicitPrototype_ReturnsNativeObjectType() {
        // Arrange
        ObjectType expected = new JSTypeRegistry().getNativeObjectType(JSTypeNative.OBJECT_TYPE);

        // Act
        ObjectType result = recordType.getImplicitPrototype();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testIsSubtype_RecordTypeIsSubtypeOfItself_ReturnsTrue() {
        // Arrange
        RecordType other = recordType;

        // Act
        boolean result = recordType.isSubtype(other);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsSubtype_SimilarRecordTypeWithSameProperties_ReturnsTrue() {
        // Arrange
        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty("TYPE_1"));
        otherProperties.put("b", new RecordTypeBuilder.RecordProperty("TYPE_2"));
        RecordType other = new RecordType(new JSTypeRegistry(), otherProperties);

        // Act
        boolean result = recordType.isSubtype(other);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsSubtype_SimilarRecordTypeWithDifferentType_ReturnsFalse() {
        // Arrange
        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty("TYPE_2"));
        otherProperties.put("b", new RecordTypeBuilder.RecordProperty("TYPE_2"));
        RecordType other = new RecordType(new JSTypeRegistry(), otherProperties);

        // Act
        boolean result = recordType.isSubtype(other);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSubtype_RecordTypeWithSupersetProperties_ReturnsFalse() {
        // Arrange
        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty("TYPE_1"));
        otherProperties.put("b", new RecordTypeBuilder.RecordProperty("TYPE_2"));
        otherProperties.put("c", new RecordTypeBuilder.RecordProperty("TYPE_3"));
        RecordType other = new RecordType(new JSTypeRegistry(), otherProperties);

        // Act
        boolean result = recordType.isSubtype(other);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetLeastSupertype_RecordTypeWithSubsetProperties_ReturnsSupertype() {
        // Arrange
        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty("TYPE_1"));
        RecordType other = new RecordType(new JSTypeRegistry(), otherProperties);
        
        // Act
        JSType result = recordType.getLeastSupertype(other);
        
        // Assert
        assertEquals(recordType, result);
    }

    @Test
    public void testGetGreatestSubtype_RecordTypeWithSupersetProperties_ReturnsSubtype() {
        // Arrange
        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty("TYPE_1"));
        otherProperties.put("b", new RecordTypeBuilder.RecordProperty("TYPE_2"));
        otherProperties.put("c", new RecordTypeBuilder.RecordProperty("TYPE_3"));
        RecordType other = new RecordType(new JSTypeRegistry(), otherProperties);

        // Act
        JSType result = recordType.getGreatestSubtypeHelper(other);

        // Assert
        assertEquals(other, result);
    }

}