import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {
    private RecordType recordType;

    @Before
    public void setUp() {
        // create sample properties
        RecordTypeBuilder.RecordProperty property1 = new RecordTypeBuilder.RecordProperty("property1", JSType.UNKNOWN_TYPE, null);
        RecordTypeBuilder.RecordProperty property2 = new RecordTypeBuilder.RecordProperty("property2", JSType.UNKNOWN_TYPE, null);

        // create properties map
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", property1);
        properties.put("property2", property2);

        // create record type
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecord = new RecordType(registry, properties);

        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, true));
    }

    @Test
    public void testGetImplicitPrototype() {
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        String propertyName = "property3";
        JSType type = JSType.UNKNOWN_TYPE;
        boolean inferred = true;
        Node propertyNode = null;

        assertTrue(recordType.defineProperty(propertyName, type, inferred, propertyNode));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType that = new ObjectType();

        assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), recordType.getGreatestSubtypeHelper(that));
    }

    @Test
    public void testIsSubtype() {
        JSType that = new ObjectType();

        assertFalse(recordType.isSubtype(that));
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter t = null;
        StaticScope<JSType> scope = null;

        assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), recordType.resolveInternal(t, scope));
    }
}