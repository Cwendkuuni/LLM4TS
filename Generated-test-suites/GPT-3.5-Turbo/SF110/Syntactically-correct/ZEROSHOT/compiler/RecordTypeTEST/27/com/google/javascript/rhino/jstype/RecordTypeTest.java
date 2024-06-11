import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testIsSynthetic() {
        RecordType recordType = new RecordType(null, null);
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType recordType1 = new RecordType(null, null);
        RecordType recordType2 = new RecordType(null, null);
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2));
    }

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(null, null);
        assertTrue(recordType.defineProperty("property", null, false, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType = new RecordType(null, null);
        JSType subtype = recordType.getGreatestSubtypeHelper(null);
        assertEquals(subtype, null);
    }

    @Test
    public void testToMaybeRecordType() {
        RecordType recordType = new RecordType(null, null);
        assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType = new RecordType(null, null);
        assertTrue(recordType.isSubtype(null));
    }

    @Test
    public void testIsSubtype_static() {
        ObjectType objectType = new ObjectType(null, null, null);
        RecordType recordType = new RecordType(null, null);
        assertFalse(RecordType.isSubtype(objectType, recordType));
    }
}