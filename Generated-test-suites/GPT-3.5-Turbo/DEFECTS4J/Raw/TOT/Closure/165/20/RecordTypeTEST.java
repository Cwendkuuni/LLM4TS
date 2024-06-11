import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        recordType = createRecordType();
    }

    @Test
    public void testDefineProperty_SingleProperty_TypeInferred() {
        assertFalse(recordType.defineProperty("property1", JSType.UNKNOWN_TYPE, true, null));
        assertTrue(recordType.hasProperty("property1"));
        assertEquals(JSType.UNKNOWN_TYPE, recordType.getPropertyType("property1"));
    }

    @Test
    public void testDefineProperty_SingleProperty_TypeDeclared() {
        assertFalse(recordType.defineProperty("property2", JSType.BOOLEAN_TYPE, false, null));
        assertTrue(recordType.hasProperty("property2"));
        assertEquals(JSType.BOOLEAN_TYPE, recordType.getPropertyType("property2"));
    }

    @Test
    public void testIsSubtype_RecordTypeWithSameProperties_ReturnsTrue() {
        RecordType otherRecordType = createRecordType();
        assertTrue(recordType.isSubtype(otherRecordType));
    }

    @Test
    public void testIsSubtype_RecordTypeWithExtraProperties_ReturnsFalse() {
        RecordType otherRecordType = createRecordType();
        otherRecordType.defineProperty("extraProperty", JSType.STRING_TYPE, false, null);
        assertFalse(recordType.isSubtype(otherRecordType));
    }

    @Test
    public void testIsSubtype_RecordTypeWithDifferentPropertyTypes_ReturnsFalse() {
        RecordType otherRecordType = createRecordType();
        otherRecordType.defineProperty("property1", JSType.STRING_TYPE, false, null);
        assertFalse(recordType.isSubtype(otherRecordType));
    }

    @Test
    public void testIsSubtype_NonRecordType_ReturnsFalse() {
        assertFalse(recordType.isSubtype(JSType.NUMBER_TYPE));
    }

    @Test
    public void testIsSubtype_ObjectType_ReturnsTrue() {
        assertTrue(recordType.isSubtype(JSTypeNative.OBJECT_TYPE));
    }

    private RecordType createRecordType() {
        // Create a record type with two properties
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry()); 
        builder.addProperty("property1", JSType.UNKNOWN_TYPE, null);
        builder.addProperty("property2", JSType.BOOLEAN_TYPE, null);
        return builder.build();
    }
}