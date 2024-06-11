import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType();
        boolean result = recordType.defineProperty("property", JSType.UNKNOWN, false, null);
        assertTrue(result);
    }

    @Test
    public void testGetPropertyType() {
        RecordType recordType = new RecordType();
        recordType.defineProperty("property", JSType.UNKNOWN, false, null);
        JSType propertyType = recordType.getPropertyType("property");
        assertEquals(JSType.UNKNOWN, propertyType);
    }

    @Test(expected = IllegalStateException.class)
    public void testDefinePropertyFrozen() {
        RecordType recordType = new RecordType();
        recordType.setFrozen(true);
        recordType.defineProperty("property", JSType.UNKNOWN, false, null);
    }

    @Test
    public void testIsEquivalentTo() {
        RecordType recordType1 = new RecordType();
        recordType1.defineProperty("property", JSType.UNKNOWN, false, null);
        RecordType recordType2 = new RecordType();
        recordType2.defineProperty("property", JSType.UNKNOWN, false, null);
        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsEquivalentToDifferentProperties() {
        RecordType recordType1 = new RecordType();
        recordType1.defineProperty("property1", JSType.UNKNOWN, false, null);
        RecordType recordType2 = new RecordType();
        recordType2.defineProperty("property2", JSType.UNKNOWN, false, null);
        assertFalse(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType = new RecordType();
        recordType.defineProperty("property", JSType.UNKNOWN, false, null);
        JSType otherType = new JSType();
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(otherType);
        assertEquals(otherType, greatestSubtype);
    }

    @Test
    public void testToMaybeRecordType() {
        RecordType recordType = new RecordType();
        RecordType maybeRecordType = recordType.toMaybeRecordType();
        assertEquals(recordType, maybeRecordType);
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType = new RecordType();
        JSType otherType = new JSType();
        assertTrue(recordType.isSubtype(otherType));
    }

    @Test
    public void testIsSubtypeNativeObjectType() {
        RecordType recordType = new RecordType();
        JSType nativeObjectType = new JSType();
        nativeObjectType.setNativeType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordType.isSubtype(nativeObjectType));
    }

    @Test
    public void testIsSubtypeRecordType() {
        RecordType recordType1 = new RecordType();
        recordType1.defineProperty("property", JSType.UNKNOWN, false, null);
        RecordType recordType2 = new RecordType();
        recordType2.defineProperty("property", JSType.UNKNOWN, false, null);
        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testIsSubtypeNoPropertyMatch() {
        RecordType recordType1 = new RecordType();
        recordType1.defineProperty("property1", JSType.UNKNOWN, false, null);
        RecordType recordType2 = new RecordType();
        recordType2.defineProperty("property2", JSType.UNKNOWN, false, null);
        assertFalse(recordType1.isSubtype(recordType2));
    }
}