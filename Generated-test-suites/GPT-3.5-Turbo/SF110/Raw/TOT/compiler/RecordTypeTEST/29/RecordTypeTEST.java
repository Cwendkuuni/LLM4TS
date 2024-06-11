import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        recordType = new RecordType(new JSTypeRegistry(), null);
    }

    @Test
    public void testDefineProperty_success() {
        boolean result = recordType.defineProperty("property", JSTypeNative.STRING_TYPE, false, null);
        assertTrue(result);
    }

    @Test
    public void testDefineProperty_failFrozen() {
        recordType.setFrozen(true);
        boolean result = recordType.defineProperty("property", JSTypeNative.STRING_TYPE, false, null);
        assertFalse(result);
    }

    @Test
    public void testIsSynthetic() {
        boolean result = recordType.isSynthetic();
        assertFalse(result);
    }

    @Test
    public void testCheckRecordEquivalenceHelper_equalRecords() {
        RecordType otherRecord = new RecordType(new JSTypeRegistry(), null);
        boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.DEFAULT);
        assertTrue(result);
    }

    @Test
    public void testCheckRecordEquivalenceHelper_unequalRecords() {
        RecordType otherRecord = new RecordType(new JSTypeRegistry(), null);
        otherRecord.defineProperty("property", JSTypeNative.STRING_TYPE, false, null);
        boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.DEFAULT);
        assertFalse(result);
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType prototype = recordType.getImplicitPrototype();
        assertEquals(JSTypeNative.OBJECT_TYPE, prototype);
    }

    @Test
    public void testGetGreatestSubtypeHelper_recordType() {
        RecordType that = new RecordType(new JSTypeRegistry(), null);
        that.defineProperty("property", JSTypeNative.STRING_TYPE, false, null);
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
        assertNotNull(greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtypeHelper_objectType() {
        JSType that = new JSType(new JSTypeRegistry(), JSTypeNative.OBJECT_TYPE);
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
        assertNotNull(greatestSubtype);
    }

    @Test
    public void testIsSubtype_sameRecordType() {
        boolean result = recordType.isSubtype(recordType);
        assertTrue(result);
    }

    @Test
    public void testIsSubtype_subtype() {
        JSTypeNative objectType = JSTypeNative.OBJECT_TYPE;
        ObjectType object = new ObjectType(new JSTypeRegistry(), null, objectType);
        boolean result = recordType.isSubtype(object);
        assertTrue(result);
    }

    @Test
    public void testIsSubtype_notSubtype() {
        JSType that = new JSType(new JSTypeRegistry(), JSTypeNative.NUMBER_TYPE);
        boolean result = recordType.isSubtype(that);
        assertFalse(result);
    }

}