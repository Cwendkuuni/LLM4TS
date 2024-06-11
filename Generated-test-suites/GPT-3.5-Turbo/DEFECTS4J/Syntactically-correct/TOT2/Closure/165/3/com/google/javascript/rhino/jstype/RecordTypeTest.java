import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType();
        assertTrue(recordType.defineProperty("property1", JSType.UNKNOWN, false, null));
        assertFalse(recordType.defineProperty("property1", JSType.NUMBER, false, null)); // Property already defined
    }

    @Test
    public void testIsSubtype() {
        RecordType recordTypeA = new RecordType();
        RecordType recordTypeB = new RecordType();
        assertTrue(RecordType.isSubtype(recordTypeA, recordTypeB));
    }

    @Test
    public void testDefineRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordProperty(JSType.UNKNOWN, null));
        RecordType recordType = new RecordType(registry, properties);
        assertEquals(1, recordType.getProperties().size());
    }

    @Test
    public void testEquivalentTo() {
        RecordType recordTypeA = new RecordType();
        RecordType recordTypeB = new RecordType();
        assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordTypeA = new RecordType();
        RecordType recordTypeB = new RecordType();
        assertEquals(recordTypeB, recordTypeA.getGreatestSubtypeHelper(recordTypeB));
    }

    @Test
    public void testIsFrozen() {
        RecordType recordType = new RecordType();
        assertTrue(recordType.isFrozen());
    }

}