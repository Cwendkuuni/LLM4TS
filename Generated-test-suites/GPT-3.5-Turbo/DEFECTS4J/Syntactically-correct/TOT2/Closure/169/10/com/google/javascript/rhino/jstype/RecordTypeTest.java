import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        assertTrue(recordType.defineProperty("testProperty", JSType.UNKNOWN, false, null));
        assertFalse(recordType.defineProperty("testProperty", JSType.STRICT_ERROR, false, null));
    }

    @Test
    public void testGetImplicitPrototype() {
        RecordType recordType = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        assertEquals(JSType.OBJECT_TYPE, recordType.getImplicitPrototype());
    }

    @Test
    public void testIsSubtype() {
        RecordType recordTypeA = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        RecordType recordTypeB = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        assertTrue(RecordType.isSubtype(recordTypeA, recordTypeB));
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType recordTypeA = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        RecordType recordTypeB = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, true));
    }

    @Test
    public void testResolveInternal() {
        RecordType recordType = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
        assertNull(recordType.resolveInternal(null, null));
    }

}