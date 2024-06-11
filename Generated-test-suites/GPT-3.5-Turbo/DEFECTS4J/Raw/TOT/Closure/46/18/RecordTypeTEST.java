import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setup() {
        // Create a sample RecordType object for testing
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        builder.addProperty("a", JSTypeRegistry.getNativeType(JSTypeNative.STRING_TYPE), null);
        builder.addProperty("b", JSTypeRegistry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
        recordType = builder.build();
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
    public void testIsEquivalentTo_IdenticalProperties_ReturnsTrue() {
        // Arrange
        RecordType other = new RecordTypeBuilder(new JSTypeRegistry())
                .addProperty("a", JSTypeRegistry.getNativeType(JSTypeNative.STRING_TYPE), null)
                .addProperty("b", JSTypeRegistry.getNativeType(JSTypeNative.NUMBER_TYPE), null)
                .build();

        // Act
        boolean result = recordType.isEquivalentTo(other);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEquivalentTo_DifferentProperties_ReturnsFalse() {
        // Arrange
        RecordType other = new RecordTypeBuilder(new JSTypeRegistry())
                .addProperty("c", JSTypeRegistry.getNativeType(JSTypeNative.STRING_TYPE), null)
                .build();

        // Act
        boolean result = recordType.isEquivalentTo(other);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSubtype_SubtypeWithSameProperties_ReturnsTrue() {
        // Arrange
        RecordType subtype = new RecordTypeBuilder(new JSTypeRegistry())
                .addProperty("a", JSTypeRegistry.getNativeType(JSTypeNative.STRING_TYPE), null)
                .addProperty("b", JSTypeRegistry.getNativeType(JSTypeNative.NUMBER_TYPE), null)
                .build();

        // Act
        boolean result = subtype.isSubtype(recordType);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsSubtype_SubtypeWithAdditionalProperties_ReturnsTrue() {
        // Arrange
        RecordType subtype = new RecordTypeBuilder(new JSTypeRegistry())
                .addProperty("a", JSTypeRegistry.getNativeType(JSTypeNative.STRING_TYPE), null)
                .addProperty("b", JSTypeRegistry.getNativeType(JSTypeNative.NUMBER_TYPE), null)
                .addProperty("c", JSTypeRegistry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null)
                .build();

        // Act
        boolean result = subtype.isSubtype(recordType);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsSubtype_SubtypeWithDifferentPropertyType_ReturnsFalse() {
        // Arrange
        RecordType subtype = new RecordTypeBuilder(new JSTypeRegistry())
                .addProperty("a", JSTypeRegistry.getNativeType(JSTypeNative.STRING_TYPE), null)
                .addProperty("b", JSTypeRegistry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null)
                .build();

        // Act
        boolean result = subtype.isSubtype(recordType);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSubtype_Supertype_ReturnsFalse() {
        // Arrange
        RecordType supertype = new RecordTypeBuilder(new JSTypeRegistry())
                .addProperty("a", JSTypeRegistry.getNativeType(JSTypeNative.STRING_TYPE), null)
                .build();

        // Act
        boolean result = recordType.isSubtype(supertype);

        // Assert
        assertFalse(result);
    }

}