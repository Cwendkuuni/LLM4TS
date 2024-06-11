import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        RecordType recordType1 = new RecordType(null, null);
        RecordType recordType2 = new RecordType(null, null);

        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsEquivalentTo_NotEquivalent() {
        RecordType recordType1 = new RecordType(null, null);
        StringType stringType = new StringType();

        assertFalse(recordType1.isEquivalentTo(stringType));
    }

    @Test
    public void testGetImplicitPrototype() {
        RecordType recordType = new RecordType(null, null);
        ObjectType objectType = new ObjectType();

        assertEquals(objectType, recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(null, null);
        StringType stringType = new StringType();

        assertTrue(recordType.defineProperty("property", stringType, false, null));
    }

    @Test
    public void testDefineProperty_Frozen() {
        RecordType recordType = new RecordType(null, null);
        recordType.setFrozen(true);
        StringType stringType = new StringType();

        assertFalse(recordType.defineProperty("property", stringType, false, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordProperty recordProperty = new RecordProperty();
        recordProperty.setType(new StringType());
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property", recordProperty);

        RecordType recordType1 = new RecordType(null, properties);
        RecordType recordType2 = new RecordType(null, properties);

        assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType2));
    }

    @Test
    public void testToMaybeRecordType() {
        RecordType recordType = new RecordType(null, null);

        assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testIsSubtype() {
        RecordProperty recordProperty = new RecordProperty();
        recordProperty.setType(new StringType());
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property", recordProperty);

        RecordType recordType1 = new RecordType(null, properties);
        RecordType recordType2 = new RecordType(null, properties);

        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testIsSubtype_NotSubtype() {
        RecordProperty recordProperty1 = new RecordProperty();
        recordProperty1.setType(new StringType());
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", recordProperty1);

        RecordProperty recordProperty2 = new RecordProperty();
        recordProperty2.setType(new NumberType());
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("property2", recordProperty2);

        RecordType recordType1 = new RecordType(null, properties1);
        RecordType recordType2 = new RecordType(null, properties2);

        assertFalse(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testIsSubtype_NotRecordType() {
        RecordType recordType = new RecordType(null, null);
        StringType stringType = new StringType();

        assertFalse(recordType.isSubtype(stringType));
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter();
        RecordType recordType = new RecordType(null, null);

        assertEquals(recordType, recordType.resolveInternal(errorReporter, null));
    }
}