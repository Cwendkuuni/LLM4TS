import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType();
        recordType.defineProperty("property1", JSType.NULL_TYPE, false, null);
        assertTrue(recordType.hasProperty("property1"));
        assertEquals(JSType.NULL_TYPE, recordType.getPropertyType("property1"));
    }

    @Test
    public void testIsEquivalentTo() {
        RecordType recordType1 = new RecordType();
        RecordType recordType2 = new RecordType();
        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType();
        RecordType recordType2 = new RecordType();
        assertTrue(RecordType.isSubtype(recordType1, recordType2));
    }

    @Test
    public void testGetLeastSupertype() {
        RecordType recordType1 = new RecordType();
        RecordType recordType2 = new RecordType();
        JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
        assertNotNull(leastSupertype);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType1 = new RecordType();
        RecordType recordType2 = new RecordType();
        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertNotNull(greatestSubtype);
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType();
        RecordType recordType2 = new RecordType();
        assertTrue(recordType1.isSubtype(recordType2));
    }
}