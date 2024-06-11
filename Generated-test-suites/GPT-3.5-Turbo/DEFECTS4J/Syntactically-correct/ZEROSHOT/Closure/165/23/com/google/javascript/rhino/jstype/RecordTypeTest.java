import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        // Create test record type
        RecordTypeBuilder builder = new RecordTypeBuilder();
        recordType = builder.addProperty("a", TYPE_1)
                .addProperty("b", TYPE_2)
                .build();
    }

    @Test
    public void testGetProperty_existingProperty_returnsType() {
        // Arrange

        // Act
        JSType result = recordType.getPropertyType("a");

        // Assert
        assertNotNull(result);
        assertEquals(TYPE_1, result);
    }

    @Test
    public void testGetProperty_nonExistingProperty_returnsNull() {
        // Arrange

        // Act
        JSType result = recordType.getPropertyType("c");

        // Assert
        assertNull(result);
    }

    @Test
    public void testIsEquivalentTo_sameRecordType_returnsTrue() {
        // Arrange
        RecordType other = recordType;

        // Act
        boolean result = recordType.isEquivalentTo(other);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEquivalentTo_differentRecordType_returnsFalse() {
        // Arrange
        RecordType other = new RecordTypeBuilder()
                .addProperty("a", TYPE_1)
                .addProperty("b", TYPE_2)
                .build();

        // Act
        boolean result = recordType.isEquivalentTo(other);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSubtype_subtypeRecordType_returnsTrue() {
        // Arrange
        RecordType subtype = new RecordTypeBuilder()
                .addProperty("a", TYPE_1)
                .addProperty("b", TYPE_2)
                .build();

        // Act
        boolean result = subtype.isSubtype(recordType);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsSubtype_nonSubtypeRecordType_returnsFalse() {
        // Arrange
        RecordType nonSubtype = new RecordTypeBuilder()
                .addProperty("a", TYPE_1)
                .build();

        // Act
        boolean result = nonSubtype.isSubtype(recordType);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSubtype_otherTypeIsObject_returnsTrue() {
        // Arrange
        ObjectType other = new ObjectTypeBuilder()
                .setPrototype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE))
                .build();

        // Act
        boolean result = recordType.isSubtype(other);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsSubtype_otherTypeIsNotRecordType_returnsFalse() {
        // Arrange
        JSType other = new JSTypeBuilder()
                .setNativeType(JSTypeNative.UNKNOWN_TYPE)
                .build();

        // Act
        boolean result = recordType.isSubtype(other);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSubtype_otherTypeIsNonSubtypeRecordType_returnsFalse() {
        // Arrange
        RecordType other = new RecordTypeBuilder()
                .addProperty("a", TYPE_2)
                .addProperty("b", TYPE_1)
                .build();

        // Act
        boolean result = recordType.isSubtype(other);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSubtype_otherTypeIsEquivalentRecordType_returnsFalse() {
        // Arrange
        RecordType other = new RecordTypeBuilder()
                .addProperty("a", TYPE_1)
                .addProperty("b", TYPE_2)
                .build();

        // Act
        boolean result = recordType.isSubtype(other);

        // Assert
        assertFalse(result);
    }

    // Add more test methods for other methods in the RecordType class

}