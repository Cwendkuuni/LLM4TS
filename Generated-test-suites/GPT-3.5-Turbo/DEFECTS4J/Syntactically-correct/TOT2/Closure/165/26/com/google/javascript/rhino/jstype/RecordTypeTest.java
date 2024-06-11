import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(registry, properties);
        assertTrue(recordType.defineProperty("testProperty", JSType.BOOLEAN, false, node));
        assertFalse(recordType.defineProperty("testProperty", JSType.NUMBER, false, node));
    }

    @Test
    public void testIsEquivalentTo() {
        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);
        assertTrue(recordType1.isEquivalentTo(recordType1));
        assertFalse(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);
        assertTrue(RecordType.isSubtype(recordType1, recordType2));
        assertFalse(RecordType.isSubtype(recordType2, recordType1));
    }

}