import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testIsSynthetic() {
        RecordType recordType = new RecordType(null, null);
        assertFalse(recordType.isSynthetic());

        RecordType syntheticRecordType = new RecordType(null, null, false);
        assertTrue(syntheticRecordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType recordType1 = new RecordType(null, null);
        RecordType recordType2 = new RecordType(null, null);

        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
    }

    @Test
    public void testGetPropertyType() {
        RecordType recordType = new RecordType(null, null);
        assertEquals(null, recordType.getPropertyType("property"));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry(null);
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordProperty recordProperty = new RecordProperty(null, null);
        properties.put("property", recordProperty);
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);

        assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), recordType1.getGreatestSubtypeHelper(recordType2));
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType = new RecordType(null, null);
        ObjectType objectType = new ObjectType(null, null);
        assertTrue(recordType.isSubtype(objectType));
    }

    @Test
    public void testResolveInternal() {
        RecordType recordType = new RecordType(null, null);
        ErrorReporter reporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        assertEquals(recordType, recordType.resolveInternal(reporter, scope));
    }
}