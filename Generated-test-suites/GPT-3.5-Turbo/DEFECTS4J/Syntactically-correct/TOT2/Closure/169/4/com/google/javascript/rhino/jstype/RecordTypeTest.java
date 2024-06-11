import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(null, null);
        assertTrue(recordType.defineProperty("property1", JSType.UNKNOWN, false, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType1 = new RecordType(null, null);
        RecordType recordType2 = new RecordType(null, null);
        assertTrue(recordType1.getGreatestSubtypeHelper(recordType2).isEquivalentTo(JSType.UNKNOWN));
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType(null, null);
        RecordType recordType2 = new RecordType(null, null);
        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testResolveInternal() {
        RecordType recordType = new RecordType(null, null);
        assertTrue(recordType.resolveInternal(null, null).isEquivalentTo(JSType.UNKNOWN));
    }
}