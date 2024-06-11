import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        recordType = new RecordType(new JSTypeRegistry(), null, true);
    }

    @Test
    public void testDefineProperty() {
        String propertyName = "property";
        JSType type = new JSType(new JSTypeRegistry());

        boolean result = recordType.defineProperty(propertyName, type, false, null);

        assertTrue(result);
        assertEquals(type, recordType.getPropertyType(propertyName));
    }

    @Test
    public void testDefinePropertyWithInferred() {
        String propertyName = "property";
        JSType type = new JSType(new JSTypeRegistry());

        recordType.defineProperty(propertyName, type, true, null);

        assertNull(recordType.getPropertyType(propertyName));
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType implicitPrototype = recordType.getImplicitPrototype();

        assertNotNull(implicitPrototype);
        assertEquals("OBJECT_TYPE", implicitPrototype.getName());
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecordType = new RecordType(new JSTypeRegistry(), null, true);

        boolean result = recordType.checkRecordEquivalenceHelper(otherRecordType, false);

        assertTrue(result);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType that = new JSType(new JSTypeRegistry());

        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);

        assertNotNull(greatestSubtype);
        assertEquals("NO_OBJECT_TYPE", greatestSubtype.getName());
    }

    @Test
    public void testIsSubtype() {
        JSType that = new JSType(new JSTypeRegistry());

        boolean result = recordType.isSubtype(that);

        assertFalse(result);
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();

        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);

        assertNotNull(resolvedType);
        assertEquals("OBJECT_TYPE", resolvedType.getName());
    }
}