import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        recordType = new RecordType(null, null, true);
    }

    @Test
    public void testIsSynthetic() {
        boolean result = recordType.isSynthetic();
        assertFalse(result);
    }

    @Test
    public void testDefineProperty() {
        boolean result = recordType.defineProperty("property", null, false, null);
        assertFalse(result);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType type = recordType.getGreatestSubtypeHelper(null);
        assertNull(type);
    }

    @Test
    public void testToMaybeRecordType() {
        RecordType type = recordType.toMaybeRecordType();
        assertNotNull(type);
    }

    @Test
    public void testIsSubtype() {
        boolean result = recordType.isSubtype(null);
        assertFalse(result);
    }

    @Test
    public void testIsSubtype_staticMethod() {
        JSType typeA = new ObjectType();
        RecordType typeB = new RecordType(null, null, true);
        boolean result = RecordType.isSubtype(typeA, typeB);
        assertFalse(result);
    }
}