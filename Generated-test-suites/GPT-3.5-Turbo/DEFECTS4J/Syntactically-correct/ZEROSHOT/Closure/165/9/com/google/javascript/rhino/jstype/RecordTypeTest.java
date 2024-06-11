import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(registry, properties);
        assertTrue(recordType.defineProperty("propertyName", type, inferred, propertyNode));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType = new RecordType(registry, properties);
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
        assertNotNull(greatestSubtype);
    }

    @Test
    public void testToMaybeRecordType() {
        RecordType recordType = new RecordType(registry, properties);
        RecordType maybeRecordType = recordType.toMaybeRecordType();
        assertEquals(recordType, maybeRecordType);
    }

    @Test
    public void testIsSubtype() {
        RecordType typeA = new RecordType(registry, propertiesA);
        RecordType typeB = new RecordType(registry, propertiesB);
        boolean isSubtype = RecordType.isSubtype(typeA, typeB);
        assertFalse(isSubtype);
    }

    @Test
    public void testResolveInternal() {
        RecordType recordType = new RecordType(registry, properties);
        JSType resolvedType = recordType.resolveInternal(t, scope);
        assertNotNull(resolvedType);
    }
}