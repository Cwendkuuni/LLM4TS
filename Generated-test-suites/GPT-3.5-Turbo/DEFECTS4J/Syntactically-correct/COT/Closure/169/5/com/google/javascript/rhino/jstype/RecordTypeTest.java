
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        recordType = new RecordType();
    }

    @Test
    public void testGetImplicitPrototype() {
        assertEquals(Registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
    }

    @Test
    public void testIsSubtype() {
        RecordType otherRecord = new RecordType();
        assertTrue(recordType.isSubtype(otherRecord));
    }

    @Test
    public void testIsSubtypeWithNonRecordType() {
        ObjectType objectType = new ObjectType();
        assertFalse(recordType.isSubtype(objectType));
    }

    @Test
    public void testIsSubtypeWithRecordType() {
        RecordType otherRecord = new RecordType();
        assertFalse(recordType.isSubtype(otherRecord));
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecord = new RecordType();
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, true));
    }

    @Test
    public void testDefineProperty() {
        boolean result = recordType.defineProperty("property", new JSType(), true, new Node());
        assertTrue(result);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType that = new JSType();
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
        assertEquals(Registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
    }

    @Test
    public void testToMaybeRecordType() {
        RecordType maybeRecord = recordType.toMaybeRecordType();
        assertEquals(recordType, maybeRecord);
    }

    @Test
    public void testIsSynthetic() {
        boolean isSynthetic = recordType.isSynthetic();
        assertFalse(isSynthetic);
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        assertEquals(recordType, resolvedType);
    }
}
