import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testDefineProperty() {
        // Test defining a property in RecordType
        RecordType recordType = new RecordType();
        recordType.defineProperty("name", JSType.STRING_TYPE, false, null);

        assertTrue(recordType.hasProperty("name"));
        assertEquals(JSType.STRING_TYPE, recordType.getPropertyType("name"));
    }

    @Test
    public void testIsSubtype() {
        // Test if one record type is a subtype of another
        RecordType recordType1 = new RecordType();
        recordType1.defineProperty("age", JSType.NUMBER_TYPE, false, null);

        RecordType recordType2 = new RecordType();
        recordType2.defineProperty("age", JSType.NUMBER_TYPE, false, null);

        assertTrue(RecordType.isSubtype(recordType1, recordType2));
    }

    @Test
    public void testGetLeastSupertype() {
        // Test getting the least supertype of two record types
        RecordType recordType1 = new RecordType();
        recordType1.defineProperty("name", JSType.STRING_TYPE, false, null);

        RecordType recordType2 = new RecordType();
        recordType2.defineProperty("age", JSType.NUMBER_TYPE, false, null);

        JSType leastSupertype = recordType1.getLeastSupertype(recordType2);

        assertTrue(leastSupertype.isRecordType());
        assertTrue(leastSupertype.hasProperty("name"));
        assertTrue(leastSupertype.hasProperty("age"));
    }

    @Test
    public void testResolveInternal() {
        // Test resolving internal references in RecordType
        RecordType recordType = new RecordType();
        recordType.defineProperty("color", JSType.STRING_TYPE, false, null);

        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<JSType>();

        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);

        assertEquals(recordType, resolvedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateRecordTypeWithNullProperty() {
        // Test creating a RecordType with a null property, should throw an exception
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("size", null);

        new RecordType(properties);
    }
}